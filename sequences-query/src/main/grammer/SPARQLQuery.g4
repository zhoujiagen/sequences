grammar SPARQLQuery;
import SPARQLLex;


//[1]
gQueryUnit	  :  	gQuery;
//[2]
gQuery	  :  	gPrologue
                ( gSelectQuery | gConstructQuery | gDescribeQuery | gAskQuery )
                gValuesClause;
//[3]
gUpdateUnit	  :  	gUpdate;
//[4]
gPrologue	  :  	( gBaseDecl | gPrefixDecl )*;
//[5]
gBaseDecl	  :  	'BASE' IRIREF;
//[6]
gPrefixDecl	  :  	'PREFIX' PNAME_NS IRIREF;
//[7]
gSelectQuery	  :  	gSelectClause gDatasetClause* gWhereClause gSolutionModifier;
//[8]
gSubSelect	  :  	gSelectClause gWhereClause gSolutionModifier gValuesClause;
//[9]
gSelectClause	  :  	'SELECT' ( 'DISTINCT' | 'REDUCED' )? ( ( gVar | ( '(' gExpression 'AS' gVar ')' ) )+ | '*' );
//[10]
gConstructQuery	  :  	'CONSTRUCT' ( gConstructTemplate gDatasetClause* gWhereClause gSolutionModifier | gDatasetClause* 'WHERE' '{' gTriplesTemplate? '}' gSolutionModifier );
//[11]
gDescribeQuery	  :  	'DESCRIBE' ( gVarOrIri+ | '*' ) gDatasetClause* gWhereClause? gSolutionModifier;
//[12]
gAskQuery	  :  	'ASK' gDatasetClause* gWhereClause gSolutionModifier;
//[13]
gDatasetClause	  :  	'FROM' ( gDefaultGraphClause | gNamedGraphClause );
//[14]
gDefaultGraphClause	  :  	gSourceSelector;
//[15]
gNamedGraphClause	  :  	'NAMED' gSourceSelector;
//[16]
gSourceSelector	  :  	giri;
//[17]
gWhereClause	  :  	'WHERE'? gGroupGraphPattern;
//[18]
gSolutionModifier	  :  	gGroupClause? gHavingClause? gOrderClause? gLimitOffsetClauses?;
//[19]
gGroupClause	  :  	'GROUP' 'BY' gGroupCondition+;
//[20]
gGroupCondition	  :  	gBuiltInCall | gFunctionCall | '(' gExpression ( 'AS' gVar )? ')' | gVar;
//[21]
gHavingClause	  :  	'HAVING' gHavingCondition+;
//[22]
gHavingCondition	  :  	gConstraint;
//[23]
gOrderClause	  :  	'ORDER' 'BY' gOrderCondition+;
//[24]
gOrderCondition	  :  	( ( 'ASC' | 'DESC' ) gBrackettedExpression )
 | ( gConstraint | gVar );
