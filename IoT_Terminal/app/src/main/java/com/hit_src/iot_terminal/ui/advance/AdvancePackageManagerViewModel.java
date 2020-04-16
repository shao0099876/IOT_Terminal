package com.hit_src.iot_terminal.ui.advance;

import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.object.XMLRecord;

import java.util.ArrayList;

class AdvancePackageManagerViewModel extends ViewModel {
    public final MutableLiveData<ArrayList<XMLRecord>> packageListViewModel=new MutableLiveData<>();
    public AdvancePackageManagerViewModel(){
        packageListViewModel.setValue(new ArrayList<XMLRecord>());
        GlobalVar.xmlRecords.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<XMLRecord>>() {
            @Override
            public void onChanged(ObservableList<XMLRecord> sender) {
                ArrayList<XMLRecord> tmp = new ArrayList<>(sender.subList(0, sender.size()));
                packageListViewModel.postValue(tmp);
            }

            @Override
            public void onItemRangeChanged(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                ArrayList<XMLRecord> tmp = new ArrayList<>(sender.subList(0, sender.size()));
                packageListViewModel.postValue(tmp);
            }

            @Override
            public void onItemRangeInserted(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                ArrayList<XMLRecord> tmp = new ArrayList<>(sender.subList(0, sender.size()));
                packageListViewModel.postValue(tmp);
            }

            @Override
            public void onItemRangeMoved(ObservableList<XMLRecord> sender, int fromPosition, int toPosition, int itemCount) {
                ArrayList<XMLRecord> tmp = new ArrayList<>(sender.subList(0, sender.size()));
                packageListViewModel.postValue(tmp);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                ArrayList<XMLRecord> tmp = new ArrayList<>(sender.subList(0, sender.size()));
                packageListViewModel.postValue(tmp);
            }
        });
    }
}
