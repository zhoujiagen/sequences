grammar SPARQLQuery;

@header {
package com.spike.giantdataanalysis.sequences.query.antlr.sparql;
}
//---------------------------------------------------------------------------
// parser grammar
//---------------------------------------------------------------------------
//[1] 查询单元
gQueryUnit: gQuery;
//[2] 查询语句
gQuery: gPrologue
                ( gSelectQuery | gConstructQuery | gDescribeQuery | gAskQuery )
                gValuesClause;
//[3] 更新单元
gUpdateUnit: gUpdate;
//[4]
gPrologue: ( gBaseDecl | gPrefixDecl )*;
//[5]
gBaseDecl: K_BASE IRIREF;
//[6]
gPrefixDecl: K_PREFIX PNAME_NS IRIREF;
//[7]
gSelectQuery: gSelectClause gDatasetClause* gWhereClause gSolutionModifier;
//[8]
gSubSelect: gSelectClause gWhereClause gSolutionModifier gValuesClause;
//[9]
gSelectClause: K_SELECT ( K_DISTINCT | K_REDUCED )? ( ( gVar | ( '(' gExpression K_AS gVar ')' ) )+ | '*' );
//[10]
gConstructQuery: K_CONSTRUCT ( gConstructTemplate gDatasetClause* gWhereClause gSolutionModifier | gDatasetClause* K_WHERE '{' gTriplesTemplate? '}' gSolutionModifier );
//[11]
gDescribeQuery: K_DESCRIBE ( gVarOrIri+ | '*' ) gDatasetClause* gWhereClause? gSolutionModifier;
//[12]
gAskQuery: K_ASK gDatasetClause* gWhereClause gSolutionModifier;
//[13]
gDatasetClause: K_FROM ( gDefaultGraphClause | gNamedGraphClause );
//[14]
gDefaultGraphClause: gSourceSelector;
//[15]
gNamedGraphClause: K_NAMED gSourceSelector;
//[16]
gSourceSelector: giri;
//[17] WHERE子句
gWhereClause: K_WHERE? gGroupGraphPattern;
//[18]
gSolutionModifier: gGroupClause? gHavingClause? gOrderClause? gLimitOffsetClauses?;
//[19]
gGroupClause: K_GROUP K_BY gGroupCondition+;
//[20]
gGroupCondition: gBuiltInCall | gFunctionCall | '(' gExpression ( K_AS gVar )? ')' | gVar;
//[21]
gHavingClause: K_HAVING gHavingCondition+;
//[22]
gHavingCondition: gConstraint;
//[23]
gOrderClause: K_ORDER K_BY gOrderCondition+;
//[24]
gOrderCondition: ( ( K_ASC | K_DESC ) gBrackettedExpression )
 | ( gConstraint | gVar );
