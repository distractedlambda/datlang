package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.staticobject.DefaultStaticProperty;
import com.oracle.truffle.api.staticobject.StaticProperty;
import com.oracle.truffle.api.staticobject.StaticShape;
import com.oracle.truffle.api.strings.TruffleString;
import org.datlang.language.util.ConcurrentWeakCacheMap;
import org.datlang.language.util.ConcurrentWeakCacheSet;
import org.graalvm.collections.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.oracle.truffle.api.CompilerAsserts.neverPartOfCompilation;

@TruffleLanguage.Registration(id = "dat", name = "Dat")
public final class DatLanguage extends TruffleLanguage<DatContext> {
    private static final LanguageReference<DatLanguage> REFERENCE = LanguageReference.create(DatLanguage.class);

    public static DatLanguage get(@Nullable Node node) {
        return REFERENCE.get(node);
    }

    private final ConcurrentWeakCacheSet<TruffleString> internedStrings = new ConcurrentWeakCacheSet<>();
    private final ConcurrentWeakCacheMap<TruffleString, DatSymbol> symbols = new ConcurrentWeakCacheMap<>();
    private final ConcurrentWeakCacheMap<Pair<DatSymbol, List<Class<?>>>, DatTupleType> tupleTypes = new ConcurrentWeakCacheMap<>();
    private final ConcurrentWeakCacheMap<Pair<DatSymbol, Map<DatSymbol, Class<?>>>, DatRecordType> recordTypes = new ConcurrentWeakCacheMap<>();

    private final TruffleString trueString = internedString("true");
    private final TruffleString falseString = internedString("false");
    private final TruffleString nanString = internedString("NaN");
    private final TruffleString infinityString = internedString("∞");
    private final TruffleString negativeInfinityString = internedString("-∞");

    @Override protected DatContext createContext(Env env) {
        return new DatContext();
    }

    @TruffleBoundary
    public @NotNull TruffleString internedString(@NotNull TruffleString string) {
        return internedStrings.intern(string.switchEncodingUncached(TruffleString.Encoding.UTF_8));
    }

    @TruffleBoundary
    public @NotNull TruffleString internedString(@NotNull String string) {
        return internedString(TruffleString.fromJavaStringUncached(string, TruffleString.Encoding.UTF_8));
    }

    @TruffleBoundary
    public @NotNull DatSymbol symbol(@NotNull TruffleString name) {
        return symbols.getOrCompute(internedString(name), DatSymbol::new);
    }

    @TruffleBoundary
    public @NotNull DatSymbol symbol(@NotNull String name) {
        return symbols.getOrCompute(internedString(name), DatSymbol::new);
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
                var property = new DefaultStaticProperty(entry.getKey().getName().toString());
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

    public @NotNull TruffleString getTrueString() {
        return trueString;
    }

    public @NotNull TruffleString getFalseString() {
        return falseString;
    }

    public @NotNull TruffleString booleanToString(boolean value) {
        return value ? getTrueString() : getFalseString();
    }

    public @NotNull TruffleString getNanString() {
        return nanString;
    }

    public @NotNull TruffleString getInfinityString() {
        return infinityString;
    }

    public @NotNull TruffleString getNegativeInfinityString() {
        return negativeInfinityString;
    }
}
