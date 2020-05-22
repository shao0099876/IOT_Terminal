package com.hit_src.iot_terminal.ui.sensor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.sensortype.SensorType;

import java.util.ArrayList;

public class SensorAddFragment extends Fragment {
    private Spinner typeSpinner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensoradd, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        final View view = getView();
        assert view != null;
        typeSpinner = view.findViewById(R.id.Sensor_Type_Spinner);
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                typeSpinner.setAdapter(new SensorTypeAdapter(GlobalVar.getSensorTypeList()));
                typeSpinner.setSelection(0);
            }
        });
        view.findViewById(R.id.Sensor_Add_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText loraAddrEditText = view.findViewById(R.id.Sensor_Add_LoraAddr_EditText);
                if (loraAddrEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "LoRa地址不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    SensorType sensorType = (SensorType) typeSpinner.getSelectedItem();
                    GlobalVar.addSensor(sensorType, Integer.parseInt(loraAddrEditText.getText().toString()));
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.remove(SensorAddFragment.this);
                    transaction.commit();
                }
            }
        });
    }
}

class SensorTypeAdapter extends BaseAdapter {
    private ArrayList<SensorType> sensorTypes;

    public SensorTypeAdapter(ArrayList<SensorType> p) {
        sensorTypes = p;
    }

    @Override
    public int getCount() {
        return sensorTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return sensorTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(MainActivity.self, R.layout.spinner_display_style, null);
        TextView textView = view.findViewById(R.id.Spinner_TextView);
        textView.setText(sensorTypes.get(position).name);
        return view;
    }
}