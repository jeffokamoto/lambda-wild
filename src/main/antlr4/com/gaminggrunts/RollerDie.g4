grammar RollerDie;

// Parser

// Can't start with a leading '-' (nor a '+')
command :
    noModifierTerm ( dieList+=modifierTerm )* # DiceCommand
    ;

modifierTerm :
    mod noModifierTerm ;

noModifierTerm :
    ( dieSpec | num ) ;

// Allow both "D<m>" and "<n>D<m>"
dieSpec :
    num? dieType num ;

// Add additional die types here
dieType :
    ( die | wild ) ;

wild : WILD ;
die : DIE ;
num : NUM ;
mod : MOD ;

// Lexer

WILD : [Ww];
DIE : [Dd];
NUM : [0-9]+;
MOD : [+-];

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
COMMENT : ('#' ~('\r' | '\n')* '\r'? '\n') -> skip; // Comments