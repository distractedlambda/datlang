package org.datlang.language.nodes.pattern;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.strings.TruffleString;

@NodeField(name = "expectedString", type = TruffleString.class)
public abstract class DatStringEqualityPatternNode extends DatPatternNode {
    protected abstract TruffleString getExpectedString();

    @Specialization
    protected boolean matchString(TruffleString subject, @Cached TruffleString.EqualNode equalNode) {
        return equalNode.execute(getExpectedString(), subject, TruffleString.Encoding.UTF_8);
    }

    @Fallback
    protected boolean matchOther(Object subject) {
        return false;
    }
}
