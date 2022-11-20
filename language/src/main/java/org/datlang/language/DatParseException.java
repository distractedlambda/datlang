package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.exception.AbstractTruffleException;
import com.oracle.truffle.api.nodes.EncapsulatingNodeReference;
import com.oracle.truffle.api.source.SourceSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DatParseException extends AbstractTruffleException {
    private final @NotNull SourceSection sourceSection;

    @TruffleBoundary
    public DatParseException(
        @NotNull SourceSection sourceSection,
        @Nullable String message,
        @Nullable Throwable cause
    ) {
        super(message, cause, UNLIMITED_STACK_TRACE, EncapsulatingNodeReference.getCurrent().get());
        this.sourceSection = sourceSection;
    }

    public @NotNull SourceSection getSourceSection() {
        return sourceSection;
    }
}
