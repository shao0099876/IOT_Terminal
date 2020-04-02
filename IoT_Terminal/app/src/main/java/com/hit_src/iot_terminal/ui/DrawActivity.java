package com.hit_src.iot_terminal.ui;

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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.hit_src.iot_terminal.Global;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.DrawPoint;
import com.hit_src.iot_terminal.object.Sensor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class DrawActivity extends AbstractActivity {

    @Override
    protected void runOnBindService() {
        //Global.setSensorList(self,null,null,statusService,dbService,sensorListView);
    }
    private DrawActivity self;
    private ListView sensorListView;
    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        self=this;

        sensorListView=findViewById(R.id.Draw_SensorList_ListView);
        sensorListView.setSelection(-1);

        lineChart=findViewById(R.id.Draw_LineChart);

        XAxis xAxis=lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setDragXEnabled(true);



        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lineChart.invalidate();
                    }
                });
            }
        });
    }


}
