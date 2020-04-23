package com.hit_src.iot_terminal.ui.sensor.add;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.object.sensortype.SensorType;

import java.util.HashSet;
import java.util.Set;

public class SensorAddFragment extends Fragment {
    private SensorAddViewModel viewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensoradd, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();

        final View view=getView();
        assert view != null;
        //需要响应的控件代码
        {
            view.findViewById(R.id.SensorAdd_ConfirmButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String loraAddr=((EditText)(view.findViewById(R.id.SensorAdd_LoraAddrEditText))).getText().toString();
                    if(loraAddr.isEmpty()){
                        Toast.makeText(getContext(),"LoRa地址不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String datatype= (String) ((Spinner)(view.findViewById(R.id.SensorAdd_TypeSpinner))).getSelectedItem();
                    for(SensorType i:GlobalVar.sensorTypes){
                        if(datatype.equals(i.name)){
                            int id=0;
                            for(Sensor j:GlobalVar.sensors){
                                if(j.getID()>=id){
                                    id=j.getID()+1;
                                }
                            }
                            Sensor tmp=new Sensor();
                            tmp.setID(id);
                            tmp.setType(i.id);
                            tmp.setLoraAddr(Integer.parseInt(loraAddr));
                            tmp.setEnabled(true);
                            tmp.setConnected(false);
                            GlobalVar.sensors.add(tmp);
                            break;
                        }
                    }
                    FragmentManager manager=getParentFragmentManager();
                    FragmentTransaction transaction=manager.beginTransaction();
                    transaction.remove(SensorAddFragment.this);
                    transaction.commit();
                }
            });
        }
        //获取ViewModel
        viewModel = new ViewModelProvider(this).get(SensorAddViewModel.class);
        //需要显示的控件与ViewModel绑定
        {
            viewModel.sensorTypeLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayAdapter<String>>() {
                @Override
                public void onChanged(final ArrayAdapter<String> stringArrayAdapter) {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((Spinner)(view.findViewById(R.id.SensorAdd_TypeSpinner))).setAdapter(stringArrayAdapter);
                            ((Spinner)(view.findViewById(R.id.SensorAdd_TypeSpinner))).setSelection(0);
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
                    ((Spinner)(view.findViewById(R.id.SensorAdd_TypeSpinner))).setAdapter(viewModel.sensorTypeLiveData.getValue());
                    ((Spinner)(view.findViewById(R.id.SensorAdd_TypeSpinner))).setSelection(0);
                }
            });
        }
    }
}
