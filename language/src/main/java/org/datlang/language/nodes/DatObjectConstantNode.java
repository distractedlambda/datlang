package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;

@NodeField(name = "value", type = Object.class)
public abstract class DatObjectConstantNode extends DatExpressionNode {
    protected abstract Object getValue();

    @Specialization
    protected Object execute() {
        return getValue();
    }
}
