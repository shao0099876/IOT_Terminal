package com.hit_src.iot_terminal.tools;

import android.os.RemoteException;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.xml.XML2SensorType;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class PackageManager {
    private static File getLocalXMLListFile(){
        String path=MainApplication.self.getFilesDir()+"/SensorType/packageList";
        File res=new File(path);
        try {
            res.createNewFile();
        } catch (IOException e) {
            return null;
        }
        return res;
    }
    private static void addToLocalXMLListFile(String name, int version){
        File listFile=getLocalXMLListFile();
        try {
            BufferedReader reader=new BufferedReader(new FileReader(listFile));
            StringBuilder sb=new StringBuilder();
            while(true){
                String s=reader.readLine();
                if(s==null){
                    break;
                }
                String[] ss=s.split(" ");
                if(ss[0].equals(name)){
                    continue;
                }
                sb.append(s);sb.append("\n");
            }
            reader.close();
            sb.append(name);sb.append(" ");
            sb.append(version);sb.append("\n");
            BufferedWriter writer=new BufferedWriter(new FileWriter(getLocalXMLListFile()));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void delFromLocalXMLListFile(String name){
        File listFile=getLocalXMLListFile();
        try {
            FileReader fileReader=new FileReader(listFile);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            StringBuilder stringBuilder=new StringBuilder();
            while(true){
                String s=bufferedReader.readLine();
                if(s==null){
                    break;
                }
                String[] ss=s.split(" ");
                if(ss[0].equals(name)){
                    continue;
                }
                stringBuilder.append(s);
                stringBuilder.append("\n");
            }
            bufferedReader.close();
            fileReader.close();
            FileWriter fileWriter=new FileWriter(listFile);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static File getLocalXMLFile(String name){
        String path=MainApplication.self.getFilesDir()+"/SensorType/"+name+".xml";
        return new File(path);
    }
    private static void addLocalXMLFile(String name, String content){
        String path=MainApplication.self.getFilesDir()+"/SensorType/"+name+".xml";
        File file=new File(path);
        try {
            file.createNewFile();
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(content);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testPackageListDir() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path=MainApplication.self.getFilesDir()+"/SensorType";
                File file=new File(path);
                file.mkdir();
            }
        }).start();
    }
    public static ArrayList<XMLRecord> readLocalXMLList(){
        ArrayList<XMLRecord> res=new ArrayList<>();
        try {
            BufferedReader reader=new BufferedReader(new FileReader(getLocalXMLListFile()));
            while(true){
                String s=reader.readLine();
                if(s==null){
                    break;
                }
                String[] ss=s.split(" ");
                String name=ss[0];
                int localVersion= Integer.parseInt(ss[1]);
                res.add(new XMLRecord(name,localVersion,null));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    private static ArrayList<XMLRecord> readRemoteXMLList(){
        String addr= null;
        int port = -1;
        try {
            addr = MainApplication.settingsService.getUpperServerAddr();
            port=MainApplication.settingsService.getUpperServerXMLPort();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(addr.isEmpty()||port==-1){
            Toast.makeText(MainApplication.self, "网络配置不正确", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }
        ArrayList<XMLRecord> res=new ArrayList<>();
        try {
            Socket socket=new Socket(MainApplication.settingsService.getUpperServerAddr(),MainApplication.settingsService.getUpperServerXMLPort());
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("getList"+"\n");
            writer.flush();
            String s=reader.readLine();
            int n=Integer.parseInt(s);
            for(int i=0;i<n;i++){
                s=reader.readLine();
                Log.e("SRCDEBUG",s);
                String[] ss=s.split(" ");
                Integer remoteVersion= Integer.valueOf(ss[1]);
                res.add(new XMLRecord(ss[0],null,remoteVersion));
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void fetch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<XMLRecord> localList=readLocalXMLList();
                ArrayList<XMLRecord> remoteList=readRemoteXMLList();
                GlobalVar.xmlRecords.clear();
                for(XMLRecord i:remoteList){
                    for(XMLRecord j:localList){
                        if(i.name.equals(j.name)){
                            i.localVersion=j.localVersion;
                            break;
                        }
                    }
                    GlobalVar.xmlRecords.add(i);
                }
            }
        }).start();
    }
    public static void pull(final String name, final int version){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket=new Socket(MainApplication.settingsService.getUpperServerAddr(),MainApplication.settingsService.getUpperServerXMLPort());
                    BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    StringBuilder sb=new StringBuilder();
                    sb.append("add");sb.append("\n");
                    sb.append(name);sb.append("\n");
                    writer.write(sb.toString());
                    writer.flush();
                    String s=reader.readLine();
                    int n= Integer.parseInt(s);
                    sb=new StringBuilder();
                    for(int i=0;i<n;i++){
                        sb.append(reader.readLine()).append("\n");
                    }
                    String content=sb.toString();
                    reader.close();
                    writer.close();
                    socket.close();
                    addToLocalXMLListFile(name,version);
                    addLocalXMLFile(name, content);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                fetch();
                build();
            }
        }).start();

    }
    public static void build(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(XMLRecord i: GlobalVar.xmlRecords){
                    try {
                        FileInputStream fileInputStream=new FileInputStream(getLocalXMLFile(i.name));
                        XML2SensorType xml2SensorType=new XML2SensorType();
                        Xml.parse(fileInputStream,Xml.Encoding.UTF_8,xml2SensorType);
                        SensorType sensorType=xml2SensorType.getResults();
                        GlobalVar.sensorTypeMap.put(sensorType.id,sensorType);
                        GlobalVar.xmlRecordtoSensorTypeMap.put(i.name,sensorType.id);
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void delete(final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                XMLRecord xmlRecord = null;
                for(XMLRecord i:GlobalVar.xmlRecords){
                    if(i.name.equals(name)){
                        xmlRecord=i;
                        GlobalVar.xmlRecords.remove(i);
                        break;
                    }
                }
                int sensorType=GlobalVar.xmlRecordtoSensorTypeMap.get(xmlRecord.name);
                try {
                    MainApplication.dbService.delSensorByType(sensorType);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                delFromLocalXMLListFile(name);
                String path=MainApplication.self.getFilesDir()+"/SensorType/"+name+".xml";
                File file=new File(path);
                file.delete();
                fetch();
                build();
            }
        }).start();
    }
}
