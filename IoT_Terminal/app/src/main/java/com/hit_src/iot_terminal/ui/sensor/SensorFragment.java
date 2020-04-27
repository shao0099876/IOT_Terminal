package com.hit_src.iot_terminal.ui.sensor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;

public class SensorFragment extends Fragment {
    private SensorViewModel viewModel;

    private ListView sensorListView;
    private View lastSelectedView = null;
    private Sensor selectedSensor;
    private Fragment childFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        assert view != null;
        viewModel = new ViewModelProvider(this).get(SensorViewModel.class);
        sensorListView = view.findViewById(R.id.Sensor_SensorListView);
        viewModel.sensorListLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Sensor>>() {
            @Override
            public void onChanged(ArrayList<Sensor> sensors) {
                updateSensorListView(sensors);
            }
        });
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateSensorListView(viewModel.sensorListLiveData.getValue());
            }
        });
        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastSelectedView != null) {
                    lastSelectedView.setBackgroundColor(0);
                }
                lastSelectedView = view;
                view.setBackgroundColor(getResources().getColor(R.color.List_Clicked_Color));
                selectedSensor = (Sensor) parent.getItemAtPosition(position);
                childFragment = new SensorInfoFragment(selectedSensor);
                FragmentTransaction transaction = MainActivity.self.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.Sensor_Detailed_Fragment, childFragment);
                transaction.commit();
            }
        });
        view.findViewById(R.id.Sensor_Add_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSensor = null;
                if (lastSelectedView != null) {
                    lastSelectedView.setBackgroundColor(0);
                }
                lastSelectedView = null;
                childFragment = new SensorAddFragment();
                FragmentTransaction transaction = MainActivity.self.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.Sensor_Detailed_Fragment, childFragment);
                transaction.commit();
            }
        });
        view.findViewById(R.id.Sensor_Delete_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSensor == null) {
                    Toast.makeText(getContext(), "未选中传感器", Toast.LENGTH_SHORT).show();
                } else {
                    GlobalVar.removeSensor(selectedSensor.getID());
                    if (childFragment != null) {
                        FragmentTransaction transaction = MainActivity.self.getSupportFragmentManager().beginTransaction();
                        transaction.remove(childFragment);
                        transaction.commit();
                    }
                }
            }
        });
    }

    private void updateSensorListView(ArrayList<Sensor> sensors) {
        sensorListView.setAdapter(new SensorAdapter(sensors));
    }

}

class SensorAdapter extends BaseAdapter {
    private ArrayList<Sensor> list;

    public SensorAdapter(ArrayList<Sensor> p) {
        list = p;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View res = View.inflate(MainActivity.self, R.layout.sensor_listview_layout, null);
        TextView id = res.findViewById(R.id.Sensor_ListView_No);
        TextView type = res.findViewById(R.id.Sensor_ListView_Type);
        TextView status = res.findViewById(R.id.Sensor_ListView_ConnectionStatus);
        id.setText(String.valueOf(list.get(position).getID()));
        type.setText(GlobalVar.sensorTypeHashMap.get(list.get(position).getType()).name);
        status.setText(list.get(position).isConnected() ? "已连接" : "未连接");
        return res;
    }
}