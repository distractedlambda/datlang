package org.datlang.language.runtime;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.CompilerDirectives.ValueType;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import org.datlang.language.nodes.DatNode;
import org.jetbrains.annotations.NotNull;

import static java.lang.Double.doubleToRawLongBits;
import static java.util.Objects.hash;

public sealed abstract class DatImaginaryNumber implements TruffleObject {
    @ValueType
    protected static final class LongMagnitude extends DatImaginaryNumber {
        private final long magnitude;

        private LongMagnitude(long magnitude) {
            this.magnitude = magnitude;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof LongMagnitude other)) {
                return false;
            }

            return magnitude == other.magnitude;
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), magnitude);
        }

        @TruffleBoundary
        @Override public String toString() {
            return magnitude + "i";
        }
    }

    @ValueType
    protected static final class DoubleMagnitude extends DatImaginaryNumber {
        private final double magnitude;

        private DoubleMagnitude(double magnitude) {
            this.magnitude = magnitude;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof DoubleMagnitude other)) {
                return false;
            }

            return doubleToRawLongBits(magnitude) == doubleToRawLongBits(other.magnitude);
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), magnitude);
        }

        @TruffleBoundary
        @Override public String toString() {
            return magnitude + "i";
        }
    }

    @ValueType
    protected static final class GenericMagnitude extends DatImaginaryNumber {
        private final @NotNull Object magnitude;

        private GenericMagnitude(@NotNull Object magnitude) {
            this.magnitude = magnitude;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof GenericMagnitude other)) {
                return false;
            }

            return magnitude.equals(other.magnitude);
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), magnitude);
        }

        @TruffleBoundary
        @Override public String toString() {
            return magnitude + "i";
        }
    }

    @GenerateUncached
    public static abstract class GetMagnitudeNode extends DatNode {
        public abstract Object executeGeneric(DatImaginaryNumber operand);

        public abstract long executeLong(DatImaginaryNumber operand) throws UnexpectedResultException;

        public abstract double executeDouble(DatImaginaryNumber operand) throws UnexpectedResultException;

        @Specialization
        protected long doLongMagnitude(LongMagnitude operand) {
            return operand.magnitude;
        }

        @Specialization
        protected double doDoubleMagnitude(DoubleMagnitude operand) {
            return operand.magnitude;
        }

        @Specialization
        protected Object doGenericMagnitude(GenericMagnitude operand) {
            return operand.magnitude;
        }
    }

    @GenerateUncached
    public static abstract class CreateNode extends DatNode {
        public abstract DatImaginaryNumber execute(long magnitude);

        public abstract DatImaginaryNumber execute(double magnitude);

        public abstract DatImaginaryNumber execute(Object magnitude);

        @Specialization
        protected LongMagnitude doLongMagnitude(long magnitude) {
            return new LongMagnitude(magnitude);
        }

        @Specialization
        protected DoubleMagnitude doDoubleMagnitude(double magnitude) {
            return new DoubleMagnitude(magnitude);
        }

        @Fallback
        protected GenericMagnitude doGenericMagnitude(Object magnitude) {
            return new GenericMagnitude(magnitude);
        }
    }
}
