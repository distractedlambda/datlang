package org.datlang.language.runtime;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.staticobject.StaticProperty;
import com.oracle.truffle.api.strings.TruffleString;
import org.jetbrains.annotations.NotNull;

public final class DatClosureType extends DatAggregateType {
    private final @NotNull DatClosure.Factory instanceFactory;
    private final @NotNull CallTarget callTarget;

    @CompilationFinal(dimensions = 1)
    private final @NotNull CaptureProperty @NotNull[] captureProperties;

    public DatClosureType(
        @NotNull DatClosure.Factory instanceFactory,
        @NotNull CallTarget callTarget,
        @NotNull CaptureProperty @NotNull[] captureProperties
    ) {
        this.instanceFactory = instanceFactory;
        this.callTarget = callTarget;
        this.captureProperties = captureProperties;
    }

    public @NotNull DatClosure newInstance() {
        return instanceFactory.newInstance(this);
    }

    public @NotNull CallTarget getCallTarget() {
        return callTarget;
    }

    public int getCaptureCount() {
        return captureProperties.length;
    }

    public @NotNull CaptureProperty getCaptureProperty(int index) {
        return captureProperties[index];
    }

    @Override protected @NotNull CallTarget createStringBuildingFunction() {
        return null;
    }

    @Override protected @NotNull CallTarget createHashingFunction() {
        return null;
    }

    public static final class CaptureProperty extends StaticProperty {
        private final @NotNull TruffleString bindingName;
        private final @NotNull String id;

        public CaptureProperty(@NotNull TruffleString bindingName) {
            this.bindingName = bindingName;
            this.id = bindingName.toJavaStringUncached();
        }

        public @NotNull TruffleString getBindingName() {
            return bindingName;
        }

        @Override public @NotNull String getId() {
            return id;
        }
    }
}
