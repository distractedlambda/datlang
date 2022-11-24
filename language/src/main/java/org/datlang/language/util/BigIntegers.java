package org.datlang.language.util;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import org.datlang.language.DatLanguage;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public final class BigIntegers {
    private BigIntegers() {}

    @TruffleBoundary
    public static double toDouble(@NotNull BigInteger value) {
        return value.doubleValue();
    }

    @TruffleBoundary
    public static @NotNull Object negate(@NotNull BigInteger value) {
        return normalize(value.negate());
    }

    @TruffleBoundary
    public static @NotNull Object divide(@NotNull BigInteger lhs, @NotNull BigInteger rhs) {
        return normalize(lhs.divide(rhs));
    }

    @TruffleBoundary
    public static @NotNull Object divide(@NotNull BigInteger lhs, long rhs) {
        return normalize(lhs.divide(BigInteger.valueOf(rhs)));
    }

    @TruffleBoundary
    public static @NotNull Object normalize(@NotNull BigInteger value) {
        try {
            return value.longValueExact();
        }
        catch (ArithmeticException exception) {
            return DatLanguage.get(null).getInternedBigInteger(value);
        }
    }
}
