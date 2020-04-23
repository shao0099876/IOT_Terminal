package com.hit_src.iot_terminal.ui.advance.packagemanager;

import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.object.XMLRecord;

import java.util.ArrayList;

public class AdvancePackageManagerViewModel extends ViewModel {
    MutableLiveData<BaseAdapter> packageListViewAdapterLiveData=new MutableLiveData<>();
    public AdvancePackageManagerViewModel(){
        //包管理器列表
        {
            packageListViewAdapterLiveData.setValue(new PackageListAdapter(GlobalVar.xmlRecords));
            GlobalVar.xmlRecords.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<XMLRecord>>() {
                private void work(ObservableList<XMLRecord> sender){
                    packageListViewAdapterLiveData.postValue(new PackageListAdapter(sender));
                }
                @Override
                public void onChanged(ObservableList<XMLRecord> sender) {
                    work(sender);
                }

                @Override
                public void onItemRangeChanged(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                    work(sender);
                }

                @Override
                public void onItemRangeInserted(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                    work(sender);
                }

                @Override
                public void onItemRangeMoved(ObservableList<XMLRecord> sender, int fromPosition, int toPosition, int itemCount) {
                    work(sender);
                }

                @Override
                public void onItemRangeRemoved(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                    work(sender);
                }
            });
        }
    }
}
