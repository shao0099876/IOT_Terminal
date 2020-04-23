package com.hit_src.iot_terminal.ui.advance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.ui.advance.packagemanager.AdvancePackageManagerFragment;

public class AdvanceFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advance, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        View view=getView();
        assert view != null;
        //需要响应的控件代码
        view.findViewById(R.id.Advance_PackageManagerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager= MainActivity.self.getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.Advance_Fragment,new AdvancePackageManagerFragment());
                transaction.commit();
            }
        });


    }
}
