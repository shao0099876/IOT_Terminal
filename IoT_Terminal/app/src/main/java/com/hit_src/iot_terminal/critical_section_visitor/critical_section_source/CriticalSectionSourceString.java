package com.hit_src.iot_terminal.critical_section_visitor.critical_section_source;


public class CriticalSectionSourceString extends CriticalSectionSource {
    private String data;
    public CriticalSectionSourceString(){
        super();
        data=null;
    }
    public CriticalSectionSourceString(String p){
        super();
        data=p;
    }
}
