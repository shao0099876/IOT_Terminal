package com.hit_src.iot_terminal.ui.overview.components;

import android.content.Context;
import android.util.AttributeSet;

import com.hit_src.iot_terminal.ui.advance.AdvanceFragment;

public class AdvanceGuidanceButton extends GuidanceButton {
    public AdvanceGuidanceButton(Context context) {
        super(context);
        init(new AdvanceFragment());
    }

    public AdvanceGuidanceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(new AdvanceFragment());
    }

    public AdvanceGuidanceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(new AdvanceFragment());
    }
}
