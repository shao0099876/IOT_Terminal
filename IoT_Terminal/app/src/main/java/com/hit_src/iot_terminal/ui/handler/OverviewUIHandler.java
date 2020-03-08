package com.hit_src.iot_terminal.ui.handler;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.db.Database;
import com.hit_src.iot_terminal.factory.SensorListAdapterFactory;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.profile.status.Status;
import com.hit_src.iot_terminal.ui.OverviewActivity;

import java.util.HashSet;
import java.util.List;

public class OverviewUIHandler extends Handler {
    public static final int SENSOR_UPDATE=1;
    public static final int INTERNET_UPDATE=2;

    public static final int SENSOR_ALLBROKEN=0;
    public static final int SENSOR_PARTBROKEN=1;
    public static final int SENSOR_NORMAL=2;

    public static final int INTERNET_NORMAL=0;
    public static final int INTERNET_BROKEN=1;

    public static final int OVERVIEW_NORMAL=0;
    public static final int OVERVIEW_PARTBROKEN=1;
    public static final int OVERVIEW_ALLBROKEN=2;

    private static boolean sensor_status=false;
    private static boolean internet_status=false;

    private OverviewActivity self;
    public OverviewUIHandler(OverviewActivity p){
        self=p;
    }
    private int judgeSensorStaus(HashSet<Integer> sensorList, List<Sensor> list){
        if(sensorList.isEmpty()){
            return SENSOR_ALLBROKEN;
        }
        if(sensorList.size()<list.size()){
            return SENSOR_PARTBROKEN;
        }
        if(sensorList.size()==list.size()){
            return SENSOR_NORMAL;
        }
        return -1;
    }
    private int judgeOverviewStatus(){
        int cnt=0;
        if(sensor_status){
            cnt+=1;
        }
        if(internet_status){
            cnt+=1;
        }
        switch(cnt){
            case 0:return OVERVIEW_ALLBROKEN;
            case 1:return OVERVIEW_PARTBROKEN;
            case 2:return OVERVIEW_NORMAL;
        }
        return -1;
    }
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case SENSOR_UPDATE:
                //获取ListView控件并设置适配器
                HashSet<Integer> sensorList= (HashSet<Integer>) Status.getStatusData(Status.SENSOR_LIST);//获取当前连接的传感器列表
                List<Sensor> list= (List<Sensor>) Database.exec(Database.READ_SENSOR_TABLE_ALL,null);
                ListView sensorListView=self.findViewById(R.id.Overview_sensorlist_ListView);
                sensorListView.setAdapter(SensorListAdapterFactory.product(self,list,sensorList));
                setSensorStatusShow(judgeSensorStaus(sensorList,list));
                break;
            case INTERNET_UPDATE:
                boolean status= (boolean) Status.getStatusData(Status.INTERNET_CONNECTION_STATUS);
                String lasttime= (String) Status.getStatusData(Status.INTERNET_CONNECTION_LASTTIME);
                setInternetStatusShow(status,lasttime);
                break;
            default:
                Log.w("OverviewStatusHandler","Not matched value!");
        }
        setOverviewStatusShow(judgeOverviewStatus());
        super.handleMessage(msg);
    }
    private void setOverviewStatusShow(int cmd){
        ImageView imageView=((Activity)self).findViewById(R.id.Overview_Overviewstatus_ImageView);
        TextView textView=((Activity)self).findViewById(R.id.Overview_OverviewStatus_TextView);
        switch(cmd){
            case OVERVIEW_ALLBROKEN:
                imageView.setImageResource(R.drawable.redlight);
                textView.setText(R.string.Overview_allbroken);
                break;
            case OVERVIEW_PARTBROKEN:
                imageView.setImageResource(R.drawable.yellowlight);
                textView.setText(R.string.Overview_partbroken);
                break;
            case OVERVIEW_NORMAL:
                imageView.setImageResource(R.drawable.greenlight);
                textView.setText(R.string.Overview_normal);
                break;
            default:
                Log.w("OverviewStatusHandler","Not Matched value!");
        }
    }
    private void setSensorStatusShow(int cmd) {
        ImageView imageView = ((Activity)self).findViewById(R.id.Overview_Serialstatus_ImageView);
        TextView textView = ((Activity)self).findViewById(R.id.Overview_Serialstatus_Textview);

        int imageID = 0;
        int textID = 0;
        switch (cmd){
            case SENSOR_ALLBROKEN:
                imageID=R.drawable.redlight;
                textID=R.string.Serialstatus_allbroken;
                sensor_status=false;
                break;
            case SENSOR_PARTBROKEN:
                imageID=R.drawable.yellowlight;
                textID=R.string.Serialstatus_partbroken;
                sensor_status=false;
                break;
            case SENSOR_NORMAL:
                imageID=R.drawable.greenlight;
                textID=R.string.Serialstatus_normal;
                sensor_status=true;
                break;
            default:
                Log.w("OverviewStatusHandler:","Not Matched value!");
        }
        imageView.setImageResource(imageID);
        textView.setText(textID);
    }
    private void setInternetStatusShow(boolean cmd,String lasttime){
        ImageView imageView=((Activity)self).findViewById(R.id.Overview_Internetstatus_Imageview);
        TextView textView=((Activity)self).findViewById(R.id.Overview_Internetstatus_Textview);
        TextView internetarea_connectionstatus_TextView=((Activity)self).findViewById(R.id.Overview_internet_status_TextView);
        TextView internetarea_connectionlasttime_TextView=((Activity)self).findViewById(R.id.Overview_internet_status_lasttime_TextView);
        if(cmd){
            imageView.setImageResource(R.drawable.greenlight);
            textView.setText(R.string.Internetstatus_normal);
            internetarea_connectionstatus_TextView.setText(R.string.Internet_connection_normal);
            internet_status=true;
        }
        else{
            imageView.setImageResource(R.drawable.redlight);
            textView.setText(R.string.Internetstatus_broken);
            internetarea_connectionstatus_TextView.setText(R.string.Internet_connection_failure);
            internet_status=false;
        }
        internetarea_connectionlasttime_TextView.setText(lasttime);
    }
}
