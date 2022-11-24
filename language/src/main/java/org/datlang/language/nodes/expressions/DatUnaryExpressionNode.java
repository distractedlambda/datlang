package org.datlang.language.nodes.expressions;

import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild(value = "operandNode", type = DatExpressionNode.class)
public abstract class DatUnaryExpressionNode extends DatExpressionNode {
    protected abstract DatExpressionNode getOperandNode();
}
