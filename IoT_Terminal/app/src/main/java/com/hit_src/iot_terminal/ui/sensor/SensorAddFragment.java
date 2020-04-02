package com.hit_src.iot_terminal.ui.sensor;

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

import com.hit_src.iot_terminal.Global;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;

public class SensorAddFragment extends Fragment {
    private Spinner typeSpinner;
    private EditText loraAddrEditText;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensoradd, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();

        View view=getView();
        typeSpinner=view.findViewById(R.id.Sensor_Type_Spinner);
        loraAddrEditText=view.findViewById(R.id.Sensor_Add_LoraAddr_EditText);
        Button confirmButton=view.findViewById(R.id.Sensor_Add_Button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loraAddrEditText.getText().toString()==null||loraAddrEditText.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"LoRa地址不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    MainApplication.dbService.addSensor((String) typeSpinner.getSelectedItem(),Integer.valueOf(loraAddrEditText.getText().toString()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                FragmentManager manager=getParentFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.remove(SensorAddFragment.this);
                transaction.commit();
            }
        });

        final ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<>(getContext(),R.layout.spinner_display_style, Global.getSensorTypeList());
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                typeSpinner.setAdapter(spinnerAdapter);
            }
        });

    }
}
