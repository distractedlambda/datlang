package org.datlang.language.nodes.pattern;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class DatFalsePatternNode extends DatPatternNode {
    @Specialization
    protected boolean matchBoolean(boolean subject) {
        return !subject;
    }

    @Fallback
    protected boolean matchOther(Object subject) {
        return false;
    }
}
