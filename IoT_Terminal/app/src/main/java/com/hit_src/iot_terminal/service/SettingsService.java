package com.hit_src.iot_terminal.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class SettingsService extends Service {
    private SharedPreferences settingsFile;
    @Override
    public void onCreate(){
        super.onCreate();
        settingsFile=getSharedPreferences("settings",MODE_PRIVATE);
    }
    public class SettingsServiceImpl extends ISettingsService.Stub {

        @Override
        public String getUpperServerAddr() {
            return settingsFile.getString("UpperServerAddr","");
        }

        @Override
        public void setUpperServerAddr(String addr) {
            SharedPreferences.Editor editor=settingsFile.edit();
            editor.putString("UpperServerAddr",addr);
            editor.apply();
        }

        @Override
        public int getUpperServerModbusPort() {
            return settingsFile.getInt("UpperServerModbusPort",-1);
        }

        @Override
        public void setUpperServerModbusPort(int port) {
            SharedPreferences.Editor editor=settingsFile.edit();
            editor.putInt("UpperServerModbusPort",port);
            editor.apply();
        }

        @Override
        public int getUpperServerXMLPort() {
            return settingsFile.getInt("UpperServerXMLPort",-1);
        }

        @Override
        public void setUpperServerXMLPort(int port) {
            SharedPreferences.Editor editor=settingsFile.edit();
            editor.putInt("UpperServerXMLPort",port);
            editor.apply();
        }

        @Override
        public boolean getSerialQuerySetting() {
            return settingsFile.getBoolean("SerialQuerySetting",true);
        }

        @Override
        public void setSerialQuerySetting(boolean setting) {
            SharedPreferences.Editor editor=settingsFile.edit();
            editor.putBoolean("SerialQuerySetting",setting);
            editor.apply();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SettingsServiceImpl();
    }
}
