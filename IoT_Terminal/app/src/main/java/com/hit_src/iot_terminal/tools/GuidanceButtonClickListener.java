package com.hit_src.iot_terminal.tools;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.hit_src.iot_terminal.ui.OverviewActivity;
import com.hit_src.iot_terminal.ui.SerialActivity;

public class GuidanceButtonClickListener implements View.OnClickListener {
    private Activity self;
    private Class<?> target;
    public GuidanceButtonClickListener(){
        self=null;
        target=null;
    }

    public GuidanceButtonClickListener(Activity self, Class<?> overviewActivityClass) {
        this.self=self;
        target=overviewActivityClass;
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(self,target);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        self.startActivity(intent);
    }
}
