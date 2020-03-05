package com.hit_src.iot_terminal.ui.handler;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.db.Database;
import com.hit_src.iot_terminal.factory.SensorListAdapterFactory;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.profile.status.Status;

import java.util.HashSet;
import java.util.List;

public class OverviewStatusHandler extends Handler {
    public static final int SENSOR_UPDATE=1;
    public static final int INTERNET_UPDATE=2;

    public static final int SENSOR_ALLBROKEN=0;
    public static final int SENSOR_PARTBROKEN=1;
    public static final int SENSOR_NORMAL=2;


    private static boolean sensor_status=false;
    private static boolean internet_status=false;

    private Activity self;
    public OverviewStatusHandler(Context p){
        self= (Activity) p;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case SENSOR_UPDATE:
                HashSet<Integer> sensorList= (HashSet<Integer>) Status.getStatusData(Status.SENSOR_LIST);//获取当前连接的传感器列表

                List<Sensor> list= (List<Sensor>) Database.exec(Database.READ_SENSOR_TABLE_ALL,null);

                //获取ListView控件并设置适配器
                ListView sensorListView=self.findViewById(R.id.Overview_sensorlist_ListView);
                sensorListView.setAdapter(SensorListAdapterFactory.product(self,list,sensorList));

                //通过sensorList的个数来确定当前的状态
                if(sensorList.isEmpty()) {
                    setSensor(SENSOR_ALLBROKEN);
                }
                else if (sensorList.size()<list.size()) {
                    setSensor(SENSOR_PARTBROKEN);

                }
                else if(sensorList.size()==list.size()) {
                    setSensor(SENSOR_NORMAL);
                }
                break;
            case INTERNET_UPDATE:
                int internetConnection= (int) Status.getStatusData(Status.INTERNET_CONNECTION_STATUS);
                String lasttime= (String) Status.getStatusData(Status.INTERNET_CONNECTION_LASTTIME);
                ImageView imageView=((Activity)self).findViewById(R.id.Overview_Internetstatus_Imageview);
                TextView textView=((Activity)self).findViewById(R.id.Overview_Internetstatus_Textview);
                TextView internetarea_connectionstatus_TextView=((Activity)self).findViewById(R.id.Overview_internet_status_TextView);
                TextView internetarea_connectionlasttime_TextView=((Activity)self).findViewById(R.id.Overview_internet_status_lasttime_TextView);
                switch(internetConnection){
                    case 0:
                        imageView.setImageResource(R.drawable.greenlight);
                        textView.setText(R.string.Internetstatus_normal);
                        internetarea_connectionstatus_TextView.setText(R.string.Internet_connection_normal);
                        internet_status=true;
                        break;
                    case 1:
                        imageView.setImageResource(R.drawable.redlight);
                        textView.setText(R.string.Internetstatus_broken);
                        internetarea_connectionstatus_TextView.setText(R.string.Internet_connection_failure);
                        internet_status=false;
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.redlight);
                        textView.setText(R.string.Internetstatus_broken);
                        internetarea_connectionstatus_TextView.setText(R.string.Internet_connection_heartfailed);
                        internet_status=false;
                        break;
                }
                internetarea_connectionlasttime_TextView.setText(lasttime);
                break;
        }
        ImageView imageView=((Activity)self).findViewById(R.id.Overview_Overviewstatus_ImageView);
        TextView textView=((Activity)self).findViewById(R.id.Overview_OverviewStatus_TextView);
        int cnt=0;
        if(sensor_status){
            cnt+=1;
        }
        if(internet_status){
            cnt+=1;
        }
        switch(cnt){
            case 0:
                imageView.setImageResource(R.drawable.redlight);
                textView.setText(R.string.Overview_allbroken);
                break;
            case 1:
                imageView.setImageResource(R.drawable.yellowlight);
                textView.setText(R.string.Overview_partbroken);
                break;
            case 2:
                imageView.setImageResource(R.drawable.greenlight);
                textView.setText(R.string.Overview_normal);
                break;
        }
        super.handleMessage(msg);
    }

    private void setSensor(int cmd) {
        ImageView imageView = ((Activity)self).findViewById(R.id.Overview_Serialstatus_ImageView);
        TextView textView = ((Activity)self).findViewById(R.id.Overview_Serialstatus_Textview);

        int imageID;
        int textID;
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
                throw new IllegalStateException("Unexpected value: " + cmd);
        }
        imageView.setImageResource(imageID);
        textView.setText(textID);
    }
}
