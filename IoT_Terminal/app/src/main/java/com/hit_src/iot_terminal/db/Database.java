package com.hit_src.iot_terminal.db;

import android.app.Activity;
import android.util.Log;

import com.hit_src.iot_terminal.db.table.SensorTable;
import com.hit_src.iot_terminal.object.Sensor;

public class Database {
    //DatabaseOpenHelper用于获取数据库对象
    private static DatabaseOpenHelper databaseOpenHelper;

    //数据库执行命令表
    public static final int READ_SENSOR_TABLE_ALL=0;
    public static final int ADD_SENSOR=1;
    public static final int UPDATE_SENSOR=2;
    public static final int DELETE_SENSOR=3;

    private Activity self;
    //构造函数，初始化databaseOpenHelper
    public Database(Activity p){
        Log.d("Database","Database initing...");
        self=p;
        databaseOpenHelper=new DatabaseOpenHelper(self,"iot",null,1);
        Log.d("Database","Database inited");
    }

    //处理数据库执行命令
    public static Object exec(int cmd,Object[] arg){
        Object res=null;
        switch(cmd){
            case READ_SENSOR_TABLE_ALL://读数据库
                Log.d("Database","exec:"+"READ_SENSOR_TABLE_ALL");
                res=SensorTable.getAllSensor(databaseOpenHelper.getReadableDatabase());
                break;
            case ADD_SENSOR:
                Log.d("Database","exec:"+"ADD_SENSOR");
                SensorTable.addSensor(databaseOpenHelper.getWritableDatabase(), (Sensor) arg[0]);
                break;
            case UPDATE_SENSOR:
                Log.d("Database","exec:"+"UPDATE_SENSOR");
                SensorTable.updateSensor(databaseOpenHelper.getWritableDatabase(),(int)arg[0],(int)arg[1]);
                break;
            case DELETE_SENSOR:
                Log.d("Database","exec:"+"DELETE_SENSOR");
                SensorTable.delSensor(databaseOpenHelper.getWritableDatabase(),(int)arg[0]);
                break;
            default:
                Log.w("Database","exec:"+"NO_MATCHED_CASE!");
        }
        return res;
    }
}
