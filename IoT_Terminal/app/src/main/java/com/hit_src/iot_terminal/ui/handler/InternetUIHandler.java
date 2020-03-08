package com.hit_src.iot_terminal.ui.handler;

import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.hardware.EthernetNetworkCard;
import com.hit_src.iot_terminal.profile.settings.InternetSettings;
import com.hit_src.iot_terminal.profile.status.Status;
import com.hit_src.iot_terminal.ui.InternetActivity;

public class InternetUIHandler extends Handler {
    public static final int STATUS_UPDATE=1;
    public static final int CONFIG_UPDATE=2;

    private InternetActivity self;
    public InternetUIHandler(InternetActivity p){
        self=p;
    }

    @Override
    public void handleMessage(Message msg){
        switch(msg.what){
            case STATUS_UPDATE:
                setStatusShow((boolean) Status.getStatusData(Status.INTERNET_CONNECTION_STATUS),(String) Status.getStatusData(Status.INTERNET_CONNECTION_LASTTIME));
                break;
            case CONFIG_UPDATE:
                setConfigShow();
                break;
        }
        super.handleMessage(msg);
    }
    private void setStatusShow(boolean cmd,String lasttime){
        TextView linkTextView=self.findViewById(R.id.Internet_status_link_TextView);
        TextView lasttimeTextView=self.findViewById(R.id.Internet_status_lasttime_TextView);
        if(cmd){
            linkTextView.setText(R.string.Internet_connection_normal);
        }
        else{
            linkTextView.setText(R.string.Internet_connection_failure);
        }
        lasttimeTextView.setText(lasttime);
    }
    private void setConfigShow(){
        EditText localIPEditText=self.findViewById(R.id.Internet_edit_IP_EditText);
        localIPEditText.setEnabled(false);
        localIPEditText.setText(EthernetNetworkCard.getInfo(EthernetNetworkCard.ADDR));

        EditText netMaskEditText=self.findViewById(R.id.Internet_edit_NetMask_EditText);
        netMaskEditText.setEnabled(false);
        netMaskEditText.setText(EthernetNetworkCard.getInfo(EthernetNetworkCard.NETMASK));

        EditText gatewayEditText=self.findViewById(R.id.Internet_edit_Gateway_EditText);
        gatewayEditText.setEnabled(false);
        gatewayEditText.setText("none");

        EditText serverEditText=self.findViewById(R.id.Internet_edit_server_EditText);
        serverEditText.setText(InternetSettings.getServer());
        EditText serverportEditText=self.findViewById(R.id.Internet_edit_serverport_EditText);
        serverportEditText.setText(Integer.toString(InternetSettings.getServerPort()));
    }
}
