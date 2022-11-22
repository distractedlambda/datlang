package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.ValueType;
import com.oracle.truffle.api.interop.TruffleObject;
import org.jetbrains.annotations.NotNull;

@ValueType
public final class DatTuple implements TruffleObject {
    private final @NotNull DatTupleType type;
    private final @NotNull Object storage;

    public DatTuple(@NotNull DatTupleType type, @NotNull Object storage) {
        this.type = type;
        this.storage = storage;
    }

    public @NotNull DatTupleType getType() {
        return type;
    }

    public @NotNull Object getStorage() {
        return storage;
    }
}
