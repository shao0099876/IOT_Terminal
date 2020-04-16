package com.hit_src.iot_terminal.ui.advance;

import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.object.XMLRecord;

import java.util.ArrayList;

public class AdvancePackageManagerViewModel extends ViewModel {
    public MutableLiveData<ArrayList<XMLRecord>> packageListViewModel=new MutableLiveData<>();
    public AdvancePackageManagerViewModel(){
        packageListViewModel.setValue(new ArrayList<XMLRecord>());
        GlobalVar.xmlRecords.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<XMLRecord>>() {
            @Override
            public void onChanged(ObservableList<XMLRecord> sender) {
                packageListViewModel.postValue((ArrayList<XMLRecord>) sender.subList(0,sender.size()));
            }

            @Override
            public void onItemRangeChanged(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                packageListViewModel.postValue((ArrayList<XMLRecord>) sender.subList(0,sender.size()));
            }

            @Override
            public void onItemRangeInserted(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                packageListViewModel.postValue((ArrayList<XMLRecord>) sender.subList(0,sender.size()));
            }

            @Override
            public void onItemRangeMoved(ObservableList<XMLRecord> sender, int fromPosition, int toPosition, int itemCount) {
                packageListViewModel.postValue((ArrayList<XMLRecord>) sender.subList(0,sender.size()));
            }

            @Override
            public void onItemRangeRemoved(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                packageListViewModel.postValue((ArrayList<XMLRecord>) sender.subList(0,sender.size()));
            }
        });
    }
}
