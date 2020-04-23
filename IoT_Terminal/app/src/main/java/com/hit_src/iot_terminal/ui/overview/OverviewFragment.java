package com.hit_src.iot_terminal.ui.overview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.ui.advance.AdvanceFragment;
import com.hit_src.iot_terminal.ui.data.DataFragment;
import com.hit_src.iot_terminal.ui.sensor.SensorFragment;
import com.hit_src.iot_terminal.ui.settings.SettingsFragment;

public class OverviewFragment extends Fragment {

    private OverviewViewModel viewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        final View view = getView();
        assert view != null;
        //需要响应的控件代码
        {
            view.findViewById(R.id.Overview_SensorGuidanceButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = MainActivity.self.getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.MainFragment, new SensorFragment());
                    transaction.commit();
                }
            });
            view.findViewById(R.id.Overview_DataGuidanceButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = MainActivity.self.getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.MainFragment, new DataFragment());
                    transaction.commit();
                }
            });
            view.findViewById(R.id.Overview_SettingsGuidanceButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = MainActivity.self.getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.MainFragment, new SettingsFragment());
                    transaction.commit();
                }
            });
            view.findViewById(R.id.Overview_AdvanceGuidanceButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = MainActivity.self.getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.MainFragment, new AdvanceFragment());
                    transaction.commit();
                }
            });
        }
        //获取ViewModel
        viewModel = new ViewModelProvider(this).get(OverviewViewModel.class);
        //需要显示的控件与ViewModel绑定
        {
            //Overview_ClockTextView
            viewModel.timeLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(final String s) {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) (view.findViewById(R.id.Overview_ClockTextView))).setText(s);
                        }
                    });
                }
            });
            //Overview_DateTextView
            viewModel.dateLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(final String s) {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) (view.findViewById(R.id.Overview_DateTextView))).setText(s);
                        }
                    });
                }
            });
            //Overview_SensorStatusLinearLayout
            viewModel.sensorStatusColorLiveData.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(final Integer integer) {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.findViewById(R.id.Overview_SensorStatusLinearLayout).setBackgroundColor(getResources().getColor(integer));
                        }
                    });
                }
            });
            //Overview_SensorStatusTextView
            viewModel.sensorStatusTextLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(final String s) {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) (view.findViewById(R.id.Overview_SensorStatusTextView))).setText(s);
                        }
                    });
                }
            });
            //Overview_InternetStatusLinearLayout
            viewModel.internetStatusColorLiveData.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(final Integer integer) {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.findViewById(R.id.Overview_InternetStatusLinearLayout).setBackgroundColor(getResources().getColor(integer));
                        }
                    });
                }
            });
            //Overview_InternetStatusTextView
            viewModel.internetStatusTextLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(final String s) {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) (view.findViewById(R.id.Overview_InternetStatusTextView))).setText(s);
                        }
                    });
                }
            });
            //Overview_LogEditText
            viewModel.logLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(final String s) {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EditText logEditText = view.findViewById(R.id.Overview_LogEditText);
                            logEditText.setText(s);
                            logEditText.setSelection(s.length());
                        }
                    });
                }
            });
        }
        //初始化显示
        {
            MainActivity.self.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Overview_ClockTextView
                    ((TextView) (view.findViewById(R.id.Overview_ClockTextView))).setText(viewModel.timeLiveData.getValue());
                    //Overview_DateTextView
                    ((TextView) (view.findViewById(R.id.Overview_DateTextView))).setText(viewModel.dateLiveData.getValue());
                    //Overview_SensorStatusLinearLayout
                    view.findViewById(R.id.Overview_SensorStatusLinearLayout).setBackgroundColor(getResources().getColor(viewModel.sensorStatusColorLiveData.getValue()));
                    //Overview_SensorStatusTextView
                    ((TextView) (view.findViewById(R.id.Overview_SensorStatusTextView))).setText(viewModel.sensorStatusTextLiveData.getValue());
                    //Overview_InternetStatusLinearLayout
                    view.findViewById(R.id.Overview_InternetStatusLinearLayout).setBackgroundColor(getResources().getColor(viewModel.internetStatusColorLiveData.getValue()));
                    //Overview_InternetStatusTextView
                    ((TextView) (view.findViewById(R.id.Overview_InternetStatusTextView))).setText(viewModel.internetStatusTextLiveData.getValue());
                }
            });
        }

    }
}
