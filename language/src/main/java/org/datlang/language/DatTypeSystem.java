package org.datlang.language;

import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.strings.TruffleString;
import org.datlang.language.runtime.DatLoneTag;
import org.datlang.language.runtime.DatRecord;
import org.datlang.language.runtime.DatTuple;

@TypeSystem({
    boolean.class,
    double.class,
    long.class,
    DatLoneTag.class,
    DatRecord.class,
    DatTuple.class,
    TruffleString.class,
})
public abstract class DatTypeSystem {
}
