package com.hit_src.iot_terminal.ui.component.button.internet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.hit_src.iot_terminal.tools.MessageThread;
import com.hit_src.iot_terminal.ui.InternetActivity;
import com.hit_src.iot_terminal.ui.handler.InternetUIHandler;

@SuppressLint("AppCompatCustomView")
public class InternetStatusFlushButton extends Button {
    private InternetActivity self;
    public InternetStatusFlushButton(Context context) {
        super(context);
        self= (InternetActivity) context;
        setListener();
    }

    public InternetStatusFlushButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        self= (InternetActivity) context;
        setListener();
    }

    public InternetStatusFlushButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        self= (InternetActivity) context;
        setListener();
    }
    private void setListener(){
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageThread.sendMessage(self.handler, InternetUIHandler.STATUS_UPDATE);
            }
        });
    }
}
