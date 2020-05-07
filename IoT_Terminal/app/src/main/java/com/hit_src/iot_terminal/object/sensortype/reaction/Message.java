package com.hit_src.iot_terminal.object.sensortype.reaction;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.object.Environment;
import com.hit_src.iot_terminal.service.SettingsService;

import static java.lang.Thread.sleep;

public class Message extends React {
    public String pattern;
    public Message(String trim) {
        super();
        pattern=trim;
    }

    @Override
    public void react(Environment environment) {
        String s=pattern.replace("{$sensor}",environment.sensor.getID()+"号传感器");
        s=s.replace("{$value}",String.valueOf(environment.value));
        final String ss=s;
        if(MainActivity.self!=null){
            MainActivity.self.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(MainActivity.alertDialog!=null){
                        MainActivity.alertDialog.dismiss();
                    }
                    MainActivity.alertDialog=new AlertDialog.Builder(MainActivity.self).setTitle("警告！").setMessage(ss)
                            .setPositiveButton("我已了解，关闭警报3分钟", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SettingsService.getInstance().setAlertEnableSetting(false);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                sleep(180000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            SettingsService.getInstance().setAlertEnableSetting(true);
                                        }
                                    });
                                }
                            }).create();
                    MainActivity.alertDialog.show();
                }
            });
        }

    }
}