//[25]
gLimitOffsetClauses: gLimitClause gOffsetClause? | gOffsetClause gLimitClause?;
//[26]
gLimitClause: K_LIMIT INTEGER;
//[27]
gOffsetClause: K_OFFSET INTEGER;
//[28]
gValuesClause: ( K_VALUES gDataBlock )?;
//[29]
gUpdate: gPrologue ( gUpdate1 ( ';' gUpdate )? )?;
//[30]
gUpdate1: gLoad | gClear | gDrop | gAdd | gMove | gCopy | gCreate | gInsertData | gDeleteData | gDeleteWhere | gModify;
//[31]
gLoad: K_LOAD K_SILENT? giri ( K_INTO gGraphRef )?;
//[32]
gClear: K_CLEAR K_SILENT? gGraphRefAll;
//[33]
gDrop: K_DROP K_SILENT? gGraphRefAll;
//[34]
gCreate: K_CREATE K_SILENT? gGraphRef;
//[35]
gAdd: K_ADD K_SILENT? gGraphOrDefault K_TO gGraphOrDefault;
//[36]
gMove: K_MOVE K_SILENT? gGraphOrDefault K_TO gGraphOrDefault;
//[37]
gCopy: K_COPY K_SILENT? gGraphOrDefault K_TO gGraphOrDefault;
//[38]
gInsertData: K_INSERT K_DATA gQuadData;
//[39]
gDeleteData: K_DELETE K_DATA gQuadData;
//[40]
gDeleteWhere: K_DELETE K_WHERE gQuadPattern;
//[41]
gModify: ( K_WITH giri )? ( gDeleteClause gInsertClause? | gInsertClause ) gUsingClause* K_WHERE gGroupGraphPattern;
//[42] 删除子句
gDeleteClause: K_DELETE gQuadPattern;
//[43] 插入子句
gInsertClause: K_INSERT gQuadPattern;
//[44] 使用子句
gUsingClause: K_USING ( giri | K_NAMED giri );
//[45]
gGraphOrDefault: K_DEFAULT | K_GRAPH? giri;
//[46] 图引用
gGraphRef: K_GRAPH giri;
//[47]
gGraphRefAll: gGraphRef | K_DEFAULT | K_NAMED | K_ALL;
//[48]
gQuadPattern: '{' gQuads '}';
//[49]
gQuadData: '{' gQuads '}';
//[50]
gQuads: gTriplesTemplate? ( gQuadsNotTriples '.'? gTriplesTemplate? )*;
//[51]
gQuadsNotTriples: K_GRAPH gVarOrIri '{' gTriplesTemplate? '}';
//[52]
gTriplesTemplate: gTriplesSameSubject ( '.' gTriplesTemplate? )?;
//[53] 分组图模式
gGroupGraphPattern: '{' ( gSubSelect | gGroupGraphPatternSub ) '}';
//[54] 子分组图模式
gGroupGraphPatternSub: gTriplesBlock? ( gGraphPatternNotTriples '.'? gTriplesBlock? )*;
//[55] 三元组块
gTriplesBlock: gTriplesSameSubjectPath ( '.' gTriplesBlock? )?;
//[56] 非三元组的图模式
gGraphPatternNotTriples: gGroupOrUnionGraphPattern
    | gOptionalGraphPattern
    | gMinusGraphPattern
    | gGraphGraphPattern
    | gServiceGraphPattern
    | gFilter
    | gBind
    | gInlineData;
