package org.datlang.language.nodes.strings;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import org.datlang.language.DatAggregate;
import org.datlang.language.DatAggregateType;
import org.datlang.language.DatSymbol;

@GenerateUncached
public abstract class DatValueStringBuildingNode extends DatStringBuildingNode {
    @Specialization
    protected void doBoolean(
        TruffleStringBuilder builder,
        boolean value,
        @Cached TruffleStringBuilder.AppendStringNode appendStringNode
    ) {
        appendStringNode.execute(builder, getLanguage().booleanToString(value));
    }

    @Specialization
    protected void doLong(
        TruffleStringBuilder builder,
        long value,
        @Cached TruffleStringBuilder.AppendLongNumberNode appendLongNumberNode
    ) {
        appendLongNumberNode.execute(builder, value);
    }

    @Specialization
    protected void doDouble(
        TruffleStringBuilder builder,
        double value,
        @Cached DatDoubleToStringNode doubleToStringNode,
        @Cached TruffleStringBuilder.AppendStringNode appendStringNode
    ) {
        appendStringNode.execute(builder, doubleToStringNode.execute(value));
    }

    @Specialization
    protected void doString(
        TruffleStringBuilder builder,
        TruffleString value,
        @Cached TruffleStringBuilder.AppendStringNode appendStringNode
    ) {
        appendStringNode.execute(builder, value);
    }

    @Specialization
    protected void doSymbol(
        TruffleStringBuilder builder,
        DatSymbol value,
        @Cached TruffleStringBuilder.AppendStringNode appendStringNode
    ) {
        appendStringNode.execute(builder, value.getName());
    }

    @Specialization(guards = "value.getType() == cachedType")
    protected void doAggregateDirect(
        TruffleStringBuilder builder,
        DatAggregate value,
        @Cached("value.getType()") DatAggregateType cachedType,
        @Cached("create(cachedType.getStringBuildingFunction())") DirectCallNode callNode
    ) {
        callNode.call(builder, value);
    }

    @Specialization(replaces = "doAggregateDirect")
    protected void doAggregateIndirect(
        TruffleStringBuilder builder,
        DatAggregate value,
        @Cached IndirectCallNode callNode
    ) {
        callNode.call(value.getType().getStringBuildingFunction(), builder, value);
    }
}
