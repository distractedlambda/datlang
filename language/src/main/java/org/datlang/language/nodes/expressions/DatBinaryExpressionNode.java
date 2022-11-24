package org.datlang.language.nodes.expressions;

import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild(value = "lhsNode", type = DatExpressionNode.class)
@NodeChild(value = "rhsNode", type = DatExpressionNode.class)
public abstract class DatBinaryExpressionNode extends DatExpressionNode {
    protected abstract DatExpressionNode getLhsNode();

    protected abstract DatExpressionNode getRhsNode();
}
