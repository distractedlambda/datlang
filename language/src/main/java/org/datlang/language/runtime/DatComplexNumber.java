package org.datlang.language.runtime;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.CompilerDirectives.ValueType;
import com.oracle.truffle.api.interop.TruffleObject;
import org.jetbrains.annotations.NotNull;

import static java.lang.Double.doubleToRawLongBits;
import static java.util.Objects.hash;

public sealed abstract class DatComplexNumber implements TruffleObject {
    @ValueType
    public static final class LongLong extends DatComplexNumber {
        private final long realPart;
        private final long imaginaryPart;

        public LongLong(long realPart, long imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        public long getRealPart() {
            return realPart;
        }

        public long getImaginaryPart() {
            return imaginaryPart;
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
    public static final class LongDouble extends DatComplexNumber {
        private final long realPart;
        private final double imaginaryPart;

        public LongDouble(long realPart, double imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        public long getRealPart() {
            return realPart;
        }

        public double getImaginaryPart() {
            return imaginaryPart;
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
    public static final class LongGeneric extends DatComplexNumber {
        private final long realPart;
        private final @NotNull Object imaginaryPart;

        public LongGeneric(long realPart, @NotNull Object imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        public long getRealPart() {
            return realPart;
        }

        public @NotNull Object getImaginaryPart() {
            return imaginaryPart;
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
    public static final class DoubleLong extends DatComplexNumber {
        private final double realPart;
        private final long imaginaryPart;

        public DoubleLong(double realPart, long imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        public double getRealPart() {
            return realPart;
        }

        public long getImaginaryPart() {
            return imaginaryPart;
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
    public static final class DoubleDouble extends DatComplexNumber {
        private final double realPart;
        private final double imaginaryPart;

        public DoubleDouble(double realPart, double imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        public double getRealPart() {
            return realPart;
        }

        public double getImaginaryPart() {
            return imaginaryPart;
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
    public static final class DoubleGeneric extends DatComplexNumber {
        private final double realPart;
        private final @NotNull Object imaginaryPart;

        public DoubleGeneric(double realPart, @NotNull Object imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        public double getRealPart() {
            return realPart;
        }

        public @NotNull Object getImaginaryPart() {
            return imaginaryPart;
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
    public static final class GenericLong extends DatComplexNumber {
        private final @NotNull Object realPart;
        private final long imaginaryPart;

        public GenericLong(@NotNull Object realPart, long imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        public @NotNull Object getRealPart() {
            return realPart;
        }

        public long getImaginaryPart() {
            return imaginaryPart;
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
    public static final class GenericDouble extends DatComplexNumber {
        private final @NotNull Object realPart;
        private final double imaginaryPart;

        public GenericDouble(@NotNull Object realPart, double imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        public @NotNull Object getRealPart() {
            return realPart;
        }

        public double getImaginaryPart() {
            return imaginaryPart;
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
    public static final class GenericGeneric extends DatComplexNumber {
        private final @NotNull Object realPart;
        private final @NotNull Object imaginaryPart;

        public GenericGeneric(@NotNull Object realPart, @NotNull Object imaginaryPart) {
            this.realPart = realPart;
            this.imaginaryPart = imaginaryPart;
        }

        public @NotNull Object getRealPart() {
            return realPart;
        }

        public @NotNull Object getImaginaryPart() {
            return imaginaryPart;
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

    @ValueType
    public static final class ZeroLong extends DatComplexNumber {
        private final long imaginaryPart;

        public ZeroLong(long imaginaryPart) {
            this.imaginaryPart = imaginaryPart;
        }

        public long getImaginaryPart() {
            return imaginaryPart;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof ZeroLong other)) {
                return false;
            }

            return imaginaryPart == other.imaginaryPart;
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), imaginaryPart);
        }

        @TruffleBoundary
        @Override public String toString() {
            return imaginaryPart + "i";
        }
    }

    @ValueType
    public static final class ZeroDouble extends DatComplexNumber {
        private final double imaginaryPart;

        public ZeroDouble(double imaginaryPart) {
            this.imaginaryPart = imaginaryPart;
        }

        public double getImaginaryPart() {
            return imaginaryPart;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof ZeroDouble other)) {
                return false;
            }

            return doubleToRawLongBits(imaginaryPart) == doubleToRawLongBits(other.imaginaryPart);
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), doubleToRawLongBits(imaginaryPart));
        }

        @TruffleBoundary
        @Override public String toString() {
            return imaginaryPart + "i";
        }
    }

    @ValueType
    public static final class ZeroGeneric extends DatComplexNumber {
        private final @NotNull Object imaginaryPart;

        public ZeroGeneric(@NotNull Object imaginaryPart) {
            this.imaginaryPart = imaginaryPart;
        }

        public @NotNull Object getImaginaryPart() {
            return imaginaryPart;
        }

        @TruffleBoundary
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof ZeroGeneric other)) {
                return false;
            }

            return imaginaryPart.equals(other.imaginaryPart);
        }

        @TruffleBoundary
        @Override public int hashCode() {
            return hash(getClass(), imaginaryPart);
        }

        @TruffleBoundary
        @Override public String toString() {
            return imaginaryPart + "i";
        }
    }
}
