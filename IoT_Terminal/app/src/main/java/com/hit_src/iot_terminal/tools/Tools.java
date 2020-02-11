package com.hit_src.iot_terminal.tools;

import android.app.Activity;
import android.widget.Button;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.ui.OverviewActivity;
import com.hit_src.iot_terminal.ui.SerialActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Tools {
    public static void guidanceButtonSet(Activity self){
        Button overviewButton=self.findViewById(R.id.GuidanceBar_Overview_Button);
        overviewButton.setOnClickListener(new GuidanceButtonClickListener(self, OverviewActivity.class));
        overviewButton.setEnabled(true);

        Button serialButton=self.findViewById(R.id.GuidanceBar_Serial_Button);
        serialButton.setOnClickListener(new GuidanceButtonClickListener(self, SerialActivity.class));
        serialButton.setEnabled(true);


    }


}
