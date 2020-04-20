package com.hit_src.iot_terminal.tools;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DataChart {
    private LineChart chart;
    private Map<Integer,LineDataSet> lineDataSetMap=new HashMap<>();
    private Map<Long,DataRecord> dataRecordMap=new HashMap<>();
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM/dd HH:mm:ss", Locale.CHINA);
    private void setXAxis(){
        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setDragXEnabled(true);
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                String res;
                try{
                    DataRecord dataRecord=dataRecordMap.get((long)value);
                    assert dataRecord!=null;
                    res=simpleDateFormat.format(dataRecord.time);
                } catch (NullPointerException e){
                    res="";
                }
                return res;
            }
        };
        xAxis.setValueFormatter(formatter);
    }
    public void setComponent(LineChart lineChart){
        chart=lineChart;
        setXAxis();
    }
    private void buildLineDataSetMap(List<DataRecord> list){
        lineDataSetMap=new HashMap<>();
        for(DataRecord tmp:list){
            if(!lineDataSetMap.containsKey(tmp.sensorID)){
                ArrayList<Entry> entryArrayList=new ArrayList<>();
                lineDataSetMap.put(tmp.sensorID,new LineDataSet(entryArrayList,"Sensor#"+tmp.sensorID));
            }
            LineDataSet lineDataSet=lineDataSetMap.get(tmp.sensorID);
            dataRecordMap.put((long) dataRecordMap.size(),tmp);
            assert lineDataSet != null;
            lineDataSet.addEntry(new Entry(dataRecordMap.size(),tmp.data));
        }
    }

    public void setData(List<DataRecord> list) {
        if(list.isEmpty()){
            return;
        }
        buildLineDataSetMap(list);
        ArrayList<ILineDataSet> tt=new ArrayList<>();
        Set<Integer> set=lineDataSetMap.keySet();
        for(int i:set){
            tt.add(lineDataSetMap.get(i));
        }
        LineData lineData=new LineData(tt);
        chart.setData(lineData);
    }
    public void addData(DataRecord dataRecord){
        if(dataRecord==null||dataRecord.data==null){
            return;
        }
        if(!lineDataSetMap.containsKey(dataRecord.sensorID)){
            ArrayList<Entry> entryArrayList=new ArrayList<>();
            lineDataSetMap.put(dataRecord.sensorID,new LineDataSet(entryArrayList,"Sensor#"+dataRecord.sensorID));
        }
        LineDataSet lineDataSet=lineDataSetMap.get(dataRecord.sensorID);
        dataRecordMap.put((long) dataRecordMap.size(),dataRecord);
        assert lineDataSet != null;
        lineDataSet.addEntry(new Entry(dataRecordMap.size(),dataRecord.data));
        ArrayList<ILineDataSet> tt=new ArrayList<>();
        Set<Integer> set=lineDataSetMap.keySet();
        for(int i:set){
            tt.add(lineDataSetMap.get(i));
        }
        LineData lineData=new LineData(tt);
        chart.setData(lineData);
    }

    public void invalidate(boolean realtime) {
        if(realtime){
            chart.setVisibleXRange(5,10);
        }
        else{
            chart.fitScreen();
        }
        chart.moveViewToX(dataRecordMap.size());
        chart.invalidate();
    }
}
