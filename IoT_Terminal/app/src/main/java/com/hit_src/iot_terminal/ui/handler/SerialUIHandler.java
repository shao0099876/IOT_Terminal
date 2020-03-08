package com.hit_src.iot_terminal.ui.handler;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.db.Database;
import com.hit_src.iot_terminal.factory.SensorListAdapterFactory;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.profile.status.Status;
import com.hit_src.iot_terminal.ui.SerialActivity;

import java.util.HashSet;
import java.util.List;

public class SerialUIHandler extends Handler {
    public static final int LIST_FLUSH=0;
    public static final int EDITAREA_FLUSH=1;
    public static final int EDITAREA_CLEAR=2;

    private SerialActivity self;
    public SerialUIHandler(SerialActivity p){
        self=p;
    }
    @Override
    public void handleMessage(Message msg){
        switch(msg.what){
            case LIST_FLUSH:
                setSensorListShow();
                break;
            case EDITAREA_FLUSH:
                setEditAreaShow(msg.getData().getInt("p1"));
                break;
            case EDITAREA_CLEAR:
                setEditAreaShow(-1);
                break;
        }
        super.handleMessage(msg);
    }
    private void setSensorListShow(){
        ListView listView=self.findViewById(R.id.Serial_sensorlist_ListView);
        listView.setAdapter((SensorListAdapterFactory.product(self, (List<Sensor>) Database.exec(Database.READ_SENSOR_TABLE_ALL,null), (HashSet<Integer>) Status.getStatusData(Status.SENSOR_LIST))));
    }
    private void setEditAreaShow(int pos){
        EditText editText=self.findViewById(R.id.Serial_edit_ID_editText);
        Spinner spinner=self.findViewById(R.id.Serial_edit_type_Spinner);
        if(pos==-1){
            editText.setText("");
            spinner.setSelection(-1);
            return;
        }
        List<Sensor> dbList= (List<Sensor>) Database.exec(Database.READ_SENSOR_TABLE_ALL,null);
        Sensor selected=dbList.get(pos);
        editText.setText(Integer.toString(selected.ID));
        spinner.setSelection(selected.type);
    }
}
