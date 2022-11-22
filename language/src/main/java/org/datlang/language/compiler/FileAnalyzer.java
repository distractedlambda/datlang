package org.datlang.language.compiler;

import com.oracle.truffle.api.ArrayUtils;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.datlang.language.DatLanguage;
import org.datlang.language.DatParseException;
import org.datlang.language.compiler.DatParser.BinIntegerExpressionContext;
import org.datlang.language.compiler.DatParser.DecIntegerExpressionContext;
import org.datlang.language.compiler.DatParser.DecRealExpressionContext;
import org.datlang.language.compiler.DatParser.ExpressionContext;
import org.datlang.language.compiler.DatParser.FalseExpressionContext;
import org.datlang.language.compiler.DatParser.FileContext;
import org.datlang.language.compiler.DatParser.HexIntegerExpressionContext;
import org.datlang.language.compiler.DatParser.HexRealExpressionContext;
import org.datlang.language.compiler.DatParser.OctIntegerExpressionContext;
import org.datlang.language.compiler.DatParser.StringExpressionContext;
import org.datlang.language.compiler.DatParser.TopLevelBindingContext;
import org.datlang.language.compiler.DatParser.TopLevelFunctionContext;
import org.datlang.language.compiler.DatParser.TopLevelImportContext;
import org.datlang.language.compiler.DatParser.TrueExpressionContext;
import org.datlang.language.nodes.DatDoubleConstantNodeGen;
import org.datlang.language.nodes.DatExpressionNode;
import org.datlang.language.nodes.DatFalseConstantNodeGen;
import org.datlang.language.nodes.DatLongConstantNodeGen;
import org.datlang.language.nodes.DatObjectConstantNodeGen;
import org.datlang.language.nodes.DatProgramNode;
import org.datlang.language.nodes.DatTrueConstantNodeGen;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.parseUnsignedInt;

public final class FileAnalyzer implements ANTLRErrorListener {
    private final @NotNull DatLanguage language;
    private final @NotNull Source source;

    private final Set<String> importedModuleNames = new LinkedHashSet<>();

    private @Nullable DatParseException error;

    public FileAnalyzer(@NotNull DatLanguage language, @NotNull Source source) {
        this.language = language;
        this.source = source;

        var lexer = new DatLexer(CharStreams.fromString(source.getCharacters().toString(), source.getName()));
        lexer.removeErrorListeners();
        lexer.addErrorListener(this);

        var parser = new DatParser(new CommonTokenStream(lexer));
        parser.setBuildParseTree(false);
        parser.removeErrorListeners();
        parser.addErrorListener(this);

        var fileContext = parser.file();

        if (error != null) {
            return;
        }

        analyze(fileContext);
    }

    public @Nullable DatParseException getError() {
        return error;
    }

    private void recordError(@NotNull Throwable exception) {
        if (error == null) {
            if (exception instanceof DatParseException datParseException) {
                error = datParseException;
            }
            else {
                error = new DatParseException(source.createUnavailableSection(), null, exception);
            }
        }
        else {
            error.addSuppressed(exception);
        }
    }

    private void analyze(@NotNull FileContext context) {
        for (var item : context.items) {
            if (item instanceof TopLevelImportContext topLevelImportContext) {
                analyze(topLevelImportContext);
            }
            else if (item instanceof TopLevelFunctionContext topLevelFunctionContext) {
                analyze(topLevelFunctionContext);
            }
            else if (item instanceof TopLevelBindingContext topLevelBindingContext) {
                analyze(topLevelBindingContext);
            }
            else {
                throw new ClassCastException();
            }
        }
    }

    private void analyze(@NotNull TopLevelImportContext context) {
        importedModuleNames.add(context.moduleName.toString());
    }

    private void analyze(@NotNull TopLevelFunctionContext context) {
    }

    private void analyze(@NotNull TopLevelBindingContext context) {
    }

