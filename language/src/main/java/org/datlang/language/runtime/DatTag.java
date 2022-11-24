package org.datlang.language.runtime;

import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.strings.TruffleString;
import org.jetbrains.annotations.NotNull;

public final class DatTag implements TruffleObject {
    private final @NotNull TruffleString name;
    private final @NotNull DatTagScope scope;

    public DatTag(@NotNull TruffleString name, @NotNull DatTagScope scope) {
        this.name = name;
        this.scope = scope;
    }

    public @NotNull TruffleString getName() {
        return name;
    }

    public @NotNull DatTagScope getScope() {
        return scope;
    }
}
