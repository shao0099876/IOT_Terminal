package com.hit_src.iot_terminal.ui.overview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.ui.overview.components.StatusLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class OverviewFragment extends Fragment {

    private OverviewViewModel viewModel;

    private StatusLinearLayout sensorStatusLinearLayout;
    private TextView sensorStatusTextView;
    private StatusLinearLayout internetStatusLinearLayout;
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
        sensorStatusLinearLayout=view.findViewById(R.id.Overview_SensorStatus_LinearLayout);
        sensorStatusTextView=view.findViewById(R.id.Overview_SensorStatus_TextView);
        internetStatusLinearLayout=view.findViewById(R.id.Overview_InternetConnectionStatus_LinearLayout);
        internetStatusTextView=view.findViewById(R.id.Overview_InternetStatus_TextView);
        logEditText=view.findViewById(R.id.Overview_Log_EditText);

        viewModel=ViewModelProviders.of(getActivity()).get(OverviewViewModel.class);
        viewModel.sensorConnectedLiveData.observe(getViewLifecycleOwner(),sensorStatusObserver);
        viewModel.sensorAmountLiveData.observe(getViewLifecycleOwner(),sensorStatusObserver);
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setSensorStatusShow(viewModel.sensorConnectedLiveData.getValue(),viewModel.sensorAmountLiveData.getValue());
                setInternetStatusShow(viewModel.internetConnectionLiveData.getValue());
                logEditText.setText("");
            }
        });
        List<Fragment> list=getFragmentManager().getFragments();
        for(Fragment i:list){
            Log.d("SRCDEBUG", String.valueOf(i));
        }
    }
    private void setSensorStatusShow(int connected,int amount){
        if(connected==amount){
            sensorStatusLinearLayout.setGreen();
        } else if(connected==0){
            sensorStatusLinearLayout.setRed();
        } else{
            sensorStatusLinearLayout.setYellow();
        }
        final String s=connected+"/"+amount;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sensorStatusTextView.setText(s);
            }
        });
    }
    private void setInternetStatusShow(Boolean newStatus){
        String s;
        boolean status=newStatus;
        if(status){
            internetStatusLinearLayout.setGreen();
            s="已连接";
        } else{
            internetStatusLinearLayout.setRed();
            s="未连接";
        }
        final String final_s=s;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                internetStatusTextView.setText(final_s);
            }
        });
    }
    private Observer<Integer> sensorStatusObserver=new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            int connected=viewModel.sensorConnectedLiveData.getValue();
            int amount=viewModel.sensorAmountLiveData.getValue();
            setSensorStatusShow(connected,amount);
        }
    };

}
