grammar Dat;

@header {
package org.datlang.language;
}

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
  | name=UpperIdentifier ('(' ')')? #SingletonTermPattern
  | '(' ')' #UnitPattern
  | '(' inner=pattern ')' #ParenthesizedPattern
  | lhs=pattern '|' rhs=pattern #AlternativePattern
  | base=pattern 'where' condition=expression #GuardedPattern
  | 'in' set=expression #MembershipPattern
;

expression:
    token=LowerIdentifier #LocalExpression
  | name=UpperIdentifier ('(' ')')? #SingletonTermExpression
  | '(' ')' #UnitExpression
  | token=BinNumber #BinNumberExpression
  | token=OctNumber #OctNumberExpression
  | token=DecNumber #DecNumberExpression
  | token=HexNumber #HexNumberExpression
  | token=String #StringExpression
  | 'break' ':' target=LowerIdentifier (value=expression)? #BreakExpression
  | name=LowerIdentifier ':' body=expression #LabeledExpression
  | 'true' #TrueExpression
  | 'false' #FalseExpression
  | '(' inner=expression ')' #ParenthesizedExpression
  | owner=expression '.' member=LowerIdentifier #NamedMemberExpression
  | owner=expression '[' key=expression ']' #IndexedMemberExpression
  | op=('!' | '~' | '+' | '-') operand=expression #PrefixOpExpression
  | <assoc=right> lhs=expression '**' rhs=expression #PowExpression
  | lhs=expression op=('*' | '/' | '%') rhs=expression #MulDivRemExpression
  | lhs=expression op=('+' | '-') rhs=expression #AddSubExpression
  | kind=('if' | 'unless') condition=expression 'then' consequent=expression ('else' alternate=expression)? #ConditionalExpression
  | kind=('while' | 'until') condition=expression 'do' body=expression #PreCheckedLoopExpression
  | 'repeat' body=expression kind=('while' | 'until') condition=expression #PostCheckedLoopExpression
  | steps+=expression (';' steps+=expression)+ ';'? #SequenceExpression
;

KwDo: 'do';
KwElse: 'else';
KwFalse: 'false';
KwIf: 'if';
KwIn: 'in';
KwRepeat: 'repeat';
KwThen: 'then';
KwTrue: 'true';
KwUnless: 'unless';
KwUntil: 'until';
KwWhere: 'where';
KwWhile: 'while';

Bang: '!';
Dot: '.';
LParen: '(';
LSquare: '[';
Minus: '-';
Percent: '%';
Pipe: '|';
Plus: '+';
RParen: ')';
RSquare: ']';
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
