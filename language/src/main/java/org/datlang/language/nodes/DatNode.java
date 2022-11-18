package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.ReportPolymorphism;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import org.datlang.language.DatTypeSystem;

@ReportPolymorphism
@GenerateNodeFactory
@NodeInfo(language = "Dat")
@TypeSystemReference(DatTypeSystem.class)
public abstract class DatNode extends Node {
}
