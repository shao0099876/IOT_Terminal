package com.hit_src.iot_terminal.ui.overview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.ui.data.DataFragment;
import com.hit_src.iot_terminal.ui.sensor.SensorFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class OverviewFragment extends Fragment {

    private OverviewViewModel viewModel;

    private TextView clockTextView;
    private TextView dateTextView;
    private LinearLayout sensorStatusLinearLayout;
    private TextView sensorStatusTextView;
    private LinearLayout internetStatusLinearLayout;
    private TextView internetStatusTextView;
    private EditText logEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onStart(){
        super.onStart();

        View view=getView();
        clockTextView=view.findViewById(R.id.Overview_Clock_TextView);
        dateTextView=view.findViewById(R.id.Overview_Date_TextView);
        sensorStatusLinearLayout=view.findViewById(R.id.Overview_SensorStatus_LinearLayout);
        sensorStatusTextView=view.findViewById(R.id.Overview_SensorStatus_TextView);
        internetStatusLinearLayout=view.findViewById(R.id.Overview_InternetConnectionStatus_LinearLayout);
        internetStatusTextView=view.findViewById(R.id.Overview_InternetStatus_TextView);
        logEditText=view.findViewById(R.id.Overview_Log_EditText);

        Button sensorButton=view.findViewById(R.id.Overview_Sensor_Button);
        sensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=getParentFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.add(R.id.MainFragment,new SensorFragment());
                transaction.commit();
            }
        });
        Button dataButton=view.findViewById(R.id.Overview_Data_Button);
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=getParentFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                DataFragment dataFragment=new DataFragment();
                transaction.add(R.id.MainFragment,dataFragment);
                transaction.commit();
            }
        });

        viewModel=ViewModelProviders.of(getActivity()).get(OverviewViewModel.class);
        viewModel.sensorConnectedLiveData.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                int connected=integer;
                int amount=viewModel.sensorAmountLiveData.getValue();
                setSensorStatusShow(connected,amount);
            }
        });
        viewModel.sensorAmountLiveData.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                int connected=viewModel.sensorConnectedLiveData.getValue();
                int amount=integer;
                setSensorStatusShow(connected,amount);
            }
        });
        viewModel.internetConnectionLiveData.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean newStatus) {
                setInternetStatusShow(newStatus);
            }
        });
        viewModel.logLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                StringBuilder sb=new StringBuilder();
                for(String i:strings){
                    sb.append(i);
                    sb.append("\n");
                }
                final String s=sb.toString();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logEditText.setText(s);
                        logEditText.setSelection(s.length());
                    }
                });
            }
        });
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                final String s1=simpleDateFormat.format(new Date());
                simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
                final String s2=simpleDateFormat.format(new Date());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clockTextView.setText(s1);
                        dateTextView.setText(s2);
                    }
                });
            }
        },10,1000);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setSensorStatusShow(viewModel.sensorConnectedLiveData.getValue(),viewModel.sensorAmountLiveData.getValue());
                final int colorID;
                String s;
                boolean status=viewModel.internetConnectionLiveData.getValue();
                if(status){
                    colorID=getResources().getColor(R.color.Overview_Status_Green);
                    s="已连接";
                } else{
                    colorID=getResources().getColor(R.color.Overview_Status_Red);
                    s="未连接";
                }
                final int final_colorID= colorID;
                final String final_s=s;
                internetStatusLinearLayout.setBackgroundColor(final_colorID);
                internetStatusTextView.setText(final_s);
                logEditText.setText("");
            }
        });
    }
    private void setSensorStatusShow(int connected,int amount){
        int colorID;
        if(connected==amount){
            colorID=R.color.Overview_Status_Green;
        } else if(connected==0){
            colorID=R.color.Overview_Status_Red;
        } else{
            colorID=R.color.Overview_Status_Yellow;
        }
        final String s=connected+"/"+amount;
        final int final_colorID=colorID;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sensorStatusLinearLayout.setBackgroundColor(getResources().getColor(final_colorID));
                sensorStatusTextView.setText(s);
            }
        });
    }
    private void setInternetStatusShow(Boolean newStatus){
        final int colorID;
        String s;
        boolean status=newStatus;
        if(status){
            colorID=getResources().getColor(R.color.Overview_Status_Green);
            s="已连接";
        } else{
            colorID=getResources().getColor(R.color.Overview_Status_Red);
            s="未连接";
        }
        final int final_colorID= colorID;
        final String final_s=s;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                internetStatusLinearLayout.setBackgroundColor(final_colorID);
                internetStatusTextView.setText(final_s);
            }
        });
    }

}
