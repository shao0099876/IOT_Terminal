package com.hit_src.iot_terminal.xml;

import com.hit_src.iot_terminal.object.sensortype.Alert;
import com.hit_src.iot_terminal.object.sensortype.Datatype;
import com.hit_src.iot_terminal.object.sensortype.Operation;
import com.hit_src.iot_terminal.object.sensortype.Reaction;
import com.hit_src.iot_terminal.object.sensortype.Receive;
import com.hit_src.iot_terminal.object.sensortype.Send;
import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.object.sensortype.Trigger;
import com.hit_src.iot_terminal.object.sensortype.Validity;
import com.hit_src.iot_terminal.object.sensortype.op.Add;
import com.hit_src.iot_terminal.object.sensortype.op.Div;
import com.hit_src.iot_terminal.object.sensortype.op.Mul;
import com.hit_src.iot_terminal.object.sensortype.reaction.Message;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class XML2SensorType extends DefaultHandler {
    private SensorType res;
    private Datatype data;
    private Send send;
    private Receive receive;
    private Operation operation;
    private Validity validity;
    private ArrayList<Alert> alerts;
    private Alert alert;
    private Trigger trigger;
    private Reaction reaction;
    private StringBuilder sb = new StringBuilder();

    public SensorType getResults() {
        return res;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        res = new SensorType();
        data = new Datatype();
        send = new Send();
        receive = new Receive();
        operation = new Operation();
        validity = new Validity();
        alerts = new ArrayList<>();
        alert = new Alert();
        trigger = new Trigger();
        reaction = new Reaction();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        sb.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("id")) {
            res.setID(sb.toString().trim());
        }
        if (localName.equals("name")) {
            res.setName(sb.toString().trim());
        }
        if (localName.equals("dataname")) {
            data.setName(sb.toString().trim());
        }
        if (localName.equals("unit")) {
            data.setUnit(sb.toString().trim());
        }
        if (localName.equals("data")) {
            res.setDataType(data);
        }
        if (localName.equals("sendlength")) {
            send.setLength(sb.toString().trim());
        }
        if (localName.equals("sendvalue")) {
            send.addValue(sb.toString().trim());
        }
        if (localName.equals("send")) {
            res.setSend(send);
        }
        if (localName.equals("recvlength")) {
            receive.setLength(sb.toString().trim());
        }
        if (localName.equals("add")) {
            operation.addOP(new Add(sb.toString().trim()));
        }
        if (localName.equals("mul")) {
            operation.addOP(new Mul(sb.toString().trim()));
        }
        if (localName.equals("div")) {
            operation.addOP(new Div(sb.toString().trim()));
        }
        if (localName.equals("operation")) {
            receive.setOperation(operation);
        }
        if (localName.equals("receive")) {
            res.setReceive(receive);
        }
        if (localName.equals("min")) {
            validity.setMin(sb.toString().trim());
        }
        if (localName.equals("max")) {
            validity.setMax(sb.toString().trim());
        }
        if (localName.equals("validity")) {
            res.setValidity(validity);
        }
        if (localName.equals("triggertype")) {
            trigger.setType(sb.toString().trim());
        }
        if (localName.equals("triggervalue")) {
            trigger.setValue(sb.toString().trim());
        }
        if (localName.equals("trigger")) {
            alert.setTrigger(trigger);
        }
        if (localName.equals("message")) {
            reaction.setReaction(new Message(sb.toString().trim()));
        }
        if (localName.equals("reaction")) {
            alert.setReaction(reaction);
        }
        if (localName.equals("alert")) {
            alerts.add(alert);
        }
        if (localName.equals("alerts")) {
            res.setAlerts(alerts);
        }
        sb.setLength(0);
        super.endElement(uri, localName, qName);
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
