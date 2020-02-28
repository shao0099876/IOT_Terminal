package com.hit_src.iot_terminal.ui.component.button.internet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.profile.settings.InternetSettings;
import com.hit_src.iot_terminal.ui.InternetActivity;

@SuppressLint("AppCompatCustomView")
public class InternetEditSaveButton extends Button {
    private InternetActivity self;
    public InternetEditSaveButton(Context context) {
        super(context);
        self= (InternetActivity) context;
        setListener();
    }

    public InternetEditSaveButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        self= (InternetActivity) context;
        setListener();
    }

    public InternetEditSaveButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        self= (InternetActivity) context;
        setListener();
    }
    private void setListener(){
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=self.findViewById(R.id.Internet_edit_IP_EditText);
                String ip=editText.getText().toString();
                if(!check(ip)){
                    new AlertDialog.Builder(self).setTitle("输入格式错误！").setMessage("IP格式错误").show();
                    return;
                }
                editText=self.findViewById(R.id.Internet_edit_NetMask_EditText);
                String netMask=editText.getText().toString();
                if(!check(netMask)){
                    new AlertDialog.Builder(self).setTitle("输入格式错误！").setMessage("子网掩码格式错误").show();
                    return;
                }
                editText=self.findViewById(R.id.Internet_edit_Gateway_EditText);
                String gateway=editText.getText().toString();
                if(!check(gateway)){
                    new AlertDialog.Builder(self).setTitle("输入格式错误！").setMessage("子网掩码格式错误").show();
                    return;
                }
                editText=self.findViewById(R.id.Internet_edit_server_EditText);
                String server=editText.getText().toString();
                editText=self.findViewById(R.id.Internet_edit_serverport_EditText);
                int port=Integer.valueOf(editText.getText().toString());
                InternetSettings.setIP(ip);
                InternetSettings.setNetMask(netMask);
                InternetSettings.setGateway(gateway);
                InternetSettings.setServer(server);
                InternetSettings.setServerPort(port);
            }
        });
    }

    private boolean check(String ip) {
        if(ip.isEmpty()){
            return true;
        }
        int cnt=0;
        for(int i=0;i<ip.length();i++){
            char c=ip.charAt(i);
            if(!Character.isDigit(c)&&c!='.'){
                return false;
            }
            if(c=='.'){
                cnt+=1;
            }
        }
        if(cnt!=3){
            return false;
        }
        String[] tmp=ip.split(".");
        for(int i=0;i<tmp.length;i++){
            int x=Integer.valueOf(tmp[i]);
            if(x<0||x>255){
                return false;
            }
        }
        return true;
    }
}
