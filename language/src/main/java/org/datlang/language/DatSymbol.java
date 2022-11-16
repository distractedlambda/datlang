package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.strings.TruffleString;
import org.jetbrains.annotations.NotNull;

public final class DatSymbol implements Comparable<DatSymbol> {
    private final @NotNull TruffleString name;

    public DatSymbol(@NotNull TruffleString name) {
        this.name = name;
    }

    public @NotNull TruffleString name() {
        return name;
    }

    @TruffleBoundary
    @Override public @NotNull String toString() {
        return name.toString();
    }

    @TruffleBoundary
    @Override public int compareTo(@NotNull DatSymbol other) {
        return name.compareBytesUncached(other.name, TruffleString.Encoding.UTF_8);
    }
}
