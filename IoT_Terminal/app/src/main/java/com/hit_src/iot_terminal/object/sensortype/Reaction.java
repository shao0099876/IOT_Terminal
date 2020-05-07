package com.hit_src.iot_terminal.object.sensortype;

import com.hit_src.iot_terminal.object.Environment;
import com.hit_src.iot_terminal.object.sensortype.reaction.Message;
import com.hit_src.iot_terminal.object.sensortype.reaction.React;

public class Reaction {
    public React react;
    public void setReaction(React reaction) {
        react=reaction;
    }

    public void activited(Environment environment) {
        react.react(environment);
    }
}
