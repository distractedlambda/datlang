package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class DatEqualityOperatorNode extends DatBinaryOperatorNode {
    @Specialization
    protected boolean booleans(boolean lhs, boolean rhs) {
        return lhs == rhs;
    }

    @Specialization
    protected boolean longs(long lhs, long rhs) {
        return lhs == rhs;
    }

    @Specialization
    protected boolean doubles(double lhs, double rhs) {
        return lhs == rhs;
    }

    @Fallback
    protected boolean other(Object lhs, Object rhs) {
        return lhs == rhs;
    }
}
