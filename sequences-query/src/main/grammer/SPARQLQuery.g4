grammar SPARQLQuery;

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
gFilter: ('FILTER'| 'filter' | K_FILTER) gConstraint;
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


// KEYWORDS
K_ABS: A B S  WS*;
K_ADD: A D D  WS*;
K_ALL: A L L  WS*;
K_AS: A S  WS*;
K_ASC: A S C  WS*;
K_ASK: A S K  WS*;
K_AVG: A V G  WS*;
K_BASE: B A S E  WS*;
K_BIND: B I N D  WS*;
K_BNODE: B N O D E  WS*;
K_BOUND: B O U N D  WS*;
K_BY: B Y  WS*;
K_CEIL: C E I L  WS*;
K_CLEAR: C L E A R  WS*;
K_COALESCE: C O A L E S C E  WS*;
K_CONCAT: C O N C A T  WS*;
K_CONSTRUCT: C O N S T R U C T  WS*;
K_CONTAINS: C O N T A I N S  WS*;
K_COPY: C O P Y  WS*;
K_COUNT: C O U N T  WS*;
K_CREATE: C R E A T E  WS*;
K_DATATYPE: D A T A T Y P E  WS*;
K_DAY: D A Y  WS*;
K_DEFAULT: D E F A U L T  WS*;
K_DELETE: D E L E T E  WS*;
K_DESC: D E S C  WS*;
K_DESCRIBE: D E S C R I B E  WS*;
K_DISTINCT: D I S T I N C T  WS*;
K_DROP: D R O P  WS*;
K_ENCODE_FOR_URI: E N C O D E '_' F O R '_' U R I  WS*;
K_EXISTS: E X I S T S  WS*;
K_FILTER: F I L T E R  WS*;
K_FLOOR: F L O O R  WS*;
K_FROM: F R O M  WS*;
K_GRAPH: G R A P H  WS*;
K_GROUP: G R O U P  WS*;
K_GROUP_CONCAT: G R O U P '_' C O N C A T  WS*;
K_HAVING: H A V I N G  WS*;
K_HOURS: H O U R S  WS*;
K_IF: I F  WS*;
K_IN: I N  WS*;
K_INSERT: I N S E R T  WS*;
K_INTO: I N T O  WS*;
K_IRI: I R I  WS*;
K_LANG: L A N G  WS*;
K_LANGMATCHES: L A N G M A T C H E S  WS*;
K_LCASE: L C A S E  WS*;
K_LIMIT: L I M I T  WS*;
K_LOAD: L O A D  WS*;
K_MAX: M A X  WS*;
K_MD5: M D '5'  WS*;
K_MIN: M I N  WS*;
K_MINUS: M I N U S  WS*;
K_MINUTES: M I N U T E S  WS*;
K_MONTH: M O N T H  WS*;
K_MOVE: M O V E  WS*;
K_NAMED: N A M E D  WS*;
K_NOT: N O T  WS*;
K_NOW: N O W  WS*;
K_OFFSET: O F F S E T  WS*;
K_OPTIONAL: O P T I O N A L  WS*;
K_ORDER: O R D E R  WS*;
K_PREFIX: P R E F I X  WS*;
K_RAND: R A N D  WS*;
K_REDUCED: R E D U C E D  WS*;
K_REGEX: R E G E X  WS*;
K_REPLACE: R E P L A C E  WS*;
K_ROUND: R O U N D  WS*;
K_SAMPLE: S A M P L E  WS*;
K_SECONDS: S E C O N D S  WS*;
K_SELECT: S E L E C T  WS*;
K_SEPARATOR: S E P A R A T O R  WS*;
K_SERVICE: S E R V I C E  WS*;
K_SHA1: S H A '1'  WS*;
K_SHA256: S H A '256'  WS*;
K_SHA384: S H A '384'  WS*;
K_SHA512: S H A '512'  WS*;
K_SILENT: S I L E N T  WS*;
K_STR: S T R  WS*;
K_STRAFTER: S T R A F T E R  WS*;
K_STRBEFORE: S T R B E F O R E  WS*;
K_STRDT: S T R D T  WS*;
K_STRENDS: S T R E N D S  WS*;
K_STRLANG: S T R L A N G  WS*;
K_STRLEN: S T R L E N  WS*;
K_STRSTARTS: S T R S T A R T S  WS*;
K_STRUUID: S T R U U I D  WS*;
K_SUBSTR: S U B S T R  WS*;
K_SUM: S U M  WS*;
K_TIMEZONE: T I M E Z O N E  WS*;
K_TO: T O  WS*;
K_TZ: T Z  WS*;
K_UCASE: U C A S E  WS*;
K_UNDEF: U N D E F  WS*;
K_UNION: U N I O N  WS*;
K_URI: U R I  WS*;
K_USING: U S I N G  WS*;
K_UUID: U U I D  WS*;
K_VALUES: V A L U E S  WS*;
K_WHERE: W H E R E  WS*;
K_WITH: W I T H  WS*;
K_YEAR: Y E A R  WS*;
K_A: A  WS*;
K_false: F A L S E  WS*;
K_isBLANK: I S B L A N K  WS*;
K_isIRI: I S I R I  WS*;
K_isLITERAL: I S L I T E R A L  WS*;
K_isNUMERIC: I S N U M E R I C  WS*;
K_isURI: I S U R I  WS*;
K_sameTerm: S A M E T E R M  WS*;
K_true: T R U E  WS*;
K_DATA: D A T A;


// Case-Insensitive Lexing: https://github.com/antlr/antlr4/blob/master/doc/case-insensitive-lexing.md
fragment A: [aA]; // match either an 'a' or 'A'
fragment B: [bB];
fragment C: [cC];
fragment D: [dD];
fragment E: [eE];
fragment F: [fF];
fragment G: [gG];
fragment H: [hH];
fragment I: [iI];
fragment J: [jJ];
fragment K: [kK];
fragment L: [lL];
fragment M: [mM];
fragment N: [nN];
fragment O: [oO];
fragment P: [pP];
fragment Q: [qQ];
fragment R: [rR];
fragment S: [sS];
fragment T: [tT];
fragment U: [uU];
fragment V: [vV];
fragment W: [wW];
fragment X: [xX];
fragment Y: [yY];
fragment Z: [zZ];
