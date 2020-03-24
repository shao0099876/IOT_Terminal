package com.hit_src.iot_terminal.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.hit_src.iot_terminal.object.DrawPoint;
import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseService extends Service {
    public class DatabaseServiceImpl extends IDatabaseService.Stub{

        @Override
        public List getSensorList() {
            SQLiteDatabase readDB=databaseOpenHelper.getReadableDatabase();
            Cursor cursor=readDB.query("Sensor",new String[]{"sensor_id","sensor_type","sensor_addr"},null,
                    null,null,null,null);
            ArrayList<Sensor> res=new ArrayList<>();
            while(cursor.moveToNext()){
                res.add(new Sensor(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2)));
            }
            cursor.close();
            return res;
        }

        @Override
        public void addSensor(Sensor newsensor) {
            SQLiteDatabase writeDB=databaseOpenHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put("sensor_id",newsensor.ID);
            contentValues.put("sensor_type",newsensor.type);
            contentValues.put("sensor_addr",newsensor.addr);
            writeDB.insert("Sensor",null,contentValues);
        }

        @Override
        public void updateSensor(int ID, int type,int addr) {
            SQLiteDatabase writeDB=databaseOpenHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put("sensor_type",type);
            contentValues.put("sensor_addr",addr);
            writeDB.update("Sensor",contentValues,"sensor_id=?",
                    new String[]{Integer.toString(ID)});
        }

        @Override
        public void delSensor(int ID) {
            SQLiteDatabase writeDB=databaseOpenHelper.getWritableDatabase();
            String[] arg=new String[1];
            arg[0]=Integer.toString(ID);
            writeDB.delete("Sensor","sensor_id=?",arg);
        }

        @Override
        public void addSensorData(int ID,int data){
            SQLiteDatabase writeDB=databaseOpenHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put("SensorID",ID);
            contentValues.put("time",new Date().getTime());
            contentValues.put("data",data);
            writeDB.insert("SensorData",null,contentValues);
        }

        @Override
        public List getDrawPoint(int sensorID) {
            List<DrawPoint> res=new ArrayList<>();
            SQLiteDatabase readDB=databaseOpenHelper.getReadableDatabase();
            Cursor cursor=readDB.query("SensorData",new String[]{"time","data"},"SensorID=?",
                    new String[]{Integer.toString(sensorID)},null,null,null);
            while(cursor.moveToNext()){
                long time=cursor.getLong(0);
                int data=cursor.getInt(1);
                res.add(new DrawPoint(time,data));
            }
            cursor.close();
            return res;
        }


    }
    private DatabaseOpenHelper databaseOpenHelper;
    @Override
    public void onCreate(){//初始化
        super.onCreate();
        databaseOpenHelper=new DatabaseOpenHelper(this,"iot",null,1);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new DatabaseServiceImpl();
    }
    static class DatabaseOpenHelper extends SQLiteOpenHelper {
        static String[] dbCreateSQL=new String[]{"CREATE TABLE Sensor ( sensor_id INTEGER PRIMARY KEY, sensor_type INTEGER NOT NULL, sensor_addr INTEGER NOT NULL );",
                                                 "CREATE TABLE SensorData ( SensorID INTEGER NOT NULL, time DATETIME PRIMARY KEY, data INTEGER NOT NULL);"};
        DatabaseOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for(String i:dbCreateSQL){
                db.execSQL(i);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
