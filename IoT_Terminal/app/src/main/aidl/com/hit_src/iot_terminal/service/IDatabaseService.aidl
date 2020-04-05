// IDatabaseService.aidl
package com.hit_src.iot_terminal.service;

// Declare any non-default types here with import statements
interface IDatabaseService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    List getSensorList();
    int getSensorAmount();
    void addSensor(in String type,in int addr);
    void updateSensor(in int ID,in String type,in int addr,in boolean enabled);
    void delSensor(in int ID);

    void addSensorData(in int sensorID,in int data);
    List getDrawPointbySensor(in int sensorID);
}
