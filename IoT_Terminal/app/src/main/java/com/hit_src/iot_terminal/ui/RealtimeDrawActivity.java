package com.hit_src.iot_terminal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hit_src.iot_terminal.Global;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.DrawPoint;
import com.hit_src.iot_terminal.object.Sensor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

public class RealtimeDrawActivity extends AbstractActivity {
    private RealtimeDrawActivity self;
    private ListView sensorList;
    private LineChart lineChart;
    @Override
    protected void runOnBindService() {
        //Global.setSensorList(self,null,null,statusService,dbService,sensorList);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_draw);
        self=this;

        sensorList=findViewById(R.id.RealtimeDraw_Sensor_ListView);
        lineChart=findViewById(R.id.RealTimeDraw_LineChart);
        XAxis xAxis=lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setDragXEnabled(true);

        sensorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true) {
                            ArrayList<Sensor> dbList=null;
                            try {
                                dbList= (ArrayList<Sensor>) dbService.getSensorList();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            Sensor now=dbList.get(position);
                            List<DrawPoint> pointList=null;
                            try {
                                pointList=dbService.getDrawPointbySensor(now.ID);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                            final String[] xlinetag=new String[pointList.size()];
                            for(int i=0;i<xlinetag.length;i++){
                                DrawPoint tmp=pointList.get(i);
                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM/dd hh:mm:ss");
                                xlinetag[i]=simpleDateFormat.format(new Date(tmp.time));
                            }
                            ValueFormatter formatter=new ValueFormatter() {
                                @Override
                                public String getAxisLabel(float value, AxisBase axis) {
                                    return xlinetag[(int) value];
                                }
                            };

                            List<Entry> dataList=new ArrayList<>();
                            for(int i=0;i<pointList.size();i++){
                                dataList.add(new Entry((float) i,pointList.get(i).data));
                            }

                            LineDataSet lineDataSet=new LineDataSet(dataList,"");
                            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

                            List<ILineDataSet> lineDataSetList=new ArrayList<>();
                            lineDataSetList.add(lineDataSet);

                            LineData lineData=new LineData(lineDataSetList);
                            lineChart.setData(lineData);
                            lineChart.getXAxis().setValueFormatter(formatter);
                            lineChart.setVisibleXRange(10,20);
                            lineChart.moveViewToX(pointList.size());
                            try {
                                sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }


}
