package com.hit_src.iot_terminal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static DatabaseOpenHelper databaseOpenHelper;
    public Database(Context context){
        databaseOpenHelper=new DatabaseOpenHelper(context,"iot",null,1);
    }
    public static List<Sensor> getSensorList(){
        SQLiteDatabase db=databaseOpenHelper.getReadableDatabase();
        Cursor cursor=db.query("Sensor", new String[]{"sensor_id", "sensor_type"},null,null,null,null,null);
        List<Sensor> res= new ArrayList<>();
        while(cursor.moveToNext()){
            res.add(new Sensor(cursor.getInt(0),cursor.getInt(1)));
        }
        cursor.close();
        return res;
    }

    public static void addSensor(Sensor newsensor) {
        SQLiteDatabase db=databaseOpenHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("sensor_id",newsensor.getID());
        contentValues.put("sensor_type",newsensor.getTypeNum());
        db.insert("Sensor",null,contentValues);
    }

    public static void delSensor(int id) {
        SQLiteDatabase db=databaseOpenHelper.getWritableDatabase();
        String[] arg=new String[1];
        arg[0]=Integer.toString(id);
        db.delete("Sensor","sensor_id=?",arg);
    }

    public static void updateSensor(int id, int typenum) {
        SQLiteDatabase db=databaseOpenHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("sensor_type",typenum);
        db.update("Sensor",contentValues,"sensor_id=?", new String[]{Integer.toString(id)});
    }
}
