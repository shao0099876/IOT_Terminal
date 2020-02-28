package com.hit_src.iot_terminal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.tools.MessageThread;
import com.hit_src.iot_terminal.ui.handler.InternetUIHandler;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class InternetActivity extends AppCompatActivity {

    public static InternetUIHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        handler=new InternetUIHandler(this);




    }

    @Override
    protected void onResume(){
        super.onResume();
        MessageThread.sendMessage(handler,InternetUIHandler.STATUS_UPDATE);
        MessageThread.sendMessage(handler,InternetUIHandler.CONFIG_UPDATE);
    }
}
