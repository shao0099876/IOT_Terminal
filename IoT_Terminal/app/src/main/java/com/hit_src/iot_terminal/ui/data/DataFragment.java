package com.hit_src.iot_terminal.ui.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hit_src.iot_terminal.Global;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataFragment extends Fragment {
    private ListView dataTypeListView;

    private int selectedIndex;
    private Fragment childFragment=null;
    private ArrayList<String> dataTypeList=new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onStart(){
        super.onStart();
        View view=getView();
        dataTypeListView=view.findViewById(R.id.Data_ListView);

        dataTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(333399);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                selectedIndex= position;
                childFragment=new DataDetailedFragment(dataTypeList.get(selectedIndex));
                transaction.replace(R.id.Data_DrawFragment, childFragment);
                transaction.commit();
            }
        });

        Set<String> SensorTypeStringSet= MainApplication.sensorTypeHashMap.keySet();
        HashSet<String> dataTypeSet =new HashSet<>();
        ArrayList<Map<String,Object>> list=new ArrayList<>();
        for(String i:SensorTypeStringSet) {
            String t = MainApplication.sensorTypeHashMap.get(i).getDataType();
            dataTypeSet.add(t);
        }
        for(String t:dataTypeSet){
            HashMap<String,Object> map=new HashMap<>();
            map.put("content",t);
            list.add(map);
            dataTypeList.add(t);
        }
        final SimpleAdapter simpleAdapter=new SimpleAdapter(getContext(),list,R.layout.data_listview_layout,new String[]{"content"},new int[]{R.id.Data_Listview_TextView});
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataTypeListView.setAdapter(simpleAdapter);
                dataTypeListView.setDivider(getResources().getDrawable(R.drawable.sensorlist_divider_shape));
            }
        });
    }
}
