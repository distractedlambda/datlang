package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild(value = "operandNode", type = DatExpressionNode.class)
public abstract class DatUnaryOperatorNode extends DatExpressionNode {
    protected abstract DatExpressionNode getOperandNode();
}
