package com.hit_src.iot_terminal.tools;

import android.os.Handler;
import android.os.Message;

public class MessageThread {

    private MessageThread(){}
    public static void sendMessage(final Handler handler, final int command){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg=new Message();
                msg.what=command;
                handler.sendMessage(msg);
            }
        }).start();
    }

}
