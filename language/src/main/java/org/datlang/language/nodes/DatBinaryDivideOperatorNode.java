package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.DatRuntimeException;

import static com.oracle.truffle.api.CompilerDirectives.transferToInterpreter;

public abstract class DatBinaryDivideOperatorNode extends DatBinaryOperatorNode {
    @Specialization
    protected long longs(long lhs, long rhs) {
        if (rhs == 0) {
            transferToInterpreter();
            throw DatRuntimeException.create("Integer division by 0", this);
        }
        else if (lhs == Long.MIN_VALUE && rhs == -1) {
            transferToInterpreter();
            throw DatRuntimeException.create("Integer overflow", this);
        }
        else {
            return lhs / rhs;
        }
    }

    @Specialization
    protected double longAndDouble(long lhs, double rhs) {
        return doubles(lhs, rhs);
    }

    @Specialization
    protected double doubleAndLong(double lhs, long rhs) {
        return doubles(lhs, rhs);
    }

    @Specialization
    protected double doubles(double lhs, double rhs) {
        return lhs / rhs;
    }

    @Fallback
    protected Object illegal(Object lhs, Object rhs) {
        transferToInterpreter();
        throw DatRuntimeException.create("Invalid operands", this);
    }
}
