package com.hit_src.iot_terminal.critical_section_visitor.critical_section;

import android.content.SharedPreferences;

import com.hit_src.iot_terminal.critical_section_visitor.critical_section_source.CriticalSectionSource;
import com.hit_src.iot_terminal.critical_section_visitor.critical_section_source.CriticalSectionSourceString;

public class CriticalSectionSharedPreferences extends CriticalSection {
    private SharedPreferences criticalSection;
    public CriticalSectionSharedPreferences(SharedPreferences section){
        super();
        criticalSection=section;
    }

    @Override
    public CriticalSectionSource read(CriticalSectionSource source) {
        return null;
    }

    @Override
    public void write(CriticalSectionSource source) {

    }
}
