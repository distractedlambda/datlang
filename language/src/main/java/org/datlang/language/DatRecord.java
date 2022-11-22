package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.ValueType;
import com.oracle.truffle.api.interop.TruffleObject;
import org.jetbrains.annotations.NotNull;

@ValueType
public final class DatRecord implements TruffleObject {
    private final @NotNull DatRecordType type;
    private final @NotNull Object storage;

    public DatRecord(@NotNull DatRecordType type, @NotNull Object storage) {
        this.type = type;
        this.storage = storage;
    }

    public @NotNull DatRecordType getType() {
        return type;
    }

    public @NotNull Object getStorage() {
        return storage;
    }
}
