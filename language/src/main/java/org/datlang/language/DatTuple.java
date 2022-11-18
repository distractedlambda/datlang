package org.datlang.language;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import org.jetbrains.annotations.NotNull;

@ExportLibrary(InteropLibrary.class)
public abstract class DatTuple implements TruffleObject {
    private final @NotNull DatTupleType type;

    public DatTuple(@NotNull DatTupleType type) {
        this.type = type;
    }

    public @NotNull DatTupleType type() {
        return type;
    }

    @FunctionalInterface
    public interface Factory {
        @NotNull DatTuple newTuple(@NotNull DatTupleType type);
    }
}
