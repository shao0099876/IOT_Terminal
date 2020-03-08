package com.hit_src.iot_terminal.profile.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import static java.lang.Thread.sleep;

public class InternetSettings {
    private Activity self;
    public InternetSettings(Activity p){
        Log.d("InternetSettings","InternetSettings initing...");
        self=p;
        file=self.getSharedPreferences(filename,Activity.MODE_PRIVATE);
        Log.d("InternetSettings","InternetSettings inited");
    }
    private static String filename="InternetSettings";
    private static SharedPreferences file=null;

    public static String getServer() {
        while(file==null){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
