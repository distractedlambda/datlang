package org.datlang.language.runtime;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.staticobject.StaticProperty;
import com.oracle.truffle.api.staticobject.StaticShape;
import org.datlang.language.DatLanguage;
import org.jetbrains.annotations.NotNull;

public final class DatTupleType extends DatTaggedAggregateType {
    private final @NotNull DatTuple.Factory instanceFactory;

    @CompilationFinal(dimensions = 1)
    private final @NotNull ElementProperty @NotNull[] elementProperties;

    public DatTupleType(
        @NotNull DatLanguage language,
        @NotNull DatTag tag,
        @NotNull Class<?> @NotNull[] elementTypes
    ) {
        super(tag);

        elementProperties = new ElementProperty[elementTypes.length];

        var shapeBuilder = StaticShape.newBuilder(language);

        for (var i = 0; i < elementProperties.length; i++) {
            elementProperties[i] = new ElementProperty(i, elementTypes[i]);
            shapeBuilder.property(elementProperties[i], elementTypes[i], true);
        }

        instanceFactory = shapeBuilder.build(DatTuple.class, DatTuple.Factory.class).getFactory();
    }

    @ExplodeLoop
    public @NotNull DatTuple newInstance(@NotNull Object @NotNull[] elements) {
        assert elements.length == elementProperties.length;

        var instance = instanceFactory.newInstance(this);

        for (var i = 0; i < elements.length; i++) {
            var element = elements[i];
            var property = elementProperties[i];

            if (element instanceof Boolean b) {
                property.setBoolean(instance, b);
            }
            else if (element instanceof Long l) {
                property.setLong(instance, l);
            }
            else if (element instanceof Double d) {
                property.setDouble(instance, d);
            }
            else {
                property.setObject(instance, element);
            }
        }

        return instance;
    }

    @ExplodeLoop
    public boolean isCompatible(@NotNull Object @NotNull[] elements) {
        assert elements.length == elementProperties.length;

        for (var i = 0; i < elements.length; i++) {
            var element = elements[i];
            var propertyType = elementProperties[i].getType();

            if (element instanceof Boolean) {
                if (propertyType != boolean.class) {
                    return false;
                }
            }
            else if (element instanceof Long) {
                if (propertyType != long.class) {
                    return false;
                }
            }
            else if (element instanceof Double) {
                if (propertyType != double.class) {
                    return false;
                }
            }
            else if (propertyType != Object.class) {
                return false;
            }
        }

        return true;
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
        private final @NotNull Class<?> type;

        private ElementProperty(int index, @NotNull Class<?> type) {
            this.id = Integer.toString(index);
            this.type = type;
        }

        @Override public @NotNull String getId() {
            return id;
        }

        public @NotNull Class<?> getType() {
            return type;
        }
    }
}
