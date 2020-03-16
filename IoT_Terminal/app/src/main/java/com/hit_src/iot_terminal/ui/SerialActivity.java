package com.hit_src.iot_terminal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.factory.SensorListAdapterFactory;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.service.IDatabaseService;
import com.hit_src.iot_terminal.service.IStatusService;

import java.util.ArrayList;

public class SerialActivity extends AbstractActivity {

    @Override
    protected void runOnBindService(){
        flushList();
    }
    private ListView sensorListView;
    private EditText idEditText;
    private Spinner spinner;
    private EditText addrEditText;

    private SerialActivity self;

    private void flushList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SimpleAdapter adapter=SensorListAdapterFactory.product(self, dbService.getSensorList(), (ArrayList<Integer>) statusService.getSensorList());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sensorListView.setAdapter(adapter);
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void flushEditArea(final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Sensor> dbList=null;
                try {
                    dbList= (ArrayList<Sensor>) dbService.getSensorList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                final Sensor selected=dbList.get(position);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        idEditText.setText(Integer.toString(selected.ID));
                        spinner.setSelection(selected.type);
                    }
                });
            }
        }).start();
    }
    private void clearEditArea(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        idEditText.setText("");
                        spinner.setSelection(-1);
                    }
                });
            }
        }).start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial);
        self=this;

        sensorListView=findViewById(R.id.Serial_sensorlist_ListView);
        Button flushButton = findViewById(R.id.Serial_flush_Button);
        Button addButton = findViewById(R.id.Serial_add_Button);
        Button delButton = findViewById(R.id.Serial_delete_Button);
        Button editButton = findViewById(R.id.Serial_edit_button);
        idEditText=findViewById(R.id.Serial_edit_ID_editText);
        spinner=findViewById(R.id.Serial_edit_type_Spinner);
        addrEditText=findViewById(R.id.Serial_edit_addr_editText);

        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flushEditArea(position);
            }
        });
        flushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flushList();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Sensor> list=null;
                try {
                    list= (ArrayList<Sensor>) dbService.getSensorList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                int id=0;
                for(Sensor sensor:list){
                    if(id<sensor.ID){
                        id=sensor.ID;
                    }
                }
                id+=1;
                int typenum=spinner.getSelectedItemPosition();
                String addr_raw=addrEditText.getText().toString();
                int addr=Integer.parseInt(addr_raw);
                Sensor newsensor=new Sensor(id,typenum,addr);
                try {
                    dbService.addSensor(newsensor);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                flushList();
                clearEditArea();
            }
        });
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp=String.valueOf(idEditText.getText());
                if(tmp.isEmpty()){
                    new AlertDialog.Builder(self).setTitle("警告").setMessage("未选中要删除的传感器").show();
                }
                else{
                    int id=Integer.parseInt(tmp);
                    try {
                        dbService.delSensor(id);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    clearEditArea();
                    flushList();
                }
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp=String.valueOf(idEditText.getText());
                if(tmp.isEmpty()){
                    new AlertDialog.Builder(self).setTitle("警告").setMessage("未选中要编辑的传感器").show();
                }
                else{
                    int id=Integer.parseInt(tmp);
                    int typenum=spinner.getSelectedItemPosition();
                    String addr_raw=addrEditText.getText().toString();
                    int addr=Integer.parseInt(addr_raw);
                    try {
                        dbService.updateSensor(id,typenum,addr);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    clearEditArea();
                    flushList();
                }
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Sensor.typeList);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent("com.hit_src.iot_terminal.service.IStatusService"),statusServiceConnection,BIND_AUTO_CREATE);
        bindService(new Intent("com.hit_src.iot_terminal.service.IDatabaseService"),dbServiceConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(dbServiceConnection);
        unbindService(statusServiceConnection);
    }


}