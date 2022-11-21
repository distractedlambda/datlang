package org.datlang.language.nodes.pattern;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.DatRecord;
import org.datlang.language.DatSymbol;

@NodeField(name = "expectedTag", type = DatSymbol.class)
public abstract class DatRecordTagPatternNode extends DatPatternNode {
    protected abstract DatSymbol getExpectedTag();

    @Specialization
    protected boolean matchRecord(DatRecord subject) {
        return subject.getRecordType().getTag() == getExpectedTag();
    }

    @Fallback
    protected boolean matchOther(Object subject) {
        return false;
    }
}
