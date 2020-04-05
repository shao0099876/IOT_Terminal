package com.hit_src.iot_terminal.xml;

import com.hit_src.iot_terminal.object.op.Add;
import com.hit_src.iot_terminal.object.op.Div;
import com.hit_src.iot_terminal.object.op.Mul;
import com.hit_src.iot_terminal.object.op.Operation;
import com.hit_src.iot_terminal.object.SensorType;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XML2SensorType extends DefaultHandler {
    private SensorType res;
    private StringBuilder sb=new StringBuilder();
    private List<Operation> opList;
    public SensorType getResults(){
        return res;
    }
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        res=new SensorType();
        opList=new ArrayList<>();
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        sb.append(ch,start,length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(localName.equals("name")){
            res.setName(sb.toString().trim());
        }
        if(localName.equals("datatype")){
            res.setDataType(sb.toString().trim());
        }
        if(localName.equals("bytecnt")){
            res.setDataLength(Integer.valueOf(sb.toString().trim()));
        }
        if(localName.equals("add")){
            opList.add(new Add(Integer.valueOf(sb.toString().trim())));
        }
        if(localName.equals("mul")){
            opList.add(new Mul(Integer.valueOf(sb.toString().trim())));
        }
        if(localName.equals("div")){
            opList.add(new Div(Integer.valueOf(sb.toString().trim())));
        }
        if(localName.equals("operation")){
            res.setOpList(opList);
        }
        sb.setLength(0);
    }
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
