package com.hit_src.iot_terminal.ui.settings;

import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.service.SettingsService;

public class SettingsInternetFragment extends Fragment {
    private EditText serverAddrEditText;
    private EditText serverModbusPortEditText;
    private EditText serverXMLPortEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_internet, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        serverAddrEditText = view.findViewById(R.id.Settings_Internet_ServerAddr_EditText);
        serverModbusPortEditText = view.findViewById(R.id.Settings_Internet_ModbusPort_EditText);
        serverXMLPortEditText = view.findViewById(R.id.Settings_Internet_XMLPort_EditText);

        serverAddrEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String addr = s.toString();
                SettingsService.getInstance().setUpperServerAddr(addr);
            }
        });
        serverModbusPortEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String port = s.toString();
                try {
                    SettingsService.getInstance().setUpperServerModbusPort(Integer.parseInt(port));
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "格式错误", Toast.LENGTH_SHORT);
                }
            }
        });
        serverXMLPortEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String port = s.toString();
                try {
                    SettingsService.getInstance().setUpperServerXMLPort(Integer.parseInt(port));
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "格式错误", Toast.LENGTH_SHORT);
                }
            }
        });
        String serverAddr = null;
        String serverModbusPort = null;
        String serverXMLPort = null;
        serverAddr = SettingsService.getInstance().getUpperServerAddr();
        int modbusPort = SettingsService.getInstance().getUpperServerModbusPort();
        serverModbusPort = modbusPort < 0 ? "" : String.valueOf(modbusPort);
        int xmlPort = SettingsService.getInstance().getUpperServerXMLPort();
        serverXMLPort = xmlPort < 0 ? "" : String.valueOf(xmlPort);
        final String serverAddr_final = serverAddr;
        final String serverModbusPort_final = serverModbusPort;
        final String serverXMLPort_final = serverXMLPort;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                serverAddrEditText.setText(serverAddr_final);
                serverModbusPortEditText.setText(serverModbusPort_final);
                serverXMLPortEditText.setText(serverXMLPort_final);
            }
        });
    }
}
