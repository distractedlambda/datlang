package org.datlang.language;

import org.jetbrains.annotations.NotNull;

public abstract class DatRecord extends DatAggregate {
    public DatRecord(@NotNull DatRecordType type) {
        super(type);
    }

    public final @NotNull DatRecordType getRecordType() {
        return (DatRecordType) getType();
    }

    @FunctionalInterface
    public interface Factory {
        @NotNull DatRecord newRecord(@NotNull DatRecordType type);
    }
}
