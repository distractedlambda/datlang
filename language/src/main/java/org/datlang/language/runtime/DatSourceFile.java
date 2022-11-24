package org.datlang.language.runtime;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.strings.TruffleString;
import org.datlang.language.util.ConcurrentWeakCacheMap;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class DatSourceFile implements DatTagScope {
    private final @NotNull Source source;

    private final ConcurrentWeakCacheMap<TruffleString, DatTag> tags = new ConcurrentWeakCacheMap<>();
    private final Function<TruffleString, DatTag> tagFactory = key -> new DatTag(key, this);

    public DatSourceFile(@NotNull Source source) {
        this.source = source;
    }

    public @NotNull Source getSource() {
        return source;
    }

    @TruffleBoundary
    @Override public @NotNull DatTag getTag(@NotNull TruffleString name) {
        return tags.getOrCompute(name, tagFactory);
    }
}
