package org.datlang.language;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.oracle.truffle.api.CompilerAsserts.partialEvaluationConstant;
import static com.oracle.truffle.api.CompilerDirectives.transferToInterpreterAndInvalidate;

public final class DatTopLevelValue {
    private final @NotNull CallTarget valueComputingCallTarget;

    @CompilationFinal private volatile @Nullable Object computedValue;

    public DatTopLevelValue(@NotNull CallTarget valueComputingCallTarget) {
        this.valueComputingCallTarget = valueComputingCallTarget;
    }

    public @NotNull Object get() {
        var value = computedValue;

        partialEvaluationConstant(value);

        if (value != null) {
            return value;
        }

        transferToInterpreterAndInvalidate();

        synchronized (this) {
            value = computedValue;

            if (value == null) {
                value = computedValue = valueComputingCallTarget.call();
                assert value != null;
            }

            return value;
        }
    }
}
