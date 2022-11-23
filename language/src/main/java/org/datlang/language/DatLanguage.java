package org.datlang.language;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.strings.TruffleString;
import org.datlang.language.runtime.DatLoneTag;
import org.datlang.language.util.ConcurrentWeakCacheMap;
import org.datlang.language.util.ConcurrentWeakCacheSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@TruffleLanguage.Registration(id = "dat", name = "Dat")
public final class DatLanguage extends TruffleLanguage<DatContext> {
    private static final LanguageReference<DatLanguage> REFERENCE = LanguageReference.create(DatLanguage.class);

    public static DatLanguage get(@Nullable Node node) {
        return REFERENCE.get(node);
    }

    private final ConcurrentWeakCacheSet<TruffleString> internedStrings = new ConcurrentWeakCacheSet<>();
    private final ConcurrentWeakCacheMap<TruffleString, DatLoneTag> loneTags = new ConcurrentWeakCacheMap<>();

    private final TruffleString unitString = getInternedString("()");
    private final TruffleString trueString = getInternedString("true");
    private final TruffleString falseString = getInternedString("false");
    private final TruffleString nanString = getInternedString("NaN");
    private final TruffleString infinityString = getInternedString("∞");
    private final TruffleString negativeInfinityString = getInternedString("-∞");

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
    public @NotNull DatLoneTag getLoneTag(@NotNull TruffleString name) {
        return loneTags.getOrCompute(getInternedString(name), DatLoneTag::new);
    }

    @TruffleBoundary
    public @NotNull DatLoneTag getLoneTag(@NotNull String name) {
        return loneTags.getOrCompute(getInternedString(name), DatLoneTag::new);
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
