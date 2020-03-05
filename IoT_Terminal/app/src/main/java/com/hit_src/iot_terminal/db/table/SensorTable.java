package com.hit_src.iot_terminal.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;
import java.util.List;

public class SensorTable {
    public static String dbCreateSQL="CREATE TABLE Sensor ( sensor_id INTEGER PRIMARY KEY, sensor_type integer NOT NULL );";

    public static List<Sensor> getAllSensor(SQLiteDatabase db) {
        Cursor cursor=db.query("Sensor", new String[]{"sensor_id", "sensor_type"},null,
                null,null,null,null);
        List<Sensor> res= new ArrayList<>();
        while(cursor.moveToNext()){
            res.add(new Sensor(cursor.getInt(0),cursor.getInt(1)));
        }
        cursor.close();
        return res;
    }

    public static void addSensor(SQLiteDatabase db,Sensor newsensor) {
        ContentValues contentValues=new ContentValues();
        contentValues.put("sensor_id",newsensor.ID);
        contentValues.put("sensor_type",newsensor.type);
        db.insert("Sensor",null,contentValues);
    }

    public static void updateSensor(SQLiteDatabase db, int id, int typenum) {
        ContentValues contentValues=new ContentValues();
        contentValues.put("sensor_type",typenum);
        db.update("Sensor",contentValues,"sensor_id=?", new String[]{Integer.toString(id)});
    }

    public static void delSensor(SQLiteDatabase db, int id) {
        String[] arg=new String[1];
        arg[0]=Integer.toString(id);
        db.delete("Sensor","sensor_id=?",arg);
    }
}
