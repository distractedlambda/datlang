package org.datlang.language;

import com.oracle.truffle.api.interop.TruffleObject;
import org.jetbrains.annotations.NotNull;

public abstract class DatAggregate implements TruffleObject {
    private final @NotNull DatAggregateType type;

    public DatAggregate(@NotNull DatAggregateType type) {
        this.type = type;
    }

    public final @NotNull DatAggregateType getType() {
        return type;
    }
}
