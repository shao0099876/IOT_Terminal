package com.hit_src.iot_terminal.ui.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.sensortype.Datatype;

import java.util.ArrayList;

public class DataFragment extends Fragment {
    private ListView dataTypeListView;
    private View lastSelectedView = null;
    private Fragment childFragment = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        assert view != null;
        dataTypeListView = view.findViewById(R.id.Data_ListView);
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataTypeListView.setAdapter(new DataAdapter(GlobalVar.getDataTypeList()));
            }
        });
        dataTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastSelectedView != null) {
                    lastSelectedView.setBackgroundColor(0);
                }
                view.setBackgroundColor(getResources().getColor(R.color.List_Clicked_Color));
                lastSelectedView = view;
                childFragment = new DataDetailedFragment((Datatype) parent.getItemAtPosition(position));
                FragmentTransaction transaction = MainActivity.self.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.Data_DrawFragment, childFragment);
                transaction.commit();
            }
        });
    }
}

class DataAdapter extends BaseAdapter {
    private ArrayList<Datatype> datatypes;

    public DataAdapter(ArrayList<Datatype> p) {
        datatypes = p;
    }

    @Override
    public int getCount() {
        return datatypes.size();
    }

    @Override
    public Object getItem(int position) {
        return datatypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(MainActivity.self, R.layout.data_listview_layout, null);
        TextView textView = view.findViewById(R.id.Data_Listview_TextView);
        textView.setText(datatypes.get(position).name);
        return view;
    }
}