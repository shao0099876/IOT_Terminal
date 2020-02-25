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

import java.util.List;

public class SerialUIHandler extends Handler {
    public static final int LIST_FLUSH=0;
    public static final int EDITAREA_FLUSH=1;
    public static final int EDITAREA_CLEAR=2;

    private Activity self;

    public SerialUIHandler(Context p){
        self= (Activity) p;
    }

    @Override
    public void handleMessage(Message msg){
        ListView listView=self.findViewById(R.id.Serial_sensorlist_ListView);
        EditText editText=self.findViewById(R.id.Serial_edit_ID_editText);
        Spinner spinner=self.findViewById(R.id.Serial_edit_type_Spinner);
        switch(msg.what){
            case LIST_FLUSH:
                listView.setAdapter((SensorListAdapterFactory.product(self, Database.getSensorList(), Status.readSensorSet())));
                break;
            case EDITAREA_FLUSH:
                int position=msg.getData().getInt("p1");
                List<Sensor> dbList=Database.getSensorList();
                Sensor selected=dbList.get(position);
                editText.setText(Integer.toString(selected.getID()));
                spinner.setSelection(selected.getTypeNum());
                break;
            case EDITAREA_CLEAR:
                editText.setText("");
                spinner.setSelection(-1);
                break;
        }
        super.handleMessage(msg);
    }

}
