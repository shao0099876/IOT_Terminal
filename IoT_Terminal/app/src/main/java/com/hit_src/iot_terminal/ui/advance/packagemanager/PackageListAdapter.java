package com.hit_src.iot_terminal.ui.advance.packagemanager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.XMLRecord;

import java.util.ArrayList;
import java.util.List;

public class PackageListAdapter extends BaseAdapter {
    private List<XMLRecord> list;
    public PackageListAdapter(List<XMLRecord> p){
        list=p;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(MainApplication.self, R.layout.packagemanager_listviewlayout,null);
        XMLRecord xmlRecord=list.get(position);
        ((TextView)(view.findViewById(R.id.PackageManager_NameTextView))).setText(xmlRecord.name);
        ((TextView)(view.findViewById(R.id.PackageManager_LocalVersionTextView))).setText(xmlRecord.getLocalVersion());
        ((TextView)(view.findViewById(R.id.PackageManager_RemoteVersionTextView))).setText(xmlRecord.getRemoteVersion());
        return view;
    }
}
