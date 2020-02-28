package com.hit_src.iot_terminal.ui.handler;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.profile.settings.InternetSettings;
import com.hit_src.iot_terminal.profile.status.IPStatus;
import com.hit_src.iot_terminal.profile.status.Status;

public class InternetUIHandler extends Handler {
    public static final int STATUS_UPDATE=1;
    public static final int CONFIG_UPDATE=2;

    private Activity self;
    public InternetUIHandler(Context p){
        self= (Activity) p;
    }

    @Override
    public void handleMessage(Message msg){
        switch(msg.what){
            case STATUS_UPDATE:
                TextView linkTextView=self.findViewById(R.id.Internet_status_link_TextView);
                TextView lasttimeTextView=self.findViewById(R.id.Internet_status_lasttime_TextView);

                IPStatus status= Status.readInternetStatus();
                if(status.connected()){
                    linkTextView.setText(R.string.Internet_connection_normal);
                }
                else{
                    linkTextView.setText(R.string.Internet_connection_failure);
                }
                lasttimeTextView.setText(status.time());
                break;
            case CONFIG_UPDATE:
                EditText localIPEditText=self.findViewById(R.id.Internet_edit_IP_EditText);
                localIPEditText.setText(InternetSettings.getLocalIP());
                EditText netMaskEditText=self.findViewById(R.id.Internet_edit_NetMask_EditText);
                netMaskEditText.setText(InternetSettings.getNetMask());
                EditText gatewayEditText=self.findViewById(R.id.Internet_edit_Gateway_EditText);
                gatewayEditText.setText(InternetSettings.getGateway());
                Switch autoconfigSwitch=self.findViewById(R.id.Internet_edit_autoconfig_Switch);
                boolean autoconfig=InternetSettings.getAutoConfigSettings();
                autoconfigSwitch.setChecked(autoconfig);
                localIPEditText.setEnabled(!autoconfig);
                netMaskEditText.setEnabled(!autoconfig);
                gatewayEditText.setEnabled(!autoconfig);
                EditText serverEditText=self.findViewById(R.id.Internet_edit_server_EditText);
                serverEditText.setText(InternetSettings.getServer());
                EditText serverportEditText=self.findViewById(R.id.Internet_edit_serverport_EditText);
                serverportEditText.setText(InternetSettings.getServerPort());
                break;
        }
        super.handleMessage(msg);
    }
}
