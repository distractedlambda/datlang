package org.datlang.language.nodes.expressions;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.runtime.DatRuntimeException;
import org.datlang.language.util.BigIntegers;

import static com.oracle.truffle.api.CompilerDirectives.transferToInterpreter;

@ImportStatic(BigIntegers.class)
public abstract class DatBinaryDivideExpressionNode extends DatBinaryExpressionNode {
    @Specialization
    protected double doLongDouble(long lhs, double rhs) {
        return lhs / rhs;
    }

    @Specialization
    protected double doDoubleLong(double lhs, long rhs) {
        return lhs / rhs;
    }

    @Specialization
    protected double doDoubleDouble(double lhs, double rhs) {
        return lhs / rhs;
    }

    @Fallback
    protected Object doInvalid(Object lhs, Object rhs) {
        transferToInterpreter();
        throw DatRuntimeException.create("Invalid operands", this);
    }
}
