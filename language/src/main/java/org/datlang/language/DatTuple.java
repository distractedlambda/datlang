package org.datlang.language;

import org.jetbrains.annotations.NotNull;

public abstract class DatTuple extends DatAggregate {
    public DatTuple(@NotNull DatTupleType type) {
        super(type);
    }

    public final @NotNull DatTupleType getTupleType() {
        return (DatTupleType) getType();
    }

    @FunctionalInterface
    public interface Factory {
        @NotNull DatTuple newTuple(@NotNull DatTupleType type);
    }
}
