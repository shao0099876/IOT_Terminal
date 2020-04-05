package com.hit_src.iot_terminal.ui.overview.components;

import android.content.Context;
import android.util.AttributeSet;

import com.hit_src.iot_terminal.ui.sensor.SensorFragment;

public class SensorGuidanceButton extends GuidanceButton {
    public SensorGuidanceButton(Context context) {
        super(context);
        init(new SensorFragment());
    }

    public SensorGuidanceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(new SensorFragment());
    }

    public SensorGuidanceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(new SensorFragment());
    }
}
