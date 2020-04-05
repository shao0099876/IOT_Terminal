package com.hit_src.iot_terminal.ui.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hit_src.iot_terminal.R;

public class DataDetailedFragment extends Fragment {
    private String Datatype=null;
    private boolean realtime=false;


    public DataDetailedFragment(){}
    public DataDetailedFragment(String datatype,boolean real){
        Datatype=datatype;
        realtime=real;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_datadetailed, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        //根据Datatype找Sensor
    }
    public void setRealtime(boolean data){

    }
}
