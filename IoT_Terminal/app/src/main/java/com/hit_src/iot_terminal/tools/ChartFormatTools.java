package com.hit_src.iot_terminal.tools;

import android.os.RemoteException;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.DrawPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartFormatTools {
    public static void LineChartFormat(LineChart chart,List<DrawPoint> pointList){
        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setDragXEnabled(true);
        chart.setVisibleXRange(10,20);
        if(pointList.isEmpty()){
            return;
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
        chart.setData(lineData);
        chart.getXAxis().setValueFormatter(formatter);
        chart.moveViewToX(pointList.size());
    }
}
