package com.hit_src.iot_terminal.internet;

import android.util.Log;

import com.hit_src.iot_terminal.profile.settings.InternetSettings;
import com.hit_src.iot_terminal.profile.status.Status;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.Timer;

public class UpperServer {
    private UpperServer self=new UpperServer();
    private UpperServer(){}
    public static void heartBeat() {
        String addr=InternetSettings.getServer();
        int port=InternetSettings.getServerPort();
        if(addr.isEmpty()||port==-1){
            return;
        }
        Socket socket=new Socket();
        SocketAddress socketAddress=new InetSocketAddress(addr,port);
        try {
            socket.connect(socketAddress,1000);
        } catch (IOException e) {
            Log.d("Internet","Unable to access server");
            //Status.setStatusData(Status.INTERNET_CONNECTION_STATUS,1);
            return;
        }
        BufferedWriter writer=null;
        try {
            socket.setSoTimeout(3000);
            writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("0:heartbeat");
            writer.flush();
            Log.d("Internet:","Sended heartbeat");
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String s=reader.readLine();
            if(s.equals("0:reply:heartbeat")){
                Log.d("Internet","heartbeat replyed");
                Status.setStatusData(Status.INTERNET_CONNECTION_LASTTIME, new Date().getTime());
            }

        } catch (IOException e) {
            Log.d("Internet","No heartbeat reply");
            Status.setStatusData(Status.INTERNET_CONNECTION_STATUS,2);
        }
    }
}
