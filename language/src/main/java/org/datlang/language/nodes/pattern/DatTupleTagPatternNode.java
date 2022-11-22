package org.datlang.language.nodes.pattern;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeField;
import org.datlang.language.DatLoneTag;

@NodeField(name = "expectedTag", type = DatLoneTag.class)
public abstract class DatTupleTagPatternNode extends DatPatternNode {
    protected abstract DatLoneTag getExpectedTag();

    @Fallback
    protected boolean matchOther(Object subject) {
        return false;
    }
}
