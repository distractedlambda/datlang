package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import org.jetbrains.annotations.NotNull;

@NodeField(name = "value", type = Object.class)
public abstract class DatObjectConstantNode extends DatExpressionNode {
    protected abstract Object getValue();

    @Specialization
    protected Object execute() {
        return getValue();
    }

    @Override public @NotNull DatExpressionNode cloneUninitialized() {
        return DatObjectConstantNodeFactory.create(getValue());
    }
}
