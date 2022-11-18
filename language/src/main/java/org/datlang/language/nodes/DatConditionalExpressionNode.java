package org.datlang.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;
import org.jetbrains.annotations.NotNull;

public final class DatConditionalExpressionNode extends DatExpressionNode {
    @Child private DatExpressionNode conditionNode;
    @Child private DatExpressionNode consequentNode;
    @Child private DatExpressionNode alternateNode;

    private final ConditionProfile profile = ConditionProfile.createCountingProfile();

    public DatConditionalExpressionNode(
        DatExpressionNode conditionNode,
        DatExpressionNode consequentNode,
        DatExpressionNode alternateNode
    ) {
        this.conditionNode = conditionNode;
        this.consequentNode = consequentNode;
        this.alternateNode = alternateNode;
    }

    private boolean profiledCondition(VirtualFrame frame) {
        try {
            return profile.profile(conditionNode.executeBoolean(frame));
        } catch (UnexpectedResultException exception) {
            throw new UnsupportedOperationException("TODO", exception);
        }
    }

    @Override public Object executeGeneric(VirtualFrame frame) {
        if (profiledCondition(frame)) {
            return consequentNode.executeGeneric(frame);
        } else {
            return alternateNode.executeGeneric(frame);
        }
    }

    @Override public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        if (profiledCondition(frame)) {
            return consequentNode.executeBoolean(frame);
        } else {
            return alternateNode.executeBoolean(frame);
        }
    }

    @Override public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        if (profiledCondition(frame)) {
            return consequentNode.executeLong(frame);
        } else {
            return alternateNode.executeLong(frame);
        }
    }

    @Override public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
        if (profiledCondition(frame)) {
            return consequentNode.executeDouble(frame);
        } else {
            return alternateNode.executeDouble(frame);
        }
    }

    @Override public @NotNull DatExpressionNode cloneUninitialized() {
        return new DatConditionalExpressionNode(
            conditionNode.cloneUninitialized(),
            consequentNode.cloneUninitialized(),
            alternateNode.cloneUninitialized()
        );
    }
}
