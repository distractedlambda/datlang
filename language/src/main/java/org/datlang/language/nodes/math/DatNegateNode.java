package org.datlang.language.nodes.math;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import org.datlang.language.DatLanguage;
import org.datlang.language.nodes.DatNode;
import org.datlang.language.runtime.DatComplexNumber;
import org.datlang.language.runtime.DatImaginaryNumber;
import org.datlang.language.runtime.DatRatio;
import org.datlang.language.runtime.DatRuntimeException;

import java.math.BigInteger;

import static com.oracle.truffle.api.CompilerDirectives.transferToInterpreter;

@GenerateUncached
public abstract class DatNegateNode extends DatNode {
    public abstract Object executeGeneric(Object operand);

    public abstract long executeLong(long operand) throws UnexpectedResultException;

    public abstract double executeDouble(double operand);

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long doLongWithoutOverflow(long operand) {
        return Math.negateExact(operand);
    }

    @Specialization(replaces = "doLongWithoutOverflow")
    protected Object doLong(long operand) {
        try {
            return Math.negateExact(operand);
        }
        catch (ArithmeticException exception) {
            return getLanguage().getMaxLongPlusOne();
        }
    }

    @Specialization
    protected double doDouble(double operand) {
        return -operand;
    }

    @Specialization(guards = "operand == cachedOperand")
    protected Object doBigIntegerCached(
        BigInteger operand,
        @Cached("operand") BigInteger cachedOperand,
        @Cached("negateBigInteger(operand)") Object cachedResult
    ) {
        return cachedResult;
    }

    @Specialization(replaces = "doBigIntegerCached")
    protected Object doBigIntegerUncached(BigInteger operand) {
        return negateBigInteger(operand);
    }

    protected Object negateBigInteger(BigInteger operand) {
        //noinspection NumberEquality
        if (operand == getLanguage().getMaxLongPlusOne()) {
            return -1L;
        }
        else {
            return negateBigIntegerBoundary(getLanguage(), operand);
        }
    }

    @TruffleBoundary
    private static BigInteger negateBigIntegerBoundary(DatLanguage language, BigInteger value) {
        return language.getInternedBigInteger(value.negate());
    }

    @Specialization
    protected DatImaginaryNumber doImaginaryNumber(
        DatImaginaryNumber operand,
        @Cached DatImaginaryNumber.GetMagnitudeNode getMagnitudeNode,
        @Cached DatNegateNode negateMagnitudeNode,
        @Cached DatImaginaryNumber.CreateNode createNewImaginaryNumberNode
    ) {
        return createNewImaginaryNumberNode.execute(
            negateMagnitudeNode.executeGeneric(getMagnitudeNode.executeGeneric(operand))
        );
    }

    @Specialization
    protected DatRatio doRatio(
        DatRatio operand,
        @Cached DatRatio.GetNumeratorNode getNumeratorNode,
        @Cached DatRatio.GetDenominatorNode getDenominatorNode,
        @Cached DatNegateNode negateNumeratorNode,
        @Cached DatRatio.CreateNode createNewRatioNode
    ) {
        return createNewRatioNode.execute(
            negateNumeratorNode.executeGeneric(getNumeratorNode.executeGeneric(operand)),
            getDenominatorNode.executeGeneric(operand)
        );
    }

    @Specialization
    protected DatComplexNumber doComplexNumber(
        DatComplexNumber operand,
        @Cached DatComplexNumber.GetRealPartNode getRealPartNode,
        @Cached DatComplexNumber.GetImaginaryPartNode getImaginaryPartNode,
        @Cached DatNegateNode negateRealPartNode,
        @Cached DatNegateNode negateImaginaryPartNode,
        @Cached DatComplexNumber.CreateNode createNewComplexNumberNode
    ) {
        return createNewComplexNumberNode.execute(
            negateRealPartNode.executeGeneric(getRealPartNode.executeGeneric(operand)),
            negateImaginaryPartNode.executeGeneric(getImaginaryPartNode.executeGeneric(operand))
        );
    }

    @Fallback
    protected Object invalid(Object operand) {
        transferToInterpreter();
        throw DatRuntimeException.create("Invalid operand", this);
    }
}
