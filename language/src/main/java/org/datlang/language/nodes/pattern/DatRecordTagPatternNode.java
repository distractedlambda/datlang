package org.datlang.language.nodes.pattern;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeField;
import org.datlang.language.runtime.DatLoneTag;

@NodeField(name = "expectedTag", type = DatLoneTag.class)
public abstract class DatRecordTagPatternNode extends DatPatternNode {
    protected abstract DatLoneTag getExpectedTag();

    @Fallback
    protected boolean matchOther(Object subject) {
        return false;
    }
}
