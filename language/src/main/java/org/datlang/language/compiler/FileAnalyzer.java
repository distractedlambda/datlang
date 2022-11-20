package org.datlang.language.compiler;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.datlang.language.DatParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;

public final class FileAnalyzer implements ANTLRErrorListener {
    private final @NotNull Source source;

    private @Nullable DatParseException error;

    public FileAnalyzer(@NotNull Source source) {
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
            error = new DatParseException(source.createUnavailableSection(), null, exception);
        } else {
            error.addSuppressed(exception);
        }
    }

    private void analyze(@NotNull DatParser.FileContext fileContext) {

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