//[25]
gLimitOffsetClauses	  :  	gLimitClause gOffsetClause? | gOffsetClause gLimitClause?;
//[26]
gLimitClause	  :  	'LIMIT' INTEGER;
//[27]
gOffsetClause	  :  	'OFFSET' INTEGER;
//[28]
gValuesClause	  :  	( 'VALUES' gDataBlock )?;
//[29]
gUpdate	  :  	gPrologue ( gUpdate1 ( ';' gUpdate )? )?;
//[30]
gUpdate1	  :  	gLoad | gClear | gDrop | gAdd | gMove | gCopy | gCreate | gInsertData | gDeleteData | gDeleteWhere | gModify;
//[31]
gLoad	  :  	'LOAD' 'SILENT'? giri ( 'INTO' gGraphRef )?;
//[32]
gClear	  :  	'CLEAR' 'SILENT'? gGraphRefAll;
//[33]
gDrop	  :  	'DROP' 'SILENT'? gGraphRefAll;
//[34]
gCreate	  :  	'CREATE' 'SILENT'? gGraphRef;
//[35]
gAdd	  :  	'ADD' 'SILENT'? gGraphOrDefault 'TO' gGraphOrDefault;
//[36]
gMove	  :  	'MOVE' 'SILENT'? gGraphOrDefault 'TO' gGraphOrDefault;
//[37]
gCopy	  :  	'COPY' 'SILENT'? gGraphOrDefault 'TO' gGraphOrDefault;
//[38]
gInsertData	  :  	'INSERT DATA' gQuadData;
//[39]
gDeleteData	  :  	'DELETE DATA' gQuadData;
//[40]
gDeleteWhere	  :  	'DELETE WHERE' gQuadPattern;
//[41]
gModify	  :  	( 'WITH' giri )? ( gDeleteClause gInsertClause? | gInsertClause ) gUsingClause* 'WHERE' gGroupGraphPattern;
//[42]
gDeleteClause	  :  	'DELETE' gQuadPattern;
//[43]
gInsertClause	  :  	'INSERT' gQuadPattern;
//[44]
gUsingClause	  :  	'USING' ( giri | 'NAMED' giri );
//[45]
gGraphOrDefault	  :  	'DEFAULT' | 'GRAPH'? giri;
//[46]
gGraphRef	  :  	'GRAPH' giri;
//[47]
gGraphRefAll	  :  	gGraphRef | 'DEFAULT' | 'NAMED' | 'ALL';
//[48]
gQuadPattern	  :  	'{' gQuads '}';
//[49]
gQuadData	  :  	'{' gQuads '}';
//[50]
gQuads	  :  	gTriplesTemplate? ( gQuadsNotTriples '.'? gTriplesTemplate? )*;
//[51]
gQuadsNotTriples	  :  	'GRAPH' gVarOrIri '{' gTriplesTemplate? '}';
//[52]
gTriplesTemplate	  :  	gTriplesSameSubject ( '.' gTriplesTemplate? )?;
//[53]
gGroupGraphPattern	  :  	'{' ( gSubSelect | gGroupGraphPatternSub ) '}';
//[54]
gGroupGraphPatternSub	  :  	gTriplesBlock? ( gGraphPatternNotTriples '.'? gTriplesBlock? )*;
//[55]
gTriplesBlock	  :  	gTriplesSameSubjectPath ( '.' gTriplesBlock? )?;
//[56]
gGraphPatternNotTriples	  :  	gGroupOrUnionGraphPattern | gOptionalGraphPattern | gMinusGraphPattern | gGraphGraphPattern | gServiceGraphPattern | gFilter | gBind | gInlineData;
//[57]
gOptionalGraphPattern	  :  	'OPTIONAL' gGroupGraphPattern;
//[58]
gGraphGraphPattern	  :  	'GRAPH' gVarOrIri gGroupGraphPattern;
//[59]
gServiceGraphPattern	  :  	'SERVICE' 'SILENT'? gVarOrIri gGroupGraphPattern;
//[60]
gBind	  :  	'BIND' '(' gExpression 'AS' gVar ')';
//[61]
gInlineData	  :  	'VALUES' gDataBlock;
//[62]
gDataBlock	  :  	gInlineDataOneVar | gInlineDataFull;
//[63]
gInlineDataOneVar	  :  	gVar '{' gDataBlockValue* '}';
//[64]
gInlineDataFull	  :  	( NIL | '(' gVar* ')' ) '{' ( '(' gDataBlockValue* ')' | NIL )* '}';
//[65]
gDataBlockValue	  :  	giri |	gRDFLiteral |	gNumericLiteral |	gBooleanLiteral |	'UNDEF';
//[66]
gMinusGraphPattern	  :  	'MINUS' gGroupGraphPattern;
//[67]
gGroupOrUnionGraphPattern	  :  	gGroupGraphPattern ( 'UNION' gGroupGraphPattern )*;
//[68]
gFilter	  :  	'FILTER' gConstraint;
//[69]
gConstraint	  :  	gBrackettedExpression | gBuiltInCall | gFunctionCall;
//[70]
gFunctionCall	  :  	giri gArgList;
//[71]
gArgList	  :  	NIL | '(' 'DISTINCT'? gExpression ( ',' gExpression )* ')';
//[72]
gExpressionList	  :  	NIL | '(' gExpression ( ',' gExpression )* ')';
//[73]
gConstructTemplate	  :  	'{' gConstructTriples? '}';
//[74]
gConstructTriples	  :  	gTriplesSameSubject ( '.' gConstructTriples? )?;
//[75]
gTriplesSameSubject	  :  	gVarOrTerm gPropertyListNotEmpty |	gTriplesNode gPropertyList;
//[76]
gPropertyList	  :  	gPropertyListNotEmpty?;
//[77]
gPropertyListNotEmpty	  :  	gVerb gObjectList ( ';' ( gVerb gObjectList )? )*;
//[78]
gVerb	  :  	gVarOrIri | 'a';
//[79]
gObjectList	  :  	gObject ( ',' gObject )*;
//[80]
gObject	  :  	gGraphNode;
//[81]
gTriplesSameSubjectPath	  :  	gVarOrTerm gPropertyListPathNotEmpty |	gTriplesNodePath gPropertyListPath;
//[82]
gPropertyListPath	  :  	gPropertyListPathNotEmpty?;
//[83]
gPropertyListPathNotEmpty	  :  	( gVerbPath | gVerbSimple ) gObjectListPath ( ';' ( ( gVerbPath | gVerbSimple ) gObjectList )? )*;
//[84]
gVerbPath	  :  	gPath;
//[85]
gVerbSimple	  :  	gVar;
//[86]
gObjectListPath	  :  	gObjectPath ( ',' gObjectPath )*;
//[87]
gObjectPath	  :  	gGraphNodePath;
//[88]
gPath	  :  	gPathAlternative;
//[89]
gPathAlternative	  :  	gPathSequence ( '|' gPathSequence )*;
//[90]
gPathSequence	  :  	gPathEltOrInverse ( '/' gPathEltOrInverse )*;
//[91]
gPathElt	  :  	gPathPrimary gPathMod?;
//[92]
gPathEltOrInverse	  :  	gPathElt | '^' gPathElt;
//[93]
gPathMod	  :  	'?' | '*' | '+';
//[94]
gPathPrimary	  :  	giri | 'a' | '!' gPathNegatedPropertySet | '(' gPath ')';
//[95]
gPathNegatedPropertySet	  :  	gPathOneInPropertySet | '(' ( gPathOneInPropertySet ( '|' gPathOneInPropertySet )* )? ')';
//[96]
gPathOneInPropertySet	  :  	giri | 'a' | '^' ( giri | 'a' );
//[97]
gInteger	  :  	INTEGER;
//[98]
gTriplesNode	  :  	gCollection |	gBlankNodePropertyList;
//[99]
gBlankNodePropertyList	  :  	'[' gPropertyListNotEmpty ']';
//[100]
gTriplesNodePath	  :  	gCollectionPath |	gBlankNodePropertyListPath;
//[101]
gBlankNodePropertyListPath	  :  	'[' gPropertyListPathNotEmpty ']';
//[102]
gCollection	  :  	'(' gGraphNode+ ')';
//[103]
gCollectionPath	  :  	'(' gGraphNodePath+ ')';
//[104]
gGraphNode	  :  	gVarOrTerm |	gTriplesNode;
//[105]
gGraphNodePath	  :  	gVarOrTerm |	gTriplesNodePath;
//[106]
gVarOrTerm	  :  	gVar | gGraphTerm;
//[107]
gVarOrIri	  :  	gVar | giri;
//[108]
gVar	  :  	VAR1 | VAR2;
//[109]
gGraphTerm	  :  	giri |	gRDFLiteral |	gNumericLiteral |	gBooleanLiteral |	gBlankNode |	NIL;
//[110]
gExpression	  :  	gConditionalOrExpression;
//[111]
gConditionalOrExpression	  :  	gConditionalAndExpression ( '||' gConditionalAndExpression )*;
//[112]
gConditionalAndExpression	  :  	gValueLogical ( '&&' gValueLogical )*;
//[113]
gValueLogical	  :  	gRelationalExpression;
//[114]
gRelationalExpression	  :  	gNumericExpression ( '=' gNumericExpression | '!=' gNumericExpression | '<' gNumericExpression | '>' gNumericExpression | '<=' gNumericExpression | '>=' gNumericExpression | 'IN' gExpressionList | 'NOT' 'IN' gExpressionList )?;
//[115]
gNumericExpression	  :  	gAdditiveExpression;
//[116]
gAdditiveExpression	  :  	gMultiplicativeExpression ( '+' gMultiplicativeExpression | '-' gMultiplicativeExpression | ( gNumericLiteralPositive | gNumericLiteralNegative ) ( ( '*' gUnaryExpression ) | ( '/' gUnaryExpression ) )* )*;
//[117]
gMultiplicativeExpression	  :  	gUnaryExpression ( '*' gUnaryExpression | '/' gUnaryExpression )*;
//[118]
gUnaryExpression	  :  	  '!' gPrimaryExpression
 |	'+' gPrimaryExpression
 |	'-' gPrimaryExpression
 |	gPrimaryExpression;
