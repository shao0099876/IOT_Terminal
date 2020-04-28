package com.hit_src.iot_terminal.ui.advance;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.object.XMLRecord;

import java.util.ArrayList;

public class PackageManagerFragmentViewModel extends ViewModel {
    public MutableLiveData<ArrayList<XMLRecord>> packageLiveData=new MutableLiveData<>();
    public PackageManagerFragmentViewModel(){
        
    }
}
