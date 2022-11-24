package org.datlang.language.nodes.expressions;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Cached.Shared;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import org.datlang.language.nodes.math.DatNegateNode;

public abstract class DatNegationExpressionNode extends DatUnaryExpressionNode {
    @Specialization(rewriteOn = UnexpectedResultException.class)
    protected long doLong(long operand, @Cached @Shared("negateNode") DatNegateNode negateNode)
    throws UnexpectedResultException {
        return negateNode.executeLong(operand);
    }

    @Specialization
    protected double doDouble(double operand, @Cached @Shared("negateNode") DatNegateNode negateNode) {
        return negateNode.executeDouble(operand);
    }

    @Specialization(replaces = {"doLong", "doDouble"})
    protected Object doGeneric(Object operand, @Cached @Shared("negateNode") DatNegateNode negateNode) {
        return negateNode.executeGeneric(operand);
    }
}