//[57]
gOptionalGraphPattern: K_OPTIONAL gGroupGraphPattern;
//[58]
gGraphGraphPattern: K_GRAPH gVarOrIri gGroupGraphPattern;
//[59]
gServiceGraphPattern: K_SERVICE K_SILENT? gVarOrIri gGroupGraphPattern;
//[60]
gBind: K_BIND '(' gExpression K_AS gVar ')';
//[61]
gInlineData: K_VALUES gDataBlock;
//[62]
gDataBlock: gInlineDataOneVar | gInlineDataFull;
//[63]
gInlineDataOneVar: gVar '{' gDataBlockValue* '}';
//[64]
gInlineDataFull: ( NIL | '(' gVar* ')' ) '{' ( '(' gDataBlockValue* ')' | NIL )* '}';
//[65]
gDataBlockValue: giri |	gRDFLiteral |	gNumericLiteral |	gBooleanLiteral |	K_UNDEF;
//[66]
gMinusGraphPattern: K_MINUS gGroupGraphPattern;
//[67]
gGroupOrUnionGraphPattern: gGroupGraphPattern ( K_UNION gGroupGraphPattern )*;
//[68] 过滤FILTER
gFilter: K_FILTER gConstraint;
//[69] 约束: 带括号的表达式, 内建调用, 函数调用
gConstraint: gBrackettedExpression | gBuiltInCall | gFunctionCall;
//[70] 函数调用: IRI 参数列表
gFunctionCall: giri gArgList;
//[71] 参数列表
gArgList: NIL | '(' K_DISTINCT? gExpression ( ',' gExpression )* ')';
//[72]
gExpressionList: NIL | '(' gExpression ( ',' gExpression )* ')';
//[73]
gConstructTemplate: '{' gConstructTriples? '}';
//[74]
gConstructTriples: gTriplesSameSubject ( '.' gConstructTriples? )?;
//[75]
gTriplesSameSubject: gVarOrTerm gPropertyListNotEmpty |	gTriplesNode gPropertyList;
//[76]
gPropertyList: gPropertyListNotEmpty?;
//[77]
gPropertyListNotEmpty: gVerb gObjectList ( ';' ( gVerb gObjectList )? )*;
//[78]
gVerb: gVarOrIri | K_A;
//[79]
gObjectList: gObject ( ',' gObject )*;
//[80]
gObject: gGraphNode;
//[81] 同一Subject的三元组路径
gTriplesSameSubjectPath: gVarOrTerm gPropertyListPathNotEmpty |	gTriplesNodePath gPropertyListPath;
//[82]
gPropertyListPath: gPropertyListPathNotEmpty?;
//[83]
gPropertyListPathNotEmpty: ( gVerbPath | gVerbSimple ) gObjectListPath ( ';' ( ( gVerbPath | gVerbSimple ) gObjectList )? )*;
//[84]
gVerbPath: gPath;
//[85]
gVerbSimple: gVar;
//[86]
gObjectListPath: gObjectPath ( ',' gObjectPath )*;
//[87]
gObjectPath: gGraphNodePath;
//[88]
gPath: gPathAlternative;
//[89]
gPathAlternative: gPathSequence ( '|' gPathSequence )*;
//[90]
gPathSequence: gPathEltOrInverse ( '/' gPathEltOrInverse )*;
//[91]
gPathElt: gPathPrimary gPathMod?;
//[92]
gPathEltOrInverse: gPathElt | '^' gPathElt;
//[93]
gPathMod: '?' | '*' | '+';
//[94]
gPathPrimary: giri | K_A | '!' gPathNegatedPropertySet | '(' gPath ')';
//[95]
gPathNegatedPropertySet: gPathOneInPropertySet | '(' ( gPathOneInPropertySet ( '|' gPathOneInPropertySet )* )? ')';
//[96]
gPathOneInPropertySet: giri | K_A | '^' ( giri | K_A );
//[97]
gInteger: INTEGER;
//[98]
gTriplesNode: gCollection |	gBlankNodePropertyList;
//[99]
gBlankNodePropertyList: '[' gPropertyListNotEmpty ']';
//[100]
gTriplesNodePath: gCollectionPath |	gBlankNodePropertyListPath;
//[101]
gBlankNodePropertyListPath: '[' gPropertyListPathNotEmpty ']';
//[102]
gCollection: '(' gGraphNode+ ')';
//[103]
gCollectionPath: '(' gGraphNodePath+ ')';
//[104]
gGraphNode: gVarOrTerm |	gTriplesNode;
//[105]
gGraphNodePath: gVarOrTerm |	gTriplesNodePath;
//[106] 变量或项
gVarOrTerm: gVar | gGraphTerm;
//[107] 变量或IRI
gVarOrIri: gVar | giri;
//[108] 变量: $var, ?var
gVar: VAR1 | VAR2;
//[109] 图项: IRI, RDF字面量, 数值字面量, 布尔字面量, 空节点, ()
gGraphTerm: giri |	gRDFLiteral |	gNumericLiteral |	gBooleanLiteral |	gBlankNode |	NIL;
//[110] 表达式
gExpression: gConditionalOrExpression;
//[111] 或条件表达式
gConditionalOrExpression: gConditionalAndExpression ( '||' gConditionalAndExpression )*;
//[112] 与条件表达式
gConditionalAndExpression: gValueLogical ( '&&' gValueLogical )*;
//[113] 值逻辑
gValueLogical: gRelationalExpression;
//[114] 关系表达式
gRelationalExpression: gNumericExpression ( '=' gNumericExpression
                                            | '!=' gNumericExpression
                                            | '<' gNumericExpression
                                            | '>' gNumericExpression
                                            | '<=' gNumericExpression
                                            | '>=' gNumericExpression
                                            | 'IN' gExpressionList
                                            | 'NOT' 'IN' gExpressionList )?;
//[115] 数值表达式
gNumericExpression: gAdditiveExpression;
//[116] 可加数值表达式
gAdditiveExpression: gMultiplicativeExpression ( '+' gMultiplicativeExpression
                                            | '-' gMultiplicativeExpression
                                            | ( gNumericLiteralPositive
                                            | gNumericLiteralNegative ) ( ( '*' gUnaryExpression ) | ( '/' gUnaryExpression ) )* )*;
