package org.datlang.language.nodes.pattern;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.profiles.ConditionProfile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class DatProductPatternNode extends DatPatternNode {
    @Children private final @NotNull DatPatternNode @NotNull[] stepNodes;

    @CompilerDirectives.CompilationFinal(dimensions = 1)
    private final @NotNull ConditionProfile @NotNull[] stepProfiles;

    public DatProductPatternNode(List<? extends DatPatternNode> stepNodes) {
        this.stepNodes = stepNodes.toArray(DatPatternNode[]::new);
        this.stepProfiles = new ConditionProfile[this.stepNodes.length];
        for (var i = 0; i < stepProfiles.length; i++) {
            stepProfiles[i] = ConditionProfile.createCountingProfile();
        }
    }

    @ExplodeLoop
    @Override public boolean executeGeneric(VirtualFrame frame, Object subject) {
        for (var i = 0; i < stepNodes.length; i++) {
            if (stepProfiles[i].profile(!stepNodes[i].executeGeneric(frame, subject))) {
                return false;
            }
        }

        return true;
    }

    @ExplodeLoop
    @Override public boolean executeBoolean(VirtualFrame frame, boolean subject) {
        for (var i = 0; i < stepNodes.length; i++) {
            if (stepProfiles[i].profile(!stepNodes[i].executeBoolean(frame, subject))) {
                return false;
            }
        }

        return true;
    }

    @ExplodeLoop
    @Override public boolean executeLong(VirtualFrame frame, long subject) {
        for (var i = 0; i < stepNodes.length; i++) {
            if (stepProfiles[i].profile(!stepNodes[i].executeLong(frame, subject))) {
                return false;
            }
        }

        return true;
    }

    @ExplodeLoop
    @Override public boolean executeDouble(VirtualFrame frame, double subject) {
        for (var i = 0; i < stepNodes.length; i++) {
            if (stepProfiles[i].profile(!stepNodes[i].executeDouble(frame, subject))) {
                return false;
            }
        }

        return true;
    }
}
