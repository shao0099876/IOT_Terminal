package com.hit_src.iot_terminal.ui.sensor;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;
import java.util.Map;

public class SensorFragment extends Fragment {
    private SensorViewModel viewModel;

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

        final View view = getView();
        assert view != null;
        //需要响应的控件代码
        {
            ((ListView) (view.findViewById(R.id.Sensor_SensorListView))).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setBackgroundColor(333399);
                    FragmentManager manager = MainActivity.self.getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    selected = position;
                    SensorAdapter adapter = (SensorAdapter) parent.getAdapter();
                    childFragment = new SensorInfoFragment((Sensor) adapter.getItem(position));
                    transaction.replace(R.id.Sensor_Detailed_Fragment, childFragment);
                    transaction.commit();
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

            MainActivity.self.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //SensorSensorListView
                    ((ListView) (view.findViewById(R.id.Sensor_SensorListView))).setAdapter(viewModel.sensorListLiveData.getValue());
                }
            });
        }
        /*
        sensorListView = view.findViewById(R.id.Sensor_SensorListView);

        Button delButton=view.findViewById(R.id.Sensor_Delete_Button);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager= MainActivity.self.getSupportFragmentManager();
                if (selected==null) {
                    Toast.makeText(getContext(), "未选中传感器", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    ArrayList<Sensor> list=viewModel.sensorListLiveData.getValue();
                    assert list != null;
                    MainApplication.dbService.delSensor(list.get(selected).getID());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if(childFragment!=null){
                    FragmentTransaction transaction=manager.beginTransaction();
                    transaction.remove(childFragment);
                    transaction.commit();
                }
            }
        });

        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(333399);
                FragmentManager manager= MainActivity.self.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                selected=position;
                ArrayList<Sensor> list=viewModel.sensorListLiveData.getValue();
                assert list != null;
                childFragment=new SensorInfoFragment(list.get(position));
                transaction.replace(R.id.Sensor_Detailed_Fragment, childFragment);
                transaction.commit();
            }
        });
        sensorListView.setDivider(getResources().getDrawable(R.drawable.sensorlist_divider_shape));

        viewModel=new ViewModelProvider(this).get(SensorViewModel.class);
        viewModel.sensorListLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Sensor>>() {
            @Override
            public void onChanged(final ArrayList<Sensor> sensors) {
                MainActivity.self.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sensorListView.setAdapter(makeAdapter(sensors));
                    }
                });
            }
        });

        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Sensor> list=viewModel.sensorListLiveData.getValue();
                assert list != null;
                sensorListView.setAdapter(makeAdapter(list));
            }
        });
    }
    private SimpleAdapter makeAdapter(ArrayList<Sensor> sensors){
        ArrayList<Map<String,Object>> arrayList = new ArrayList<>();
        for(Sensor i:sensors){
            arrayList.add(SensorAdapter.toListViewAdapter(i));
        }
        return new SimpleAdapter(getContext(),arrayList,R.layout.sensorlistview_layout,new String[]{"ID", "type", "status"}, new int[]{R.id.SensorListView_ID, R.id.SensorListView_Type, R.id.SensorListView_Status});
    }
*/
    }
}
