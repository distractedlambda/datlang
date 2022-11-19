package org.datlang.language;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.staticobject.StaticProperty;
import org.jetbrains.annotations.NotNull;

public final class DatTupleType extends DatAggregateType {
    @CompilationFinal(dimensions = 1) private final @NotNull StaticProperty @NotNull[] properties;
    private final @NotNull DatTuple.Factory instanceFactory;

    public DatTupleType(
        @NotNull DatSymbol tag,
        @NotNull StaticProperty @NotNull[] properties,
        @NotNull DatTuple.Factory instanceFactory
    ) {
        super(tag);
        this.properties = properties;
        this.instanceFactory = instanceFactory;
    }

    public int length() {
        return properties.length;
    }

    public @NotNull StaticProperty property(int index) {
        return properties[index];
    }

    public @NotNull DatTuple newInstance() {
        return instanceFactory.newTuple(this);
    }

    @Override protected @NotNull CallTarget createStringBuildingFunction() {
        return null;
    }

    @Override protected @NotNull CallTarget createHashingFunction() {
        return null;
    }
}
