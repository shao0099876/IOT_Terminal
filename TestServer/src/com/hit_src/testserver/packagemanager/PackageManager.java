package com.hit_src.testserver.packagemanager;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.HashMap;

public class PackageManager {
    public HashMap<Integer,SensorType> sensorTypeHashMap=new HashMap<>();
    public PackageManager(){
        try{
            File listFile=new File("xmlList");
            BufferedReader reader=new BufferedReader(new FileReader(listFile));
            while(true){
                String s=reader.readLine();
                if(s==null){
                    break;
                }
                File xmlFile=new File((s.split(" "))[0]+".xml");
                SAXParserFactory factory=SAXParserFactory.newInstance();
                SAXParser parser=factory.newSAXParser();
                XML2SensorType xml2SensorType=new XML2SensorType();
                parser.parse(xmlFile,xml2SensorType);
                SensorType res=xml2SensorType.getResults();
                sensorTypeHashMap.put(res.id,res);
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

    }
}
