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
import com.hit_src.iot_terminal.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataFragment extends Fragment {
    private ListView dataTypeListView;

    private String selected;
    private Fragment childFragment=null;

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
        Button backButton=view.findViewById(R.id.Data_Back_Button);

        dataTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                selected= Global.getDataTypeList()[position];
                childFragment=new DataDetailedFragment(selected);
                transaction.replace(R.id.Data_DrawFragment, childFragment);
                transaction.commit();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=getParentFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.remove(DataFragment.this);
                transaction.commit();
            }
        });
        ArrayList<Map<String,Object>> list=new ArrayList<>();
        for(String i:Global.getDataTypeList()){
            HashMap<String,Object> map=new HashMap<>();
            map.put("content",i);
            list.add(map);
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