//[117] 可乘数值表达式
gMultiplicativeExpression: gUnaryExpression ( '*' gUnaryExpression | '/' gUnaryExpression )*;
//[118] 一元表达式
gUnaryExpression: '!' gPrimaryExpression
	|	'+' gPrimaryExpression
	|	'-' gPrimaryExpression
	|	gPrimaryExpression;
//[119] 原始表达式
gPrimaryExpression: gBrackettedExpression | gBuiltInCall | giriOrFunction | gRDFLiteral | gNumericLiteral | gBooleanLiteral | gVar;
//[120] 带括号的表达式
gBrackettedExpression: '(' gExpression ')';
//[121] 内建调用
gBuiltInCall: gAggregate
	|	K_STR '(' gExpression ')'
	|	K_LANG '(' gExpression ')'
	|	K_LANGMATCHES '(' gExpression ',' gExpression ')'
	|	K_DATATYPE '(' gExpression ')'
	|	K_BOUND '(' gVar ')'
	|	K_IRI '(' gExpression ')'
	|	K_URI '(' gExpression ')'
	|	K_BNODE ( '(' gExpression ')' | NIL )
	|	K_RAND NIL
	|	K_ABS '(' gExpression ')'
	|	K_CEIL '(' gExpression ')'
	|	K_FLOOR '(' gExpression ')'
	|	K_ROUND '(' gExpression ')'
	|	K_CONCAT gExpressionList
	|	gSubstringExpression
	|	K_STRLEN '(' gExpression ')'
	|	gStrReplaceExpression
	|	K_UCASE '(' gExpression ')'
	|	K_LCASE '(' gExpression ')'
	|	K_ENCODE_FOR_URI '(' gExpression ')'
	|	K_CONTAINS '(' gExpression ',' gExpression ')'
	|	K_STRSTARTS '(' gExpression ',' gExpression ')'
	|	K_STRENDS '(' gExpression ',' gExpression ')'
	|	K_STRBEFORE '(' gExpression ',' gExpression ')'
	|	K_STRAFTER '(' gExpression ',' gExpression ')'
	|	K_YEAR '(' gExpression ')'
	|	K_MONTH '(' gExpression ')'
	|	K_DAY '(' gExpression ')'
	|	K_HOURS '(' gExpression ')'
	|	K_MINUTES '(' gExpression ')'
	|	K_SECONDS '(' gExpression ')'
	|	K_TIMEZONE '(' gExpression ')'
	|	K_TZ '(' gExpression ')'
	|	K_NOW NIL
	|	K_UUID NIL
	|	K_STRUUID NIL
	|	K_MD5 '(' gExpression ')'
	|	K_SHA1 '(' gExpression ')'
	|	K_SHA256 '(' gExpression ')'
	|	K_SHA384 '(' gExpression ')'
	|	K_SHA512 '(' gExpression ')'
	|	K_COALESCE gExpressionList
	|	K_IF '(' gExpression ',' gExpression ',' gExpression ')'
	|	K_STRLANG '(' gExpression ',' gExpression ')'
	|	K_STRDT '(' gExpression ',' gExpression ')'
	|	K_sameTerm '(' gExpression ',' gExpression ')'
	|	K_isIRI '(' gExpression ')'
	|	K_isURI '(' gExpression ')'
	|	K_isBLANK '(' gExpression ')'
	|	K_isLITERAL '(' gExpression ')'
	|	K_isNUMERIC '(' gExpression ')'
	|	gRegexExpression
	|	gExistsFunc
	|	gNotExistsFunc;
