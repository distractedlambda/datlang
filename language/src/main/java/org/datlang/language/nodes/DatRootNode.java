package org.datlang.language.nodes;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.nodes.RootNode;
import org.datlang.language.DatLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DatRootNode extends RootNode {
    protected DatRootNode(@NotNull DatLanguage language, @Nullable FrameDescriptor frameDescriptor) {
        super(language, frameDescriptor);
    }
}
