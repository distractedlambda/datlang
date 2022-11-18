package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.ReportPolymorphism;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import org.datlang.language.DatContext;
import org.datlang.language.DatLanguage;
import org.datlang.language.DatTypeSystem;

@ReportPolymorphism
@GenerateNodeFactory
@NodeInfo(language = "Dat")
@TypeSystemReference(DatTypeSystem.class)
public abstract class DatNode extends Node {
    public final DatLanguage getLanguage() {
        return DatLanguage.get(this);
    }

    public final DatContext getContext() {
        return DatContext.get(this);
    }
}
