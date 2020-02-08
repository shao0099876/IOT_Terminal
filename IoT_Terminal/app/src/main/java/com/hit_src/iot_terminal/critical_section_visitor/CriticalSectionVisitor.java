package com.hit_src.iot_terminal.critical_section_visitor;

import com.hit_src.iot_terminal.critical_section_visitor.critical_section.CriticalSection;
import com.hit_src.iot_terminal.critical_section_visitor.critical_section_source.CriticalSectionSource;

public class CriticalSectionVisitor {
    private CriticalSection criticalSection;
    public CriticalSectionVisitor(CriticalSection criticalSection){
        this.criticalSection=criticalSection;
    }
    public CriticalSectionSource read(final CriticalSectionSource source){
        final CriticalSectionSource[] res = {null};
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                res[0] =criticalSection.read(source);
            }
        });
        thread.run();
        return res[0];
    }
    public void write(final CriticalSectionSource source){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                criticalSection.write(source);
            }
        });
        thread.run();
    }

}