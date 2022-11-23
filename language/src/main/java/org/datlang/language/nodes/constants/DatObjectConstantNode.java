package org.datlang.language.nodes.constants;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.nodes.DatExpressionNode;

@NodeField(name = "value", type = Object.class)
public abstract class DatObjectConstantNode extends DatExpressionNode {
    protected abstract Object getValue();

    @Specialization
    protected Object execute() {
        return getValue();
    }
}
