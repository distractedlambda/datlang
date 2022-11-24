package org.datlang.language.nodes.expressions;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.runtime.DatRuntimeException;

import static com.oracle.truffle.api.CompilerDirectives.transferToInterpreter;

public abstract class DatBinaryTimesExpressionNode extends DatBinaryExpressionNode {
    @Specialization
    protected long longs(long lhs, long rhs) {
        try {
            return Math.multiplyExact(lhs, rhs);
        }
        catch (ArithmeticException ignored) {
            transferToInterpreter();
            throw DatRuntimeException.create("Integer overflow", this);
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
        return lhs * rhs;
    }

    @Fallback
    protected Object illegal(Object lhs, Object rhs) {
        transferToInterpreter();
        throw DatRuntimeException.create("Invalid operands", this);
    }
}
