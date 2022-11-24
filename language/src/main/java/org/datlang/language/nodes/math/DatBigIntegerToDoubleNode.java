package org.datlang.language.nodes.math;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.nodes.DatNode;
import org.datlang.language.util.BigIntegers;

import java.math.BigInteger;

@ImportStatic(BigIntegers.class)
public abstract class DatBigIntegerToDoubleNode extends DatNode {
    public abstract double execute(BigInteger value);

    @Specialization(guards = "value == cachedValue")
    protected double doCached(
        BigInteger value,
        @Cached("value") BigInteger cachedValue,
        @Cached("toDouble(cachedValue)") double cachedResult
    ) {
        return cachedResult;
    }

    @Specialization(replaces = "doCached")
    protected double doUncached(BigInteger value) {
        return BigIntegers.toDouble(value);
    }
}
