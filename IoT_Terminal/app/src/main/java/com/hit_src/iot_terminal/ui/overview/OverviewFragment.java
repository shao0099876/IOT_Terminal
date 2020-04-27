package com.hit_src.iot_terminal.ui.overview;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.ui.overview.components.StatusLinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class OverviewFragment extends Fragment {

    private OverviewViewModel viewModel;
    private TextView clockTextView;
    private TextView dateTextView;
    private LinearLayout sensorStatusLinearLayout;
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
    public void onStart() {
        super.onStart();
        viewModel = new ViewModelProvider(this).get(OverviewViewModel.class);
        final View view = getView();
        assert view != null;
        clockTextView = view.findViewById(R.id.Overview_Clock_TextView);
        dateTextView = view.findViewById(R.id.Overview_Date_TextView);
        sensorStatusLinearLayout = view.findViewById(R.id.Overview_SensorStatus_LinearLayout);
        sensorStatusTextView = view.findViewById(R.id.Overview_SensorStatus_TextView);
        internetStatusLinearLayout = view.findViewById(R.id.Overview_InternetConnectionStatus_LinearLayout);
        internetStatusTextView = view.findViewById(R.id.Overview_InternetStatus_TextView);
        logEditText = view.findViewById(R.id.Overview_Log_EditText);

        viewModel.timeLiveData.observe(getViewLifecycleOwner(), new Observer<Date>() {
            @Override
            public void onChanged(final Date date) {
                MainActivity.self.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateClock(date);
                    }
                });
            }
        });
        viewModel.sensorStatusLiveData.observe(getViewLifecycleOwner(), new Observer<Pair<Integer, Integer>>() {
            @Override
            public void onChanged(Pair<Integer, Integer> integerIntegerPair) {
                updateSensorStatus(integerIntegerPair.first, integerIntegerPair.second);
            }
        });
        viewModel.internetConnectionLiveData.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                updateInternetStatus(aBoolean);
            }
        });
        viewModel.logLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                updateLog(s);
            }
        });

        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateClock(viewModel.timeLiveData.getValue());
                updateSensorStatus(viewModel.sensorStatusLiveData.getValue().first, viewModel.sensorStatusLiveData.getValue().second);
                updateInternetStatus(viewModel.internetConnectionLiveData.getValue());
                updateLog(viewModel.logLiveData.getValue());
            }
        });
    }

    private void updateLog(final String s) {
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logEditText.setText(s);
                logEditText.setSelection(s.length());
            }
        });
    }

    private void updateInternetStatus(boolean status) {
        final int color;
        if (status) {
            color = getResources().getColor(R.color.Overview_Status_Green);
        } else {
            color = getResources().getColor(R.color.Overview_Status_Red);
        }
        final String s = status ? "已连接" : "未连接";
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                internetStatusLinearLayout.setBackgroundColor(color);
                internetStatusTextView.setText(s);
            }
        });
    }

    private void updateSensorStatus(int connected, int amount) {
        final int color;
        if (connected == amount) {
            color = getResources().getColor(R.color.Overview_Status_Green);
        } else if (connected == 0) {
            color = getResources().getColor(R.color.Overview_Status_Red);
        } else {
            color = getResources().getColor(R.color.Overview_Status_Yellow);
        }
        final String s = connected + "/" + amount;
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sensorStatusTextView.setText(s);
                sensorStatusLinearLayout.setBackgroundColor(color);
            }
        });
    }

    private void updateClock(final Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        final String s1 = simpleDateFormat.format(date);
        simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        final String s2 = simpleDateFormat.format(date);
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clockTextView.setText(s1);
                dateTextView.setText(s2);
            }
        });
    }

}
