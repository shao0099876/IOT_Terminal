package com.hit_src.iot_terminal.ui.component.button.serial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.db.Database;
import com.hit_src.iot_terminal.factory.SensorListAdapterFactory;
import com.hit_src.iot_terminal.profile.Status;
import com.hit_src.iot_terminal.tools.MessageThread;
import com.hit_src.iot_terminal.ui.SerialActivity;
import com.hit_src.iot_terminal.ui.handler.SerialUIHandler;

@SuppressLint("AppCompatCustomView")
public class SerialFlushButton extends Button {
    public SerialFlushButton(Context context) {
        super(context);
        addListener();
    }

    public SerialFlushButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        addListener();
    }

    public SerialFlushButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addListener();
    }
    private void addListener(){
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageThread.sendMessage(((SerialActivity)v.getContext()).handler, SerialUIHandler.LIST_FLUSH);
            }
        });
    }
}
