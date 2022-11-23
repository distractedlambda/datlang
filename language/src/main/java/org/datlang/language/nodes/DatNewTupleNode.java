package org.datlang.language.nodes;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.strings.TruffleString;
import org.datlang.language.DatTuple;
import org.datlang.language.DatTupleType;

@NodeField(name = "tag", type = TruffleString.class)
public abstract class DatNewTupleNode extends DatNode {
    protected abstract TruffleString getTag();

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
        throw new UnsupportedOperationException("TODO");
    }
}
