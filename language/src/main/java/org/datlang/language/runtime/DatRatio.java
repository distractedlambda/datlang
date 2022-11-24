package org.datlang.language.runtime;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.CompilerDirectives.ValueType;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import org.datlang.language.nodes.DatNode;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import static java.util.Objects.hash;

public sealed abstract class DatRatio implements TruffleObject {
    public static @NotNull DatRatio createUncached(Object numerator, Object denominator) {
        return DatRatioFactory.CreateNodeGen.getUncached().execute(numerator, denominator);
    }

    public final @NotNull Object getNumeratorUncached() {
        return DatRatioFactory.GetNumeratorNodeGen.getUncached().executeGeneric(this);
    }

    public final @NotNull Object getDenominatorUncached() {
        return DatRatioFactory.GetDenominatorNodeGen.getUncached().executeGeneric(this);
    }

    @ValueType
    protected static final class Small extends DatRatio {
        private final long numerator;
        private final long denominator;

        private Small(long numerator, long denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof Small other)) {
                return false;
            }

            return numerator == other.numerator && denominator == other.denominator;
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), numerator, denominator);
        }

        @TruffleBoundary
        @Override public String toString() {
            return numerator + "/" + denominator;
        }
    }

    @ValueType
    protected static final class BigNumerator extends DatRatio {
        private final @NotNull BigInteger numerator;
        private final long denominator;

        private BigNumerator(@NotNull BigInteger numerator, long denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof BigNumerator other)) {
                return false;
            }

            return numerator.equals(other.numerator) && denominator == other.denominator;
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), numerator, denominator);
        }

        @TruffleBoundary
        @Override public String toString() {
            return numerator + "/" + denominator;
        }
    }

    @ValueType
    protected static final class BigDenominator extends DatRatio {
        private final long numerator;
        private final @NotNull BigInteger denominator;

        private BigDenominator(long numerator, @NotNull BigInteger denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof BigDenominator other)) {
                return false;
            }

            return numerator == other.numerator && denominator.equals(other.denominator);
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), numerator, denominator);
        }

        @TruffleBoundary
        @Override public String toString() {
            return numerator + "/" + denominator;
        }
    }

    @ValueType
    protected static final class Big extends DatRatio {
        private final @NotNull BigInteger numerator;
        private final @NotNull BigInteger denominator;

        private Big(@NotNull BigInteger numerator, @NotNull BigInteger denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof Big other)) {
                return false;
            }

            return numerator.equals(other.numerator) && denominator.equals(other.denominator);
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), numerator, denominator);
        }

        @TruffleBoundary
        @Override public String toString() {
            return numerator + "/" + denominator;
        }
    }

    public static abstract class GetPartNode extends DatNode {
        public abstract Object executeGeneric(DatRatio operand);

        public abstract long executeLong(DatRatio operand) throws UnexpectedResultException;
    }

    @GenerateUncached
    public static abstract class GetNumeratorNode extends GetPartNode {
        @Specialization
        protected long doSmall(Small operand) {
            return operand.numerator;
        }

        @Specialization
        protected BigInteger doBigNumerator(BigNumerator operand) {
            return operand.numerator;
        }

        @Specialization
        protected long doBigDenominator(BigDenominator operand) {
            return operand.numerator;
        }

        @Specialization
        protected BigInteger doBig(Big operand) {
            return operand.numerator;
        }
    }

    @GenerateUncached
    public static abstract class GetDenominatorNode extends GetPartNode {
        @Specialization
        protected long doSmall(Small operand) {
            return operand.denominator;
        }

        @Specialization
        protected long doBigNumerator(BigNumerator operand) {
            return operand.denominator;
        }

        @Specialization
        protected BigInteger doBigDenominator(BigDenominator operand) {
            return operand.denominator;
        }

        @Specialization
        protected BigInteger doBig(Big operand) {
            return operand.denominator;
        }
    }

    @GenerateUncached
    public static abstract class CreateNode extends DatNode {
        public abstract DatRatio execute(Object numerator, Object denominator);

        public abstract DatRatio execute(long numerator, Object denominator);

        public abstract DatRatio execute(Object numerator, long denominator);

        public abstract DatRatio execute(long numerator, long denominator);

        @Specialization
        protected Small doSmall(long numerator, long denominator) {
            return new Small(numerator, denominator);
        }

        @Specialization
        protected BigNumerator doBigNumerator(BigInteger numerator, long denominator) {
            return new BigNumerator(numerator, denominator);
        }

        @Specialization
        protected BigDenominator doBigDenominator(long numerator, BigInteger denominator)  {
            return new BigDenominator(numerator, denominator);
        }

        @Specialization
        protected Big doBig(BigInteger numerator, BigInteger denominator) {
            return new Big(numerator, denominator);
        }
    }
}
