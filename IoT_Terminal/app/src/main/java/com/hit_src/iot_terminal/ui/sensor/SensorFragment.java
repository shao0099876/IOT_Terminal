package com.hit_src.iot_terminal.ui.sensor;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.SensorAdapter;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.service.SensorService;

import java.util.ArrayList;
import java.util.Map;

public class SensorFragment extends Fragment {
    private SensorViewModel viewModel;

    private ListView sensorListView;

    private Integer selected;
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
        sensorListView = view.findViewById(R.id.Sensor_SensorListView);
        Button addButton = view.findViewById(R.id.Sensor_Add_Button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                selected = null;
                childFragment = new SensorAddFragment();
                transaction.replace(R.id.Sensor_Detailed_Fragment, childFragment);
                transaction.commit();
            }
        });
        Button delButton = view.findViewById(R.id.Sensor_Delete_Button);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                if (selected == null) {
                    Toast.makeText(getContext(), "未选中传感器", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    MainApplication.dbService.delSensor(SensorService.sensorList.get(selected).getID());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (childFragment != null) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.remove(childFragment);
                    transaction.commit();
                }
            }
        });

        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(333399);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                selected = position;
                childFragment = new SensorInfoFragment(SensorService.sensorList.get(position));
                transaction.replace(R.id.Sensor_Detailed_Fragment, childFragment);
                transaction.commit();
            }
        });
        sensorListView.setDivider(getResources().getDrawable(R.drawable.sensorlist_divider_shape));

        viewModel = ViewModelProviders.of(getActivity()).get(SensorViewModel.class);
        viewModel.sensorListLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Sensor>>() {
            @Override
            public void onChanged(final ArrayList<Sensor> sensors) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sensorListView.setAdapter(makeAdapter(sensors));
                    }
                });
            }
        });

        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Sensor> list = (ArrayList<Sensor>) MainApplication.dbService.getSensorList();
                    sensorListView.setAdapter(makeAdapter(list));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private SimpleAdapter makeAdapter(ArrayList<Sensor> sensors) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (Sensor i : sensors) {
            arrayList.add(SensorAdapter.toListViewAdapter(i));
        }
        return new SimpleAdapter(getContext(), arrayList, R.layout.sensor_listview_layout, new String[]{"ID", "type", "status"}, new int[]{R.id.Sensor_ListView_No, R.id.Sensor_ListView_Type, R.id.Sensor_ListView_ConnectionStatus});
    }

}
