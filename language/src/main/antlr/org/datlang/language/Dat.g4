grammar Dat;

@header {
package org.datlang.language;
}

file:
    (topLevelItem ';;'?)* EOF;

topLevelItem:
    'import' moduleName=UpperIdentifier #TopLevelImport
  | visibility=('fileprivate' | 'export')? 'def' name=LowerIdentifier (args+=pattern)* '=' body=expression #TopLevelFunction
  | visibility=('fileprivate' | 'export')? 'def' lhs=pattern '=' rhs=expression #TopLevelBinding
  | expression #TopLevelExpression
;

pattern:
    token=LowerIdentifier #BindingPattern
  | '_' #WildcardPattern
  | 'true' #TruePattern
  | 'false' #FalsePattern
  | token=BinNumber #BinNumberPattern
  | token=OctNumber #OctNumberPattern
  | token=DecNumber #DecNumberPattern
  | token=HexNumber #HexNumberPattern
  | token=String #StringPattern
  | name=UpperIdentifier ('(' ')')? #SymbolPattern
  | '(' ')' #UnitPattern
  | '(' inner=pattern ')' #ParenthesizedPattern
  | tag=UpperIdentifier? '(' elements+=pattern (',' elements+=pattern)* ','? ')' #TuplePattern
  | tag=UpperIdentifier? '(' keys+=LowerIdentifier ':' values+=pattern (',' keys+=LowerIdentifier ':' values+=pattern)* (',' ellipses='...')? ','? ')' #RecordPattern
  | name=LowerIdentifier '@' inner=pattern #DualBindingPattern
  | lhs=pattern '|' rhs=pattern #AlternativePattern
  | base=pattern 'where' condition=expression #GuardedPattern
;

expression:
    (moduleName=UpperIdentifier '.')? name=LowerIdentifier #BindingReferenceExpression
  | name=UpperIdentifier ('(' ')')? #SymbolExpression
  | token=BinNumber #BinNumberExpression
  | token=OctNumber #OctNumberExpression
  | token=DecNumber #DecNumberExpression
  | token=HexNumber #HexNumberExpression
  | token=String #StringExpression
  | '(' ')' #UnitExpression
  | '(' inner=expression ')' #ParenthesizedExpression
  | tag=UpperIdentifier? '(' elements+=expression (',' elements+=expression)* ','? ')' #TupleExpression
  | tag=UpperIdentifier? '(' keys+=LowerIdentifier ':' values+=expression (',' keys+=LowerIdentifier ':' values+=expression)* ','? ')' #RecordExpression
  | '[' (elements+=expression (',' elements+=expression)* ','?)? ']' #ListExpression
  | '{' (elements+=expression (',' elements+=expression)* ','?)? '}' #SetExpression
  | '{' (keys+=expression ':' values+=expression (',' keys+=expression ':' values+=expression)* ','?)? '}' #MapExpression
  | 'true' #TrueExpression
  | 'false' #FalseExpression
  | callee=expression (arguments+=expression)+ #CallExpression
  | op=('!' | '~' | '+' | '-') operand=expression #PrefixOpExpression
  | <assoc=right> lhs=expression '**' rhs=expression #PowExpression
  | lhs=expression op=('*' | '/' | '%') rhs=expression #MulDivRemExpression
  | lhs=expression op=('+' | '-') rhs=expression #AddSubExpression
  | lhs=expression op=('<<' | '>>' | '>>>') rhs=expression #BitShiftExpression
  | lhs=expression op=('<' | '<=' | '>' | '>=' | 'in') rhs=expression #RelationalExpression
  | lhs=expression op=('==' | '!=' | '===' | '!==') rhs=expression #EqualityExpression
  | lhs=expression '&' rhs=expression #BitwiseAndExpression
  | lhs=expression '^' rhs=expression #BitwiseXorExpression
  | lhs=expression '|' rhs=expression #BitwiseOrExpression
  | lhs=expression '&&' rhs=expression #LogicalAndExpression
  | lhs=expression '||' rhs=expression #LogicalOrExpression
  | 'break' '#' target=LowerIdentifier value=expression #BreakExpression
  | steps+=expression (';' steps+=expression)+ ';'? #SequenceExpression
  | kind=('if' | 'unless') condition=expression 'then' consequent=expression ('else' alternate=expression)? #ConditionalExpression
  | kind=('while' | 'until') condition=expression 'do' body=expression #PreCheckedLoopExpression
  | 'repeat' body=expression kind=('while' | 'until') condition=expression #PostCheckedLoopExpression
  | 'begin' body=expression 'end' #BeginEndExpression
  | label=LowerIdentifier '#' body=expression #LabeledExpression
;

KwBegin: 'begin';
KwBreak: 'break';
KwDef: 'def';
KwDo: 'do';
KwElse: 'else';
KwEnd: 'end';
KwExport: 'export';
KwFalse: 'false';
KwFileprivate: 'fileprivate';
KwIf: 'if';
KwImport: 'import';
KwIn: 'in';
KwLet: 'let';
KwRec: 'rec';
KwRepeat: 'repeat';
KwThen: 'then';
KwTrue: 'true';
KwUnless: 'unless';
KwUntil: 'until';
KwWhere: 'where';
KwWhile: 'while';

Ampersand2: '&&';
Ampersand: '&';
At: '@';
Bang: '!';
BangEquals2: '!==';
BangEquals: '!=';
Caret: '^';
Colon: ':';
Comma: ',';
Dot3: '...';
Dot: '.';
Equals2: '==';
Equals3: '===';
Equals: '=';
Hash: '#';
LAngle2: '<<';
LAngle: '<';
LAngleEquals: '<=';
LCurly: '{';
LParen: '(';
LSquare: '[';
Minus: '-';
Percent: '%';
Pipe2: '||';
Pipe: '|';
Plus: '+';
RAngle2: '>>';
RAngle3: '>>>';
RAngle: '>';
RAngleEquals: '>=';
RCurly: '}';
RParen: ')';
RSquare: ']';
Semicolon2: ';;';
Semicolon: ';';
Slash: '/';
Star2: '**';
Star: '*';
Tilde: '~';
Underscore: '_';

String:
    '"' StringToken+ '"';

BinNumber:
    '0' [bB] BinDigit+ BinExponent?;

OctNumber:
    '0' [oO] OctDigit+ BinExponent?;

DecNumber:
    ('.' DecDigit+ | DecDigit+ ('.' DecDigit*)?) DecExponent?;

HexNumber:
    '0' [xX] ('.' HexDigit+ | HexDigit+ ('.' HexDigit*)?) BinExponent?;

LowerIdentifier:
    [\p{Ll}] IdentifierRest*;

UpperIdentifier:
    [\p{Lt}\p{Lu}] IdentifierRest*;

Whitespace:
    [\p{White_Space}] -> skip;

fragment BinDigit:
    [0-1];

fragment OctDigit:
    [0-7];

fragment DecDigit:
    [0-9];

fragment HexDigit:
    [0-9a-fA-F];

fragment BinExponent:
    [pP] [+\-]? DecDigit+;

fragment DecExponent:
    [eE] [+\-]? DecDigit+;

fragment IdentifierRest:
    [\p{Ll}\p{Lm}\p{Lo}\p{Lt}\p{Lu}\p{Pc}\p{Sc}\p{Mc}\p{Me}\p{Mn}\p{Nd}\p{Nl}\p{No}];

fragment StringToken:
    [^\\"] | '\\' (Whitespace+ | .);
