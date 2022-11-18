package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import org.jetbrains.annotations.NotNull;

@NodeChild(value = "operandNode", type = DatExpressionNode.class)
public abstract class DatUnaryOperatorNode extends DatExpressionNode {
    protected abstract DatExpressionNode getOperandNode();

    @Override public @NotNull DatExpressionNode cloneUninitialized() {
        return (DatExpressionNode)createSameTypedNode(getOperandNode());
    }
}