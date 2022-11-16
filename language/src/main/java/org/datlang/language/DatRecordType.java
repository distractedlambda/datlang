package org.datlang.language;

import com.oracle.truffle.api.staticobject.StaticProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

import static com.oracle.truffle.api.CompilerAsserts.neverPartOfCompilation;

public final class DatRecordType {
    private final @NotNull DatSymbol tag;
    private final @NotNull @Unmodifiable Map<@NotNull DatSymbol, @NotNull StaticProperty> properties;
    private final @NotNull DatRecord.Factory instanceFactory;

    public DatRecordType(
        @NotNull DatSymbol tag,
        @NotNull @Unmodifiable Map<@NotNull DatSymbol, @NotNull StaticProperty> properties,
        @NotNull DatRecord.Factory instanceFactory
    ) {
        this.tag = tag;
        this.properties = properties;
        this.instanceFactory = instanceFactory;
    }

    public @NotNull DatSymbol tag() {
        return tag;
    }

    public @NotNull @Unmodifiable Map<@NotNull DatSymbol, @NotNull StaticProperty> properties() {
        neverPartOfCompilation();
        return properties;
    }

    public @NotNull DatRecord newInstance() {
        return instanceFactory.newRecord(this);
    }
}
