package com.hit_src.iot_terminal.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableList;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.DataRecord;
import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseService {

    private DatabaseOpenHelper databaseOpenHelper;
    private static DatabaseService self = null;
    private ArrayList<Sensor> sensors=null;

    public static DatabaseService getInstance() {
        if (self == null) {
            self = new DatabaseService();
        }
        return self;
    }

    private DatabaseService() {//初始化
        databaseOpenHelper = new DatabaseOpenHelper(MainApplication.self, "iot", null, 1);
        sensors=getSensorList();
        GlobalVar.sensorList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Sensor>>() {
            @Override
            public void onChanged(ObservableList<Sensor> sender) {

            }

            @Override
            public void onItemRangeChanged(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                for(int i=0;i<itemCount;i++){
                    Sensor now=sender.get(positionStart+i);
                    updateSensor(now);
                }
            }

            @Override
            public void onItemRangeInserted(ObservableList<Sensor> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeMoved(ObservableList<Sensor> sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                for(Sensor i:sensors){
                    boolean exists=false;
                    for(Sensor j:sender){
                        if(i.getID()==j.getID()){
                            exists=true;
                            break;
                        }
                    }
                    if(!exists){
                        delSensor(i.getID());
                    }
                }
            }
        });
    }



    static class DatabaseOpenHelper extends SQLiteOpenHelper {
        static String[] dbCreateSQL = new String[]{"CREATE TABLE Sensor ( sensor_id INTEGER PRIMARY KEY, sensor_type INTEGER NOT NULL, sensor_addr INTEGER NOT NULL, sensor_enabled INTEGER NOT NULL );",
                "CREATE TABLE SensorData ( SensorID INTEGER NOT NULL, time DATETIME PRIMARY KEY, data INTEGER NOT NULL);"};

        DatabaseOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for (String i : dbCreateSQL) {
                db.execSQL(i);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public ArrayList<Sensor> getSensorList() {
        SQLiteDatabase readDB = databaseOpenHelper.getReadableDatabase();
        Cursor cursor = readDB.query("Sensor", new String[]{"sensor_id", "sensor_type", "sensor_addr", "sensor_enabled"}, null,
                null, null, null, null);
        ArrayList<Sensor> res = new ArrayList<>();
        while (cursor.moveToNext()) {
            Sensor sensor = new Sensor();
            sensor.setID(cursor.getInt(0));
            sensor.setType(cursor.getInt(1));
            sensor.setLoraAddr(cursor.getInt(2));
            sensor.setEnabled(cursor.getInt(3) != 0);
            res.add(sensor);
        }
        cursor.close();
        return res;
    }

    public int getSensorAmount() {
        SQLiteDatabase readDB = databaseOpenHelper.getReadableDatabase();
        Cursor cursor = readDB.query("Sensor", new String[]{"sensor_id"}, null,
                null, null, null, null);
        int res = cursor.getCount();
        cursor.close();
        return res;
    }

    public void addSensor(int type, int loraAddr) {
        ArrayList<Sensor> list = getSensorList();
        int id = 0;
        for (Sensor i : list) {
            if (i.getID() >= id) {
                id = i.getID() + 1;
            }
        }
        SQLiteDatabase writeDB = databaseOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sensor_id", id);
        contentValues.put("sensor_type", type);
        contentValues.put("sensor_addr", loraAddr);
        contentValues.put("sensor_enabled", 1);
        writeDB.insert("Sensor", null, contentValues);
        GlobalVar.sensorList.clear();
        GlobalVar.sensorList.addAll(getSensorList());
    }
    public void updateSensor(Sensor p){
        SQLiteDatabase writeDB = databaseOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sensor_type", p.getType());
        contentValues.put("sensor_addr", p.getLoraAddr());
        contentValues.put("sensor_enabled", p.isEnabled()?1:0);
        writeDB.update("Sensor", contentValues, "sensor_id=?",
                new String[]{Integer.toString(p.getID())});
    }
    public void updateSensor(int ID, int type, int loraAddr, boolean enabled) {
        SQLiteDatabase writeDB = databaseOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sensor_type", type);
        contentValues.put("sensor_addr", loraAddr);
        contentValues.put("sensor_enabled", enabled);
        writeDB.update("Sensor", contentValues, "sensor_id=?",
                new String[]{Integer.toString(ID)});
        GlobalVar.sensorList.clear();
        GlobalVar.sensorList.addAll(getSensorList());
    }

    public void delSensor(int ID) {
        SQLiteDatabase writeDB = databaseOpenHelper.getWritableDatabase();
        String[] arg = new String[1];
        arg[0] = Integer.toString(ID);
        writeDB.delete("Sensor", "sensor_id=?", arg);
        writeDB.delete("SensorData", "SensorID=?", arg);
        sensors=getSensorList();
    }

    public void delSensorByType(int type) {
        SQLiteDatabase writeDB = databaseOpenHelper.getWritableDatabase();
        List<Sensor> list = getSensorList();
        for (Sensor i : list) {
            if (i.getType() == type) {
                String[] arg = new String[1];
                arg[0] = Integer.toString(i.getID());
                writeDB.delete("Sensor", "sensor_id=?", arg);
                writeDB.delete("SensorData", "SensorID=?", arg);
            }
        }
        GlobalVar.sensorList.clear();
        GlobalVar.sensorList.addAll(getSensorList());
    }

    public void addSensorData(int ID, int data) {
        SQLiteDatabase writeDB = databaseOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SensorID", ID);
        contentValues.put("time", new Date().getTime());
        contentValues.put("data", data);
        writeDB.insert("SensorData", null, contentValues);
    }

    public List getDataRecordbyAmount(int amount) throws RemoteException {
        List<DataRecord> res = new ArrayList<>();
        SQLiteDatabase readDB = databaseOpenHelper.getReadableDatabase();
        Cursor cursor = readDB.query("SensorData", new String[]{"SensorID", "time", "data"}, null,
                null, null, null, null);
        for (int i = 0; i < amount; i++) {
            cursor.moveToNext();
            res.add(new DataRecord(cursor.getInt(0), cursor.getLong(1), cursor.getInt(2)));
        }
        return res;
    }

    public void delDataRecordbyTime(long time) throws RemoteException {
        SQLiteDatabase writeDB = databaseOpenHelper.getWritableDatabase();
        writeDB.delete("SensorData", "time=?", new String[]{String.valueOf(time)});
    }

    public List getDrawPointbySensor(int sensorID) {
        List<DataRecord> res = new ArrayList<>();
        SQLiteDatabase readDB = databaseOpenHelper.getReadableDatabase();
        Cursor cursor = readDB.query("SensorData", new String[]{"time", "data"}, "SensorID=?",
                new String[]{Integer.toString(sensorID)}, null, null, null);
        while (cursor.moveToNext()) {
            long time = cursor.getLong(0);
            int data = cursor.getInt(1);
            res.add(new DataRecord(sensorID, time, data));
        }
        cursor.close();
        return res;
    }
    public List<DataRecord> getDataRecordsbySensorID(int id) {
        List<DataRecord> res=new ArrayList<>();
        SQLiteDatabase readDB = databaseOpenHelper.getReadableDatabase();
        Cursor cursor = readDB.query("SensorData", new String[]{"time", "data"}, "SensorID=?",
                new String[]{Integer.toString(id)}, null, null, null);
        while (cursor.moveToNext()) {
            long time = cursor.getLong(0);
            int data = cursor.getInt(1);
            res.add(new DataRecord(id, time, data));
        }
        cursor.close();
        return res;
    }
}
