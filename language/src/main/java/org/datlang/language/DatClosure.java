package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.ValueType;
import com.oracle.truffle.api.interop.TruffleObject;
import org.jetbrains.annotations.NotNull;

@ValueType
public final class DatClosure implements TruffleObject {
    private final @NotNull DatClosureType type;
    private final @NotNull Object storage;

    public DatClosure(@NotNull DatClosureType type, @NotNull Object storage) {
        this.type = type;
        this.storage = storage;
    }

    public @NotNull DatClosureType getType() {
        return type;
    }

    public @NotNull Object getStorage() {
        return storage;
    }
}