//[119]
gPrimaryExpression	  :  	gBrackettedExpression | gBuiltInCall | giriOrFunction | gRDFLiteral | gNumericLiteral | gBooleanLiteral | gVar;
//[120]
gBrackettedExpression	  :  	'(' gExpression ')';
//[121]
gBuiltInCall	  :  	  gAggregate
 |	'STR' '(' gExpression ')'
 |	'LANG' '(' gExpression ')'
 |	'LANGMATCHES' '(' gExpression ',' gExpression ')'
 |	'DATATYPE' '(' gExpression ')'
 |	'BOUND' '(' gVar ')'
 |	'IRI' '(' gExpression ')'
 |	'URI' '(' gExpression ')'
 |	'BNODE' ( '(' gExpression ')' | NIL )
 |	'RAND' NIL
 |	'ABS' '(' gExpression ')'
 |	'CEIL' '(' gExpression ')'
 |	'FLOOR' '(' gExpression ')'
 |	'ROUND' '(' gExpression ')'
 |	'CONCAT' gExpressionList
 |	gSubstringExpression
 |	'STRLEN' '(' gExpression ')'
 |	gStrReplaceExpression
 |	'UCASE' '(' gExpression ')'
 |	'LCASE' '(' gExpression ')'
 |	'ENCODE_FOR_URI' '(' gExpression ')'
 |	'CONTAINS' '(' gExpression ',' gExpression ')'
 |	'STRSTARTS' '(' gExpression ',' gExpression ')'
 |	'STRENDS' '(' gExpression ',' gExpression ')'
 |	'STRBEFORE' '(' gExpression ',' gExpression ')'
 |	'STRAFTER' '(' gExpression ',' gExpression ')'
 |	'YEAR' '(' gExpression ')'
 |	'MONTH' '(' gExpression ')'
 |	'DAY' '(' gExpression ')'
 |	'HOURS' '(' gExpression ')'
 |	'MINUTES' '(' gExpression ')'
 |	'SECONDS' '(' gExpression ')'
 |	'TIMEZONE' '(' gExpression ')'
 |	'TZ' '(' gExpression ')'
 |	'NOW' NIL
 |	'UUID' NIL
 |	'STRUUID' NIL
 |	'MD5' '(' gExpression ')'
 |	'SHA1' '(' gExpression ')'
 |	'SHA256' '(' gExpression ')'
 |	'SHA384' '(' gExpression ')'
 |	'SHA512' '(' gExpression ')'
 |	'COALESCE' gExpressionList
 |	'IF' '(' gExpression ',' gExpression ',' gExpression ')'
 |	'STRLANG' '(' gExpression ',' gExpression ')'
 |	'STRDT' '(' gExpression ',' gExpression ')'
 |	'sameTerm' '(' gExpression ',' gExpression ')'
 |	'isIRI' '(' gExpression ')'
 |	'isURI' '(' gExpression ')'
 |	'isBLANK' '(' gExpression ')'
 |	'isLITERAL' '(' gExpression ')'
 |	'isNUMERIC' '(' gExpression ')'
 |	gRegexExpression
 |	gExistsFunc
 |	gNotExistsFunc;
