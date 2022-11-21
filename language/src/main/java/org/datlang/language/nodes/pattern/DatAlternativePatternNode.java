package org.datlang.language.nodes.pattern;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.profiles.ConditionProfile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class DatAlternativePatternNode extends DatPatternNode {
    @Children private final @NotNull DatPatternNode @NotNull[] alternativeNodes;

    @CompilationFinal(dimensions = 1)
    private final @NotNull ConditionProfile @NotNull[] alternativeProfiles;

    public DatAlternativePatternNode(List<? extends DatPatternNode> alternativeNodes) {
        this.alternativeNodes = alternativeNodes.toArray(DatPatternNode[]::new);
        this.alternativeProfiles = new ConditionProfile[this.alternativeNodes.length];
        for (var i = 0; i < alternativeProfiles.length; i++) {
            alternativeProfiles[i] = ConditionProfile.createCountingProfile();
        }
    }

    @ExplodeLoop
    @Override public boolean executeGeneric(VirtualFrame frame, Object subject) {
        for (var i = 0; i < alternativeNodes.length; i++) {
            if (alternativeProfiles[i].profile(alternativeNodes[i].executeGeneric(frame, subject))) {
                return true;
            }
        }

        return false;
    }

    @ExplodeLoop
    @Override public boolean executeBoolean(VirtualFrame frame, boolean subject) {
        for (var i = 0; i < alternativeNodes.length; i++) {
            if (alternativeProfiles[i].profile(alternativeNodes[i].executeBoolean(frame, subject))) {
                return true;
            }
        }

        return false;
    }

    @ExplodeLoop
    @Override public boolean executeLong(VirtualFrame frame, long subject) {
        for (var i = 0; i < alternativeNodes.length; i++) {
            if (alternativeProfiles[i].profile(alternativeNodes[i].executeLong(frame, subject))) {
                return true;
            }
        }

        return false;
    }

    @ExplodeLoop
    @Override public boolean executeDouble(VirtualFrame frame, double subject) {
        for (var i = 0; i < alternativeNodes.length; i++) {
            if (alternativeProfiles[i].profile(alternativeNodes[i].executeDouble(frame, subject))) {
                return true;
            }
        }

        return false;
    }
}
