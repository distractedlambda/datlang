package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.CompilerDirectives.ValueType;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.utilities.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ValueType
@ExportLibrary(InteropLibrary.class)
public final class DatLoneTag implements TruffleObject, Comparable<DatLoneTag> {
    private final @NotNull TruffleString name;

    public DatLoneTag(@NotNull TruffleString name) {
        this.name = name;
    }

    public @NotNull TruffleString getName() {
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
        throw new UnsupportedOperationException("TODO");
    }

    @ExportMessage
    static abstract class IsIdenticalOrUndefined {
        @Specialization
        static TriState toLoneTag(DatLoneTag receiver, DatLoneTag other) {
            return TriState.valueOf(receiver.name == other.name);
        }

        @Specialization
        static TriState toOther(DatLoneTag receiver, Object other) {
            return TriState.UNDEFINED;
        }
    }

    @ExportMessage
    int identityHashCode(@Cached TruffleString.HashCodeNode hashCodeNode) {
        return hashCodeNode.execute(name, TruffleString.Encoding.UTF_8);
    }

    @TruffleBoundary
    @Override public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof DatLoneTag other)) {
            return false;
        }

        return name.equalsUncached(other.name, TruffleString.Encoding.UTF_8);
    }

    @TruffleBoundary
    @Override public int hashCode() {
        return name.hashCodeUncached(TruffleString.Encoding.UTF_8);
    }

    @TruffleBoundary
    @Override public @NotNull String toString() {
        return name.toJavaStringUncached();
    }

    @TruffleBoundary
    @Override public int compareTo(@NotNull DatLoneTag other) {
        return name.compareBytesUncached(other.name, TruffleString.Encoding.UTF_8);
    }
}
