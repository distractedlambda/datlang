package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;

@NodeField(name = "value", type = long.class)
public abstract class DatLongConstantNode extends DatExpressionNode {
    protected abstract long getValue();

    @Specialization
    protected long execute() {
        return getValue();
    }
}
