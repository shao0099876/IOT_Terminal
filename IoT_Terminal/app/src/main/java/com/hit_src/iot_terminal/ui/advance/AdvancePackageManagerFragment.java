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
import androidx.lifecycle.ViewModelProvider;

import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.tools.pm.PackageManager;

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
        assert view != null;
        listView=view.findViewById(R.id.Advance_SensorType_ListView);
        addButton=view.findViewById(R.id.Advance_XMLAdd_Button);
        updateButton=view.findViewById(R.id.Advance_XMLUpdate_Button);
        delButton=view.findViewById(R.id.Advance_XMLDelete_Button);
        viewModel=new ViewModelProvider(this).get(AdvancePackageManagerViewModel.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                view.setBackgroundColor(333399);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        selectedIndex=position;
                        ArrayList<XMLRecord> tmp=viewModel.packageListViewModel.getValue();
                        if(tmp==null){
                            return;
                        }
                        final XMLRecord selected=tmp.get(position);
                        MainActivity.self.runOnUiThread(new Runnable() {
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
                        ArrayList<XMLRecord> tmp=viewModel.packageListViewModel.getValue();
                        if(tmp==null){
                            return;
                        }
                        XMLRecord xmlRecord=tmp.get(selectedIndex);
                        PackageManager.pull(xmlRecord.name,xmlRecord.serverVersion);
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
                        ArrayList<XMLRecord> tmp=viewModel.packageListViewModel.getValue();
                        if(tmp==null){
                            return;
                        }
                        XMLRecord xmlRecord=tmp.get(selectedIndex);
                        PackageManager.pull(xmlRecord.name,xmlRecord.serverVersion);
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
                        ArrayList<XMLRecord> tmp=viewModel.packageListViewModel.getValue();
                        if(tmp==null){
                            return;
                        }
                        XMLRecord xmlRecord=tmp.get(selectedIndex);
                        PackageManager.delete(xmlRecord.name);
                    }
                }).start();

            }
        });

        viewModel.packageListViewModel.observe(getViewLifecycleOwner(), new Observer<ArrayList<XMLRecord>>() {
            @Override
            public void onChanged(final ArrayList<XMLRecord> xmlRecords) {
                MainActivity.self.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateListViewShow(xmlRecords);
                    }
                });
            }
        });
        PackageManager.fetch();
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<XMLRecord> tmp=viewModel.packageListViewModel.getValue();
                if(tmp==null){
                    return;
                }
                updateListViewShow(tmp);
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
            hashMap.put("localVersion",i.getLocalVersion());
            hashMap.put("serverVersion",i.getServerVersion());
            list.add(hashMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(getContext(),list,R.layout.advance_packagemanager_listview_layout,new String[]{"name","localVersion","serverVersion"},new int[]{R.id.Advance_PackageManager_Name_TextView,R.id.Advance_PackageManager_LocalVersion_TextView,R.id.Advance_PackageManager_ServerVersion_TextView});
        listView.setAdapter(simpleAdapter);
    }
}