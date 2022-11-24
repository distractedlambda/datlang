grammar Dat;

@header {
package org.datlang.language.compiler;
}

file:
    (items+=topLevelItem ';;')* EOF;

topLevelItem:
    'import' moduleName=UpperIdentifier #TopLevelImport
  | visibility=('fileprivate' | 'export')? 'def' binding=functionBinding #TopLevelFunction
  | visibility=('fileprivate' | 'export')? 'def' lhs=pattern '=' rhs=expression #TopLevelBinding
;

pattern:
    token=LowerIdentifier #BindingPattern
  | '_' #WildcardPattern
  | constant=simpleConstant #SimpleConstantPattern
  | '(' inner=pattern ')' #ParenthesizedPattern
  | tag=(UpperIdentifier | ModulePrivateUpperIdentifer | FilePrivateUpperIdentifier)? '(' elements+=pattern (',' elements+=pattern)* ','? ')' #TuplePattern
  | tag=(UpperIdentifier | ModulePrivateUpperIdentifer | FilePrivateUpperIdentifier)? '(' keys+=LowerIdentifier ':' values+=pattern (',' keys+=LowerIdentifier ':' values+=pattern)* (',' ellipses='...')? ','? ')' #RecordPattern
  | name=LowerIdentifier '@' inner=pattern #DualBindingPattern
  | lhs=pattern '|' rhs=pattern #AlternativePattern
  | base=pattern 'where' condition=expression #GuardedPattern
;

expression:
    (moduleName=UpperIdentifier '.')? name=LowerIdentifier #BindingReferenceExpression
  | constant=simpleConstant #SimpleConstantExpression
  | '(' inner=expression ')' #ParenthesizedExpression
  | tag=(UpperIdentifier | ModulePrivateUpperIdentifer | FilePrivateUpperIdentifier)? '(' elements+=expression (',' elements+=expression)* ','? ')' #TupleExpression
  | tag=(UpperIdentifier | ModulePrivateUpperIdentifer | FilePrivateUpperIdentifier)? '(' keys+=LowerIdentifier ':' values+=expression (',' keys+=LowerIdentifier ':' values+=expression)* ','? ')' #RecordExpression
  | '[' (elements+=expression (',' elements+=expression)* ','?)? ']' #ListExpression
  | '{' (elements+=expression (',' elements+=expression)* ','?)? '}' #SetExpression
  | '{' (keys+=expression ':' values+=expression (',' keys+=expression ':' values+=expression)* ','?)? '}' #MapExpression
  | (moduleName=UpperIdentifier '.')? name=LowerIdentifier (arguments+=expression)+ #NamedFunctionCallExpression
  | '(' callee=expression ')' (arguments+=expression)+ #ComputedFunctionCallExpression
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
  | 'let' lhs=pattern '=' rhs=expression 'in' body=expression #ValueBindingExpression
  | 'let' binding=functionBinding 'in' body=expression #FunctionBindingExpression
  | 'let' 'rec' bindings+=functionBinding ('and' bindings+=functionBinding)* 'in' body=expression #RecursiveFunctionBindingExpression
  | 'match' subject=expression '|' patterns+=pattern '->' bodies+=expression ('|' patterns+=pattern '->' bodies+=expression)* #MatchExpression
  | label=LowerIdentifier '#' body=expression #LabeledExpression
  | '\\' (parameters+=pattern)+ '->' body=expression #LambdaExpression
  | '\\' '|' patterns+=pattern '->' bodies+=expression ('|' patterns+=pattern '->' bodies+=expression)* #MatchLambdaExpression
;

simpleConstant:
    token=(UpperIdentifier | ModulePrivateUpperIdentifer | FilePrivateUpperIdentifier) ('(' ')')?
  | token=BinInteger
  | token=ImaginaryBinInteger
  | token=OctInteger
  | token=ImaginaryOctInteger
  | token=DecInteger
  | token=ImaginaryDecInteger
  | token=DecFloat
  | token=ImaginaryDecFloat
  | token=HexInteger
  | token=ImaginaryHexInteger
  | token=HexFloat
  | token=ImaginaryHexFloat
  | token=String
  | token='true'
  | token='false'
  | '(' ')'
;

functionBinding:
    name=LowerIdentifier (args+=pattern)+ '=' body=expression;

KwAnd: 'and';
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
KwMatch: 'match';
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
Backslash: '\\';
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
MinusRAngle: '->';
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

ImaginaryBinInteger:
    BinInteger 'i';

ImaginaryOctInteger:
    OctInteger 'i';

ImaginaryDecInteger:
    DecInteger 'i';

ImaginaryDecFloat:
    DecFloat 'i';

ImaginaryHexInteger:
    HexInteger 'i';

ImaginaryHexFloat:
    HexFloat 'i';

BinInteger:
    '0' [bB] BinDigit ('_' BinDigit)* ([pP] '+'? DecDigit ('_' DecDigit)*)?;

OctInteger:
    '0' [oO] OctDigit ('_' OctDigit)* ([pP] '+'? DecDigit ('_' OctDigit)*)?;

DecInteger:
    DecDigit ('_' DecDigit)* ([eE] '+'? DecDigit ('_' DecDigit)*)?;

DecFloat:
    ('.' DecDigit ('_' DecDigit)* | DecDigit ('_' DecDigit)* '.' (DecDigit ('_' DecDigit)*)?) ([eE] [+\-]? DecDigit ('_' DecDigit)*)?;

HexInteger:
    '0' [xX] HexDigit ('_' HexDigit)* ([pP] '+'? DecDigit ('_' DecDigit)*)?;

HexFloat:
    '0' [xX] ('.' HexDigit ('_' HexDigit)* | HexDigit ('_' HexDigit)* '.' (HexDigit ('_' HexDigit)*)?) ([pP] [+\-]? DecDigit ('_' DecDigit)*)?;

LowerIdentifier:
    [\p{Ll}] IdentifierRest*;

ModulePrivateUpperIdentifer:
    '#' UpperIdentifier;

FilePrivateUpperIdentifier:
    '##' UpperIdentifier;

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
