package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.ValueType;

@ValueType
public final class DatSmallRational {
    private final long numerator;
    private final long denominator;

    public DatSmallRational(long numerator, long denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public long numerator() {
        return numerator;
    }

    public long denominator() {
        return denominator;
    }

    @Override public boolean equals(Object obj) {
        if (!(obj instanceof DatSmallRational other)) {
            return false;
        }

        return numerator == other.numerator && denominator == other.denominator;
    }

    @Override public int hashCode() {
        return Long.hashCode(numerator) * 31 + Long.hashCode(denominator);
    }

    @Override public String toString() {
        return numerator + "/" + denominator;
    }
}
