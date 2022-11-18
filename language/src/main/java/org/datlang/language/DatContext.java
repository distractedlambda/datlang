package org.datlang.language;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;
import org.jetbrains.annotations.Nullable;

public final class DatContext {
    private static final TruffleLanguage.ContextReference<DatContext> REFERENCE =
        TruffleLanguage.ContextReference.create(DatLanguage.class);

    public static DatContext get(@Nullable Node node) {
        return REFERENCE.get(node);
    }
}
