package org.datlang.language.runtime;

import com.oracle.truffle.api.exception.AbstractTruffleException;
import com.oracle.truffle.api.nodes.EncapsulatingNodeReference;
import com.oracle.truffle.api.nodes.Node;

import static com.oracle.truffle.api.CompilerAsserts.partialEvaluationConstant;

public final class DatRuntimeException extends AbstractTruffleException {
    private DatRuntimeException(String message, Node location) {
        super(message, location);
    }

    public static DatRuntimeException create(String message, Node location) {
        partialEvaluationConstant(location);

        if (location == null || !location.isAdoptable()) {
            location = EncapsulatingNodeReference.getCurrent().get();
        }

        return new DatRuntimeException(message, location);
    }
}
