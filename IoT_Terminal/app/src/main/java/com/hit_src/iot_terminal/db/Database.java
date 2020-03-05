package com.hit_src.iot_terminal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hit_src.iot_terminal.db.table.SensorTable;
import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;
import java.util.List;

public class Database {
    //DatabaseOpenHelper用于获取数据库对象
    private static DatabaseOpenHelper databaseOpenHelper;

    //数据库执行命令表
    public static final int READ_SENSOR_TABLE_ALL=0;
    public static final int ADD_SENSOR=1;
    public static final int UPDATE_SENSOR=2;
    public static final int DELETE_SENSOR=3;


    //构造函数，初始化databaseOpenHelper
    public Database(Context context){
        databaseOpenHelper=new DatabaseOpenHelper(context,"iot",null,1);
    }

    //处理数据库执行命令
    public static Object exec(int cmd,Object[] arg){
        Object res=null;
        switch(cmd){
            case READ_SENSOR_TABLE_ALL://读数据库
                res=SensorTable.getAllSensor(databaseOpenHelper.getReadableDatabase());
                break;
            case ADD_SENSOR:
                SensorTable.addSensor(databaseOpenHelper.getWritableDatabase(), (Sensor) arg[0]);
                break;
            case UPDATE_SENSOR:
                SensorTable.updateSensor(databaseOpenHelper.getWritableDatabase(),(int)arg[0],(int)arg[1]);
                break;
            case DELETE_SENSOR:
                SensorTable.delSensor(databaseOpenHelper.getWritableDatabase(),(int)arg[0]);
                break;
        }
        return res;
    }
}
