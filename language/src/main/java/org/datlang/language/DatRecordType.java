package org.datlang.language;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.staticobject.StaticProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

import static com.oracle.truffle.api.CompilerAsserts.neverPartOfCompilation;

public final class DatRecordType extends DatAggregateType {
    private final @NotNull @Unmodifiable Map<@NotNull DatSymbol, @NotNull StaticProperty> properties;
    private final @NotNull DatRecord.Factory instanceFactory;

    public DatRecordType(
        @NotNull DatSymbol tag,
        @NotNull @Unmodifiable Map<@NotNull DatSymbol, @NotNull StaticProperty> properties,
        @NotNull DatRecord.Factory instanceFactory
    ) {
        super(tag);
        this.properties = properties;
        this.instanceFactory = instanceFactory;
    }

    public @NotNull @Unmodifiable Map<@NotNull DatSymbol, @NotNull StaticProperty> properties() {
        neverPartOfCompilation();
        return properties;
    }

    public @NotNull DatRecord newInstance() {
        return instanceFactory.newRecord(this);
    }

    @Override protected @NotNull CallTarget createStringBuildingFunction() {
        return null;
    }

    @Override protected @NotNull CallTarget createHashingFunction() {
        return null;
    }
}
