package org.datlang.language;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.strings.TruffleString;
import org.jetbrains.annotations.NotNull;

public final class DatTupleType extends DatTaggedAggregateType {
    public DatTupleType(@NotNull TruffleString tag) {
        super(tag);
    }

    @Override protected @NotNull CallTarget createStringBuildingFunction() {
        return null;
    }

    @Override protected @NotNull CallTarget createHashingFunction() {
        return null;
    }
}
