package org.datlang.language;

import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.strings.TruffleString;

import java.math.BigInteger;

@TypeSystem({boolean.class, long.class, double.class, BigInteger.class, DatSmallRational.class, TruffleString.class})
public abstract class DatTypeSystem {
}
