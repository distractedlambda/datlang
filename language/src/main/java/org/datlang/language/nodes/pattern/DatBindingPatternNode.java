package org.datlang.language.nodes.pattern;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import org.datlang.language.nodes.DatLocalStoreNode;

@NodeField(name = "frameSlot", type = int.class)
public abstract class DatBindingPatternNode extends DatPatternNode {
    @Specialization
    protected boolean matchBoolean(
        VirtualFrame frame,
        boolean subject,
        @Cached(parameters = "frameSlot") @Cached.Shared("localStoreNode") DatLocalStoreNode localStoreNode
    ) {
        localStoreNode.executeBoolean(frame, subject);
        return true;
    }

    @Specialization
    protected boolean matchLong(
        VirtualFrame frame,
        long subject,
        @Cached(parameters = "frameSlot") @Cached.Shared("localStoreNode") DatLocalStoreNode localStoreNode
    ) {
        localStoreNode.executeLong(frame, subject);
        return true;
    }

    @Specialization
    protected boolean matchDouble(
        VirtualFrame frame,
        double subject,
        @Cached(parameters = "frameSlot") @Cached.Shared("localStoreNode") DatLocalStoreNode localStoreNode
    ) {
        localStoreNode.executeDouble(frame, subject);
        return true;
    }

    @Specialization(replaces = {"matchBoolean", "matchLong", "matchDouble"})
    protected boolean matchObject(
        VirtualFrame frame,
        Object subject,
        @Cached(parameters = "frameSlot") @Cached.Shared("localStoreNode") DatLocalStoreNode localStoreNode
    ) {
        localStoreNode.executeGeneric(frame, subject);
        return true;
    }
}
