package com.hit_src.iot_terminal.hardware;

public class SerialPort {
    public static native byte[] read(int length);

    public static native int write(byte[] content);
}
