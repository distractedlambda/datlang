package org.datlang.language;

import org.jetbrains.annotations.NotNull;

public abstract class DatRecord {
    private final @NotNull DatRecordType type;

    public DatRecord(@NotNull DatRecordType type) {
        this.type = type;
    }

    public @NotNull DatRecordType type() {
        return type;
    }

    @FunctionalInterface
    public interface Factory {
        @NotNull DatRecord newRecord(@NotNull DatRecordType type);
    }
}
