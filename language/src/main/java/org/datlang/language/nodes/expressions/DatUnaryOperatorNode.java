package org.datlang.language.nodes.expressions;

import com.oracle.truffle.api.dsl.NodeChild;
import org.datlang.language.nodes.expressions.DatExpressionNode;

@NodeChild(value = "operandNode", type = DatExpressionNode.class)
public abstract class DatUnaryOperatorNode extends DatExpressionNode {
    protected abstract DatExpressionNode getOperandNode();
}
