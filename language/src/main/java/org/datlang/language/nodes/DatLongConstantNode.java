package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import org.jetbrains.annotations.NotNull;

@NodeField(name = "value", type = long.class)
public abstract class DatLongConstantNode extends DatExpressionNode {
    protected abstract long getValue();

    @Override public @NotNull DatExpressionNode cloneUninitialized() {
        return DatLongConstantNodeFactory.create(getValue());
    }

    @Specialization
    protected long execute() {
        return getValue();
    }
}
