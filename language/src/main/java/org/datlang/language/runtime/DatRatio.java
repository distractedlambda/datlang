package org.datlang.language.runtime;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.CompilerDirectives.ValueType;
import com.oracle.truffle.api.interop.TruffleObject;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import static java.util.Objects.hash;

public sealed abstract class DatRatio implements TruffleObject {
    @ValueType
    public static final class Small extends DatRatio {
        private final long numerator;
        private final long denominator;

        public Small(long numerator, long denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public long getNumerator() {
            return numerator;
        }

        public long getDenominator() {
            return denominator;
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
    public static final class BigNumerator extends DatRatio {
        private final @NotNull BigInteger numerator;
        private final long denominator;

        public BigNumerator(@NotNull BigInteger numerator, long denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public @NotNull BigInteger getNumerator() {
            return numerator;
        }

        public long getDenominator() {
            return denominator;
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
    public static final class BigDenominator extends DatRatio {
        private final long numerator;
        private final @NotNull BigInteger denominator;

        public BigDenominator(long numerator, @NotNull BigInteger denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public long getNumerator() {
            return numerator;
        }

        public @NotNull BigInteger getDenominator() {
            return denominator;
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
    public static final class Big extends DatRatio {
        private final @NotNull BigInteger numerator;
        private final @NotNull BigInteger denominator;

        public Big(@NotNull BigInteger numerator, @NotNull BigInteger denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public @NotNull BigInteger getNumerator() {
            return numerator;
        }

        public @NotNull BigInteger getDenominator() {
            return denominator;
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
}
