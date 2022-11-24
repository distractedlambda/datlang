package org.datlang.language;

import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.strings.TruffleString;
import org.datlang.language.runtime.DatClosure;
import org.datlang.language.runtime.DatComplexNumber;
import org.datlang.language.runtime.DatRatio;
import org.datlang.language.runtime.DatRecord;
import org.datlang.language.runtime.DatTag;
import org.datlang.language.runtime.DatTuple;

import java.math.BigInteger;

@TypeSystem({
    boolean.class,
    double.class,
    long.class,
    BigInteger.class,
    DatClosure.class,
    DatComplexNumber.class,
    DatRatio.class,
    DatRecord.class,
    DatTag.class,
    DatTuple.class,
    TruffleString.class,
})
public abstract class DatTypeSystem {
}
