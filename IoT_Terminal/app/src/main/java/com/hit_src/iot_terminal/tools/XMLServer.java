package com.hit_src.iot_terminal.tools;

import android.content.Context;
import android.os.RemoteException;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.xml.XML;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class XMLServer {
    public static ArrayList<XML> getList(){
        ArrayList<XML> res=new ArrayList<>();
        try {
            Socket socket=new Socket(MainApplication.settingsService.getUpperServerAddr(),MainApplication.settingsService.getUpperServerXMLPort());
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("getList"+"\n");
            writer.flush();
            String s=reader.readLine();
            int n=Integer.valueOf(s);
            for(int i=0;i<n;i++){
                s=reader.readLine();
                XML xml=new XML(s);
                res.add(xml);
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

    public static void addXML(Context context, XMLRecord selected) {
        try {
            Socket socket=new Socket(MainApplication.settingsService.getUpperServerAddr(),MainApplication.settingsService.getUpperServerXMLPort());
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            StringBuilder sb=new StringBuilder();
            sb.append("add");sb.append("\n");
            sb.append(selected.name);sb.append("\n");
            writer.write(sb.toString());
            writer.flush();
            String s=reader.readLine();
            int n= Integer.parseInt(s);
            File file=Filesystem.createXMLFile(context,selected);
            BufferedWriter fileWriter=new BufferedWriter(new FileWriter(file));
            for(int i=0;i<n;i++){
                s=reader.readLine();
                fileWriter.write(s+"\n");
            }
            fileWriter.flush();
            fileWriter.close();
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void updateXML(Context context,XMLRecord selected) {
        try {
            Socket socket=new Socket(MainApplication.settingsService.getUpperServerAddr(),MainApplication.settingsService.getUpperServerXMLPort());
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            StringBuilder sb=new StringBuilder();
            sb.append("update");sb.append("\n");
            sb.append(selected.name);sb.append("\n");
            writer.write(sb.toString());
            writer.flush();
            String s=reader.readLine();
            int n= Integer.parseInt(s);
            File file=Filesystem.updateXMLFile(context,selected);
            BufferedWriter fileWriter=new BufferedWriter(new FileWriter(file));
            for(int i=0;i<n;i++){
                s=reader.readLine();
                fileWriter.write(s+"\n");
            }
            fileWriter.flush();
            fileWriter.close();
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void delXML(XMLRecord selected) {
        try {
            List<Sensor> sensors=MainApplication.dbService.getSensorList();
            for(Sensor i:sensors){
                if(selected.name.equals(i.getType())){
                    MainApplication.dbService.delSensor(i.getID());
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
