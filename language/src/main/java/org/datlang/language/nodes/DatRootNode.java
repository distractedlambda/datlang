package org.datlang.language.nodes;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;
import org.datlang.language.DatLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DatRootNode extends RootNode {
    private final @Nullable SourceSection sourceSection;

    protected DatRootNode(
        @NotNull DatLanguage language,
        @Nullable FrameDescriptor frameDescriptor,
        @Nullable SourceSection sourceSection
    ) {
        super(language, frameDescriptor);
        this.sourceSection = sourceSection;
    }

    @Override public final @Nullable SourceSection getSourceSection() {
        return sourceSection;
    }
}
