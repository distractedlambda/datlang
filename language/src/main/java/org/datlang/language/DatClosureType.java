package org.datlang.language;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.staticobject.StaticProperty;
import org.jetbrains.annotations.NotNull;

public final class DatClosureType extends DatAggregateType {
    private final @NotNull CallTarget callTarget;

    private final @NotNull DatSymbol @NotNull[] captureIdentifiers;
    private final @NotNull StaticProperty @NotNull[] captureProperties;
    private final @NotNull DatClosure.Factory instanceFactory;

    public DatClosureType(
        @NotNull CallTarget callTarget,
        @NotNull DatSymbol @NotNull[] captureIdentifiers,
        @NotNull StaticProperty @NotNull[] captureProperties,
        @NotNull DatClosure.Factory instanceFactory
    ) {
        this.callTarget = callTarget;
        this.captureIdentifiers = captureIdentifiers;
        this.captureProperties = captureProperties;
        this.instanceFactory = instanceFactory;
    }

    public @NotNull CallTarget getCallTarget() {
        return callTarget;
    }

    public @NotNull DatClosure newInstance() {
        return instanceFactory.newClosure(this);
    }

    @Override protected @NotNull CallTarget createStringBuildingFunction() {
        return null;
    }

    @Override protected @NotNull CallTarget createHashingFunction() {
        return null;
    }
}
