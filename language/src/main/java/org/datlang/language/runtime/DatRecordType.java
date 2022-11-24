package org.datlang.language.runtime;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.staticobject.StaticProperty;
import com.oracle.truffle.api.strings.TruffleString;
import org.jetbrains.annotations.NotNull;

public final class DatRecordType extends DatTaggedAggregateType {
    private final @NotNull DatRecord.Factory instanceFactory;

    @CompilationFinal(dimensions = 1)
    private final @NotNull MemberProperty @NotNull[] memberProperties;

    public DatRecordType(
        @NotNull DatTag tag,
        @NotNull DatRecord.Factory instanceFactory,
        @NotNull MemberProperty @NotNull[] memberProperties
    ) {
        super(tag);
        this.memberProperties = memberProperties;
        this.instanceFactory = instanceFactory;
    }

    public int getMemberCount() {
        return memberProperties.length;
    }

    public @NotNull MemberProperty getMemberProperty(int index) {
        return memberProperties[index];
    }

    public @NotNull DatRecord newInstance() {
        return instanceFactory.newInstance(this);
    }

    @Override protected @NotNull CallTarget createStringBuildingFunction() {
        return null;
    }

    @Override protected @NotNull CallTarget createHashingFunction() {
        return null;
    }

    public static final class MemberProperty extends StaticProperty {
        private final @NotNull TruffleString memberName;
        private final @NotNull String id;

        public MemberProperty(@NotNull TruffleString memberName) {
            this.memberName = memberName;
            this.id = memberName.toJavaStringUncached();
        }

        public @NotNull TruffleString getMemberName() {
            return memberName;
        }

        @Override public @NotNull String getId() {
            return id;
        }
    }
}
