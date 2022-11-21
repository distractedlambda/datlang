package org.datlang.language.nodes.pattern;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;

@NodeField(name = "expectedValue", type = Object.class)
public abstract class DatIdenticalObjectPatternNode extends DatPatternNode {
    protected abstract Object getExpectedValue();

    @Specialization
    protected boolean matchBoolean(boolean subject) {
        return false;
    }

    @Specialization
    protected boolean matchLong(long subject) {
        return false;
    }

    @Specialization
    protected boolean matchDouble(double subject) {
        return false;
    }

    @Specialization(replaces = {"matchBoolean", "matchLong", "matchDouble"})
    protected boolean matchObject(Object subject) {
        return subject == getExpectedValue();
    }
}
