package org.datlang.language.nodes.expressions;

import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.nodes.expressions.DatUnaryOperatorNode;
import org.datlang.language.runtime.DatRuntimeException;

import static com.oracle.truffle.api.CompilerDirectives.transferToInterpreter;

public abstract class DatUnaryMinusOperatorNode extends DatUnaryOperatorNode {
    @Specialization
    protected long negateLong(long operand) {
        if (operand == Long.MIN_VALUE) {
            transferToInterpreter();
            throw DatRuntimeException.create("Integer overflow", this);
        }
        else {
            return -operand;
        }
    }

    @Specialization
    protected double negateDouble(double operand) {
        return -operand;
    }

    @Specialization
    protected Object illegal(Object operand) {
        transferToInterpreter();
        throw DatRuntimeException.create("Invalid operand", this);
    }
}
