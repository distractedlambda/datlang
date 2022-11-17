package org.datlang.language.nodes;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

public abstract class DatBinaryTimesOperatorNode extends DatBinaryOperatorNode {
    @Specialization(rewriteOn = ArithmeticException.class)
    protected long longsWithoutOverflow(long lhs, long rhs) {
        return Math.multiplyExact(lhs, rhs);
    }

    @Specialization(replaces = "longsWithoutOverflow")
    protected Object longs(long lhs, long rhs) {
        try {
            return Math.multiplyExact(lhs, rhs);
        } catch (ArithmeticException ignored) {
            return multiplyLongsToBigInteger(lhs, rhs);
        }
    }

    @TruffleBoundary
    private static BigInteger multiplyLongsToBigInteger(long lhs, long rhs) {
        return BigInteger.valueOf(lhs).multiply(BigInteger.valueOf(rhs));
    }

    @Specialization
    @TruffleBoundary
    protected Object bigIntegers(BigInteger lhs, BigInteger rhs) {
        var result = lhs.multiply(rhs);
        try {
            return result.longValueExact();
        } catch (ArithmeticException ignored) {
            return result;
        }
    }

    @Specialization
    protected double doubles(double lhs, double rhs) {
        return lhs * rhs;
    }
}
