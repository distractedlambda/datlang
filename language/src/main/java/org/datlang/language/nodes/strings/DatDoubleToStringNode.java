package org.datlang.language.nodes.strings;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.strings.TruffleString;
import org.datlang.language.DatLanguage;
import org.datlang.language.nodes.DatNode;
import org.jetbrains.annotations.NotNull;

@GenerateUncached
@ImportStatic(Double.class)
public abstract class DatDoubleToStringNode extends DatNode {
    public abstract TruffleString execute(double value);

    @Specialization(guards = "doubleToRawLongBits(value) == doubleToRawLongBits(cachedValue)")
    protected TruffleString cachedValue(
        double value,
        @Cached("value") double cachedValue,
        @Cached(value = "doubleToLiteralString(cachedValue)") TruffleString cachedResult
    ) {
        return cachedResult;
    }

    @TruffleBoundary
    @Specialization(replaces = "cachedValue")
    protected TruffleString dynamicValue(double value) {
        return TruffleString.fromJavaStringUncached(Double.toString(value), TruffleString.Encoding.UTF_8);
    }

    @TruffleBoundary
    protected static @NotNull TruffleString doubleToLiteralString(double value) {
        return DatLanguage.get(null).internedString(Double.toString(value));
    }
}
