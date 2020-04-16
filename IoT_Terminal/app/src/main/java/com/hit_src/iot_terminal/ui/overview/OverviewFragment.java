package com.hit_src.iot_terminal.ui.overview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.ui.overview.components.StatusLinearLayout;

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
        assert view != null;
        sensorStatusLinearLayout=view.findViewById(R.id.Overview_SensorStatus_LinearLayout);
        sensorStatusTextView=view.findViewById(R.id.Overview_SensorStatus_TextView);
        internetStatusLinearLayout=view.findViewById(R.id.Overview_InternetConnectionStatus_LinearLayout);
        internetStatusTextView=view.findViewById(R.id.Overview_InternetStatus_TextView);
        logEditText=view.findViewById(R.id.Overview_Log_EditText);

        viewModel= new ViewModelProvider(this).get(OverviewViewModel.class);
        viewModel.sensorConnectedLiveData.observe(getViewLifecycleOwner(),sensorStatusObserver);
        viewModel.sensorAmountLiveData.observe(getViewLifecycleOwner(),sensorStatusObserver);
        viewModel.internetConnectionLiveData.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean newStatus) {
                setInternetStatusShow(newStatus);
            }
        });
        viewModel.logLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(final String s) {
                MainActivity.self.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logEditText.setText(s);
                        logEditText.setSelection(s.length());
                    }
                });
            }
        });
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Integer connected=viewModel.sensorConnectedLiveData.getValue();
                Integer amount=viewModel.sensorAmountLiveData.getValue();
                if(connected==null||amount==null){
                    return;
                }
                setSensorStatusShow(connected,amount);
                setInternetStatusShow(viewModel.internetConnectionLiveData.getValue());
                logEditText.setText("");
            }
        });
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
        MainActivity.self.runOnUiThread(new Runnable() {
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
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                internetStatusTextView.setText(final_s);
            }
        });
    }
    private final Observer<Integer> sensorStatusObserver=new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            Integer connected=viewModel.sensorConnectedLiveData.getValue();
            Integer amount=viewModel.sensorAmountLiveData.getValue();
            if(connected==null||amount==null){
                return;
            }
            setSensorStatusShow(connected,amount);
        }
    };

}
