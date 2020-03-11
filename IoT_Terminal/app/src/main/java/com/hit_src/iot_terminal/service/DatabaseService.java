package com.hit_src.iot_terminal.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService extends Service {
    public class DatabaseServiceImpl extends IDatabaseService.Stub{

        @Override
        public List getSensorList() throws RemoteException {
            SQLiteDatabase readDB=databaseOpenHelper.getReadableDatabase();
            Cursor cursor=readDB.query("Sensor",new String[]{"sensor_id","sensor_type"},null,
                    null,null,null,null);
            ArrayList<Sensor> res=new ArrayList<>();
            while(cursor.moveToNext()){
                res.add(new Sensor(cursor.getInt(0),cursor.getInt(1)));
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
            writeDB.insert("Sensor",null,contentValues);
        }

        @Override
        public void updateSensor(int ID, int type) throws RemoteException {
            SQLiteDatabase writeDB=databaseOpenHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put("sensor_type",type);
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
        static String dbCreateSQL="CREATE TABLE Sensor ( sensor_id INTEGER PRIMARY KEY, sensor_type integer NOT NULL );";

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
