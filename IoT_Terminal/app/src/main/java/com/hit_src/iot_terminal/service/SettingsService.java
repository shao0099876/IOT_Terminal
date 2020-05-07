package com.hit_src.iot_terminal.service;

import android.content.SharedPreferences;

import com.hit_src.iot_terminal.MainApplication;

import static android.content.Context.MODE_PRIVATE;

public class SettingsService {
    private SharedPreferences settingsFile;
    private static SettingsService self = null;

    public static SettingsService getInstance() {
        if (self == null) {
            self = new SettingsService();
        }
        return self;
    }

    private SettingsService() {
        settingsFile = MainApplication.self.getSharedPreferences("settings", MODE_PRIVATE);
    }

    public String getUpperServerAddr() {
        return settingsFile.getString("UpperServerAddr", "");
    }

    public void setUpperServerAddr(String addr) {
        SharedPreferences.Editor editor = settingsFile.edit();
        editor.putString("UpperServerAddr", addr);
        editor.commit();
    }

    public int getUpperServerModbusPort() {
        return settingsFile.getInt("UpperServerModbusPort", -1);
    }

    public void setUpperServerModbusPort(int port) {
        SharedPreferences.Editor editor = settingsFile.edit();
        editor.putInt("UpperServerModbusPort", port);
        editor.commit();
    }

    public int getUpperServerXMLPort() {
        return settingsFile.getInt("UpperServerXMLPort", -1);
    }

    public void setUpperServerXMLPort(int port) {
        SharedPreferences.Editor editor = settingsFile.edit();
        editor.putInt("UpperServerXMLPort", port);
        editor.commit();
    }

    public boolean getSerialQuerySetting() {
        return settingsFile.getBoolean("SerialQuerySetting", true);
    }

    public void setSerialQuerySetting(boolean setting) {
        SharedPreferences.Editor editor = settingsFile.edit();
        editor.putBoolean("SerialQuerySetting", setting);
        editor.commit();
    }

    public boolean getAlertEnableSetting() {
        return settingsFile.getBoolean("AlertEnableSetting",true);
    }
    public void setAlertEnableSetting(boolean setting){
        SharedPreferences.Editor editor = settingsFile.edit();
        editor.putBoolean("AlertEnableSetting", setting);
        editor.commit();
    }
}
