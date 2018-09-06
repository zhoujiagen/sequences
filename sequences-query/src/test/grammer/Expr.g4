grammar Expr ;

@header {
package com.spike.giantdataanalysis.sequences.query.antlr;
}

prog
	: (expr NEWLINE)* ;

expr
	: expr ('*'|'/') expr
	| expr ('+'|'-') expr
	| INT
	| '(' expr ')'
	| K_SELECT K_UPDATE
	| K_FILTER expr;

NEWLINE
	: [\r\n]+ ;

INT
	: [0-9]+ ;

// [160]  	ECHAR	  ::=  	'\' [tbnrf\"']
ECHAR	  :  	'\\' [tbnrf\\"'];
//'\\' [tbnrf'\\"''];

UNSERSCORE: '_';
//[139]
//IRIREF	  ::=  	'<' ([^<>"{}|^`\]-[#x00-#x20])* '>'
IRIREF	  :  	'<' ([^[<>"{}|`\]-])* '>';
TESTIRIREF: ~[[\]];


K_FILTER: F I L T E R WS?;
K_SELECT: S E L E C T;
K_UPDATE : U P D A T E;


WS: [ \r\t\n]+ -> skip;

fragment A : [aA]; // match either an 'a' or 'A'
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];