package org.datlang.language;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import static java.util.Objects.requireNonNull;

public abstract class DatAggregateType {
    private volatile @Nullable CallTarget stringBuildingFunction;
    private volatile @Nullable CallTarget hashingFunction;

    @TruffleBoundary
    public final @NotNull CallTarget getStringBuildingFunction() {
        var function = stringBuildingFunction;

        if (function != null) {
            return function;
        }

        function = createStringBuildingFunction();

        if (!STRING_BUILDING_FUNCTION_UPDATER.compareAndSet(this, null, function)) {
            function = requireNonNull(stringBuildingFunction);
        }

        return function;
    }

    protected abstract @NotNull CallTarget createStringBuildingFunction();

    @TruffleBoundary
    public final @NotNull CallTarget getHashingFunction() {
        var function = hashingFunction;

        if (function != null) {
            return function;
        }

        function = createHashingFunction();

        if (!HASHING_FUNCTION_UPDATER.compareAndSet(this, null, function)) {
            function = requireNonNull(hashingFunction);
        }

        return function;
    }

    protected abstract @NotNull CallTarget createHashingFunction();

    private static final AtomicReferenceFieldUpdater<DatAggregateType, CallTarget> STRING_BUILDING_FUNCTION_UPDATER =
        AtomicReferenceFieldUpdater.newUpdater(DatAggregateType.class, CallTarget.class, "stringBuildingFunction");

    private static final AtomicReferenceFieldUpdater<DatAggregateType, CallTarget> HASHING_FUNCTION_UPDATER =
        AtomicReferenceFieldUpdater.newUpdater(DatAggregateType.class, CallTarget.class, "hashingFunction");
}
