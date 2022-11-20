package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.Specialization;

public abstract class DatTrueConstantNode extends DatExpressionNode {
    @Specialization
    protected boolean execute() {
        return true;
    }
}
