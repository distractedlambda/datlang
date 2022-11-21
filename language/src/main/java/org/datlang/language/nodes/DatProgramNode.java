package org.datlang.language.nodes;

import com.oracle.truffle.api.source.SourceSection;
import org.jetbrains.annotations.Nullable;

public abstract class DatProgramNode extends DatNode {
    private int sourceOffset = -1, sourceLength;

    public final void setSourceRange(int start, int end) {
        sourceOffset = start;
        sourceLength = end - start;
    }

    @Override public final @Nullable SourceSection getSourceSection() {
        if (sourceOffset < 0) {
            return null;
        }

        var root = getRootNode();
        if (root == null) {
            return null;
        }

        var rootSourceSection = root.getSourceSection();
        if (rootSourceSection == null) {
            return null;
        }

        return rootSourceSection.getSource().createSection(sourceOffset, sourceLength);
    }
}