//[122]
gRegexExpression	  :  	'REGEX' '(' gExpression ',' gExpression ( ',' gExpression )? ')';
//[123]
gSubstringExpression	  :  	'SUBSTR' '(' gExpression ',' gExpression ( ',' gExpression )? ')';
//[124]
gStrReplaceExpression	  :  	'REPLACE' '(' gExpression ',' gExpression ',' gExpression ( ',' gExpression )? ')';
//[125]
gExistsFunc	  :  	'EXISTS' gGroupGraphPattern;
//[126]
gNotExistsFunc	  :  	'NOT' 'EXISTS' gGroupGraphPattern;
//[127]
gAggregate	  :  	  'COUNT' '(' 'DISTINCT'? ( '*' | gExpression ) ')'
| 'SUM' '(' 'DISTINCT'? gExpression ')'
| 'MIN' '(' 'DISTINCT'? gExpression ')'
| 'MAX' '(' 'DISTINCT'? gExpression ')'
| 'AVG' '(' 'DISTINCT'? gExpression ')'
| 'SAMPLE' '(' 'DISTINCT'? gExpression ')'
| 'GROUP_CONCAT' '(' 'DISTINCT'? gExpression ( ';' 'SEPARATOR' '=' gString )? ')';
//[128]
giriOrFunction	  :  	giri gArgList?;
//[129]
gRDFLiteral	  :  	gString ( LANGTAG | ( '^^' giri ) )?;
//[130]
gNumericLiteral	  :  	gNumericLiteralUnsigned | gNumericLiteralPositive | gNumericLiteralNegative;
//[131]
gNumericLiteralUnsigned	  :  	INTEGER |	DECIMAL |	DOUBLE;
//[132]
gNumericLiteralPositive	  :  	INTEGER_POSITIVE |	DECIMAL_POSITIVE |	DOUBLE_POSITIVE;
//[133]
gNumericLiteralNegative	  :  	INTEGER_NEGATIVE |	DECIMAL_NEGATIVE |	DOUBLE_NEGATIVE;
//[134]
gBooleanLiteral	  :  	'true' |	'false';
//[135]
gString	  :  	STRING_LITERAL1 | STRING_LITERAL2 | STRING_LITERAL_LONG1 | STRING_LITERAL_LONG2;
//[136]
giri	  :  	IRIREF |	gPrefixedName;
//[137]
gPrefixedName	  :  	PNAME_LN | PNAME_NS;
//[138]
gBlankNode	  :  	BLANK_NODE_LABEL |	ANON;