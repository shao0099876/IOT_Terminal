package com.hit_src.iot_terminal.ui.sensor;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.ui.sensor.add.SensorAddFragment;

public class SensorFragment extends Fragment {
    private SensorViewModel viewModel;

    private View selectedView=null;
    private Sensor selected=null;
    private Fragment childFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        final View view = getView();
        assert view != null;
        //需要响应的控件代码
        {
            ((ListView) (view.findViewById(R.id.Sensor_SensorListView))).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(selectedView!=null){
                        selectedView.setBackgroundColor(0);
                    }
                    selectedView=view;
                    selected= (Sensor) parent.getAdapter().getItem(position);
                    view.setBackgroundColor(getResources().getColor(R.color.ListView_SelectedColor));
                    FragmentManager manager = MainActivity.self.getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    childFragment = new SensorInfoFragment(selected);
                    transaction.replace(R.id.Sensor_Detailed_Fragment, childFragment);
                    transaction.commit();
                }
            });
            view.findViewById(R.id.SensorAdd_ConfirmButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = MainActivity.self.getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    childFragment = new SensorAddFragment();
                    transaction.replace(R.id.Sensor_Detailed_Fragment, childFragment);
                    transaction.commit();
                }
            });
            view.findViewById(R.id.Sensor_DeleteButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager= MainActivity.self.getSupportFragmentManager();
                    if (selected==null) {
                        Toast.makeText(getContext(), "未选中传感器", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    GlobalVar.sensors.remove(selected);
                    if(childFragment!=null){
                        FragmentTransaction transaction=manager.beginTransaction();
                        transaction.remove(childFragment);
                        transaction.commit();
                    }
                }
            });
        }
        //获取ViewModel
        viewModel = new ViewModelProvider(this).get(SensorViewModel.class);
        //需要显示的控件与ViewModel绑定
        {
            //Sensor_SensorListView
            viewModel.sensorListLiveData.observe(getViewLifecycleOwner(), new Observer<BaseAdapter>() {
                @Override
                public void onChanged(final BaseAdapter baseAdapter) {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ListView) (view.findViewById(R.id.Sensor_SensorListView))).setAdapter(baseAdapter);
                        }
                    });
                }
            });
        }
        //初始化
        {
            MainActivity.self.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //SensorSensorListView
                    ((ListView) (view.findViewById(R.id.Sensor_SensorListView))).setAdapter(viewModel.sensorListLiveData.getValue());
                }
            });
        }
    }
}
