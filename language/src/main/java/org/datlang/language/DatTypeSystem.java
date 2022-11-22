package org.datlang.language;

import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.strings.TruffleString;

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
