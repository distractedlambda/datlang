package org.datlang.language.runtime;

import com.oracle.truffle.api.interop.TruffleObject;
import org.jetbrains.annotations.NotNull;

public abstract class DatClosure implements TruffleObject {
    private final @NotNull DatClosureType type;

    public DatClosure(@NotNull DatClosureType type) {
        this.type = type;
    }

    public final @NotNull DatClosureType getType() {
        return type;
    }

    public interface Factory {
        @NotNull DatClosure newInstance(@NotNull DatClosureType type);
    }
}
