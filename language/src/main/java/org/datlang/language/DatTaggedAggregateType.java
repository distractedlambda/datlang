package org.datlang.language;

import org.jetbrains.annotations.NotNull;

public abstract class DatTaggedAggregateType extends DatAggregateType {
    private final @NotNull DatSymbol tag;

    protected DatTaggedAggregateType(@NotNull DatSymbol tag) {
        this.tag = tag;
    }

    public final @NotNull DatSymbol getTag() {
        return tag;
    }
}
