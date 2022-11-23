package org.datlang.language.nodes.constants;

import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.nodes.DatExpressionNode;

public abstract class DatTrueConstantNode extends DatExpressionNode {
    @Specialization
    protected boolean execute() {
        return true;
    }
}
