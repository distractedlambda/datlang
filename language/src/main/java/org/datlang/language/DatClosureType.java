package org.datlang.language;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.strings.TruffleString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class DatClosureType extends DatAggregateType {
    private final @NotNull CallTarget callTarget;

    @CompilationFinal(dimensions = 1)
    private final @NotNull TruffleString @NotNull[] captureIdentifiers;

    public DatClosureType(@NotNull CallTarget callTarget, @NotNull List<@NotNull TruffleString> captureIdentifiers) {
        this.callTarget = callTarget;
        this.captureIdentifiers = captureIdentifiers.toArray(TruffleString[]::new);
    }

    public @NotNull CallTarget getCallTarget() {
        return callTarget;
    }

    @Override protected @NotNull CallTarget createStringBuildingFunction() {
        return null;
    }

    @Override protected @NotNull CallTarget createHashingFunction() {
        return null;
    }
}
