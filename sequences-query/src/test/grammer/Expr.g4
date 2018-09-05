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
	| '(' expr ')' ;

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