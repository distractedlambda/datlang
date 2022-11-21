package org.datlang.language.nodes.pattern;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.DatSymbol;
import org.datlang.language.DatTuple;

@NodeField(name = "expectedTag", type = DatSymbol.class)
public abstract class DatTupleTagPatternNode extends DatPatternNode {
    protected abstract DatSymbol getExpectedTag();

    @Specialization
    protected boolean matchTuple(DatTuple subject) {
        return subject.getTupleType().getTag() == getExpectedTag();
    }

    @Fallback
    protected boolean matchOther(Object subject) {
        return false;
    }
}
