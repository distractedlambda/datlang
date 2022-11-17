package org.datlang.language.nodes;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

public abstract class DatBinaryPlusOperatorNode extends DatBinaryOperatorNode {
    @Specialization(rewriteOn = ArithmeticException.class)
    protected long longsWithoutOverflow(long lhs, long rhs) {
        return Math.addExact(lhs, rhs);
    }

    @Specialization(replaces = "longsWithoutOverflow")
    protected Object longs(long lhs, long rhs) {
        try {
            return Math.addExact(lhs, rhs);
        } catch (ArithmeticException ignored) {
            return addLongsToBigInteger(lhs, rhs);
        }
    }

    @TruffleBoundary
    private static BigInteger addLongsToBigInteger(long lhs, long rhs) {
        return BigInteger.valueOf(lhs).add(BigInteger.valueOf(rhs));
    }

    @Specialization
    @TruffleBoundary
    protected Object bigIntegers(BigInteger lhs, BigInteger rhs) {
        var sum = lhs.add(rhs);
        try {
            return sum.longValueExact();
        } catch (ArithmeticException ignored) {
            return sum;
        }
    }

    @Specialization
    protected double doubles(double lhs, double rhs) {
        return lhs + rhs;
    }
}
