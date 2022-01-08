grammar RollerDie;

// Parser

command :
    noModifierTerm ( dieList+=modifierTerm )* # DiceCommand
    ;

modifierTerm :
    mod noModifierTerm ;

noModifierTerm :
    ( dieSpec | num ) ;

dieSpec :
    num? dieType num ;

dieType :
    ( die | wild ) ;

wild : WILD ;
die : DIE ;
num : NUM ;
mod : MOD ;
// dieSize : NUM ;

// Lexer

WILD : [Ww];
DIE : [Dd];
NUM : [0-9]+;
MOD : [+-];

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
COMMENT : ('#' ~('\r' | '\n')* '\r'? '\n') -> skip; // Comments