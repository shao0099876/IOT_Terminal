// IStatusService.aidl
package com.hit_src.iot_terminal.service;

// Declare any non-default types here with import statements
interface IStatusService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List getSensorList();
    boolean getInternetConnectionStatus();
    long getInternetConnectionLasttime();
    void setSensorStatus(in int i, in boolean status);
}
