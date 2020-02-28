package com.hit_src.iot_terminal.profile.settings;

        import android.app.Activity;
        import android.content.SharedPreferences;

public class InternetSettings {
    private static String filename="InternetSettings";
    private static SharedPreferences file;
    public InternetSettings(Activity p){
        file=p.getSharedPreferences(filename,Activity.MODE_PRIVATE);
    }

    public static String getLocalIP() {
        return file.getString("localip","");
    }

    public static String getNetMask() {
        return file.getString("netmask","");
    }

    public static String getGateway() {
        return file.getString("gateway","");
    }

    public static boolean getAutoConfigSettings() {
        return file.getBoolean("autoconfig",false);
    }

    public static String getServer() {
        return file.getString("server","");
    }

    public static String getServerPort() {
        return Integer.toString(file.getInt("serverport",-1));
    }

    public static void setAutoConfig(boolean isChecked) {
        SharedPreferences.Editor editor=file.edit();
        editor.putBoolean("autoconfig",isChecked);
        editor.apply();
    }

    public static void setIP(String ip) {
        SharedPreferences.Editor editor=file.edit();
        editor.putString("localip",ip);
        editor.apply();
    }
    public static void setNetMask(String netMask){
        SharedPreferences.Editor editor=file.edit();
        editor.putString("netmask",netMask);
        editor.apply();
    }
    public static void setGateway(String gateway){
        SharedPreferences.Editor editor=file.edit();
        editor.putString("gateway",gateway);
        editor.apply();
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
