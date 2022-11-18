package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.staticobject.StaticProperty;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import org.datlang.language.DatTuple;

@NodeField(name = "property", type = StaticProperty.class)
public abstract class DatAppendBooleanTupleFieldToStringBuilderNode extends DatTupleStringBuildingNode {
    protected abstract StaticProperty getProperty();

    public abstract void execute(TruffleStringBuilder builder, DatTuple tuple);

    @Specialization
    protected void append(
        TruffleStringBuilder builder,
        DatTuple tuple,
        @Cached TruffleStringBuilder.AppendStringNode appendStringNode
    ) {
        var language = getLanguage();
        var fieldValue = getProperty().getBoolean(tuple);
        var fieldString = fieldValue ? language.getTrueString() : language.getFalseString();
        appendStringNode.execute(builder, fieldString);
    }
}
