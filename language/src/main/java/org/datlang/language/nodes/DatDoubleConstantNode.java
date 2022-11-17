package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import org.jetbrains.annotations.NotNull;

@NodeField(name = "value", type = double.class)
public abstract class DatDoubleConstantNode extends DatExpressionNode {
    protected abstract double getValue();

    @Specialization
    protected double execute() {
        return getValue();
    }

    @Override public @NotNull DatExpressionNode cloneUninitialized() {
        return DatDoubleConstantNodeFactory.create(getValue());
    }
}
