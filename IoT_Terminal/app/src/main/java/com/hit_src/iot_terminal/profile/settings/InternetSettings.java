package com.hit_src.iot_terminal.profile.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.hit_src.iot_terminal.hardware.EthernetNetworkCard;

public class InternetSettings {
    private static String filename="InternetSettings";
    private static SharedPreferences file;
    public InternetSettings(Activity p){
        file=p.getSharedPreferences(filename,Activity.MODE_PRIVATE);
    }

    public static String getServer() {
        return file.getString("server","");
    }

    public static int getServerPort() {
        return file.getInt("serverport",-1);
    }

    public static void setServer(String server){
        SharedPreferences.Editor editor=file.edit();
        editor.putString("server",server);
        editor.apply();
    }
    public static void setServerPort(int port){
        SharedPreferences.Editor editor=file.edit();
        editor.putInt("serverport",port);
        editor.apply();
    }
}
