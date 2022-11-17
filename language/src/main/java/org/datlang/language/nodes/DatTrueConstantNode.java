package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.Specialization;
import org.jetbrains.annotations.NotNull;

public abstract class DatTrueConstantNode extends DatExpressionNode {
    @Specialization
    protected boolean execute() {
        return true;
    }

    @Override public @NotNull DatExpressionNode cloneUninitialized() {
        return DatTrueConstantNodeFactory.create();
    }
}
