package org.datlang.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import static org.datlang.language.DatTypeSystemGen.expectBoolean;
import static org.datlang.language.DatTypeSystemGen.expectDouble;
import static org.datlang.language.DatTypeSystemGen.expectLong;

public abstract class DatExpressionNode extends DatProgramNode {
    public abstract Object executeGeneric(VirtualFrame frame);

    public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        return expectBoolean(executeGeneric(frame));
    }

    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return expectLong(executeGeneric(frame));
    }

    public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
        return expectDouble(executeGeneric(frame));
    }
}
