package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.utilities.TriState;
import org.jetbrains.annotations.NotNull;

@ExportLibrary(InteropLibrary.class)
public final class DatSymbol implements TruffleObject, Comparable<DatSymbol> {
    private final @NotNull TruffleString name;

    public DatSymbol(@NotNull TruffleString name) {
        this.name = name;
    }

    public @NotNull TruffleString name() {
        return name;
    }

    @ExportMessage
    boolean hasLanguage() {
        return true;
    }

    @ExportMessage
    Class<DatLanguage> getLanguage() {
        return DatLanguage.class;
    }

    @ExportMessage
    TruffleString toDisplayString(boolean allowSideEffects) {
        return name;
    }

    @ExportMessage
    TriState isIdenticalOrUndefined(Object other) {
        return TriState.valueOf(this == other);
    }

    @ExportMessage
    int identityHashCode() {
        return System.identityHashCode(this);
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