//[122] 正则表达式
gRegexExpression: K_REGEX '(' gExpression ',' gExpression ( ',' gExpression )? ')';
//[123] 字符串子串SUBSTR函数
gSubstringExpression: K_SUBSTR '(' gExpression ',' gExpression ( ',' gExpression )? ')';
//[124] 字符串替换REPLACE函数
gStrReplaceExpression: K_REPLACE '(' gExpression ',' gExpression ',' gExpression ( ',' gExpression )? ')';
//[125] EXIST函数
gExistsFunc: K_EXISTS gGroupGraphPattern;
//[126] NOT EXIST函数
gNotExistsFunc: K_NOT K_EXISTS gGroupGraphPattern;
//[127] 内建聚合调用: COUNT, SUM, MIN, MAX. AVG, SAMPLE, GROUP CONCAT
gAggregate: K_COUNT '(' K_DISTINCT? ( '*' | gExpression ) ')'
    | K_SUM '(' K_DISTINCT? gExpression ')'
    | K_MIN '(' K_DISTINCT? gExpression ')'
    | K_MAX '(' K_DISTINCT? gExpression ')'
    | K_AVG '(' K_DISTINCT? gExpression ')'
    | K_SAMPLE '(' K_DISTINCT? gExpression ')'
    | K_GROUP_CONCAT '(' K_DISTINCT? gExpression ( ';' K_SEPARATOR '=' gString )? ')';
//[128]
giriOrFunction: giri gArgList?;
//[129]
gRDFLiteral: gString ( LANGTAG | ( '^^' giri ) )?;
//[130]
gNumericLiteral: gNumericLiteralUnsigned | gNumericLiteralPositive | gNumericLiteralNegative;
//[131]
gNumericLiteralUnsigned: INTEGER |	DECIMAL |	DOUBLE;
//[132]
gNumericLiteralPositive: INTEGER_POSITIVE |	DECIMAL_POSITIVE |	DOUBLE_POSITIVE;
//[133]
gNumericLiteralNegative: INTEGER_NEGATIVE |	DECIMAL_NEGATIVE |	DOUBLE_NEGATIVE;
//[134]
gBooleanLiteral: K_true |	K_false;
//[135] 字符串: 添加空字符串规则gBlankString和词法规则NON_SKIP_WS
gString: STRING_LITERAL1 | STRING_LITERAL2 | STRING_LITERAL_LONG1 | STRING_LITERAL_LONG2 | gBlankString;
gBlankString: '"' NON_SKIP_WS* '"' | '\'' NON_SKIP_WS* '\'';
//[136] IRI: IRI引用, 带前缀的名字(例foaf:knows)
giri: IRIREF |	gPrefixedName;
//[137]
gPrefixedName: PNAME_LN | PNAME_NS;
//[138]
gBlankNode: BLANK_NODE_LABEL |	ANON;


