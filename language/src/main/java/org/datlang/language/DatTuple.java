package org.datlang.language;

import org.jetbrains.annotations.NotNull;

public abstract class DatTuple {
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
