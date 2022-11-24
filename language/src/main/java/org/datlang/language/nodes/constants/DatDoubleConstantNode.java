package org.datlang.language.nodes.constants;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.nodes.expressions.DatExpressionNode;

@NodeField(name = "value", type = double.class)
public abstract class DatDoubleConstantNode extends DatExpressionNode {
    protected abstract double getValue();

    @Specialization
    protected double execute() {
        return getValue();
    }
}
