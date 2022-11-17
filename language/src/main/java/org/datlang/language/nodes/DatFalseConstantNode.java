package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.Specialization;
import org.jetbrains.annotations.NotNull;

public abstract class DatFalseConstantNode extends DatExpressionNode {
    @Specialization
    protected boolean execute() {
        return false;
    }

    @Override public @NotNull DatExpressionNode cloneUninitialized() {
        return DatFalseConstantNodeFactory.create();
    }
}
