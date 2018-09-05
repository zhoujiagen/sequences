lexer grammar SPARQLLex;

//[139]
//IRIREF	  ::=  	'<' ([^<>"{}|^`\]-[#x00-#x20])* '>'
//IRIREF  :  	'<' ([^[<>"{}|`\\] |'-'| [0-9a-zA-Z#/:] | '.')* '>';
IRIREF  :  	'<' ([^[<>"{}|`\\\]-])* '>';
//[140]
PNAME_NS	  :  	PN_PREFIX? WS* ':';
//[141]
PNAME_LN	  :  	PNAME_NS PN_LOCAL;
//[142]
BLANK_NODE_LABEL	  :  	'_:' ( PN_CHARS_U | [0-9] ) ((PN_CHARS|'.')* PN_CHARS)?;
//[143]
VAR1	  :  	'?' VARNAME;
//[144]
VAR2	  :  	'$' VARNAME;
//[145]
LANGTAG	  :  	'@' [a-zA-Z]+ ('-' [a-zA-Z0-9]+)*;
//[146]
INTEGER	  :  	[0-9]+;
//[147]
DECIMAL	  :  	[0-9]* '.' [0-9]+;
//[148]
DOUBLE	  :  	[0-9]+ '.' [0-9]* EXPONENT | '.' ([0-9])+ EXPONENT | ([0-9])+ EXPONENT;
//[149]
INTEGER_POSITIVE	  :  	'+' INTEGER;
//[150]
DECIMAL_POSITIVE	  :  	'+' DECIMAL;
//[151]
DOUBLE_POSITIVE	  :  	'+' DOUBLE;
//[152]
INTEGER_NEGATIVE	  :  	'-' INTEGER;
//[153]
DECIMAL_NEGATIVE	  :  	'-' DECIMAL;
//[154]
DOUBLE_NEGATIVE	  :  	'-' DOUBLE;
//[155]
EXPONENT	  :  	[eE] [+-]? [0-9]+;
//[156]
//STRING_LITERAL1	  ::=  	"'" ( ([^#x27#x5C#xA#xD]) | ECHAR )* "'"
STRING_LITERAL1	  :  	'\'' ( ECHAR )* '\'';
//[157]
//	STRING_LITERAL2	  ::=  	'"' ( ([^#x22#x5C#xA#xD]) | ECHAR )* '"'
STRING_LITERAL2	  :  	'"' ( ECHAR )* '"';
//[158]
//STRING_LITERAL_LONG1	  ::=  	"'''" ( ( "'" | "''" )? ( [^'\] | ECHAR ) )* "'''"
STRING_LITERAL_LONG1	  :  	'\'\'\'' ( ( '\'' | '\'\'' )? ( [^'\\] | ECHAR ) )* '\'\'\'';
//[159]
STRING_LITERAL_LONG2	  :  	'"""' ( ( '"' | '""' )? ( [^"\\] | ECHAR ) )* '"""';
//[160]
//ECHAR	  ::=  	'\' [tbnrf\"']
ECHAR	  :  	'\\' [tbnrf\\"'];
//[161]
NIL	  :  	'(' WS* ')';
//[162]
//WS	  ::=  	#x20 | #x9 | #xD | #xA
WS	  :  	[ \r\t\n]+ -> skip;
//[163]
ANON	  :  	'[' WS* ']';
//[164]
//PN_CHARS_BASE	  ::=  	[A-Z] | [a-z] | [#x00C0-#x00D6] | [#x00D8-#x00F6] | [#x00F8-#x02FF] | [#x0370-#x037D] | [#x037F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
PN_CHARS_BASE	  :  	[A-Z] | [a-z];
//[165]
PN_CHARS_U	  :  	PN_CHARS_BASE | '_';
//[166]
//VARNAME	  ::=  	( PN_CHARS_U | [0-9] ) ( PN_CHARS_U | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040] )*
VARNAME	  :  	( PN_CHARS_U | [0-9] ) ( PN_CHARS_U | [0-9] )*;
//[167]
//PN_CHARS	  ::=  	PN_CHARS_U | '-' | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040]
PN_CHARS	  :  	PN_CHARS_U | '-' | [0-9];
//[168]
PN_PREFIX	  :  	PN_CHARS_BASE ((PN_CHARS|'.')* PN_CHARS)?;
//[169]
PN_LOCAL	  :  	(PN_CHARS_U | ':' | [0-9] | PLX ) ((PN_CHARS | '.' | ':' | PLX)* (PN_CHARS | ':' | PLX) )?;
//[170]
PLX	  :  	PERCENT | PN_LOCAL_ESC;
//[171]
PERCENT	  :  	'%' HEX HEX;
//[172]
HEX	  :  	[0-9] | [A-F] | [a-f];
//[173]
PN_LOCAL_ESC	  :  	'\\' ( '_' | '~' | '.' | '-' | '!' | '$' | '&' | '\'' | '(' | ')' | '*' | '+' | ',' | ';' | '=' | '/' | '?' | '#' | '@' | '%' );
