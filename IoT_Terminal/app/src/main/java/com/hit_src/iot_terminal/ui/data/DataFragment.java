package com.hit_src.iot_terminal.ui.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataFragment extends Fragment {
    private ListView dataTypeListView;

    private Fragment childFragment = null;
    private ArrayList<String> dataTypeList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        dataTypeListView = view.findViewById(R.id.Data_ListView);

        dataTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(333399);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                childFragment = new DataDetailedFragment(dataTypeList.get(position));
                transaction.replace(R.id.Data_DrawFragment, childFragment);
                transaction.commit();
            }
        });

        Set<Integer> sensorTypeIntegerSet = MainApplication.sensorTypeHashMap.keySet();
        HashSet<String> dataTypeSet = new HashSet<>();
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i : sensorTypeIntegerSet) {
            dataTypeSet.add(MainApplication.sensorTypeHashMap.get(i).data.name);
        }
        for (String t : dataTypeSet) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("content", t);
            list.add(map);
            dataTypeList.add(t);
        }
        final SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), list, R.layout.data_listview_layout, new String[]{"content"}, new int[]{R.id.Data_Listview_TextView});
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataTypeListView.setAdapter(simpleAdapter);
                dataTypeListView.setDivider(getResources().getDrawable(R.drawable.sensorlist_divider_shape));
            }
        });
    }
}
