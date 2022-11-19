package org.datlang.language.nodes.strings;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import org.datlang.language.DatLanguage;
import org.datlang.language.nodes.DatRootNode;
import org.jetbrains.annotations.NotNull;

import static com.oracle.truffle.api.CompilerDirectives.shouldNotReachHere;

public final class DatStringBuildingRootNode extends DatRootNode {
    @Child private DatStringBuildingNode bodyNode;

    public DatStringBuildingRootNode(@NotNull DatLanguage language, @NotNull DatStringBuildingNode bodyNode) {
        super(language, null);
        this.bodyNode = bodyNode;
    }

    @Override public Object execute(VirtualFrame frame) {
        var arguments = frame.getArguments();

        if (arguments.length != 2) {
            throw shouldNotReachHere("Invalid number of arguments");
        }

        if (!(arguments[0] instanceof TruffleStringBuilder builder)) {
            throw shouldNotReachHere("First argument is not a TruffleStringBuilder");
        }

        bodyNode.execute(builder, arguments[1]);
        return null;
    }
}