//---------------------------------------------------------------------------
// lexer grammar
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
// KEYWORDS
// use 'CaseChangingCharStream'
// REF: https://github.com/antlr/antlr4/blob/master/doc/case-insensitive-lexing.md
//---------------------------------------------------------------------------
K_ABS: 'ABS' ;
K_ADD: 'ADD' ;
K_ALL: 'ALL' ;
K_AS: 'AS' ;
K_ASC: 'ASC' ;
K_ASK: 'ASK' ;
K_AVG: 'AVG' ;
K_BASE: 'BASE' ;
K_BIND: 'BIND' ;
K_BNODE: 'BNODE' ;
K_BOUND: 'BOUND' ;
K_BY: 'BY' ;
K_CEIL: 'CEIL' ;
K_CLEAR: 'CLEAR' ;
K_COALESCE: 'COALESCE' ;
K_CONCAT: 'CONCAT' ;
K_CONSTRUCT: 'CONSTRUCT' ;
K_CONTAINS: 'CONTAINS' ;
K_COPY: 'COPY' ;
K_COUNT: 'COUNT' ;
K_CREATE: 'CREATE' ;
K_DATATYPE: 'DATATYPE' ;
K_DAY: 'DAY' ;
K_DEFAULT: 'DEFAULT' ;
K_DELETE: 'DELETE' ;
K_DESC: 'DESC' ;
K_DESCRIBE: 'DESCRIBE' ;
K_DISTINCT: 'DISTINCT' ;
K_DROP: 'DROP' ;
K_ENCODE_FOR_URI: 'ENCODE_FOR_URI' ;
K_EXISTS: 'EXISTS' ;
K_FILTER: 'FILTER' ;
K_FLOOR: 'FLOOR' ;
K_FROM: 'FROM' ;
K_GRAPH: 'GRAPH' ;
K_GROUP: 'GROUP' ;
K_GROUP_CONCAT: 'GROUP_CONCAT' ;
K_HAVING: 'HAVING' ;
K_HOURS: 'HOURS' ;
K_IF: 'IF' ;
K_IN: 'IN' ;
K_INSERT: 'INSERT' ;
K_INTO: 'INTO' ;
K_IRI: 'IRI' ;
K_LANG: 'LANG' ;
K_LANGMATCHES: 'LANGMATCHES' ;
K_LCASE: 'LCASE' ;
K_LIMIT: 'LIMIT' ;
K_LOAD: 'LOAD' ;
K_MAX: 'MAX' ;
K_MD5: 'MD5' ;
K_MIN: 'MIN' ;
K_MINUS: 'MINUS' ;
K_MINUTES: 'MINUTES' ;
K_MONTH: 'MONTH' ;
K_MOVE: 'MOVE' ;
K_NAMED: 'NAMED' ;
K_NOT: 'NOT' ;
K_NOW: 'NOW' ;
K_OFFSET: 'OFFSET' ;
K_OPTIONAL: 'OPTIONAL' ;
K_ORDER: 'ORDER' ;
K_PREFIX: 'PREFIX' ;
K_RAND: 'RAND' ;
K_REDUCED: 'REDUCED' ;
K_REGEX: 'REGEX' ;
K_REPLACE: 'REPLACE' ;
K_ROUND: 'ROUND' ;
K_SAMPLE: 'SAMPLE' ;
K_SECONDS: 'SECONDS' ;
K_SELECT: 'SELECT' ;
K_SEPARATOR: 'SEPARATOR' ;
K_SERVICE: 'SERVICE' ;
K_SHA1: 'SHA1' ;
K_SHA256: 'SHA256' ;
K_SHA384: 'SHA384' ;
K_SHA512: 'SHA512' ;
K_SILENT: 'SILENT' ;
K_STR: 'STR' ;
K_STRAFTER: 'STRAFTER' ;
K_STRBEFORE: 'STRBEFORE' ;
K_STRDT: 'STRDT' ;
K_STRENDS: 'STRENDS' ;
K_STRLANG: 'STRLANG' ;
K_STRLEN: 'STRLEN' ;
K_STRSTARTS: 'STRSTARTS' ;
K_STRUUID: 'STRUUID' ;
K_SUBSTR: 'SUBSTR' ;
K_SUM: 'SUM' ;
K_TIMEZONE: 'TIMEZONE' ;
K_TO: 'TO' ;
K_TZ: 'TZ' ;
K_UCASE: 'UCASE' ;
K_UNDEF: 'UNDEF' ;
K_UNION: 'UNION' ;
K_URI: 'URI' ;
K_USING: 'USING' ;
K_UUID: 'UUID' ;
K_VALUES: 'VALUES' ;
K_WHERE: 'WHERE' ;
K_WITH: 'WITH' ;
K_YEAR: 'YEAR' ;
K_A: 'A' ;
K_false: 'FALSE' ;
K_isBLANK: 'ISBLANK' ;
K_isIRI: 'ISIRI' ;
K_isLITERAL: 'ISLITERAL' ;
K_isNUMERIC: 'ISNUMERIC' ;
K_isURI: 'ISURI' ;
K_sameTerm: 'SAMETERM' ;
K_true: 'TRUE' ;
K_DATA: 'DATA';

