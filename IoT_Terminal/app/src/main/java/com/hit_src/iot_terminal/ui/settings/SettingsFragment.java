package com.hit_src.iot_terminal.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.R;

public class SettingsFragment extends Fragment {
    private Fragment childFragment;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        View view=getView();
        Button internet=view.findViewById(R.id.Settings_Internet_Button);
        internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childFragment=new SettingsInternetFragment();
                FragmentManager manager= MainActivity.self.getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.Settings_Fragment,childFragment);
                transaction.commit();
            }
        });
    }
}