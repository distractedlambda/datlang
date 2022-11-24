package org.datlang.language.nodes.expressions;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import org.datlang.language.nodes.aggregates.DatNewTupleNode;
import org.datlang.language.runtime.DatTag;
import org.datlang.language.runtime.DatTuple;
import org.jetbrains.annotations.NotNull;

public abstract class DatTupleExpressionNode extends DatExpressionNode {
    protected final @NotNull DatTag tag;
    @Children private final @NotNull DatExpressionNode @NotNull[] elementNodes;

    protected DatTupleExpressionNode(@NotNull DatTag tag, @NotNull DatExpressionNode @NotNull[] elementNodes) {
        this.tag = tag;
        this.elementNodes = elementNodes;
    }

    @ExplodeLoop
    @Specialization
    protected DatTuple doExecute(VirtualFrame frame, @Cached(parameters = "tag") DatNewTupleNode newTupleNode) {
        var elements = new Object[elementNodes.length];

        for (var i = 0; i < elements.length; i++) {
            elements[i] = elementNodes[i].executeGeneric(frame);
        }

        return newTupleNode.execute(elements);
    }
}
