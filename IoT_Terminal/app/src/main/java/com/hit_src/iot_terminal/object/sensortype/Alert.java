package com.hit_src.iot_terminal.object.sensortype;

import com.hit_src.iot_terminal.object.Environment;

public class Alert {
    public Trigger trigger;
    public Reaction reaction;

    public void setTrigger(Trigger trigger) {
        this.trigger=trigger;
    }

    public void setReaction(Reaction reaction) {
        this.reaction=reaction;
    }

    public boolean activated(int res) {
        return trigger.trigger(res);
    }

    public void react(Environment environment) {
        reaction.activited(environment);
    }
}
