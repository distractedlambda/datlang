package org.datlang.language.nodes.strings;

import com.oracle.truffle.api.strings.TruffleStringBuilder;
import org.datlang.language.nodes.DatNode;

public abstract class DatStringBuildingNode extends DatNode {
    public abstract void execute(TruffleStringBuilder builder, Object value);
}
