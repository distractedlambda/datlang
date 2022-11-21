package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

import static com.oracle.truffle.api.CompilerDirectives.transferToInterpreter;

@NodeField(name = "slot", type = int.class)
public abstract class DatLocalLoadNode extends DatExpressionNode {
    protected abstract int getSlot();

    @Specialization(guards = "frame.isBoolean(getSlot())")
    protected boolean loadBoolean(VirtualFrame frame) {
        return frame.getBoolean(getSlot());
    }

    @Specialization(guards = "frame.isLong(getSlot())")
    protected long loadLong(VirtualFrame frame) {
        return frame.getLong(getSlot());
    }

    @Specialization(guards = "frame.isDouble(getSlot())")
    protected double loadDouble(VirtualFrame frame) {
        return frame.getDouble(getSlot());
    }

    @Specialization(replaces = {"loadBoolean", "loadLong", "loadDouble"})
    protected Object loadObject(VirtualFrame frame) {
        if (frame.isObject(getSlot())) {
            return frame.getObject(getSlot());
        } else {
            transferToInterpreter();
            var value = frame.getValue(getSlot());
            frame.setObject(getSlot(), value);
            return value;
        }
    }
}
