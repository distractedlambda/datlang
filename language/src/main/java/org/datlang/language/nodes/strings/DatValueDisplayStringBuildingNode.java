package org.datlang.language.nodes.strings;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import org.datlang.language.runtime.DatLoneTag;

@GenerateUncached
public abstract class DatValueDisplayStringBuildingNode extends DatStringBuildingNode {
    @Specialization
    protected void doBoolean(
        TruffleStringBuilder builder,
        boolean value,
        @Cached TruffleStringBuilder.AppendStringNode appendStringNode
    ) {
        appendStringNode.execute(builder, getLanguage().booleanToString(value));
    }

    @Specialization
    protected void doLong(
        TruffleStringBuilder builder,
        long value,
        @Cached TruffleStringBuilder.AppendLongNumberNode appendLongNumberNode
    ) {
        appendLongNumberNode.execute(builder, value);
    }

    @Specialization
    protected void doDouble(
        TruffleStringBuilder builder,
        double value,
        @Cached DatDoubleToStringNode doubleToStringNode,
        @Cached TruffleStringBuilder.AppendStringNode appendStringNode
    ) {
        appendStringNode.execute(builder, doubleToStringNode.execute(value));
    }

    @Specialization
    @TruffleBoundary
    protected void doString(
        TruffleStringBuilder builder,
        TruffleString value
    ) {
        builder.appendByteUncached((byte) '"');

        var iterator = value.createCodePointIteratorUncached(TruffleString.Encoding.UTF_8);
        while (iterator.hasNext()) {
            var codePoint = iterator.nextUncached();
            switch (codePoint) {
                case '\u0007' -> appendMnemonicEscape(builder, 'a');
                case '\u0008' -> appendMnemonicEscape(builder, 'b');
                case '\u001b' -> appendMnemonicEscape(builder, 'e');
                case '\f' -> appendMnemonicEscape(builder, 'f');
                case '\n' -> appendMnemonicEscape(builder, 'n');
                case '\r' -> appendMnemonicEscape(builder, 'r');
                case '\t' -> appendMnemonicEscape(builder, 't');
                case '\u000b' -> appendMnemonicEscape(builder, 'v');
                case '\\' -> appendMnemonicEscape(builder, '\\');
                case '"' -> appendMnemonicEscape(builder, '"');
                default -> {
                    switch (Character.getType(codePoint)) {
                        case Character.LOWERCASE_LETTER:
                        case Character.MODIFIER_LETTER:
                        case Character.OTHER_LETTER:
                        case Character.TITLECASE_LETTER:
                        case Character.UPPERCASE_LETTER:
                        case Character.COMBINING_SPACING_MARK:
                        case Character.ENCLOSING_MARK:
                        case Character.NON_SPACING_MARK:
                        case Character.DECIMAL_DIGIT_NUMBER:
                        case Character.LETTER_NUMBER:
                        case Character.OTHER_NUMBER:
                        case Character.CONNECTOR_PUNCTUATION:
                        case Character.DASH_PUNCTUATION:
                        case Character.END_PUNCTUATION:
                        case Character.FINAL_QUOTE_PUNCTUATION:
                        case Character.INITIAL_QUOTE_PUNCTUATION:
                        case Character.OTHER_PUNCTUATION:
                        case Character.START_PUNCTUATION:
                        case Character.CURRENCY_SYMBOL:
                        case Character.MODIFIER_SYMBOL:
                        case Character.MATH_SYMBOL:
                        case Character.OTHER_SYMBOL:
                        case Character.SPACE_SEPARATOR:
                            builder.appendCodePointUncached(codePoint);
                            break;

                        default:
                            builder.appendByteUncached((byte) '\\');

                            if (codePoint <= Character.MAX_VALUE) {
                                builder.appendByteUncached((byte) 'u');
                            }
                            else {
                                builder.appendByteUncached((byte) 'U');
                                appendHexDigit(builder, codePoint >>> 28);
                                appendHexDigit(builder, (codePoint >> 24) & 0xf);
                                appendHexDigit(builder, (codePoint >> 20) & 0xf);
                                appendHexDigit(builder, (codePoint >> 16) & 0xf);
                            }
                            appendHexDigit(builder, (codePoint >> 12) & 0xf);
                            appendHexDigit(builder, (codePoint >> 8) & 0xf);
                            appendHexDigit(builder, (codePoint >> 4) & 0xf);
                            appendHexDigit(builder, codePoint & 0xf);

                            break;
                    }
                }
            }
        }

        builder.appendByteUncached((byte) '"');
    }

    private static void appendMnemonicEscape(TruffleStringBuilder builder, char mnemonic) {
        builder.appendByteUncached((byte) '\\');
        builder.appendByteUncached((byte) mnemonic);
    }

    private static void appendHexDigit(TruffleStringBuilder builder, int digitValue) {
        builder.appendByteUncached((byte) switch (digitValue) {
            case 0 -> '0';
            case 1 -> '1';
            case 2 -> '2';
            case 3 -> '3';
            case 4 -> '4';
            case 5 -> '5';
            case 6 -> '6';
            case 7 -> '7';
            case 8 -> '8';
            case 9 -> '9';
            case 10 -> 'a';
            case 11 -> 'b';
            case 12 -> 'c';
            case 13 -> 'd';
            case 14 -> 'e';
            default -> 'f';
        });
    }

    @Specialization
    protected void doSymbol(
        TruffleStringBuilder builder,
        DatLoneTag value,
        @Cached TruffleStringBuilder.AppendStringNode appendStringNode
    ) {
        appendStringNode.execute(builder, value.getName());
    }
}
