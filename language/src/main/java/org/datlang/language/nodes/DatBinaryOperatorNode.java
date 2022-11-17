package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import org.jetbrains.annotations.NotNull;

@NodeChild(value = "lhsNode", type = DatExpressionNode.class)
@NodeChild(value = "rhsNode", type = DatExpressionNode.class)
public abstract class DatBinaryOperatorNode extends DatExpressionNode {
    protected abstract DatExpressionNode getLhsNode();

    protected abstract DatExpressionNode getRhsNode();

    @Override public @NotNull DatExpressionNode cloneUninitialized() {
        return (DatExpressionNode)createSameTypedNode(getLhsNode(), getRhsNode());
    }
}
