package com.hit_src.iot_terminal.tools.pm;

import android.os.RemoteException;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.Sensor;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageManager {
    private static String path;
    private static PackageManager packageManager=null;
    public static void createInstance(){
        if(packageManager==null){
            packageManager=new PackageManager();
        }
    }
    private PackageManager(){
        path= MainApplication.self.getFilesDir()+"/SensorType/packageList";
        File file=new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static ArrayList<XMLRecord> buildXMLRecords() {
        ArrayList<XMLRecord> res=new ArrayList<>();
        try {
            BufferedReader reader=new BufferedReader(new FileReader(new File(path)));
            while(true){
                String s=reader.readLine();
                if(s==null){
                    break;
                }
                String[] ss=s.split(" ");
                res.add(new XMLRecord(ss[0],Integer.parseInt(ss[1]),null));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    public static ArrayList<SensorType> buildSensorTypes() {
        ArrayList<SensorType> res=new ArrayList<>();
        for(XMLRecord i:GlobalVar.xmlRecords){
            File file=new File(MainApplication.self.getFilesDir()+"/SensorType/"+i.name+".xml");
            try {
                FileInputStream fileInputStream=new FileInputStream(file);
                XML2SensorType xml2SensorType=new XML2SensorType();
                Xml.parse(fileInputStream,Xml.Encoding.UTF_8,xml2SensorType);
                SensorType sensorType=xml2SensorType.getResults();
                res.add(sensorType);
                i.setSensorTypeID(sensorType.id);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
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
        String path=MainApplication.self.getFilesDir()+"/SensorType";
        File file=new File(path);
        file.mkdir();
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
        assert addr != null;
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
                ArrayList<XMLRecord> remoteList=readRemoteXMLList();
                for(XMLRecord i:remoteList){
                    boolean exists=false;
                    for(int j=0;j<GlobalVar.xmlRecords.size();j++){
                        XMLRecord now=GlobalVar.xmlRecords.get(j);
                        if(i.name.equals(now.name)){
                            now.setRemoteVersion(i.remoteVersion);
                            GlobalVar.xmlRecords.set(j,now);
                            exists=true;
                            break;
                        }
                    }
                    if(!exists){
                        GlobalVar.xmlRecords.add(new XMLRecord(i.name,null,i.remoteVersion));
                    }
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

                    for(int i=0;i<GlobalVar.xmlRecords.size();i++){
                        XMLRecord record=GlobalVar.xmlRecords.get(i);
                        if(record.name.equals(name)){
                            record.setLocalVersion(version);
                            GlobalVar.xmlRecords.set(i,record);
                            break;
                        }
                    }
                    addLocalXMLFile(name, content);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static void delete(final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(XMLRecord i:GlobalVar.xmlRecords){
                    if(i.name.equals(name)){
                        GlobalVar.xmlRecords.remove(i);
                        break;
                    }
                }
                String path=MainApplication.self.getFilesDir()+"/SensorType/"+name+".xml";
                File file=new File(path);
                file.delete();
            }
        }).start();
    }
    public static void write(List<XMLRecord> list){
        try {
            BufferedWriter writer=new BufferedWriter(new FileWriter(new File(path)));
            StringBuilder stringBuilder=new StringBuilder();
            for(XMLRecord i:list){
                if(i.localVersion==null){
                    continue;
                }
                stringBuilder.append(i.name);stringBuilder.append(" ");
                stringBuilder.append(i.localVersion);stringBuilder.append("\n");
            }
            writer.write(stringBuilder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
