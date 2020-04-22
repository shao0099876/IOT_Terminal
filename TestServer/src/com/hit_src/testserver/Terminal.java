package com.hit_src.testserver;

public class Terminal {
    public static int DevCnt=-1;

    public static String toLog() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("设备数量:");
        stringBuilder.append(DevCnt==-1?"未知":DevCnt);
        stringBuilder.append("\n");
        return stringBuilder.toString().trim();
    }
}
