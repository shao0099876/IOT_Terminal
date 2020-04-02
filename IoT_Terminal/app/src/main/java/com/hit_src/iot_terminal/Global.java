package com.hit_src.iot_terminal;

import android.os.RemoteException;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hit_src.iot_terminal.factory.SensorListAdapterFactory;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.service.IDatabaseService;
import com.hit_src.iot_terminal.ui.AbstractActivity;

import java.util.ArrayList;

public class Global {
    static {

    }

    public static native byte[] CRC(byte[] data);
}
