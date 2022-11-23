package org.datlang.language;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.staticobject.StaticProperty;
import com.oracle.truffle.api.strings.TruffleString;
import org.jetbrains.annotations.NotNull;

public final class DatTupleType extends DatTaggedAggregateType {
    private final @NotNull DatTuple.Factory instanceFactory;

    @CompilationFinal(dimensions = 1)
    private final @NotNull ElementProperty @NotNull[] elementProperties;

    public DatTupleType(
        @NotNull TruffleString tag,
        @NotNull DatTuple.Factory instanceFactory,
        @NotNull ElementProperty @NotNull[] elementProperties
    ) {
        super(tag);
        this.elementProperties = elementProperties;
        this.instanceFactory = instanceFactory;
    }

    public @NotNull DatTuple newInstance() {
        return instanceFactory.newInstance(this);
    }

    public int getElementCount() {
        return elementProperties.length;
    }

    public @NotNull ElementProperty getElementProperty(int index) {
        return elementProperties[index];
    }

    @Override protected @NotNull CallTarget createStringBuildingFunction() {
        return null;
    }

    @Override protected @NotNull CallTarget createHashingFunction() {
        return null;
    }

    public static final class ElementProperty extends StaticProperty {
        private final @NotNull String id;

        public ElementProperty(int index) {
            this.id = Integer.toString(index);
        }

        @Override public @NotNull String getId() {
            return id;
        }
    }
}
