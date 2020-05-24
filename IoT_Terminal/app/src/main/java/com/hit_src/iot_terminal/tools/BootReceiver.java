package com.hit_src.iot_terminal.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.ui.LogoActivity;

public class BootReceiver extends BroadcastReceiver {
    private final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION));
        {
            Intent intent2 = new Intent(context, LogoActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }
    }

}
