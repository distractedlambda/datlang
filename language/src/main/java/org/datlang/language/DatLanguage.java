package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.strings.TruffleString;
import org.datlang.language.runtime.DatTag;
import org.datlang.language.runtime.DatTagScope;
import org.datlang.language.runtime.DatTupleType;
import org.datlang.language.util.ConcurrentWeakCacheMap;
import org.datlang.language.util.ConcurrentWeakCacheSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Function;

@TruffleLanguage.Registration(id = "dat", name = "Dat")
public final class DatLanguage extends TruffleLanguage<DatContext> implements DatTagScope {
    private record TupleTypeInputs(@NotNull DatTag tag, @NotNull Class<?> @NotNull[] elementTypes) {
        @Override public boolean equals(Object obj) {
            if (!(obj instanceof TupleTypeInputs other)) {
                return false;
            }

            return tag == other.tag && Arrays.equals(elementTypes, other.elementTypes);
        }

        @Override public int hashCode() {
            return 31 * tag.hashCode() + Arrays.hashCode(elementTypes);
        }
    }

    private static final LanguageReference<DatLanguage> REFERENCE = LanguageReference.create(DatLanguage.class);

    public static DatLanguage get(@Nullable Node node) {
        return REFERENCE.get(node);
    }

    private final ConcurrentWeakCacheMap<TruffleString, DatTag> globalTags = new ConcurrentWeakCacheMap<>();
    private final Function<TruffleString, DatTag> globalTagFactory = key -> new DatTag(key, this);

    private final ConcurrentWeakCacheSet<TruffleString> internedStrings = new ConcurrentWeakCacheSet<>();
    private final ConcurrentWeakCacheSet<BigInteger> internedBigIntegers = new ConcurrentWeakCacheSet<>();

    private final ConcurrentWeakCacheMap<TupleTypeInputs, DatTupleType> tupleTypes = new ConcurrentWeakCacheMap<>();

    private final @NotNull TruffleString emptyString = getInternedString("");
    private final @NotNull TruffleString unitString = getInternedString("()");
    private final @NotNull TruffleString trueString = getInternedString("true");
    private final @NotNull TruffleString falseString = getInternedString("false");
    private final @NotNull TruffleString nanString = getInternedString("NaN");
    private final @NotNull TruffleString infinityString = getInternedString("∞");
    private final @NotNull TruffleString negativeInfinityString = getInternedString("-∞");

    private final @NotNull BigInteger maxLongPlusOne = getInternedBigInteger(
        BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE)
    );

    @Override protected DatContext createContext(Env env) {
        return new DatContext();
    }

    @TruffleBoundary
    public @NotNull TruffleString getInternedString(@NotNull TruffleString string) {
        return internedStrings.intern(string.switchEncodingUncached(TruffleString.Encoding.UTF_8));
    }

    @TruffleBoundary
    public @NotNull TruffleString getInternedString(@NotNull String string) {
        return getInternedString(TruffleString.fromJavaStringUncached(string, TruffleString.Encoding.UTF_8));
    }

    @TruffleBoundary
    public @NotNull BigInteger getInternedBigInteger(@NotNull BigInteger value) {
        return internedBigIntegers.intern(value);
    }

    @TruffleBoundary
    @Override public @NotNull DatTag getTag(@NotNull TruffleString name) {
        return globalTags.getOrCompute(name, globalTagFactory);
    }

    @TruffleBoundary
    public @NotNull DatTupleType getTupleType(@NotNull DatTag tag, @NotNull Class<?> @NotNull[] elementTypes) {
        return tupleTypes.getOrCompute(
            new TupleTypeInputs(tag, elementTypes),
            inputs -> new DatTupleType(this, inputs.tag, inputs.elementTypes)
        );
    }

    public @NotNull TruffleString getEmptyString() {
        return emptyString;
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

    public @NotNull BigInteger getMaxLongPlusOne() {
        return maxLongPlusOne;
    }
}
