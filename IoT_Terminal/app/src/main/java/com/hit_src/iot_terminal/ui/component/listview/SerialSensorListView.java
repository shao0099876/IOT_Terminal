package com.hit_src.iot_terminal.ui.component.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.db.Database;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.tools.MessageThread;
import com.hit_src.iot_terminal.ui.SerialActivity;
import com.hit_src.iot_terminal.ui.handler.SerialUIHandler;

import java.util.List;

public class SerialSensorListView extends ListView {
    private Context context;
    public SerialSensorListView(Context context) {
        super(context);
        this.context=context;
        addListener();
    }

    public SerialSensorListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        addListener();
    }

    public SerialSensorListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        addListener();
    }
    private void addListener() {
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageThread.sendMessage(((SerialActivity)context).handler,SerialUIHandler.EDITAREA_FLUSH,position);
            }
        });
    }
}
