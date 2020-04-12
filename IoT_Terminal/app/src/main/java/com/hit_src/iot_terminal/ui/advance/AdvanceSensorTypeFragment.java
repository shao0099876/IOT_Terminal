package com.hit_src.iot_terminal.ui.advance;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.tools.Filesystem;
import com.hit_src.iot_terminal.tools.XMLServer;
import com.hit_src.iot_terminal.ui.overview.components.StatusLinearLayout;
import com.hit_src.iot_terminal.xml.XML;

import java.io.File;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvanceSensorTypeFragment extends Fragment {
    private XMLRecord selected=null;
    private Button addButton;
    private Button updateButton;
    private Button delButton;
    private ListView listView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advance_sensortype, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        View view=getView();
        listView=view.findViewById(R.id.Advance_SensorType_ListView);
        addButton=view.findViewById(R.id.Advance_XMLAdd_Button);
        updateButton=view.findViewById(R.id.Advance_XMLUpdate_Button);
        delButton=view.findViewById(R.id.Advance_XMLDelete_Button);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(333399);
                selected=xmlRecords.get(position);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(selected.localExists()){
                            if(selected.isOutDated()){
                                addButton.setClickable(false);
                                updateButton.setClickable(true);
                                delButton.setClickable(true);
                            }
                            else{
                                addButton.setClickable(false);
                                updateButton.setClickable(false);
                                delButton.setClickable(true);
                            }
                        } else{
                            addButton.setClickable(true);
                            updateButton.setClickable(false);
                            delButton.setClickable(false);
                        }
                    }
                });
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        XMLServer.addXML(getContext(),selected);
                        updateShow();
                    }
                }).start();

            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        XMLServer.updateXML(getContext(),selected);
                        updateShow();
                    }
                }).start();

            }
        });
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        XMLServer.delXML(selected);
                        Filesystem.deleteXMLFile(getContext(),selected);
                        updateShow();
                    }
                }).start();

            }
        });

        updateShow();
    }
    private ArrayList<XMLRecord> xmlRecords;
    private SimpleAdapter product(){
        List<Map<String,Object>> list=new ArrayList<>();
        for(int i=0;i<xmlRecords.size();i++){
            XMLRecord now=xmlRecords.get(i);
            Map<String,Object> map=new HashMap<>();
            map.put("name",now.name);
            map.put("local",now.localVersion);
            map.put("server",now.serverVersion);
            list.add(map);
        }
        return new SimpleAdapter(getContext(),list,R.layout.advance_xml_listview_layout,new String[]{"name","local","server"},new int[]{R.id.Advance_XML_Name_TextView,R.id.Advance_XML_LocalVersion_TextView,R.id.Advance_XML_ServerVersion_TextView});
    }
    private void updateShow(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Filesystem.build(getContext());
                final ArrayList<XML> serverList= XMLServer.getList();
                final ArrayList<XML> localList= Filesystem.getXMLList(getContext());
                xmlRecords=new ArrayList<>();
                for(XML i:serverList){
                    boolean exist=false;
                    for(XML j:localList){
                        if(i.name.equals(j.name)){
                            exist=true;
                            xmlRecords.add(new XMLRecord(i.name,j.version,i.version));
                            break;
                        }
                    }
                    if(!exist){
                        xmlRecords.add(new XMLRecord(i.name,i.version));
                    }
                }
                final SimpleAdapter simpleAdapter=product();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(simpleAdapter);
                        addButton.setClickable(false);
                        updateButton.setClickable(false);
                        delButton.setClickable(false);
                    }
                });
            }
        }).start();
    }
}