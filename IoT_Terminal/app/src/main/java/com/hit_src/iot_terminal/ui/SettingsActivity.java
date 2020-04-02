package com.hit_src.iot_terminal.ui;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.hit_src.iot_terminal.R;

public class SettingsActivity extends AbstractActivity {
    private Switch serialQuerySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        serialQuerySwitch=findViewById(R.id.Settings_serialquery_Switch);

        serialQuerySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    settingsService.setSerialQuerySetting(isChecked);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void runOnBindService() {
        flush();
    }
    private void flush(){
        //TODO: 刷新界面显示
        boolean serialQuerySwitchData = false;
        try {
            serialQuerySwitchData=settingsService.getSerialQuerySetting();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        final boolean finalSerialQuerySwitchData = serialQuerySwitchData;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                serialQuerySwitch.setChecked(finalSerialQuerySwitchData);
            }
        });


    }
}