    private @NotNull DatExpressionNode analyze(@NotNull ExpressionContext context) {
        if (context instanceof TrueExpressionContext trueExpressionContext) {
            return analyze(trueExpressionContext);
        }
        else if (context instanceof FalseExpressionContext falseExpressionContext) {
            return analyze(falseExpressionContext);
        }
        else if (context instanceof BinIntegerExpressionContext binIntegerExpressionContext) {
            return analyze(binIntegerExpressionContext);
        }
        else if (context instanceof OctIntegerExpressionContext octIntegerExpressionContext) {
            return analyze(octIntegerExpressionContext);
        }
        else if (context instanceof DecIntegerExpressionContext decIntegerExpressionContext) {
            return analyze(decIntegerExpressionContext);
        }
        else if (context instanceof HexIntegerExpressionContext hexIntegerExpressionContext) {
            return analyze(hexIntegerExpressionContext);
        }
        else if (context instanceof DecRealExpressionContext decRealExpressionContext) {
            return analyze(decRealExpressionContext);
        }
        else if (context instanceof HexRealExpressionContext hexRealExpressionContext) {
            return analyze(hexRealExpressionContext);
        }
        else if (context instanceof StringExpressionContext stringExpressionContext) {
            return analyze(stringExpressionContext);
        }
        else {
            throw new ClassCastException();
        }
    }

    private @NotNull DatExpressionNode analyze(@NotNull StringExpressionContext context) {
        return setSourceRange(DatObjectConstantNodeGen.create(parseStringLiteral(context.token)), context);
    }

    private @NotNull DatExpressionNode analyze(@NotNull TrueExpressionContext context) {
        return setSourceRange(DatTrueConstantNodeGen.create(), context);
    }

    private @NotNull DatExpressionNode analyze(@NotNull FalseExpressionContext context) {
        return setSourceRange(DatFalseConstantNodeGen.create(), context);
    }

    private @NotNull DatExpressionNode analyze(@NotNull BinIntegerExpressionContext context) {
        return setSourceRange(makeIntegerConstantNode(parseBinaryIntegerLiteral(context.token)), context);
    }

    private @NotNull DatExpressionNode analyze(@NotNull OctIntegerExpressionContext context) {
        return setSourceRange(makeIntegerConstantNode(parseOctalIntegerLiteral(context.token)), context);
    }

    private @NotNull DatExpressionNode analyze(@NotNull DecIntegerExpressionContext context) {
        return setSourceRange(makeIntegerConstantNode(parseDecimalIntegerLiteral(context.token)), context);
    }

    private @NotNull DatExpressionNode analyze(@NotNull HexIntegerExpressionContext context) {
        return setSourceRange(makeIntegerConstantNode(parseHexadecimalIntegerLiteral(context.token)), context);
    }

    private @NotNull DatExpressionNode analyze(@NotNull DecRealExpressionContext context) {
        return setSourceRange(DatDoubleConstantNodeGen.create(parseDecimalReal(context.token)), context);
    }

    private @NotNull DatExpressionNode analyze(@NotNull HexRealExpressionContext context) {
        return setSourceRange(DatDoubleConstantNodeGen.create(parseHexadecimalReal(context.token)), context);
    }

    private @NotNull DatExpressionNode makeIntegerConstantNode(@NotNull BigInteger value) {
        try {
            var longValue = value.longValueExact();
            return DatLongConstantNodeGen.create(longValue);
        }
        catch (ArithmeticException ignored) {
            return DatObjectConstantNodeGen.create(value);
        }
    }

    private @NotNull TruffleString parseStringLiteral(@NotNull Token token) {
        var text = token.getText();
        var contents = text.substring(1, text.length() - 1);

        var lexer = new DatStringLexer(CharStreams.fromString(contents));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener() {
            @Override public void syntaxError(
                Recognizer<?, ?> recognizer,
                Object offendingSymbol,
                int line,
                int charPositionInLine,
                String message,
                RecognitionException exception
            ) {
                throw new DatParseException(sourceSection(token), "Invalid string literal: " + message, exception);
            }
        });

