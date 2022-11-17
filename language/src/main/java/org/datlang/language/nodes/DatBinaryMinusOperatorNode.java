package org.datlang.language.nodes;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

public abstract class DatBinaryMinusOperatorNode extends DatBinaryOperatorNode {
    @Specialization(rewriteOn = ArithmeticException.class)
    protected long longsWithoutOverflow(long lhs, long rhs) {
        return Math.subtractExact(lhs, rhs);
    }

    @Specialization(replaces = "longsWithoutOverflow")
    protected Object longs(long lhs, long rhs) {
        try {
            return Math.subtractExact(lhs, rhs);
        } catch (ArithmeticException ignored) {
            return subtractLongsToBigInteger(lhs, rhs);
        }
    }

    @TruffleBoundary
    private static BigInteger subtractLongsToBigInteger(long lhs, long rhs) {
        return BigInteger.valueOf(lhs).subtract(BigInteger.valueOf(rhs));
    }

    @Specialization
    @TruffleBoundary
    protected Object bigIntegers(BigInteger lhs, BigInteger rhs) {
        var result = lhs.subtract(rhs);
        try {
            return result.longValueExact();
        } catch (ArithmeticException ignored) {
            return result;
        }
    }

    @Specialization
    protected double doubles(double lhs, double rhs) {
        return lhs - rhs;
    }
}
