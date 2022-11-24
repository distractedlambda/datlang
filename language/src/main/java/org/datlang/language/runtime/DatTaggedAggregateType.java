package org.datlang.language.runtime;

import org.jetbrains.annotations.NotNull;

public abstract class DatTaggedAggregateType extends DatAggregateType {
    private final @NotNull DatTag tag;

    protected DatTaggedAggregateType(@NotNull DatTag tag) {
        this.tag = tag;
    }

    public final @NotNull DatTag getTag() {
        return tag;
    }
}
