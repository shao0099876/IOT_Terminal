package com.hit_src.iot_terminal.tools;

import android.util.Xml;
import android.widget.Toast;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.ui.overview.OverviewViewModel;
import com.hit_src.iot_terminal.xml.XML2SensorType;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PackageManager {
    public static File getLocalXMLListFile(){
        String path=MainApplication.self.getFilesDir()+"/SensorType/packageList";
        File res=new File(path);
        boolean tmp;
        try {
            tmp = res.createNewFile();
        } catch (IOException e) {
            Toast.makeText(MainApplication.self, "致命错误：创建包管理器文件失败", Toast.LENGTH_SHORT).show();
            return null;
        }
        if(tmp){
            GlobalVar.addLogLiveData("已创建包管理器列表文件");
        }
        else{
            GlobalVar.addLogLiveData("已存在包管理器列表文件");
        }
        return res;
    }
    public static void addToLocalXMLListFile(String name,int version){
        File listFile=getLocalXMLListFile();
        FileWriter fileWriter= null;
        try {
            fileWriter = new FileWriter(listFile,true);
        } catch (IOException e) {
            Toast.makeText(MainApplication.self, "致命错误：包管理器列表文件写入失败", Toast.LENGTH_SHORT).show();
            return;
        }
        BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(name);stringBuffer.append(" ");
        stringBuffer.append(version);stringBuffer.append("\n");
        try {
            bufferedWriter.write(stringBuffer.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void delFromLocalXMLListFile(String name){
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
    public static ArrayList<XMLRecord> getLocalXMLList(){
        ArrayList<XMLRecord> res=new ArrayList<>();
        File listFile=getLocalXMLListFile();
        FileReader fileReader= null;
        try {
            fileReader = new FileReader(listFile);
        } catch (FileNotFoundException e) {
            Toast.makeText(MainApplication.self,"致命错误：包管理器列表文件不存在",Toast.LENGTH_LONG).show();
        }
        if(fileReader==null){
            return res;
        }
        BufferedReader reader=new BufferedReader(fileReader);
        while(true){
            String s= null;
            try {
                s = reader.readLine();
            } catch (IOException e) {
                Toast.makeText(MainApplication.self,"致命错误：读文件出错",Toast.LENGTH_LONG).show();
            }
            if(s==null){
                break;
            }
            String[] ss=s.split(" ");
            String name=ss[0];
            int localVersion= Integer.parseInt(ss[1]);
            res.add(new XMLRecord(name,localVersion,null));
        }
        try {
            reader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    public static void build(){
        ArrayList<XMLRecord> localList=getLocalXMLList();
        Map<String,XMLRecord> xmlRecordMap=new HashMap<>();
        for(XMLRecord i: localList){
            xmlRecordMap.put(i.name,i);
            String path=MainApplication.self.getFilesDir()+"/SensorType/"+i.name+".xml";
            File file=new File(path);
            FileInputStream fileInputStream= null;
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                Toast.makeText(MainApplication.self,"致命错误：无法找到传感器配置文件", Toast.LENGTH_SHORT).show();
                continue;
            }
            XML2SensorType xml2SensorType=new XML2SensorType();
            try {
                Xml.parse(fileInputStream, Xml.Encoding.UTF_8,xml2SensorType);
            } catch (IOException e) {
                Toast.makeText(MainApplication.self, "致命错误：传感器配置文件解析错误", Toast.LENGTH_SHORT).show();
            } catch (SAXException e) {
                Toast.makeText(MainApplication.self, "致命错误：传感器配置文件解析错误", Toast.LENGTH_SHORT).show();
            }
            SensorType sensorType=xml2SensorType.getResults();
            GlobalVar.sensorTypeHashMap.put(sensorType.id,sensorType);
        }
        ArrayList<XMLRecord> res=new ArrayList<>();
        ArrayList<XMLRecord> serverList=XMLServer.getList();
        if(serverList==null){
            for(XMLRecord i:localList){
                GlobalVar.xmlRecords.add(i);
            }
        } else{
            for(XMLRecord i:serverList){
                if(xmlRecordMap.containsKey(i.name)){
                    XMLRecord xmlRecord=xmlRecordMap.get(i.name);
                    res.add(new XMLRecord(i.name,xmlRecord.localVersion,i.serverVersion));
                }
                else{
                    res.add(new XMLRecord(i.name,null,i.serverVersion));
                }
            }
            GlobalVar.xmlRecords.addAll(res);
        }
    }
    public static File getLocalXMLFile(String name){
        String path=MainApplication.self.getFilesDir()+"/SensorType/"+name+".xml";
        return new File(path);
    }
    public static void addLocalXMLFile(String name,int version,String content){
        addToLocalXMLListFile(name,version);
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
        build();
    }
    public static void updateLocalXMLFile(String name, Integer serverVersion, String content) {
        delFromLocalXMLListFile(name);
        addToLocalXMLListFile(name,serverVersion);
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
        build();
    }
    public static void delLocalXMLFile(String name){
        delFromLocalXMLListFile(name);
        String path=MainApplication.self.getFilesDir()+"/SensorType/"+name+".xml";
        File file=new File(path);
        file.delete();
        build();
    }


    public static void testPackageListDir() {
        String path=MainApplication.self.getFilesDir()+"/SensorType";
        File file=new File(path);
        file.mkdir();
    }
}
