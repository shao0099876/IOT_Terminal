package com.hit_src.iot_terminal.tools;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class GuidanceButtonClickListener implements View.OnClickListener {
    private Activity self;
    private Class<?> target;

    public GuidanceButtonClickListener(Activity self, Class<?> activityClass) {
        this.self=self;
        target=activityClass;
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
