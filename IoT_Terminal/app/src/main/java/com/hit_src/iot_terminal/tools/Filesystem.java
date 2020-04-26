package com.hit_src.iot_terminal.tools;

import android.content.Context;
import android.util.Xml;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.xml.XML;
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

public class Filesystem {
    private static File getSensorTypeDir(Context context) {
        String path = context.getFilesDir() + "/SensorType";
        File res = new File(path);
        if (!res.exists()) {
            res.mkdir();
        }
        return res;
    }

    public static void build(Context context) {
        getSensorTypeDir(context);
        MainApplication.sensorTypeHashMap.clear();
        MainApplication.xmlRecordHashMap.clear();
        ArrayList<XML> xmls = getXMLList(context);
        for (XML j : xmls) {
            File i = new File(context.getFilesDir() + "/SensorType/" + j.name + ".xml");
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(i);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            XML2SensorType xml2SensorType = new XML2SensorType();
            try {
                Xml.parse(fileInputStream, Xml.Encoding.UTF_8, xml2SensorType);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            SensorType res = xml2SensorType.getResults();
            MainApplication.sensorTypeHashMap.put(res.id, res);
            MainApplication.xmlRecordHashMap.put(j.name, res.id);
        }
    }

    private static File getXMLListFile(Context context) {
        String path = context.getFilesDir() + "/SensorType/packageList";
        File res = new File(path);
        if (!res.exists()) {
            try {
                res.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static ArrayList<XML> getXMLList(Context context) {
        ArrayList<XML> res = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getXMLListFile(context)));
            while (true) {
                String s = reader.readLine();
                if (s == null) {
                    break;
                }
                XML xml = new XML(s);
                res.add(xml);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static File createXMLFile(Context context, XMLRecord selected) {
        File list = getXMLListFile(context);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(list, true));
            writer.write(selected.name + " " + selected.serverVersion + "\n");
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = context.getFilesDir() + "/SensorType/";
        File res = new File(path + selected.name + ".xml");
        if (!res.exists()) {
            try {
                res.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static File updateXMLFile(Context context, XMLRecord selected) {
        File list = getXMLListFile(context);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(list));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String s = reader.readLine();
                if (s == null) {
                    break;
                }
                XML xml = new XML(s);
                if (xml.name.equals(selected.name)) {
                    sb.append(selected.name + " " + selected.serverVersion + "\n");
                } else {
                    sb.append(s);
                    sb.append("\n");
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(list));
            writer.write(sb.toString());
            writer.flush();
            reader.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = context.getFilesDir() + "/SensorType/";
        File res = new File(path + selected.name + ".xml");
        return res;
    }

    public static void deleteXMLFile(Context context, XMLRecord selected) {
        File list = getXMLListFile(context);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(list));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String s = reader.readLine();
                if (s == null) {
                    break;
                }
                XML xml = new XML(s);
                if (xml.name.equals(selected.name)) {
                    continue;
                } else {
                    sb.append(s);
                    sb.append("\n");
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(list));
            writer.write(sb.toString());
            writer.flush();
            reader.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = context.getFilesDir() + "/SensorType/";
        File file = new File(path + selected.name + ".xml");
        file.delete();
        build(context);
    }
}
