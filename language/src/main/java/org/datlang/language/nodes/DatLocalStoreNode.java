package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class DatLocalStoreNode extends DatNode {
    private final int slot;

    protected DatLocalStoreNode(int slot) {
        this.slot = slot;
    }

    public abstract void executeBoolean(VirtualFrame frame, boolean value);

    public abstract void executeLong(VirtualFrame frame, long value);

    public abstract void executeDouble(VirtualFrame frame, double value);

    public abstract void executeGeneric(VirtualFrame frame, Object value);

    @Specialization(guards = "isBooleanOrIllegal(frame)")
    protected void storeBoolean(VirtualFrame frame, boolean value) {
        frame.getFrameDescriptor().setSlotKind(slot, FrameSlotKind.Boolean);
        frame.setBoolean(slot, value);
    }

    @Specialization(guards = "isLongOrIllegal(frame)")
    protected void storeLong(VirtualFrame frame, long value) {
        frame.getFrameDescriptor().setSlotKind(slot, FrameSlotKind.Long);
        frame.setLong(slot, value);
    }

    @Specialization(guards = "isDoubleOrIllegal(frame)")
    protected void storeDouble(VirtualFrame frame, double value) {
        frame.getFrameDescriptor().setSlotKind(slot, FrameSlotKind.Double);
        frame.setDouble(slot, value);
    }

    @Specialization(replaces = {"storeBoolean", "storeLong", "storeDouble"})
    protected void storeObject(VirtualFrame frame, Object value) {
        frame.getFrameDescriptor().setSlotKind(slot, FrameSlotKind.Object);
        frame.setObject(slot, value);
    }

    protected boolean isBooleanOrIllegal(VirtualFrame frame) {
        return switch (slotKind(frame)) {
            case Boolean, Illegal -> true;
            default -> false;
        };
    }

    protected boolean isLongOrIllegal(VirtualFrame frame) {
        return switch (slotKind(frame)) {
            case Long, Illegal -> true;
            default -> false;
        };
    }

    protected boolean isDoubleOrIllegal(VirtualFrame frame) {
        return switch (slotKind(frame)) {
            case Double, Illegal -> true;
            default -> false;
        };
    }

    private FrameSlotKind slotKind(VirtualFrame frame) {
        return frame.getFrameDescriptor().getSlotKind(slot);
    }
}
