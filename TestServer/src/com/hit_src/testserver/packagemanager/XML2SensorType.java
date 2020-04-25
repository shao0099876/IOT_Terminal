package com.hit_src.testserver.packagemanager;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XML2SensorType extends DefaultHandler {
    private SensorType res;
    private Datatype data;
    private StringBuilder sb=new StringBuilder();
    public SensorType getResults(){
        return res;
    }
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        res=new SensorType();
        data=new Datatype();
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
        if(localName.equals("id")){
            res.setID(sb.toString().trim());
        }
        if(localName.equals("name")){
            res.setName(sb.toString().trim());
        }
        if(localName.equals("dataname")){
            data.setName(sb.toString().trim());
        }
        if(localName.equals("unit")){
            data.setUnit(sb.toString().trim());
        }
        if(localName.equals("data")){
            res.setDataType(data);
        }
        sb.setLength(0);
        super.endElement(uri, localName, qName);
    }
}
