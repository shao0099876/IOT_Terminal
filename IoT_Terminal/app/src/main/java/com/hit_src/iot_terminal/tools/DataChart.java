package com.hit_src.iot_terminal.tools;

import android.provider.ContactsContract;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hit_src.iot_terminal.object.DataRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataChart {
    private LineChart chart;
    private ArrayList<String> xlineTagList=new ArrayList<>();
    private Map<Integer,LineDataSet> lineDataSetMap=new HashMap<>();
    public void setXAxis(){
        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setDragXEnabled(true);
        chart.setVisibleXRange(10,20);
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return xlineTagList.get((int) value);
            }
        };
        xAxis.setValueFormatter(formatter);
    }
    public void setComponent(LineChart lineChart){
        chart=lineChart;
        setXAxis();
    }

    public void setData(List<DataRecord> list) {
        if(list.isEmpty()){
            return;
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM/dd HH:mm:ss");
        xlineTagList=new ArrayList<>();
        lineDataSetMap=new HashMap<>();
        for(int i=0;i<list.size();i++){
            DataRecord tmp=list.get(i);
            if(!lineDataSetMap.containsKey(tmp.sensorID)){
                ArrayList<Entry> entryArrayList=new ArrayList<>();
                lineDataSetMap.put(tmp.sensorID,new LineDataSet(entryArrayList,"Sensor#"+tmp.sensorID));
            }
            LineDataSet lineDataSet=lineDataSetMap.get(tmp.sensorID);
            lineDataSet.addEntry(new Entry(tmp.data,i));
            xlineTagList.add(simpleDateFormat.format(new Date(tmp.time)));
        }
        ArrayList<ILineDataSet> tt=new ArrayList<>();
        Set<Integer> set=lineDataSetMap.keySet();
        for(int i:set){
            tt.add(lineDataSetMap.get(i));
        }
        LineData lineData=new LineData(tt);
        chart.setData(lineData);
        chart.moveViewToX(xlineTagList.size());
    }
    public void addData(DataRecord dataRecord){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM/dd HH:mm:ss");
        if(!lineDataSetMap.containsKey(dataRecord.sensorID)){
            ArrayList<Entry> entryArrayList=new ArrayList<>();
            lineDataSetMap.put(dataRecord.sensorID,new LineDataSet(entryArrayList,"Sensor#"+dataRecord.sensorID));
        }
        LineDataSet lineDataSet=lineDataSetMap.get(dataRecord.sensorID);
        lineDataSet.addEntry(new Entry(dataRecord.data,xlineTagList.size()));
        xlineTagList.add(simpleDateFormat.format(new Date(dataRecord.time)));
        chart.moveViewToX(xlineTagList.size());
    }

    public void invalidate() {
        chart.invalidate();
    }
}
