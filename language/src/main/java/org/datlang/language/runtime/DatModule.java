package org.datlang.language.runtime;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.strings.TruffleString;
import org.datlang.language.util.ConcurrentWeakCacheMap;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class DatModule implements DatTagScope {
    private final ConcurrentWeakCacheMap<TruffleString, DatTag> tags = new ConcurrentWeakCacheMap<>();
    private final Function<TruffleString, DatTag> tagFactory = key -> new DatTag(key, this);

    @TruffleBoundary
    @Override public @NotNull DatTag getTag(@NotNull TruffleString name) {
        return tags.getOrCompute(name, tagFactory);
    }
}
