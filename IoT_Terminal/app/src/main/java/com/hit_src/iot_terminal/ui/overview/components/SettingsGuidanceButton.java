package com.hit_src.iot_terminal.ui.overview.components;

import android.content.Context;
import android.util.AttributeSet;

import com.hit_src.iot_terminal.ui.settings.SettingsFragment;

public class SettingsGuidanceButton extends GuidanceButton {
    public SettingsGuidanceButton(Context context) {
        super(context);
        init(new SettingsFragment());
    }

    public SettingsGuidanceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(new SettingsFragment());
    }

    public SettingsGuidanceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(new SettingsFragment());
    }
}
