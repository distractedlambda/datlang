package org.datlang.language.nodes;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import org.datlang.language.nodes.expressions.DatExpressionNode;
import org.datlang.language.runtime.DatTopLevelValue;

@NodeField(name = "topLevelValue", type = DatTopLevelValue.class)
public abstract class DatReadTopLevelValueNode extends DatExpressionNode {
    protected abstract DatTopLevelValue getTopLevelValue();

    @Specialization
    protected Object doRead() {
        return getTopLevelValue().get();
    }
}
