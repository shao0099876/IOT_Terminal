// IDatabaseService.aidl
package com.hit_src.iot_terminal.service;

// Declare any non-default types here with import statements
import com.hit_src.iot_terminal.object.Sensor;
interface IDatabaseService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    List getSensorList();
    void addSensor(in Sensor newsensor);
    void updateSensor(in int ID,in int type);
    void delSensor(in int ID);
}
