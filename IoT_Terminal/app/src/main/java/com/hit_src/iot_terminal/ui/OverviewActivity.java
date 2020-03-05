package com.hit_src.iot_terminal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.ui.handler.OverviewStatusHandler;
import com.hit_src.iot_terminal.tools.MessageThread;

public class OverviewActivity extends AppCompatActivity {
    /**
     * OverviewActivity是系统概述界面
     * OverviewStatusHandler用于处理界面显示变更
     * onNewIntent是固定的处理活动切换的生命周期函数
     * onCreate是固定的处理活动创建的生命周期函数
     * onResume是固定的处理活动显示的生命周期函数
     */
    public static OverviewStatusHandler handler;
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        handler=new OverviewStatusHandler(this);//注册界面变更handler
    }

    @Override
    protected void onResume(){
        super.onResume();
        MessageThread.sendMessage(handler,OverviewStatusHandler.SENSOR_UPDATE);//更新传感器部分显示
        MessageThread.sendMessage(handler,OverviewStatusHandler.INTERNET_UPDATE);//更新网络部分显示
    }
}