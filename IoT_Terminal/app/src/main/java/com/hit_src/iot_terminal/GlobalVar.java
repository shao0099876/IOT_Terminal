package com.hit_src.iot_terminal;

import androidx.databinding.ObservableArrayList;

import com.hit_src.iot_terminal.object.Sensor;

public class GlobalVar {
    public volatile static ObservableArrayList<Sensor> sensorList = new ObservableArrayList<>();
}
