package com.hit_src.iot_terminal.tools;

import android.app.Activity;
import android.widget.Button;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.ui.OverviewActivity;
import com.hit_src.iot_terminal.ui.SerialActivity;

public class Tools {
    public static void guidanceButtonSet(Activity self){
        setGuidanceButton((Button) self.findViewById(R.id.GuidanceBar_Overview_Button),self,OverviewActivity.class);
        setGuidanceButton((Button) self.findViewById(R.id.GuidanceBar_Serial_Button),self,SerialActivity.class);
    }
    private static void setGuidanceButton(Button button,Activity self,Class<?> activityClass){
        button.setOnClickListener(new GuidanceButtonClickListener(self,activityClass));
        button.setEnabled(true);
    }

}
