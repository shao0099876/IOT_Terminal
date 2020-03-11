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
import com.hit_src.iot_terminal.profile.settings.InternetSettings;
import com.hit_src.iot_terminal.service.IStatusService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InternetActivity extends AppCompatActivity {
    private IStatusService statusService;
    private ServiceConnection statusServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            statusService=IStatusService.Stub.asInterface(service);
            updateStatus();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private InternetActivity self;
    private TextView statusTextView;
    private TextView lasttimeTextView;
    private EditText ipEditText;
    private EditText netmaskEditText;
    private EditText gatewayEditText;
    private EditText serverEditText;
    private EditText portEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        self=this;

        statusTextView=findViewById(R.id.Internet_status_link_TextView);
        lasttimeTextView=findViewById(R.id.Internet_status_lasttime_TextView);
        ipEditText=findViewById(R.id.Internet_edit_IP_EditText);
        netmaskEditText=findViewById(R.id.Internet_edit_NetMask_EditText);
        gatewayEditText=findViewById(R.id.Internet_edit_Gateway_EditText);
        serverEditText=findViewById(R.id.Internet_edit_server_EditText);
        portEditText=findViewById(R.id.Internet_edit_serverport_EditText);
        Button flushButton = findViewById(R.id.Internet_status_reflush_Button);
        Button editButton = findViewById(R.id.Internet_edit_button);

        flushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String server=serverEditText.getText().toString();
                int port=Integer.parseInt(portEditText.getText().toString());
                InternetSettings.setServer(server);
                InternetSettings.setServerPort(port);
                new AlertDialog.Builder(self).setTitle("成功").setMessage("网络参数设置成功！").show();
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        bindService(new Intent("com.hit_src.iot_terminal.service.IStatusService"),statusServiceConnection,BIND_AUTO_CREATE);
        updateConfig();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(statusServiceConnection);
    }
    private void updateStatus(){
        boolean connection=false;
        try {
            connection=statusService.getInternetConnectionStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        long lasttime_tmp=0;
        try {
            lasttime_tmp=statusService.getInternetConnectionLasttime();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String lasttime=simpleDateFormat.format(new Date(lasttime_tmp));
        if(connection){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusTextView.setText(R.string.Internet_connection_normal);
                }
            });
        } else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusTextView.setText(R.string.Internet_connection_failure);
                }
            });
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lasttimeTextView.setText(lasttime);
            }
        });
    }
    private void updateConfig(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ipEditText.setEnabled(false);
                        ipEditText.setText(EthernetNetworkCard.getInfo(EthernetNetworkCard.ADDR));
                        netmaskEditText.setEnabled(false);
                        netmaskEditText.setText(EthernetNetworkCard.getInfo(EthernetNetworkCard.NETMASK));
                        gatewayEditText.setEnabled(false);
                        gatewayEditText.setText("none");
                        serverEditText.setText(InternetSettings.getServer());
                        portEditText.setText(Integer.toString(InternetSettings.getServerPort()));
                    }
                });
            }
        }).start();
    }
}
