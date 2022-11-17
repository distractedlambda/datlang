package org.datlang.language.nodes;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

public abstract class DatUnaryMinusOperatorNode extends DatUnaryOperatorNode {
    @Specialization(rewriteOn = ArithmeticException.class)
    protected long negateLongWithoutOverflow(long operand) {
        return Math.negateExact(operand);
    }

    @Specialization(replaces = "negateLongWithoutOverflow")
    protected Object negateLong(long operand) {
        if (operand == Long.MIN_VALUE) {
            return MIN_LONG_NEGATION;
        } else {
            return -operand;
        }
    }

    @Specialization
    @TruffleBoundary
    protected Object negateBigInteger(BigInteger operand) {
        var result = operand.negate();
        try {
            return result.longValueExact();
        } catch (ArithmeticException ignored) {
            return result;
        }
    }

    @Specialization
    protected double negateDouble(double operand) {
        return -operand;
    }

    private static final BigInteger MIN_LONG_NEGATION = BigInteger.valueOf(Long.MIN_VALUE).negate();
}
