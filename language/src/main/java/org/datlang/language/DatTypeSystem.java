package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.strings.TruffleString;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@TypeSystem({
    boolean.class,
    double.class,
    long.class,
    BigInteger.class,
    DatRecord.class,
    DatSmallRational.class,
    DatSymbol.class,
    DatTuple.class,
    TruffleString.class,
})
public abstract class DatTypeSystem {
    @ImplicitCast
    public static double castLongToDouble(long value) {
        return (double)value;
    }

    @ImplicitCast
    public static double castSmallRationalToDouble(@NotNull DatSmallRational value) {
        return (double)value.numerator() / value.denominator();
    }

    @ImplicitCast
    @TruffleBoundary
    public static double castBigIntegerToDouble(@NotNull BigInteger value) {
        return value.doubleValue();
    }

    @ImplicitCast
    public static @NotNull DatSmallRational castLongToSmallRational(long value) {
        return new DatSmallRational(value, 1);
    }

    @ImplicitCast
    @TruffleBoundary
    public static @NotNull BigInteger castLongToBigInteger(long value) {
        return BigInteger.valueOf(value);
    }
}