        var builder = TruffleStringBuilder.create(TruffleString.Encoding.UTF_8);

        for (var subToken = lexer.nextToken(); token.getType() != Token.EOF; subToken = lexer.nextToken()) {
            switch (subToken.getType()) {
                case DatStringLexer.BellEscape -> {
                    builder.appendByteUncached((byte) 0x07);
                }

                case DatStringLexer.BackspaceEscape -> {
                    builder.appendByteUncached((byte) '\b');
                }

                case DatStringLexer.EscapeEscape -> {
                    builder.appendByteUncached((byte) 0x1b);
                }

                case DatStringLexer.FormFeedEscape -> {
                    builder.appendByteUncached((byte) '\f');
                }

                case DatStringLexer.NewlineEscape -> {
                    builder.appendByteUncached((byte) '\n');
                }

                case DatStringLexer.CarriageReturnEscape -> {
                    builder.appendByteUncached((byte) '\r');
                }

                case DatStringLexer.TabEscape -> {
                    builder.appendByteUncached((byte) '\t');
                }

                case DatStringLexer.VerticalTabEscape -> {
                    builder.appendByteUncached((byte) 0x0b);
                }

                case DatStringLexer.BackslashEscape -> {
                    builder.appendByteUncached((byte) '\\');
                }

                case DatStringLexer.DoubleQuoteEscape -> {
                    builder.appendByteUncached((byte) '"');
                }

                case DatStringLexer.OctalEscape -> {
                    var value = parseInt(subToken.getText().substring(1), 8);

                    if (value > 255) {
                        throw new DatParseException(sourceSection(token), "Octal escape code out of range", null);
                    }

                    builder.appendByteUncached((byte) value);
                }

                case DatStringLexer.HexEscape -> {
                    builder.appendByteUncached((byte) parseInt(subToken.getText().substring(2), 16));
                }

                case DatStringLexer.UnicodeEscape -> {
                    var value = parseInt(subToken.getText().substring(2), 16);

                    if (Character.isSurrogate((char) value)) {
                        throw new DatParseException(sourceSection(token), "\"\\u\" escapes cannot be surrogates", null);
                    }

                    builder.appendCodePointUncached(value);
                }

                case DatStringLexer.WideUnicodeEscape -> {
                    builder.appendCodePointUncached(parseUnsignedInt(subToken.getText().substring(2), 16));
                }

                case DatStringLexer.LiteralSegment -> {
                    builder.appendStringUncached(
                        TruffleString.fromJavaStringUncached(
                            subToken.getText(),
                            TruffleString.Encoding.UTF_8
                        )
                    );
                }

                default -> {
                    throw new AssertionError("Unexpected token type: " + token.getType());
                }
            }
        }

