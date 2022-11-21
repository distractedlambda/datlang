package org.datlang.language.compiler;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import org.antlr.v4.runtime.ANTLRErrorListener;
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
import org.datlang.language.compiler.DatParser.ExpressionContext;
import org.datlang.language.compiler.DatParser.FalseExpressionContext;
import org.datlang.language.compiler.DatParser.FileContext;
import org.datlang.language.compiler.DatParser.TopLevelBindingContext;
import org.datlang.language.compiler.DatParser.TopLevelFunctionContext;
import org.datlang.language.compiler.DatParser.TopLevelImportContext;
import org.datlang.language.compiler.DatParser.TrueExpressionContext;
import org.datlang.language.nodes.DatExpressionNode;
import org.datlang.language.nodes.DatFalseConstantNodeGen;
import org.datlang.language.nodes.DatProgramNode;
import org.datlang.language.nodes.DatTrueConstantNodeGen;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.Set;

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
        else {
            throw new ClassCastException();
        }
    }

    private @NotNull DatExpressionNode analyze(@NotNull TrueExpressionContext context) {
        return setSourceRange(DatTrueConstantNodeGen.create(), context);
    }

    private @NotNull DatExpressionNode analyze(@NotNull FalseExpressionContext context) {
        return setSourceRange(DatFalseConstantNodeGen.create(), context);
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
