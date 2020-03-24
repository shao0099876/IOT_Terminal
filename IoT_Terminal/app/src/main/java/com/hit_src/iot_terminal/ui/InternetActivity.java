package com.hit_src.iot_terminal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.hardware.EthernetNetworkCard;
import com.hit_src.iot_terminal.service.IStatusService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class InternetActivity extends AbstractActivity {
    private TextView statusText;
    private TextView lasttimeText;
    private EditText ipEditText;
    private EditText netmaskEditText;
    private EditText gatewayEditText;
    private EditText upperServerAddrEditText;
    private EditText upperServerPortEditText;
    private Button flushButton;
    private Button saveButton;
    @Override
    protected void runOnBindService() {
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                flushStatus();
            }
        },50,3000);
        flushSettings();
    }
    private void flushStatus(){
        try {
            boolean status=statusService.getInternetConnectionStatus();
            long lasttime=statusService.getInternetConnectionLasttime();
            if(status){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText(R.string.Internet_connection_normal);
                    }
                });
            } else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText(R.string.Internet_connection_failure);
                    }
                });
            }
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            final String lasttime_str=simpleDateFormat.format(new Date(lasttime));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lasttimeText.setText(lasttime_str);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private void flushSettings(){
        final String ip=EthernetNetworkCard.getInfo(EthernetNetworkCard.ADDR);
        final String netmask=EthernetNetworkCard.getInfo(EthernetNetworkCard.NETMASK);
        final String gateway="none";//无法获取
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ipEditText.setText(ip);
                netmaskEditText.setText(netmask);
                gatewayEditText.setText(gateway);
            }
        });
        try {
            final String addr=settingsService.getUpperServerAddr();
            final String port=Integer.toString(settingsService.getUpperServerPort());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    upperServerAddrEditText.setText(addr);
                    upperServerPortEditText.setText(port);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        statusText=findViewById(R.id.Internet_status_link_TextView);
        lasttimeText=findViewById(R.id.Internet_status_lasttime_TextView);
        ipEditText=findViewById(R.id.Internet_edit_IP_EditText);
        netmaskEditText=findViewById(R.id.Internet_edit_NetMask_EditText);
        gatewayEditText=findViewById(R.id.Internet_edit_Gateway_EditText);
        upperServerAddrEditText=findViewById(R.id.Internet_edit_server_EditText);
        upperServerPortEditText=findViewById(R.id.Internet_edit_serverport_EditText);

        flushButton=findViewById(R.id.Internet_status_reflush_Button);
        saveButton=findViewById(R.id.Internet_edit_button);

        flushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flushStatus();
                flushSettings();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upperServerAddr=upperServerAddrEditText.getText().toString();
                String upperServerPort=upperServerPortEditText.getText().toString();
                try {
                    settingsService.setUpperServerAddr(upperServerAddr);
                    settingsService.setUpperServerPort(Integer.parseInt(upperServerPort));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                flushStatus();
                flushSettings();
            }
        });







    }
}
