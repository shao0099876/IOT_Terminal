package com.hit_src.iot_terminal.ui.advance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.tools.PackageManager;
import com.hit_src.iot_terminal.tools.XMLServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdvancePackageManagerFragment extends Fragment {
    private int selectedIndex=-1;
    private Button addButton;
    private Button updateButton;
    private Button delButton;
    private ListView listView;
    private AdvancePackageManagerViewModel viewModel;
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
        viewModel= ViewModelProviders.of(this).get(AdvancePackageManagerViewModel.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        view.setBackgroundColor(333399);
                        selectedIndex=position;
                        final XMLRecord selected=viewModel.packageListViewModel.getValue().get(position);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(selected.localExists()){
                                    if(selected.isOutDated()){
                                        addButton.setEnabled(false);
                                        updateButton.setEnabled(true);
                                        delButton.setEnabled(true);
                                    }
                                    else{
                                        addButton.setEnabled(false);
                                        updateButton.setEnabled(false);
                                        delButton.setEnabled(true);
                                    }
                                } else{
                                    addButton.setEnabled(true);
                                    updateButton.setEnabled(false);
                                    delButton.setEnabled(false);
                                }
                            }
                        });
                    }
                }).start();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        XMLRecord xmlRecord=viewModel.packageListViewModel.getValue().get(selectedIndex);
                        String content=XMLServer.addXML(xmlRecord);
                        PackageManager.addLocalXMLFile(xmlRecord.name,xmlRecord.serverVersion,content);
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
                        XMLRecord xmlRecord=viewModel.packageListViewModel.getValue().get(selectedIndex);
                        PackageManager.updateLocalXMLFile(xmlRecord.name,xmlRecord.serverVersion,XMLServer.addXML(xmlRecord));
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
                        XMLRecord xmlRecord=viewModel.packageListViewModel.getValue().get(selectedIndex);
                        PackageManager.delLocalXMLFile(xmlRecord.name);
                    }
                }).start();

            }
        });

        viewModel.packageListViewModel.observe(getViewLifecycleOwner(), new Observer<ArrayList<XMLRecord>>() {
            @Override
            public void onChanged(ArrayList<XMLRecord> xmlRecords) {
                updateListViewShow(xmlRecords);
            }
        });
        updateListViewShow(viewModel.packageListViewModel.getValue());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addButton.setEnabled(false);
                updateButton.setEnabled(false);
                delButton.setEnabled(false);
            }
        });
    }
    private void updateListViewShow(ArrayList<XMLRecord> xmlRecords){
        List<HashMap<String,Object>> list=new ArrayList<>();
        for(XMLRecord i:xmlRecords){
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("name",i.name);
            hashMap.put("localVersion",i.localVersion);
            hashMap.put("serverVersion",i.serverVersion);
            list.add(hashMap);
        }
        final SimpleAdapter simpleAdapter=new SimpleAdapter(getContext(),list,R.layout.advance_packagemanager_listview_layout,new String[]{"name","localVersion","serverVersion"},new int[]{R.id.Advance_PackageManager_Name_TextView,R.id.Advance_PackageManager_LocalVersion_TextView,R.id.Advance_PackageManager_ServerVersion_TextView});
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(simpleAdapter);
            }
        });
    }
}