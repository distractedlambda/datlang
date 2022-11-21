package org.datlang.language.nodes.pattern;

import com.oracle.truffle.api.frame.VirtualFrame;
import org.datlang.language.nodes.DatProgramNode;

public abstract class DatPatternNode extends DatProgramNode {
    public abstract boolean executeGeneric(VirtualFrame frame, Object subject);

    public boolean executeBoolean(VirtualFrame frame, boolean subject) {
        return executeGeneric(frame, subject);
    }

    public boolean executeLong(VirtualFrame frame, long subject) {
        return executeGeneric(frame, subject);
    }

    public boolean executeDouble(VirtualFrame frame, double subject) {
        return executeGeneric(frame, subject);
    }
}
