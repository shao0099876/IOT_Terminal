package com.hit_src.iot_terminal.ui.advance.packagemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.tools.pm.PackageManager;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdvancePackageManagerFragment extends Fragment {
    private View selectedView=null;
    private XMLRecord selected=null;
    private AdvancePackageManagerViewModel viewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advance_sensortype, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        final View view=getView();
        assert view != null;
        //需要响应的控件代码
        {
            ((ListView)(view.findViewById(R.id.Advance_PackageManagerListView))).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(selectedView!=null){
                        selectedView.setBackgroundColor(0);
                    }
                    view.setBackgroundColor(getResources().getColor(R.color.ListView_SelectedColor));
                    selectedView=view;
                    selected= (XMLRecord) parent.getItemAtPosition(position);
                }
            });
            view.findViewById(R.id.PackageManager_PullButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selected==null){
                        Toast.makeText(MainApplication.self, "尚未选中配置文件", Toast.LENGTH_SHORT).show();
                    } else{
                        PackageManager.pull(selected.name,selected.remoteVersion);
                        PackageManager.fetch();
                    }
                }
            });
            view.findViewById(R.id.PackageManager_DeleteButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selected==null){
                        Toast.makeText(MainApplication.self, "尚未选中配置文件", Toast.LENGTH_SHORT).show();
                    } else{
                        PackageManager.delete(selected.name);
                        PackageManager.fetch();
                    }
                }
            });
        }
        //获取ViewModel
        viewModel = new ViewModelProvider(this).get(AdvancePackageManagerViewModel.class);
        //需要显示的控件与ViewModel绑定
        {
            viewModel.packageListViewAdapterLiveData.observe(getViewLifecycleOwner(), new Observer<BaseAdapter>() {
                @Override
                public void onChanged(BaseAdapter baseAdapter) {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ListView)(view.findViewById(R.id.Advance_PackageManagerListView))).setAdapter(viewModel.packageListViewAdapterLiveData.getValue());
                        }
                    });
                }
            });
        }
        //初始化显示
        {
            PackageManager.fetch();
            MainActivity.self.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((ListView)(view.findViewById(R.id.Advance_PackageManagerListView))).setAdapter(viewModel.packageListViewAdapterLiveData.getValue());
                }
            });
        }
    }
}