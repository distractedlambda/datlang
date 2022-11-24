package org.datlang.language.runtime;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.CompilerDirectives.ValueType;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import org.datlang.language.nodes.DatNode;
import org.jetbrains.annotations.NotNull;

import static java.lang.Double.doubleToRawLongBits;
import static java.util.Objects.hash;

public sealed abstract class DatComplexNumber implements TruffleObject {
    @ValueType
    protected static final class LongLong extends DatComplexNumber {
        private final long realPart;
        private final long imaginaryPart;

        private LongLong(long realPart, long imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof LongLong other)) {
                return false;
            }

            return realPart == other.realPart && imaginaryPart == other.imaginaryPart;
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), realPart, imaginaryPart);
        }

        @TruffleBoundary
        @Override public String toString() {
            return realPart + (imaginaryPart < 0 ? "" : "+") + imaginaryPart + "i";
        }
    }

    @ValueType
    protected static final class LongDouble extends DatComplexNumber {
        private final long realPart;
        private final double imaginaryPart;

        private LongDouble(long realPart, double imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof LongDouble other)) {
                return false;
            }

            return realPart == other.realPart
                && doubleToRawLongBits(imaginaryPart) == doubleToRawLongBits(other.imaginaryPart);
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), realPart, doubleToRawLongBits(imaginaryPart));
        }

        @TruffleBoundary
        @Override public String toString() {
            return realPart + (imaginaryPart < 0 ? "" : "+") + imaginaryPart + "i";
        }
    }

    @ValueType
    protected static final class LongGeneric extends DatComplexNumber {
        private final long realPart;
        private final @NotNull Object imaginaryPart;

        private LongGeneric(long realPart, @NotNull Object imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof LongGeneric other)) {
                return false;
            }

            return realPart == other.realPart && imaginaryPart.equals(other.imaginaryPart);
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), realPart, imaginaryPart);
        }

        @TruffleBoundary
        @Override public String toString() {
            // FIXME: detect sign of rhs
            return realPart + "+" + imaginaryPart + "i";
        }
    }

    @ValueType
    protected static final class DoubleLong extends DatComplexNumber {
        private final double realPart;
        private final long imaginaryPart;

        private DoubleLong(double realPart, long imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof DoubleLong other)) {
                return false;
            }

            return doubleToRawLongBits(realPart) == doubleToRawLongBits(other.realPart)
                && imaginaryPart == other.imaginaryPart;
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), doubleToRawLongBits(realPart), imaginaryPart);
        }

        @TruffleBoundary
        @Override public String toString() {
            return realPart + (imaginaryPart < 0 ? "" : "+") + imaginaryPart + "i";
        }
    }

    @ValueType
    protected static final class DoubleDouble extends DatComplexNumber {
        private final double realPart;
        private final double imaginaryPart;

        private DoubleDouble(double realPart, double imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof DoubleDouble other)) {
                return false;
            }

            return doubleToRawLongBits(realPart) == doubleToRawLongBits(other.realPart)
                && doubleToRawLongBits(imaginaryPart) == doubleToRawLongBits(other.imaginaryPart);
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), doubleToRawLongBits(realPart), doubleToRawLongBits(imaginaryPart));
        }

        @TruffleBoundary
        @Override public String toString() {
            return realPart + (imaginaryPart < 0 ? "" : "+") + imaginaryPart + "i";
        }
    }

    @ValueType
    protected static final class DoubleGeneric extends DatComplexNumber {
        private final double realPart;
        private final @NotNull Object imaginaryPart;

        private DoubleGeneric(double realPart, @NotNull Object imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof DoubleGeneric other)) {
                return false;
            }

            return doubleToRawLongBits(realPart) == doubleToRawLongBits(other.realPart)
                && imaginaryPart.equals(other.imaginaryPart);
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), doubleToRawLongBits(realPart), imaginaryPart);
        }

        @TruffleBoundary
        @Override public String toString() {
            // FIXME: detect sign of rhs
            return realPart + "+" + imaginaryPart + "i";
        }
    }

    @ValueType
    protected static final class GenericLong extends DatComplexNumber {
        private final @NotNull Object realPart;
        private final long imaginaryPart;

        private GenericLong(@NotNull Object realPart, long imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof GenericLong other)) {
                return false;
            }

            return realPart.equals(other.realPart) && imaginaryPart == other.imaginaryPart;
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), realPart, imaginaryPart);
        }

        @TruffleBoundary
        @Override public String toString() {
            return realPart + (imaginaryPart < 0 ? "" : "+") + imaginaryPart + "i";
        }
    }

    @ValueType
    protected static final class GenericDouble extends DatComplexNumber {
        private final @NotNull Object realPart;
        private final double imaginaryPart;

        private GenericDouble(@NotNull Object realPart, double imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof GenericDouble other)) {
                return false;
            }

            return realPart.equals(other.realPart)
                && doubleToRawLongBits(imaginaryPart) == doubleToRawLongBits(other.imaginaryPart);
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), realPart, doubleToRawLongBits(imaginaryPart));
        }

        @TruffleBoundary
        @Override public String toString() {
            return realPart + (imaginaryPart < 0 ? "" : "+") + imaginaryPart + "i";
        }
    }

    @ValueType
    protected static final class GenericGeneric extends DatComplexNumber {
        private final @NotNull Object realPart;
        private final @NotNull Object imaginaryPart;

        private GenericGeneric(@NotNull Object realPart, @NotNull Object imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof GenericGeneric other)) {
                return false;
            }

            return realPart.equals(other.realPart) && imaginaryPart.equals(other.imaginaryPart);
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), realPart, imaginaryPart);
        }

        @TruffleBoundary
        @Override public String toString() {
            // FIXME: detect sign of rhs
            return realPart + "+" + imaginaryPart + "i";
        }
    }

    public static abstract class GetPartNode extends DatNode {
        public abstract Object executeGeneric(DatComplexNumber operand);

        public abstract long executeLong(DatComplexNumber operand) throws UnexpectedResultException;

        public abstract double executeDouble(DatComplexNumber operand) throws UnexpectedResultException;
    }

    @GenerateUncached
    public static abstract class GetRealPartNode extends GetPartNode {
        @Specialization
        protected long doLongLong(LongLong operand) {
            return operand.realPart;
        }

        @Specialization
        protected long doLongDouble(LongDouble operand) {
            return operand.realPart;
        }

        @Specialization
        protected long doLongGeneric(LongGeneric operand) {
            return operand.realPart;
        }

        @Specialization
        protected double doDoubleLong(DoubleLong operand) {
            return operand.realPart;
        }

        @Specialization
        protected double doDoubleDouble(DoubleDouble operand) {
            return operand.realPart;
        }

        @Specialization
        protected double doDoubleGeneric(DoubleGeneric operand) {
            return operand.realPart;
        }

        @Specialization
        protected Object doGenericLong(GenericLong operand) {
            return operand.realPart;
        }

        @Specialization
        protected Object doGenericDouble(GenericDouble operand) {
            return operand.realPart;
        }

        @Specialization
        protected Object doGenericGeneric(GenericGeneric operand) {
            return operand.realPart;
        }
    }

    @GenerateUncached
    public static abstract class GetImaginaryPartNode extends GetPartNode {
        @Specialization
        protected long doLongLong(LongLong operand) {
            return operand.imaginaryPart;
        }

        @Specialization
        protected double doLongDouble(LongDouble operand) {
            return operand.imaginaryPart;
        }

        @Specialization
        protected Object doLongGeneric(LongGeneric operand) {
            return operand.imaginaryPart;
        }

        @Specialization
        protected long doDoubleLong(DoubleLong operand) {
            return operand.imaginaryPart;
        }

        @Specialization
        protected double doDoubleDouble(DoubleDouble operand) {
            return operand.imaginaryPart;
        }

        @Specialization
        protected Object doDoubleGeneric(DoubleGeneric operand) {
            return operand.imaginaryPart;
        }

        @Specialization
        protected long doGenericLong(GenericLong operand) {
            return operand.imaginaryPart;
        }

        @Specialization
        protected double doGenericDouble(GenericDouble operand) {
            return operand.imaginaryPart;
        }

        @Specialization
        protected Object doGenericGeneric(GenericGeneric operand) {
            return operand.imaginaryPart;
        }
    }

    @GenerateUncached
    public static abstract class CreateNode extends DatNode {
        public abstract DatComplexNumber execute(Object realPart, Object imaginaryPart);

        public abstract DatComplexNumber execute(long realPart, Object imaginaryPart);

        public abstract DatComplexNumber execute(double realPart, Object imaginaryPart);

        public abstract DatComplexNumber execute(Object realPart, long imaginaryPart);

        public abstract DatComplexNumber execute(long realPart, long imaginaryPart);

        public abstract DatComplexNumber execute(double realPart, long imaginaryPart);

        public abstract DatComplexNumber execute(Object realPart, double imaginaryPart);

        public abstract DatComplexNumber execute(long realPart, double imaginaryPart);

        public abstract DatComplexNumber execute(double realPart, double imaginaryPart);

        @Specialization
        protected LongLong doLongLong(long realPart, long imaginaryPart) {
            return new LongLong(realPart, imaginaryPart);
        }

        @Specialization
        protected LongDouble doLongDouble(long realPart, double imaginaryPart) {
            return new LongDouble(realPart, imaginaryPart);
        }

        @Specialization(guards = "isTrulyGeneric(imaginaryPart)")
        protected LongGeneric doLongGeneric(long realPart, Object imaginaryPart) {
            return new LongGeneric(realPart, imaginaryPart);
        }

        @Specialization
        protected DoubleLong doDoubleLong(double realPart, long imaginaryPart) {
            return new DoubleLong(realPart, imaginaryPart);
        }

        @Specialization
        protected DoubleDouble doDoubleDouble(double realPart, double imaginaryPart) {
            return new DoubleDouble(realPart, imaginaryPart);
        }

        @Specialization(guards = "isTrulyGeneric(imaginaryPart)")
        protected DoubleGeneric doDoubleGeneric(double realPart, Object imaginaryPart) {
            return new DoubleGeneric(realPart, imaginaryPart);
        }

        @Specialization(guards = "isTrulyGeneric(realPart)")
        protected GenericLong doGenericLong(Object realPart, long imaginaryPart) {
            return new GenericLong(realPart, imaginaryPart);
        }

        @Specialization(guards = "isTrulyGeneric(realPart)")
        protected GenericDouble doGenericDouble(Object realPart, double imaginaryPart) {
            return new GenericDouble(realPart, imaginaryPart);
        }

        @Specialization(guards = {"isTrulyGeneric(realPart)", "isTrulyGeneric(imaginaryPart)"})
        protected GenericGeneric doGenericGeneric(Object realPart, Object imaginaryPart) {
            return new GenericGeneric(realPart, imaginaryPart);
        }

        protected static boolean isTrulyGeneric(Object part) {
            return !(part instanceof Long || part instanceof Double);
        }
    }
}
