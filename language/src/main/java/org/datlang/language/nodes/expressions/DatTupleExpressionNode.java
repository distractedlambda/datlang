package org.datlang.language.nodes.expressions;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.strings.TruffleString;
import org.datlang.language.nodes.DatExpressionNode;
import org.datlang.language.nodes.aggregates.DatNewTupleNode;
import org.datlang.language.runtime.DatTuple;
import org.jetbrains.annotations.NotNull;

public abstract class DatTupleExpressionNode extends DatExpressionNode {
    protected final @NotNull TruffleString tag;
    @Children private final @NotNull DatExpressionNode @NotNull[] elementNodes;

    protected DatTupleExpressionNode(@NotNull TruffleString tag, @NotNull DatExpressionNode @NotNull[] elementNodes) {
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
