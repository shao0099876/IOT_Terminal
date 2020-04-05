package com.hit_src.iot_terminal.ui.overview.components;

import android.content.Context;
import android.util.AttributeSet;

import com.hit_src.iot_terminal.ui.data.DataFragment;

public class DataGuidanceButton extends GuidanceButton {
    public DataGuidanceButton(Context context) {
        super(context);
        init(new DataFragment());
    }

    public DataGuidanceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(new DataFragment());
    }

    public DataGuidanceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(new DataFragment());
    }
}