        return language.internedString(builder.toStringUncached());
    }

    private @NotNull BigInteger parseBinaryIntegerLiteral(@NotNull Token token) {
        var text = token.getText().replace("_", "").substring(2);
        var exponentMarkerIndex = ArrayUtils.indexOf(text, 0, text.length(), 'p', 'P');
        if (exponentMarkerIndex == -1) {
            return new BigInteger(text, 2);
        }
        else {
            var base = new BigInteger(text.substring(0, exponentMarkerIndex), 2);
            var exponent = parseInt(text.substring(exponentMarkerIndex + 1), 10);
            return base.shiftLeft(exponent);
        }
    }

    private @NotNull BigInteger parseOctalIntegerLiteral(@NotNull Token token) {
        var text = token.getText().replace("_", "").substring(2);
        var exponentMarkerIndex = ArrayUtils.indexOf(text, 0, text.length(), 'p', 'P');
        if (exponentMarkerIndex == -1) {
            return new BigInteger(text, 8);
        }
        else {
            var base = new BigInteger(text.substring(0, exponentMarkerIndex), 8);
            var exponent = parseInt(text.substring(exponentMarkerIndex + 1), 10);
            return base.shiftLeft(exponent);
        }
    }

    private @NotNull BigInteger parseDecimalIntegerLiteral(@NotNull Token token) {
        var text = token.getText().replace("_", "");
        var exponentMarkerIndex = ArrayUtils.indexOf(text, 0, text.length(), 'e', 'E');
        if (exponentMarkerIndex == -1) {
            return new BigInteger(text, 10);
        }
        else {
            var base = new BigInteger(text.substring(0, exponentMarkerIndex), 10);
            var exponent = parseInt(text.substring(exponentMarkerIndex + 1), 10);
            return base.multiply(BigInteger.TEN.pow(exponent));
        }
    }

    private @NotNull BigInteger parseHexadecimalIntegerLiteral(@NotNull Token token) {
        var text = token.getText().replace("_", "").substring(2);
        var exponentMarkerIndex = ArrayUtils.indexOf(text, 0, text.length(), 'p', 'P');
        if (exponentMarkerIndex == -1) {
            return new BigInteger(text, 16);
        }
        else {
            var base = new BigInteger(text.substring(0, exponentMarkerIndex), 16);
            var exponent = parseInt(text.substring(exponentMarkerIndex + 1), 10);
            return base.shiftLeft(exponent);
        }
    }

    private double parseDecimalReal(@NotNull Token token) {
        return Double.parseDouble(token.getText().replace("_", ""));
    }

    private double parseHexadecimalReal(@NotNull Token token) {
        return Double.parseDouble(token.getText().replace("_", ""));
    }

    @Contract("_, _, _ -> param1")
    private static <N extends DatProgramNode> @NotNull N setSourceRange(@NotNull N node, int start, int end) {
        node.setSourceRange(start, end);
        return node;
    }

    @Contract("_, _ -> param1")
    private static <N extends DatProgramNode> @NotNull N setSourceRange(
        @NotNull N node,
        @NotNull ParserRuleContext context
    ) {
        node.setSourceRange(sourceStart(context), sourceEnd(context));
        return node;
    }

    private static int sourceStart(@NotNull ParserRuleContext context) {
        return sourceStart(context.getStart());
    }

    private static int sourceEnd(@NotNull ParserRuleContext context) {
        return sourceEnd(context.getStop());
    }

    private static int sourceStart(@NotNull Token token) {
        return token.getStartIndex();
    }

    private static int sourceEnd(@NotNull Token token) {
        return token.getStopIndex() + 1;
    }

    private @NotNull SourceSection sourceSection(int line, int charPositionInLine) {
        return source.createSection(line, charPositionInLine, 0);
    }

    private @NotNull SourceSection sourceSection(@NotNull Token token) {
        var start = sourceStart(token);
        var end = sourceEnd(token);
        return source.createSection(start, end - start);
    }

    @Override public void syntaxError(
        Recognizer<?, ?> recognizer,
        Object offendingSymbol,
        int line,
        int charPositionInLine,
        String message,
        RecognitionException exception
    ) {
        recordError(new DatParseException(sourceSection(line, charPositionInLine), message, exception));
    }

    @Override public void reportAmbiguity(
        Parser recognizer,
        DFA dfa,
        int startIndex,
        int stopIndex,
        boolean exact,
        BitSet ambiguousAlternatives,
        ATNConfigSet configs
    ) {}

    @Override public void reportAttemptingFullContext(
        Parser recognizer,
        DFA dfa,
        int startIndex,
        int stopIndex,
        BitSet conflictingAlternatives,
        ATNConfigSet configs
    ) {}

    @Override public void reportContextSensitivity(
        Parser recognizer,
        DFA dfa,
        int startIndex,
        int stopIndex,
        int prediction,
        ATNConfigSet configs
    ) {}
}
