package com.hit_src.iot_terminal.ui.sensor;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.ui.sensor.components.SensorListView;

import java.util.ArrayList;

public class SensorFragment extends Fragment {
    private SensorViewModel viewModel;

    private SensorListView sensorListView;

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

        viewModel = ViewModelProviders.of(getActivity()).get(SensorViewModel.class);
        viewModel.sensorListLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Sensor>>() {
            @Override
            public void onChanged(final ArrayList<Sensor> sensors) {
                sensorListView.setData(sensors);
            }
        });

        try {
            sensorListView.setData((ArrayList<Sensor>) MainApplication.dbService.getSensorList());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}
