package com.hit_src.iot_terminal;

import android.os.RemoteException;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hit_src.iot_terminal.factory.SensorListAdapterFactory;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.service.IDatabaseService;
import com.hit_src.iot_terminal.service.IStatusService;
import com.hit_src.iot_terminal.ui.AbstractActivity;
import com.hit_src.iot_terminal.ui.OverviewActivity;

import java.util.ArrayList;

public class Global {
    static {

    }
    public static void setSensorList(AbstractActivity self, ArrayList<Integer> stList, ArrayList<Sensor> dbList, IStatusService statusService, IDatabaseService dbService, final ListView listView){
        //通用的传感器列表设置函数
        if(stList==null){
            try {
                stList= (ArrayList<Integer>) statusService.getSensorList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if(dbList==null){
            try {
                dbList= (ArrayList<Sensor>) dbService.getSensorList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        final SimpleAdapter adapter= SensorListAdapterFactory.product(self,dbList,stList);
        self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(adapter);
            }
        });
    }
    public static native byte[] CRC(byte[] data);
}
