package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.staticobject.StaticProperty;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import org.datlang.language.runtime.DatTuple;

public abstract class DatBuildTupleStringNode extends DatNode {
    public abstract void execute(TruffleStringBuilder builder, DatTuple tuple);

    static final class TypeAssumingNode extends DatNode {
    }

    @NodeField(name = "property", type = StaticProperty.class)
    static abstract class ElementAppendingNode extends DatNode {
        abstract void execute(TruffleStringBuilder builder, DatTuple tuple);

        protected abstract StaticProperty getProperty();
    }

    static abstract class BooleanElementAppendingNode extends ElementAppendingNode {
        @Specialization
        protected void doAppend(
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
}
