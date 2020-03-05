package com.hit_src.iot_terminal.ui.component.button.serial;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.db.Database;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.tools.MessageThread;
import com.hit_src.iot_terminal.ui.SerialActivity;
import com.hit_src.iot_terminal.ui.handler.SerialUIHandler;

import java.util.List;


@SuppressLint("AppCompatCustomView")
public class SerialAddButton extends Button {
    private Activity self;
    public SerialAddButton(Context context) {
        super(context);
        self=(Activity)context;
        addListener();
    }

    public SerialAddButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        self=(Activity)context;
        addListener();
    }

    public SerialAddButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        self=(Activity)context;
        addListener();
    }
    private void addListener(){
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Sensor> list= (List<Sensor>) Database.exec(Database.READ_SENSOR_TABLE_ALL,null);
                int id=cal_id(list);
                Spinner spinner=self.findViewById(R.id.Serial_edit_type_Spinner);
                int typenum=spinner.getSelectedItemPosition();
                Sensor newsensor=new Sensor(id,typenum);
                Database.exec(Database.ADD_SENSOR,new Sensor[]{newsensor});
                MessageThread.sendMessage(((SerialActivity)self).handler, SerialUIHandler.EDITAREA_CLEAR);
                MessageThread.sendMessage(((SerialActivity)self).handler, SerialUIHandler.LIST_FLUSH);
            }
        });
    }
    private int cal_id(List<Sensor> list){
        int res=0;
        for(Sensor sensor:list){
            res=max(res,sensor.ID);
        }
        return res+1;
    }
    private int max(int a,int b){
        if(a>b){
            return a;
        }
        return b;
    }
}
