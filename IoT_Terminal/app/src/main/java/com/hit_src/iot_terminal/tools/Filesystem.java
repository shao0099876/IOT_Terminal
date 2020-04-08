package com.hit_src.iot_terminal.tools;

import android.util.Xml;

import com.hit_src.iot_terminal.Global;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.SensorType;
import com.hit_src.iot_terminal.xml.XML2SensorType;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class Filesystem {
    public static File getSensorTypeDir(MainApplication context){
        String path=context.getFilesDir()+"/SensorType";
        File res=new File(path);
        if(!res.exists()){
            res.mkdir();
        }
        return res;
    }
    public static void build(MainApplication context) {
        File[] files=getSensorTypeDir(context).listFiles();
        for(File i:files){
            FileInputStream fileInputStream=null;
            try {
                fileInputStream=new FileInputStream(i);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            XML2SensorType xml2SensorType=new XML2SensorType();
            try {
                Xml.parse(fileInputStream, Xml.Encoding.UTF_8,xml2SensorType);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            SensorType res=xml2SensorType.getResults();
            MainApplication.sensorTypeHashMap.put(res.getName(),res);
        }
    }
}
