lexer grammar DatStringLexer;

@header {
package org.datlang.language.compiler;
}

BellEscape:
    '\\a';

BackspaceEscape:
    '\\b';

EscapeEscape:
    '\\e';

FormFeedEscape:
    '\\f';

NewlineEscape:
    '\\n';

CarriageReturnEscape:
    '\\r';

TabEscape:
    '\\t';

VerticalTabEscape:
    '\\v';

BackslashEscape:
    '\\\\';

DoubleQuoteEscape:
    '\\"';

WhitespaceSkippingEscape:
    '\\' [\p{White_Space}]+ -> skip;

OctalEscape:
    '\\' OctalDigit OctalDigit OctalDigit;

HexEscape:
    '\\x' HexDigit HexDigit;

UnicodeEscape:
    '\\u' HexDigit HexDigit HexDigit HexDigit;

WideUnicodeEscape:
    '\\U' HexDigit HexDigit HexDigit HexDigit HexDigit HexDigit HexDigit HexDigit;

LiteralSegment:
    [^\\"\p{Cc}\p{Cf}\p{Zl}\p{Zp}]+;

fragment OctalDigit:
    [0-7];

fragment HexDigit:
    [0-9a-fA-F];
