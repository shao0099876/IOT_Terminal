package com.hit_src.iot_terminal.ui.sensor;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.SensorAdapter;
import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;
import java.util.Map;

public class SensorFragment extends Fragment {
    private SensorViewModel viewModel;

    private ListView sensorListView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        sensorListView = view.findViewById(R.id.Sensor_SensorListView);
        Button backButton = view.findViewById(R.id.Sensor_Back_Button);
        Button addButton = view.findViewById(R.id.Sensor_Add_Button);
        Button delButton = view.findViewById(R.id.Sensor_Delete_Button);

        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.Sensor_Detailed_Fragment, new SensorInfoFragment(viewModel.sensorListLiveData.getValue().get(position)));
                transaction.commit();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.remove(SensorFragment.this);
                transaction.commit();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.Sensor_Detailed_Fragment, new SensorAddFragment());
                transaction.commit();
            }
        });
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorListView.getSelectedItemPosition() < 0) {
                    Toast.makeText(getContext(), "未选中传感器", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel = ViewModelProviders.of(getActivity()).get(SensorViewModel.class);
        viewModel.sensorListLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Sensor>>() {
            @Override
            public void onChanged(ArrayList<Sensor> sensors) {
                final SimpleAdapter simpleAdapter = makeAdapter(sensors);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sensorListView.setAdapter(simpleAdapter);
                        sensorListView.setDivider(getResources().getDrawable(R.drawable.sensorlist_divider_shape));
                    }
                });
            }
        });

        SimpleAdapter adapter = null;
        try {
            adapter = makeAdapter((ArrayList<Sensor>) MainApplication.dbService.getSensorList());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        final SimpleAdapter simpleAdapter=adapter;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sensorListView.setAdapter(simpleAdapter);
                sensorListView.setDivider(getResources().getDrawable(R.drawable.sensorlist_divider_shape));
            }
        });
    }
    private SimpleAdapter makeAdapter(ArrayList<Sensor> sensors){
        ArrayList<Map<String,Object>> arrayList = new ArrayList<>();
        for(Sensor i:sensors){
            arrayList.add(SensorAdapter.toListViewAdapter(i));
        }
        return new SimpleAdapter(getContext(),arrayList,R.layout.sensor_listview_layout,new String[]{"ID", "type", "status"}, new int[]{R.id.Sensor_ListView_No, R.id.Sensor_ListView_Type, R.id.Sensor_ListView_ConnectionStatus});
    }

}
