// ISettingsService.aidl
package com.hit_src.iot_terminal.service;

// Declare any non-default types here with import statements

interface ISettingsService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getUpperServerAddr();
    void setUpperServerAddr(in String addr);

    int getUpperServerModbusPort();
    void setUpperServerModbusPort(in int port);

    int getUpperServerXMLPort();
    void setUpperServerXMLPort(in int port);

    boolean getSerialQuerySetting();
    void setSerialQuerySetting(in boolean setting);
}
