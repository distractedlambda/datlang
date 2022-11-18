package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.staticobject.DefaultStaticProperty;
import com.oracle.truffle.api.staticobject.StaticProperty;
import com.oracle.truffle.api.staticobject.StaticShape;
import com.oracle.truffle.api.strings.TruffleString;
import org.datlang.language.util.ConcurrentWeakCache;
import org.datlang.language.util.ConcurrentWeakCacheSet;
import org.graalvm.collections.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.oracle.truffle.api.CompilerAsserts.neverPartOfCompilation;

@TruffleLanguage.Registration(id = "dat", name = "Dat")
public final class DatLanguage extends TruffleLanguage<DatContext> {
    private final ConcurrentWeakCache<String, TruffleString> literalStrings = new ConcurrentWeakCache<>();
    private final ConcurrentWeakCache<TruffleString, DatSymbol> symbols = new ConcurrentWeakCache<>();
    private final ConcurrentWeakCache<Pair<DatSymbol, List<Class<?>>>, DatTupleType> tupleTypes = new ConcurrentWeakCache<>();
    private final ConcurrentWeakCache<Pair<DatSymbol, Map<DatSymbol, Class<?>>>, DatRecordType> recordTypes = new ConcurrentWeakCache<>();
    private final ConcurrentWeakCacheSet<BigInteger> internedBigIntegers = new ConcurrentWeakCacheSet<>();

    @Override protected DatContext createContext(Env env) {
        return new DatContext();
    }

    @TruffleBoundary
    public @NotNull TruffleString literalString(@NotNull String string) {
        return literalStrings.getOrCompute(
            string,
            str -> TruffleString.fromJavaStringUncached(str, TruffleString.Encoding.UTF_8)
        );
    }

    @TruffleBoundary
    public @NotNull DatSymbol symbol(@NotNull TruffleString name) {
        return symbols.getOrCompute(
            name.switchEncodingUncached(TruffleString.Encoding.UTF_8),
            DatSymbol::new
        );
    }

    @TruffleBoundary
    public @NotNull DatSymbol symbol(@NotNull String name) {
        return symbols.getOrCompute(literalString(name), DatSymbol::new);
    }

    public @NotNull DatTupleType tupleType(
        @NotNull DatSymbol tag,
        @NotNull List<@NotNull Class<?>> elementTypes
    ) {
        neverPartOfCompilation();
        return tupleTypes.getOrCompute(Pair.create(tag, List.copyOf(elementTypes)), key -> {
            var properties = new StaticProperty[key.getRight().size()];
            var shapeBuilder = StaticShape.newBuilder(this);

            for (var i = 0; i < properties.length; i++) {
                var property = new DefaultStaticProperty(Integer.toString(i));
                shapeBuilder.property(property, key.getRight().get(i), true);
                properties[i] = property;
            }

            return new DatTupleType(
                key.getLeft(),
                properties,
                shapeBuilder.build(DatTuple.class, DatTuple.Factory.class).getFactory()
            );
        });
    }

    public @NotNull DatRecordType recordType(
        @NotNull DatSymbol tag,
        @NotNull Map<@NotNull DatSymbol, @NotNull Class<?>> memberTypes
    ) {
        neverPartOfCompilation();
        return recordTypes.getOrCompute(Pair.create(tag, Map.copyOf(memberTypes)), key -> {
            var shapeBuilder = StaticShape.newBuilder(this);
            var propertyMap = new LinkedHashMap<DatSymbol, StaticProperty>();

            key.getRight().entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
                var property = new DefaultStaticProperty(entry.getKey().name().toString());
                shapeBuilder.property(property, entry.getValue(), true);
                propertyMap.put(entry.getKey(), property);
            });

            return new DatRecordType(
                key.getLeft(),
                Map.copyOf(propertyMap),
                shapeBuilder.build(DatRecord.class, DatRecord.Factory.class).getFactory()
            );
        });
    }

    @TruffleBoundary
    public @NotNull BigInteger internedBigInteger(@NotNull BigInteger candidate) {
        return internedBigIntegers.intern(candidate);
    }
}
