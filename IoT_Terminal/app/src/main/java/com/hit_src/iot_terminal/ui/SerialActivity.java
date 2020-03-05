package com.hit_src.iot_terminal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.tools.MessageThread;
import com.hit_src.iot_terminal.ui.handler.SerialUIHandler;

public class SerialActivity extends AppCompatActivity {
    /**
     * SerialUIHandler用于处理界面显示变更
     * onCreate用于处理界面创建的生命周期
     * onNewIntent用于处理界面切换的生命周期
     * onResume用于处理界面显示的生命周期
     */
    public SerialUIHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial);
        handler=new SerialUIHandler(this);

        //为传感器类型选择框填入数据，只需要在onCreate中执行一次即可
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item ,Sensor.typeList);
        Spinner spinner =findViewById(R.id.Serial_edit_type_Spinner);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MessageThread.sendMessage(handler,SerialUIHandler.LIST_FLUSH);//刷新传感器列表
    }

}
