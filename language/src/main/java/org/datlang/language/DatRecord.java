package org.datlang.language;

import com.oracle.truffle.api.interop.TruffleObject;
import org.jetbrains.annotations.NotNull;

public abstract class DatRecord implements TruffleObject {
    private final @NotNull DatRecordType type;

    public DatRecord(@NotNull DatRecordType type) {
        this.type = type;
    }

    public final @NotNull DatRecordType getType() {
        return type;
    }

    public interface Factory {
        @NotNull DatRecord newInstance(@NotNull DatRecordType type);
    }
}
