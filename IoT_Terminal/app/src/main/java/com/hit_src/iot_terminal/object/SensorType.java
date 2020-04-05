package com.hit_src.iot_terminal.object;

import com.hit_src.iot_terminal.object.op.Operation;

import java.util.List;

public class SensorType {
    private String name;
    private String dataTypeName;
    private int dataLength;
    private List<Operation> opList;
    public SensorType(){
        name="";
    }

    public String getName(){
        return name;
    }
    public void setName(String p) {
        name=p;
    }

    public String getDataType(){
        return dataTypeName;
    }
    public void setDataType(String trim) {
        dataTypeName=trim;
    }

    public int getDataLength(){
        return dataLength;
    }
    public void setDataLength(Integer valueOf) {
        dataLength=valueOf;
    }

    public List<Operation> getOpList(){
        return opList;
    }
    public void setOpList(List<Operation> opList) {
        this.opList=opList;
    }
}
