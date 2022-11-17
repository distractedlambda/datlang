package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;
import org.datlang.language.DatTypeSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

@GenerateNodeFactory
@NodeInfo(language = "Dat")
@TypeSystemReference(DatTypeSystem.class)
public abstract class DatNode extends Node {
    private int sourceOffset = -1, sourceLength;

    public final void setSourceRange(int offset, int length) {
        sourceOffset = offset;
        sourceLength = length;
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

    public abstract @NotNull DatNode cloneUninitialized();

    public final @NotNull DatNode createSameTypedNode(Object... args) {
        NodeFactory<?> factory;
        try {
            factory = (NodeFactory<?>)getClass().getEnclosingClass().getDeclaredMethod("getInstance").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            throw new UnsupportedOperationException(exception);
        }
        return getClass().cast(factory.createNode(args));
    }
}
