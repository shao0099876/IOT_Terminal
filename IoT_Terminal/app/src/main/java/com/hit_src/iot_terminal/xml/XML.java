package com.hit_src.iot_terminal.xml;

public class XML {
    public String name;
    public int version;

    public XML(String s) {
        String[] ss = s.split(" ");
        name = ss[0];
        version = Integer.parseInt(ss[1]);
    }
}
