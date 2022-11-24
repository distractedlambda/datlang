package org.datlang.language.runtime;

import com.oracle.truffle.api.strings.TruffleString;
import org.jetbrains.annotations.NotNull;

public interface DatTagScope {
    @NotNull DatTag getTag(@NotNull TruffleString name);
}
