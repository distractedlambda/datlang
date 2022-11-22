package org.datlang.language;

import com.oracle.truffle.api.strings.TruffleString;
import org.jetbrains.annotations.NotNull;

public abstract class DatTaggedAggregateType extends DatAggregateType {
    private final @NotNull TruffleString tag;

    protected DatTaggedAggregateType(@NotNull TruffleString tag) {
        this.tag = tag;
    }

    public final @NotNull TruffleString getTag() {
        return tag;
    }
}
