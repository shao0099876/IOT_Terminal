package com.hit_src.iot_terminal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.tools.MessageThread;
import com.hit_src.iot_terminal.ui.handler.InternetUIHandler;

public class InternetActivity extends AppCompatActivity {

    public InternetUIHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        handler=new InternetUIHandler(this);
    }
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        MessageThread.sendMessage(handler,InternetUIHandler.STATUS_UPDATE);//状态更新
        MessageThread.sendMessage(handler,InternetUIHandler.CONFIG_UPDATE);//配置更新
    }
}
