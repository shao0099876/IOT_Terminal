package com.hit_src.iot_terminal.tools;

import android.content.Context;
import android.os.RemoteException;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.XMLRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class XMLServer {
    public static ArrayList<XMLRecord> getList(){
        String addr= null;
        int port = -1;
        try {
            addr = MainApplication.settingsService.getUpperServerAddr();
            port=MainApplication.settingsService.getUpperServerXMLPort();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(addr.isEmpty()||port==-1){
            return null;
        }
        ArrayList<XMLRecord> res=new ArrayList<>();
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
                res.add(new XMLRecord(s));
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

    public static String addXML(XMLRecord selected) {
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
            StringBuffer stringBuffer=new StringBuffer();
            for(int i=0;i<n;i++){
                stringBuffer.append(reader.readLine()+"\n");
            }
            reader.close();
            writer.close();
            socket.close();
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
