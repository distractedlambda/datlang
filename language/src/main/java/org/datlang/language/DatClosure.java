package org.datlang.language;

import org.jetbrains.annotations.NotNull;

public abstract class DatClosure extends DatAggregate {
    public DatClosure(@NotNull DatClosureType type) {
        super(type);
    }

    public final @NotNull DatClosureType getClosureType() {
        return (DatClosureType) getType();
    }

    @FunctionalInterface
    public interface Factory {
        @NotNull DatClosure newClosure(@NotNull DatClosureType type);
    }
}
