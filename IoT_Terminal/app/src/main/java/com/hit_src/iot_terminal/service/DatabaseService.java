package com.hit_src.iot_terminal.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseService extends Service {
    public class DatabaseServiceImpl extends IDatabaseService.Stub{

        @Override
        public List getSensorList() throws RemoteException {
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
        public void addSensor(Sensor newsensor) throws RemoteException {
            SQLiteDatabase writeDB=databaseOpenHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put("sensor_id",newsensor.ID);
            contentValues.put("sensor_type",newsensor.type);
            contentValues.put("sensor_addr",newsensor.addr);
            writeDB.insert("Sensor",null,contentValues);
        }

        @Override
        public void updateSensor(int ID, int type,int addr) throws RemoteException {
            SQLiteDatabase writeDB=databaseOpenHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put("sensor_type",type);
            contentValues.put("sensor_addr",addr);
            writeDB.update("Sensor",contentValues,"sensor_id=?",
                    new String[]{Integer.toString(ID)});
        }

        @Override
        public void delSensor(int ID) throws RemoteException {
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

    }
    private DatabaseOpenHelper databaseOpenHelper;
    @Override
    public void onCreate(){//初始化
        super.onCreate();
        databaseOpenHelper=new DatabaseOpenHelper(this,"iot",null,1);
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SRCDEBUG","serviceonbind");
        return new DatabaseServiceImpl();
    }
    static class DatabaseOpenHelper extends SQLiteOpenHelper {
        static String dbCreateSQL= new StringBuilder().append("CREATE TABLE Sensor ( sensor_id INTEGER PRIMARY KEY, sensor_type integer NOT NULL, sensor_addr integer NOT NULL );")
                                                      .append("CREATE TABLE SensorData ( SensorID integer NOT NULL, time datetime PRIMARY KEY, data integer NOT NULL);").toString();

        public DatabaseOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(dbCreateSQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
