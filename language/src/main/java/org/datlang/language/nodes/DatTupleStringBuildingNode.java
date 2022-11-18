package org.datlang.language.nodes;

import com.oracle.truffle.api.strings.TruffleStringBuilder;
import org.datlang.language.DatTuple;

public abstract class DatTupleStringBuildingNode extends DatNode {
    public abstract void execute(TruffleStringBuilder builder, DatTuple tuple);
}
