package com.hit_src.iot_terminal.ui.component.switch_widet;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.hit_src.iot_terminal.profile.settings.InternetSettings;
import com.hit_src.iot_terminal.tools.MessageThread;
import com.hit_src.iot_terminal.ui.InternetActivity;
import com.hit_src.iot_terminal.ui.handler.InternetUIHandler;

public class InternetAutoConfigSwitch extends Switch {
    private InternetActivity self;
    public InternetAutoConfigSwitch(Context context) {
        super(context);
        self= (InternetActivity) context;
        setListener();
    }

    public InternetAutoConfigSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        self= (InternetActivity) context;
        setListener();
    }

    public InternetAutoConfigSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        self= (InternetActivity) context;
        setListener();
    }
    private void setListener(){
        this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InternetSettings.setAutoConfig(isChecked);
                MessageThread.sendMessage(self.handler, InternetUIHandler.CONFIG_UPDATE);
            }
        });
    }
}
