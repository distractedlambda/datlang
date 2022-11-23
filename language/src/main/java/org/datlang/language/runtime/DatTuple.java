package org.datlang.language.runtime;

import com.oracle.truffle.api.interop.TruffleObject;
import org.jetbrains.annotations.NotNull;

public abstract class DatTuple implements TruffleObject {
    private final @NotNull DatTupleType type;

    public DatTuple(@NotNull DatTupleType type) {
        this.type = type;
    }

    public final @NotNull DatTupleType getType() {
        return type;
    }

    public interface Factory {
        @NotNull DatTuple newInstance(@NotNull DatTupleType type);
    }
}
