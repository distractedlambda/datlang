package org.datlang.language.nodes.constants;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.nodes.expressions.DatExpressionNode;

@NodeField(name = "value", type = long.class)
public abstract class DatLongConstantNode extends DatExpressionNode {
    protected abstract long getValue();

    @Specialization
    protected long execute() {
        return getValue();
    }
}