//[139]
//IRIREF::=  	'<' ([^<>"{}|^`\]-[#x00-#x20])* '>'
//IRIREF: '<' (~([[<>"{}|`\\\]] | '^') |'-'| [0-9a-zA-Z#/:] | '.')* '>';
IRIREF: '<' (~([[<>"{}|`\\\]] | '^') |'-'| [0-9a-zA-Z#/:] | '.')* '>';
//[140]
PNAME_NS: PN_PREFIX? ':';
//[141]
PNAME_LN: PNAME_NS PN_LOCAL;
//[142]
BLANK_NODE_LABEL: '_:' ( PN_CHARS_U | [0-9] ) ((PN_CHARS|'.')* PN_CHARS)?;
//[143]
VAR1: '?' VARNAME;
//[144]
VAR2: '$' VARNAME;
//[145]
LANGTAG: '@' [a-zA-Z]+ ('-' [a-zA-Z0-9]+)*;
//[146]
INTEGER: [0-9]+;
//[147]
DECIMAL: [0-9]* '.' [0-9]+;
//[148]
DOUBLE: [0-9]+ '.' [0-9]* EXPONENT | '.' ([0-9])+ EXPONENT | ([0-9])+ EXPONENT;
//[149]
INTEGER_POSITIVE: '+' INTEGER;
//[150]
DECIMAL_POSITIVE: '+' DECIMAL;
//[151]
DOUBLE_POSITIVE: '+' DOUBLE;
//[152]
INTEGER_NEGATIVE: '-' INTEGER;
//[153]
DECIMAL_NEGATIVE: '-' DECIMAL;
//[154]
DOUBLE_NEGATIVE: '-' DOUBLE;
//[155]
EXPONENT: [eE] [+-]? [0-9]+;
//[156]
//STRING_LITERAL1::=  	"'" ( ([^#x27#x5C#xA#xD]) | ECHAR )* "'"
STRING_LITERAL1: '\'' ( [a-zA-Z_^] | ECHAR )* '\'';
//[157]
//	STRING_LITERAL2::=  	'"' ( ([^#x22#x5C#xA#xD]) | ECHAR )* '"'
STRING_LITERAL2: '"' ( [a-zA-Z_^] | ECHAR )* '"';
//[158]
//STRING_LITERAL_LONG1::=  	"'''" ( ( "'" | "''" )? ( [^'\] | ECHAR ) )* "'''"
STRING_LITERAL_LONG1: '\'\'\'' ( ( '\'' | '\'\'' )? ( [^'\\] | ECHAR ) )* '\'\'\'';
//[159]
STRING_LITERAL_LONG2: '"""' ( ( '"' | '""' )? ( [^"\\] | ECHAR ) )* '"""';
//[160] escape character
//ECHAR::=  	'\' [tbnrf\"']
ECHAR: '\\' [tbnrf\\"'];
//[161]
NIL: '(' WS* ')';
//[162]
//WS::=  	#x20 | #x9 | #xD | #xA
WS: [ \r\t\n]+ -> skip;
NON_SKIP_WS: [ \r\t\n]+;
//[163]
ANON: '[' WS* ']';
//[164]
//PN_CHARS_BASE::=  	[A-Z] | [a-z] | [#x00C0-#x00D6] | [#x00D8-#x00F6] | [#x00F8-#x02FF] | [#x0370-#x037D] | [#x037F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
PN_CHARS_BASE: [A-Z] | [a-z];
//[165]
PN_CHARS_U: PN_CHARS_BASE | '_';
//[166]
//VARNAME::=  	( PN_CHARS_U | [0-9] ) ( PN_CHARS_U | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040] )*
VARNAME: ( PN_CHARS_U | [0-9] ) ( PN_CHARS_U | [0-9] )*;
//[167]
//PN_CHARS::=  	PN_CHARS_U | '-' | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040]
PN_CHARS: PN_CHARS_U | '-' | [0-9];
//[168]
PN_PREFIX: PN_CHARS_BASE ((PN_CHARS|'.')* PN_CHARS)?;
//[169]
PN_LOCAL: (PN_CHARS_U | ':' | [0-9] | PLX ) ((PN_CHARS | '.' | ':' | PLX)* (PN_CHARS | ':' | PLX) )?;
//[170]
PLX: PERCENT | PN_LOCAL_ESC;
//[171]
PERCENT: '%' HEX HEX;
//[172]
HEX: [0-9] | [A-F] | [a-f];
//[173]
PN_LOCAL_ESC: '\\' ( '_' | '~' | '.' | '-' | '!' | '$' | '&' | '\'' | '(' | ')' | '*' | '+' | ',' | ';' | '=' | '/' | '?' | '#' | '@' | '%' );



