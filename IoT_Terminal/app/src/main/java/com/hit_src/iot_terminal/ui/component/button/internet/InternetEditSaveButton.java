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
                EditText editText=self.findViewById(R.id.Internet_edit_server_EditText);
                String server=editText.getText().toString();
                editText=self.findViewById(R.id.Internet_edit_serverport_EditText);
                int port=Integer.valueOf(editText.getText().toString());
                InternetSettings.setServer(server);
                InternetSettings.setServerPort(port);
                new AlertDialog.Builder(self).setTitle("成功").setMessage("网络参数设置成功！").show();
            }
        });
    }

}
