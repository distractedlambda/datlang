package org.datlang.language.nodes.aggregates;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.nodes.DatNode;
import org.datlang.language.runtime.DatTag;
import org.datlang.language.runtime.DatTuple;
import org.datlang.language.runtime.DatTupleType;
import org.jetbrains.annotations.NotNull;

public abstract class DatNewTupleNode extends DatNode {
    private final @NotNull DatTag tag;

    protected DatNewTupleNode(@NotNull DatTag tag) {
        this.tag = tag;
    }

    public abstract DatTuple execute(Object[] elements);

    @Specialization(guards = "chosenType.isCompatible(elements)")
    protected DatTuple doCached(Object[] elements, @Cached("chooseType(elements)") DatTupleType chosenType) {
        return chosenType.newInstance(elements);
    }

    @TruffleBoundary
    @Specialization(replaces = "doCached")
    protected DatTuple doUncached(Object[] elements) {
        return chooseType(elements).newInstance(elements);
    }

    @TruffleBoundary
    protected DatTupleType chooseType(Object[] elements) {
        var elementTypes = new Class<?>[elements.length];

        for (var i = 0; i < elements.length; i++) {
            var objectType = elements[i].getClass();
            if (objectType == Boolean.class) {
                elementTypes[i] = boolean.class;
            }
            else if (objectType == Long.class) {
                elementTypes[i] = long.class;
            }
            else if (objectType == Double.class) {
                elementTypes[i] = double.class;
            }
            else {
                elementTypes[i] = Object.class;
            }
        }

        return getLanguage().getTupleType(tag, elementTypes);
    }
}
