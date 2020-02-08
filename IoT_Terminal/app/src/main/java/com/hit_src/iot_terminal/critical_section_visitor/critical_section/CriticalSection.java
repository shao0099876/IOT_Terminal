package com.hit_src.iot_terminal.critical_section_visitor.critical_section;

import com.hit_src.iot_terminal.critical_section_visitor.critical_section_source.CriticalSectionSource;

public abstract class CriticalSection {
    public abstract CriticalSectionSource read(CriticalSectionSource source);
    public abstract void write(CriticalSectionSource source);
}
