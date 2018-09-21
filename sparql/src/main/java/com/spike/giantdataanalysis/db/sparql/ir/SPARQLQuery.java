package com.spike.giantdataanalysis.db.sparql.ir;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.collect.Lists;

// @formatter:off
public interface SPARQLQuery {

// grammar SPARQLQuery;
//
//// @header {
//// package com.spike.giantdataanalysis.db.query.antlr.sparql;
//// }
//// ---------------------------------------------------------------------------
//// parser grammar
//// ---------------------------------------------------------------------------
//// [1] 查询单元
// gQueryUnit: gQuery;
class QueryUnit {
  public Query gQuery;
}
//// [2] 查询语句
// gQuery: gPrologue ( gSelectQuery | gConstructQuery | gDescribeQuery | gAskQuery ) gValuesClause;
class Query {
  public Prologue gPrologue;
  public QueryType type;
  public SelectQuery gSelectQuery;
  public ConstructQuery gConstructQuery;
  public DescribeQuery gDescribeQuery;
  public AskQuery gAskQuery;
  public ValuesClause gValuesClause;
}
enum QueryType {
  gSelectQuery, gConstructQuery, gDescribeQuery, gAskQuery
}
//// [3] 更新单元
// gUpdateUnit: gUpdate;
class UpdateUnit {
  public Update gUpdate;
}
//// [4] 序言: BASE, PREFIX
// gPrologue: ( gBaseDecl | gPrefixDecl )*;
class Prologue {
  public List<String> gBaseDecls = Lists.newArrayList();
  public List<PrefixDecl> gPrefixDecls = Lists.newArrayList();
}
//// [5] BASE声明
// gBaseDecl: K_BASE IRIREF;
//// [6] PREFIX声明
// gPrefixDecl: K_PREFIX PNAME_NS IRIREF;
class PrefixDecl {
  public String PNAME_NS;
  public String IRIREF;
}
//// [7] SELECT查询
// gSelectQuery: gSelectClause gDatasetClause* gWhereClause gSolutionModifier;
class SelectQuery {
  public SelectClause gSelectClause;
  public List<DatasetClause> gDatasetClauses = Lists.newArrayList();
  public WhereClause gWhereClause;
  public SolutionModifier gSolutionModifier;
}
//// [8] 子SELECT
// gSubSelect: gSelectClause gWhereClause gSolutionModifier gValuesClause;
class SubSelect {
  public SelectClause gSelectClause;
  public WhereClause gWhereClause;
  public SolutionModifier gSolutionModifier;
  public ValuesClause gValuesClause;
}
//// [9] SELECT子句
// gSelectClause: K_SELECT ( K_DISTINCT | K_REDUCED )? ( ( gVar | ( '(' gExpression K_AS gVar ')' ) )+ | '*' );
class SelectClause {
  public boolean distinct = false;
  public boolean reduced = false;
  public boolean allgVar = false; // *
  public List<Var> gVars = Lists.newArrayList();
  public List<VarBind> gVarBinds = Lists.newArrayList();
}

class VarBind {
  public Expression gExpression;
  public Var gVar;
}
//// [10] CONSTRUCT查询
// gConstructQuery: K_CONSTRUCT ( gConstructTemplate gDatasetClause* gWhereClause gSolutionModifier | gDatasetClause* K_WHERE '{' gTriplesTemplate? '}' gSolutionModifier );
class ConstructQuery {
  public boolean useConstructTemplate = false;
  public ConstructTemplate gConstructTemplate;
  public List<DatasetClause> gDatasetClauses = Lists.newArrayList();
  public WhereClause gWhereClause;
  public SolutionModifier gSolutionModifier;
  
  public TriplesTemplate gTriplesTemplate;
}
//// [11] DESCRIBE查询
// gDescribeQuery: K_DESCRIBE ( gVarOrIri+ | '*' ) gDatasetClause* gWhereClause? gSolutionModifier;
class DescribeQuery {
  public boolean all = false;
  public List<VarOrIri> gVarOrIris = Lists.newArrayList();
  public List<DatasetClause> gDatasetClauses = Lists.newArrayList();
  public WhereClause gWhereClause;
  public SolutionModifier gSolutionModifier;
}
//// [12] ASK查询
// gAskQuery: K_ASK gDatasetClause* gWhereClause gSolutionModifier;
class AskQuery {
  public List<DatasetClause> gDatasetClauses = Lists.newArrayList();
  public WhereClause gWhereClause;
  public SolutionModifier gSolutionModifier;
}
//// [13] 数据集子句
// gDatasetClause: K_FROM ( gDefaultGraphClause | gNamedGraphClause );
class DatasetClause {
  public DefaultGraphClause gDefaultGraphClause;
  public NamedGraphClause gNamedGraphClause;
}
//// [14] 默认图子句
// gDefaultGraphClause: gSourceSelector;
class DefaultGraphClause {
  public SourceSelector gSourceSelector;
}
//// [15] 命名图子句
// gNamedGraphClause: K_NAMED gSourceSelector;
class NamedGraphClause {
  public SourceSelector gSourceSelector;
}
//// [16]
// gSourceSelector: giri;
class SourceSelector {
  public iri giri;
}
//// [17] WHERE子句
// gWhereClause: K_WHERE? gGroupGraphPattern;
class WhereClause {
  public GroupGraphPattern gGroupGraphPattern;
}
//// [18]
// gSolutionModifier: gGroupClause? gHavingClause? gOrderClause? gLimitOffsetClauses?;
class SolutionModifier {
  public GroupClause gGroupClause;
  public HavingClause gHavingClause;
  public OrderClause gOrderClause;
  public LimitOffsetClauses gLimitOffsetClauses;
}
//// [19] GRROUP BY子句
// gGroupClause: K_GROUP K_BY gGroupCondition+;
class GroupClause {
  public List<GroupCondition> gGroupConditions = Lists.newArrayList();
}
//// [20] GROUP BY条件
// gGroupCondition: gBuiltInCall | gFunctionCall | '(' gExpression ( K_AS gVar )? ')' | gVar;
class GroupCondition {
  public BuiltInCall gBuiltInCall;
  public FunctionCall gFunctionCall;
  public Expression gExpression;
  public Var gVar;
}
//// [21] HAVING子句
// gHavingClause: K_HAVING gHavingCondition+;
class HavingClause {
  public List<HavingCondition> gHavingConditions = Lists.newArrayList();
}
//// [22] HAVING条件
// gHavingCondition: gConstraint;
class HavingCondition {
  public Constraint gConstraint;
}
//// [23] ORDER BY子句
// gOrderClause: K_ORDER K_BY gOrderCondition+;
class OrderClause {
  public List<OrderCondition> gOrderConditions = Lists.newArrayList();
}
//// [24] ORDER BY条件
// gOrderCondition: ( ( K_ASC | K_DESC ) gBrackettedExpression ) | ( gConstraint | gVar );
class OrderCondition {
  public boolean isAsc;
  public BrackettedExpression gBrackettedExpression;
  public Constraint gConstraint;
  public Var gVar;
}
//// [25] LIMIT/OFFSET子句
// gLimitOffsetClauses: gLimitClause gOffsetClause? | gOffsetClause gLimitClause?;
class LimitOffsetClauses {
  // do not care the order
  public LimitClause gLimitClause;
  public OffsetClause gOffsetClause;
}
//// [26] LIMIT子句
// gLimitClause: K_LIMIT INTEGER;
class LimitClause {
  public int limit;
}
//// [27] OFFSET子句
// gOffsetClause: K_OFFSET INTEGER;
class OffsetClause {
  public int offset;
}
//// [28] VALUES子句
// gValuesClause: ( K_VALUES gDataBlock )?;
class ValuesClause {
  public DataBlock gDataBlock;
}
//// [29]
// gUpdate: gPrologue ( gUpdate1 ( ';' gUpdate )? )?;
class Update {
  public Prologue gPrologue;
  public Update1 gUpdate1;
  public Update gUpdate;
}
//// [30]
// gUpdate1: gLoad | gClear | gDrop | gAdd | gMove | gCopy | gCreate | gInsertData | gDeleteData | gDeleteWhere | gModify;
class Update1 {
  public Load gLoad;
  public Clear gClear;
  public Drop gDrop;
  public Add gAdd;
  public Move gMove;
  public Copy gCopy;
  public Create gCreate;
  public InsertData gInsertData;
  public DeleteData gDeleteData;
  public DeleteWhere gDeleteWhere;
  public Modify gModify;
  
}
//// [31]
// gLoad: K_LOAD K_SILENT? giri ( K_INTO gGraphRef )?;
class Load {
  public boolean isSilent = false;
  public iri giri;
  public GraphRef gGraphRef;
}
//// [32]
// gClear: K_CLEAR K_SILENT? gGraphRefAll;
class Clear {
  public boolean isSilent = false;
  public GraphRefAll gGraphRefAll;
}
//// [33]
// gDrop: K_DROP K_SILENT? gGraphRefAll;
class Drop {
  public boolean isSilent = false;
  public GraphRefAll gGraphRefAll;
}
//// [34]
// gCreate: K_CREATE K_SILENT? gGraphRef;
class Create {
  public boolean isSilent = false;
  public GraphRef gGraphRef;
}
//// [35]
// gAdd: K_ADD K_SILENT? gGraphOrDefault K_TO gGraphOrDefault;
class Add {
  public GraphOrDefault gGraphOrDefaultFirst;
  public boolean isSilent = false;
  public GraphOrDefault gGraphOrDefaultSecond;
}
//// [36]
// gMove: K_MOVE K_SILENT? gGraphOrDefault K_TO gGraphOrDefault;
class Move {
  public GraphOrDefault gGraphOrDefaultFirst;
  public boolean isSilent = false;
  public GraphOrDefault gGraphOrDefaultSecond;
}
//// [37]
// gCopy: K_COPY K_SILENT? gGraphOrDefault K_TO gGraphOrDefault;
class Copy {
  public GraphOrDefault gGraphOrDefaultFirst;
  public boolean isSilent = false;
  public GraphOrDefault gGraphOrDefaultSecond;
}
//// [38]
// gInsertData: K_INSERT K_DATA gQuadData;
class InsertData {
  public QuadData gQuadData;
}
//// [39]
// gDeleteData: K_DELETE K_DATA gQuadData;
class DeleteData {
  public QuadData gQuadData;
}
//// [40]
// gDeleteWhere: K_DELETE K_WHERE gQuadPattern;
class DeleteWhere {
  public QuadPattern gQuadPattern;
}
//// [41]
// gModify: ( K_WITH giri )? ( gDeleteClause gInsertClause? | gInsertClause ) gUsingClause* K_WHERE gGroupGraphPattern;
class Modify {
  public iri giri;
  public DeleteClause gDeleteClause;
  public InsertClause gInsertClause;
  public List<UsingClause> gUsingClauses = Lists.newArrayList();
  public GroupGraphPattern gGroupGraphPattern;
}
//// [42] 删除子句
// gDeleteClause: K_DELETE gQuadPattern;
class DeleteClause {
  public QuadPattern gQuadPattern;
}
//// [43] 插入子句
// gInsertClause: K_INSERT gQuadPattern;
class InsertClause {
  public QuadPattern gQuadPattern;
}
//// [44] 使用子句
// gUsingClause: K_USING ( giri | K_NAMED giri );
class UsingClause {
  public boolean isNamed = false;
  public iri giri;
}
//// [45] 图声明: DEFAULT或GRAPH
// gGraphOrDefault: K_DEFAULT | K_GRAPH? giri;
class GraphOrDefault {
  public boolean isDefault;
  public iri giri;
}
//// [46] 图引用
// gGraphRef: K_GRAPH giri;
class GraphRef {
  public iri giri;
}
//// [47] 所有图引用: grapah, default, named, all
// gGraphRefAll: gGraphRef | K_DEFAULT | K_NAMED | K_ALL;
class GraphRefAll {
  public GraphRef gGraphRef;
  public boolean isDefault = false;
  public boolean isNamed = false;
  public boolean isAll = false;
}
//// [48]
// gQuadPattern: '{' gQuads '}';
class QuadPattern {
  public Quads gQuads;
}
//// [49]
// gQuadData: '{' gQuads '}';
class QuadData {
  public Quads gQuads;
}
//// [50]
// gQuads: gTriplesTemplate? ( gQuadsNotTriples '.'? gTriplesTemplate? )*;
class Quads {
  public TriplesTemplate gTriplesTemplate;
  public List<QuadsNotTriplesAndTriplesTemplate> list = Lists.newArrayList();
}
class QuadsNotTriplesAndTriplesTemplate {
  public QuadsNotTriples gQuadsNotTriples;
  public TriplesTemplate gTriplesTemplate;
}
//// [51]
// gQuadsNotTriples: K_GRAPH gVarOrIri '{' gTriplesTemplate? '}';
class QuadsNotTriples {
  public VarOrIri gVarOrIri;
  public TriplesTemplate gTriplesTemplate;
}
//// [52]
// gTriplesTemplate: gTriplesSameSubject ( '.' gTriplesTemplate? )?;
class TriplesTemplate {
  public TriplesSameSubject gTriplesSameSubject;
  public TriplesTemplate gTriplesTemplate;
}
//// [53] 分组图模式
// gGroupGraphPattern: '{' ( gSubSelect | gGroupGraphPatternSub ) '}';
class GroupGraphPattern {
  public SubSelect gSubSelect;
  public GroupGraphPatternSub gGroupGraphPatternSub;
}
//// [54] 子分组图模式
// gGroupGraphPatternSub: gTriplesBlock? ( gGraphPatternNotTriples '.'? gTriplesBlock? )*;
class GroupGraphPatternSub {
  public TriplesBlock gTriplesBlock;
  public List<GraphPatternNotTriplesAndTriplesBlock> list = Lists.newArrayList();
}
class GraphPatternNotTriplesAndTriplesBlock {
  public GraphPatternNotTriples gGraphPatternNotTriples;
  public TriplesBlock gTriplesBlock;
}
//// [55] 三元组块
// gTriplesBlock: gTriplesSameSubjectPath ( '.' gTriplesBlock? )?;
class TriplesBlock {
  public TriplesSameSubjectPath gTriplesSameSubjectPath;
  public TriplesBlock gTriplesBlock;
}
//// [56] 非三元组的图模式
// gGraphPatternNotTriples: gGroupOrUnionGraphPattern
// | gOptionalGraphPattern
// | gMinusGraphPattern
// | gGraphGraphPattern
// | gServiceGraphPattern
// | gFilter
// | gBind
// | gInlineData;
class GraphPatternNotTriples {
  public GroupOrUnionGraphPattern gGroupOrUnionGraphPattern;
  public OptionalGraphPattern gOptionalGraphPattern;
  public MinusGraphPattern gMinusGraphPattern;
  public GraphGraphPattern gGraphGraphPattern;
  public ServiceGraphPattern gServiceGraphPattern;
  public Filter gFilter;
  public Bind gBind;
  public InlineData gInlineData;
}
//// [57] 可选图模式: OPTIONAL
// gOptionalGraphPattern: K_OPTIONAL gGroupGraphPattern;
class OptionalGraphPattern {
  public GroupGraphPattern gGroupGraphPattern;
}
//// [58] 图的图模式: GRAPH
// gGraphGraphPattern: K_GRAPH gVarOrIri gGroupGraphPattern;
class GraphGraphPattern {
  public VarOrIri gVarOrIri;
  public GroupGraphPattern gGroupGraphPattern;
}
//// [59] 服务的图模式: SERVICE
// gServiceGraphPattern: K_SERVICE K_SILENT? gVarOrIri gGroupGraphPattern;
class ServiceGraphPattern {
  public VarOrIri gVarOrIri;
  public GroupGraphPattern gGroupGraphPattern;
}
//// [60] BIND
// gBind: K_BIND '(' gExpression K_AS gVar ')';
class Bind {
  public Expression gExpression;
  public Var gVar;
}
//// [61] 行内数据: VALUES
// gInlineData: K_VALUES gDataBlock;
class InlineData {
  public DataBlock gDataBlock;
}
//// [62] 数据块
// gDataBlock: gInlineDataOneVar | gInlineDataFull;
class DataBlock {
  public InlineDataOneVar gInlineDataOneVar;
  public InlineDataFull gInlineDataFull;
}
//// [63]
// gInlineDataOneVar: gVar '{' gDataBlockValue* '}';
class InlineDataOneVar {
  public Var gVar;
  public List<DataBlockValue> gDataBlockValues = Lists.newArrayList();
}
//// [64]
// gInlineDataFull: ( NIL | '(' gVar* ')' ) '{' ( '(' gDataBlockValue* ')' | NIL )* '}';
class InlineDataFull {
  public boolean isFirstNIL;
  public List<Var> gVars = Lists.newArrayList();
  public List<DataBlockValueOrNIL> list = Lists.newArrayList();
}
class DataBlockValueOrNIL {
  public List<DataBlockValue> gDataBlockValue;
  public boolean isNIL;
}
//// [65]
// gDataBlockValue: giri | gRDFLiteral | gNumericLiteral | gBooleanLiteral | K_UNDEF;
class DataBlockValue {
  public iri giri;
  public RDFLiteral gRDFLiteral;
  public NumericLiteral gNumericLiteral;
  public BooleanLiteral gBooleanLiteral;
  public boolean isUndef;
}
//// [66] MINUS图模式
// gMinusGraphPattern: K_MINUS gGroupGraphPattern;
class MinusGraphPattern {
  public GroupGraphPattern gGroupGraphPattern;
}
//// [67] UNION图模式
// gGroupOrUnionGraphPattern: gGroupGraphPattern ( K_UNION gGroupGraphPattern )*;
class GroupOrUnionGraphPattern {
  public List<GroupGraphPattern> gGroupGraphPatterns = Lists.newArrayList();
}
//// [68] 过滤FILTER
// gFilter: K_FILTER gConstraint;
class Filter {
  public Constraint gConstraint;
}
//// [69] 约束: 带括号的表达式, 内建调用, 函数调用
// gConstraint: gBrackettedExpression | gBuiltInCall | gFunctionCall;
class Constraint {
  public BrackettedExpression gBrackettedExpression;
  public BuiltInCall gBuiltInCall;
  public FunctionCall gFunctionCall;
}
//// [70] 函数调用: IRI 参数列表
// gFunctionCall: giri gArgList;
class FunctionCall {
  public iri giri;
  public ArgList gArgList;
}
//// [71] 参数列表
// gArgList: NIL | '(' K_DISTINCT? gExpression ( ',' gExpression )* ')';
class ArgList {
  public boolean isNIL;
  public List<Expression> gExpressions = Lists.newArrayList();
}
//// [72] 表达式列表
// gExpressionList: NIL | '(' gExpression ( ',' gExpression )* ')';
class ExpressionList {
  public boolean isNIL;
  public List<Expression> gExpressions = Lists.newArrayList();
}
//// [73] CONSTRUCT模板
// gConstructTemplate: '{' gConstructTriples? '}';
class ConstructTemplate {
  public ConstructTriples gConstructTriples;
}
//// [74] CONSTRUCT三元组
// gConstructTriples: gTriplesSameSubject ( '.' gConstructTriples? )?;
class ConstructTriples {
  public TriplesSameSubject gTriplesSameSubject;
  public ConstructTriples gConstructTriples;
}
//// [75] 同一Subject的三元组
// gTriplesSameSubject: gVarOrTerm gPropertyListNotEmpty | gTriplesNode gPropertyList;
class TriplesSameSubject {
  // branch 1
  public VarOrTerm gVarOrTerm;
  public PropertyListNotEmpty gPropertyListNotEmpty;
  // branch 2
  public TriplesNode gTriplesNode;
  public PropertyList gPropertyList;
}
//// [76] 属性列表
// gPropertyList: gPropertyListNotEmpty?;
class PropertyList {
  public PropertyListNotEmpty gPropertyListNotEmpty;
}
//// [77] 非空的属性列表
// gPropertyListNotEmpty: gVerb gObjectList ( ';' ( gVerb gObjectList )? )*;
class PropertyListNotEmpty {
  public List<VerbObjectList> list = Lists.newArrayList();
}
class VerbObjectList {
  public Verb gVerb;
  public ObjectList gObjectList;
}
//// [78] Verb
// gVerb: gVarOrIri | K_A;
class Verb {
  public VarOrIri gVarOrIri;
  public boolean isA;
}
//// [79] Object列表
// gObjectList: gObject ( ',' gObject )*;
class ObjectList {
  public List<Object> gObject = Lists.newArrayList();
}
//// [80] Object
// gObject: gGraphNode;
class Object {
  public GraphNode gGraphNode;
}
//// [81] 同一Subject的三元组路径
// gTriplesSameSubjectPath: gVarOrTerm gPropertyListPathNotEmpty | gTriplesNodePath gPropertyListPath;
class TriplesSameSubjectPath {
  // branch 1
  public VarOrTerm gVarOrTerm;
  public PropertyListPathNotEmpty gPropertyListPathNotEmpty;
  // branch 2
  public TriplesNodePath gTriplesNodePath;
  public PropertyListPath gPropertyListPath;
}

//// [82] 属性列表路径
// gPropertyListPath: gPropertyListPathNotEmpty?;
class PropertyListPath {
  public PropertyListPathNotEmpty gPropertyListPathNotEmpty;
}
//// [83] 非空属性列表路径
// gPropertyListPathNotEmpty: ( gVerbPath | gVerbSimple ) gObjectListPath ( ';' ( ( gVerbPath | gVerbSimple ) gObjectList )? )*;
class PropertyListPathNotEmpty {
  public VerbPathOrVerbSimple gVerbPath_gVerbSimple;
  public ObjectListPath gObjectListPath;
  public List<VerbPathOrVerbSimple_ObjectList> list = Lists.newArrayList();
}
class VerbPathOrVerbSimple_ObjectList {
  public VerbPath gVerbPath;
  public VerbSimple gVerbSimple;
  public ObjectList gObjectList;
}
class VerbPathOrVerbSimple {
  public VerbPath gVerbPath;
  public VerbSimple gVerbSimple;
}
//// [84] Verb路径
// gVerbPath: gPath;
class VerbPath {
  public Path gPath;
}
//// [85] 简单Verb
// gVerbSimple: gVar;
class VerbSimple {
  public Var gVar;
}
//// [86] Object列表路径
// gObjectListPath: gObjectPath ( ',' gObjectPath )*;
class ObjectListPath {
  public List<ObjectPath> gObjectPaths = Lists.newArrayList();
}
//// [87] Object路径
// gObjectPath: gGraphNodePath;
class ObjectPath {
  public GraphNodePath gGraphNodePath;
}
//// [88] 路径
// gPath: gPathAlternative;
class Path {
  public PathAlternative gPathAlternative;
}
//// [89] 路径备选
// gPathAlternative: gPathSequence ( '|' gPathSequence )*;
class PathAlternative {
  public List<PathSequence> gPathSequences = Lists.newArrayList();
}
//// [90] 路径序列
// gPathSequence: gPathEltOrInverse ( '/' gPathEltOrInverse )*;
class PathSequence {
  public List<PathEltOrInverse> gPathEltOrInverses = Lists.newArrayList();
}
//// [91] 路径元素
// gPathElt: gPathPrimary gPathMod?;
class PathElt {
  public PathPrimary gPathPrimary;
  public PathModeEnum  pathMod;
}
//// [92] 路径元素或逆路径元素
// gPathEltOrInverse: gPathElt | '^' gPathElt;
class PathEltOrInverse {
  public boolean isInverse;
  public PathElt gPathElt;
}
//// [93] 路径模式
// gPathMod: '?' | '*' | '+';
enum PathModeEnum {
  ZERO_OR_ONE, ZERO_OR_MORE, ONE_OR_MORE
}
//// [94] 基本路径: IRI, a, 路径补, 分组路径
// gPathPrimary: giri | K_A | '!' gPathNegatedPropertySet | '(' gPath ')';
class PathPrimary {
  public iri gIri;
  public boolean isA;
  public PathNegatedPropertySet gPathNegatedPropertySet;
  public Path gPath;
}
//// [95] 路径补属性集
// gPathNegatedPropertySet: gPathOneInPropertySet | '(' ( gPathOneInPropertySet ( '|' gPathOneInPropertySet )* )? ')';
class PathNegatedPropertySet {
  public PathOneInPropertySet gPathOneInPropertySetFirst;
  public PathOneInPropertySet gPathOneInPropertySetSecond;
  public List<PathOneInPropertySet> gPathOneInPropertySets = Lists.newArrayList();
}
//// [96] 属性集中长度为1的路径
// gPathOneInPropertySet: giri | K_A | '^' ( giri | K_A );
class PathOneInPropertySet {
  public iri giri;
  public boolean isA;
  public boolean isInverse;
}
//// [97]
// gInteger: INTEGER;
//// [98] 三元组节点
// gTriplesNode: gCollection | gBlankNodePropertyList;
class TriplesNode {
   public Collection gCollection;
   public BlankNodePropertyList gBlankNodePropertyList;
}
//// [99] 空节点属性列表
// gBlankNodePropertyList: '[' gPropertyListNotEmpty ']';
class BlankNodePropertyList {
  public PropertyListNotEmpty gPropertyListNotEmpty;
}
//// [100] 三元组节点路径
// gTriplesNodePath: gCollectionPath | gBlankNodePropertyListPath;
class TriplesNodePath {
  public CollectionPath gCollectionPath;
  public BlankNodePropertyListPath gBlankNodePropertyListPath;
}
//// [101] 空节点属性列表路径
// gBlankNodePropertyListPath: '[' gPropertyListPathNotEmpty ']';
class BlankNodePropertyListPath {
  public PropertyListPathNotEmpty gPropertyListPathNotEmpty;
}
//// [102] 图节点集合
// gCollection: '(' gGraphNode+ ')';
class Collection {
  public List<GraphNode> gGraphNodes = Lists.newArrayList();
}
//// [103] 图节点路径集合
// gCollectionPath: '(' gGraphNodePath+ ')';
class CollectionPath {
  public List<GraphNodePath> gGraphNodePaths = Lists.newArrayList();
}
//// [104] 图节点
// gGraphNode: gVarOrTerm | gTriplesNode;
class GraphNode {
  public VarOrTerm gVarOrTerm;
  public TriplesNode gTriplesNode;
}
//// [105] 图节点路径
// gGraphNodePath: gVarOrTerm | gTriplesNodePath;
class GraphNodePath {
  public VarOrTerm gVarOrTerm;
  public TriplesNodePath gTriplesNodePath;
}
//// [106] 变量或项
// gVarOrTerm: gVar | gGraphTerm;
class VarOrTerm {
  public Var gVar;
  public GraphTerm gGraphTerm;
}
//// [107] 变量或IRI
// gVarOrIri: gVar | giri;
class VarOrIri {
  public Var gVar;
  public iri giri;
}
//// [108] 变量: $var, ?var
// gVar: VAR1 | VAR2;
class Var {
  public String gVar;
  public String varName;
}
//// [109] 图项: IRI, RDF字面量, 数值字面量, 布尔字面量, 空节点, ()
// gGraphTerm: giri | gRDFLiteral | gNumericLiteral | gBooleanLiteral | gBlankNode | NIL;
class GraphTerm {
  public iri giri;
  public RDFLiteral gRDFLiteral;
  public NumericLiteral gNumericLiteral;
  public BooleanLiteral gBooleanLiteral;
  public BlankNode gBlankNode;
  public boolean isNIL;
}
//// [110] 表达式
// gExpression: gConditionalOrExpression;
class Expression {
  public ConditionalOrExpression gConditionalOrExpression;
}
//// [111] 或条件表达式
// gConditionalOrExpression: gConditionalAndExpression ( K_OR gConditionalAndExpression )*;
class ConditionalOrExpression {
  public List<ConditionalAndExpression> gConditionalAndExpressions = Lists.newArrayList();
}
//// [112] 与条件表达式
// gConditionalAndExpression: gValueLogical ( K_AND gValueLogical )*;
class ConditionalAndExpression {
  public ValueLogical gValueLogical;
  public List<ValueLogical> gValueLogicals = Lists.newArrayList();
}
//// [113] 值逻辑
// gValueLogical: gRelationalExpression;
class ValueLogical {
  public RelationalExpression gRelationalExpression;
}
//// [114] 关系表达式
// gRelationalExpression: gNumericExpression ( gRelationalExpression2 )?;
// gRelationalExpression2: K_EQ gNumericExpression
// | K_NEQ gNumericExpression
// | K_LT gNumericExpression
// | K_GT gNumericExpression
// | K_LTE gNumericExpression
// | K_GTE gNumericExpression
// | K_IN gExpressionList
// | K_NOT K_IN gExpressionList;
class RelationalExpression {
  public NumericExpression gNumericExpression;
  public RelationalExpressionTypeEnum type;
  public NumericExpression gNumericExpression2;
  public ExpressionList gExpressionList;
}
enum RelationalExpressionTypeEnum {
  EQ, NEQ, LT, GT, LTE, GTE, IN, NOT_IN
}
//// [115] 数值表达式
// gNumericExpression: gAdditiveExpression;
class NumericExpression {
  public AdditiveExpression gAdditiveExpression;
}
//// [116] 可加数值表达式
//// AdditiveExpression ::= MultiplicativeExpression ( '+' MultiplicativeExpression | '-'
//// MultiplicativeExpression | ( NumericLiteralPositive | NumericLiteralNegative ) ( ( '*'
//// UnaryExpression ) | ( '/' UnaryExpression ) )* )*
// gAdditiveExpression: gMultiplicativeExpression gAdditiveExpression2*;
// gAdditiveExpression2: '+' gMultiplicativeExpression
// | '-' gMultiplicativeExpression
// | ( gNumericLiteralPositive | gNumericLiteralNegative ) ((('*' | '/') gUnaryExpression ))*;
class AdditiveExpression {
  public MultiplicativeExpression gMultiplicativeExpression;
  public List<AdditiveExpression2> gAdditiveExpression2s = Lists.newArrayList();
}
class AdditiveExpression2 {
  // branch 1
  public NumericExpressionOpEnum op; // +,-
  // branch 2
  public MultiplicativeExpression gMultiplicativeExpression;
  // branch 3
  public NumericLiteral gNumericLiteral;
  public List<UnaryExpressionWithOp> gUnaryExpressions = Lists.newArrayList();
}
//// [117] 可乘数值表达式
//// MultiplicativeExpression ::= UnaryExpression ( '*' UnaryExpression | '/' UnaryExpression )*
// gMultiplicativeExpression: gUnaryExpression ( (('*'|'/') gUnaryExpression) )*;
class MultiplicativeExpression {
  public UnaryExpression gUnaryExpressionFirst;
  public List<UnaryExpressionWithOp> gUnaryExpression = Lists.newArrayList();
}
class UnaryExpressionWithOp {
  public NumericExpressionOpEnum op;
  public UnaryExpression gUnaryExpression;
}
enum NumericExpressionOpEnum {
  ADD, SUBTRACT, MULTIPLY, DIVIDE
}
//// [118] 一元表达式
// gUnaryExpression: '!' gPrimaryExpression
// | '+' gPrimaryExpression
// | '-' gPrimaryExpression
// | gPrimaryExpression;
class UnaryExpression {
  public UnaryExpressionPrefixEnum prefixEnum;
  public PrimaryExpression gPrimaryExpression;
}
enum UnaryExpressionPrefixEnum {
  COMPLEMENT, POSITIVE, NEGATIVE,NONE
}
//// [119] 原始表达式
// gPrimaryExpression: gBrackettedExpression | gNumericLiteral | gBooleanLiteral | gRDFLiteral | gBuiltInCall | giriOrFunction | gVar;
class PrimaryExpression {
  public BrackettedExpression gBrackettedExpression;
  public NumericLiteral gNumericLiteral;
  public BooleanLiteral gBooleanLiteral;
  public RDFLiteral gRDFLiteral;
  public BuiltInCall gBuiltInCall;
  public iriOrFunction giriOrFunction;
  public Var gVar;
}
//// [120] 带括号的表达式
// gBrackettedExpression: '(' gExpression ')';
class BrackettedExpression {
  public Expression gExpression;
}
//// [121] 内建调用
// gBuiltInCall: gAggregate
// | K_STR '(' gExpression ')'
// | K_LANG '(' gExpression ')'
// | K_LANGMATCHES '(' gExpression ',' gExpression ')'
// | K_DATATYPE '(' gExpression ')'
// | K_BOUND '(' gVar ')'
// | K_IRI '(' gExpression ')'
// | K_URI '(' gExpression ')'
// | K_BNODE ( '(' gExpression ')' | NIL )
// | K_RAND NIL
// | K_ABS '(' gExpression ')'
// | K_CEIL '(' gExpression ')'
// | K_FLOOR '(' gExpression ')'
// | K_ROUND '(' gExpression ')'
// | K_CONCAT gExpressionList
// | gSubstringExpression
// | K_STRLEN '(' gExpression ')'
// | gStrReplaceExpression
// | K_UCASE '(' gExpression ')'
// | K_LCASE '(' gExpression ')'
// | K_ENCODE_FOR_URI '(' gExpression ')'
// | K_CONTAINS '(' gExpression ',' gExpression ')'
// | K_STRSTARTS '(' gExpression ',' gExpression ')'
// | K_STRENDS '(' gExpression ',' gExpression ')'
// | K_STRBEFORE '(' gExpression ',' gExpression ')'
// | K_STRAFTER '(' gExpression ',' gExpression ')'
// | K_YEAR '(' gExpression ')'
// | K_MONTH '(' gExpression ')'
// | K_DAY '(' gExpression ')'
// | K_HOURS '(' gExpression ')'
// | K_MINUTES '(' gExpression ')'
// | K_SECONDS '(' gExpression ')'
// | K_TIMEZONE '(' gExpression ')'
// | K_TZ '(' gExpression ')'
// | K_NOW NIL
// | K_UUID NIL
// | K_STRUUID NIL
// | K_MD5 '(' gExpression ')'
// | K_SHA1 '(' gExpression ')'
// | K_SHA256 '(' gExpression ')'
// | K_SHA384 '(' gExpression ')'
// | K_SHA512 '(' gExpression ')'
// | K_COALESCE gExpressionList
// | K_IF '(' gExpression ',' gExpression ',' gExpression ')'
// | K_STRLANG '(' gExpression ',' gExpression ')'
// | K_STRDT '(' gExpression ',' gExpression ')'
// | K_sameTerm '(' gExpression ',' gExpression ')'
// | K_isIRI '(' gExpression ')'
// | K_isURI '(' gExpression ')'
// | K_isBLANK '(' gExpression ')'
// | K_isLITERAL '(' gExpression ')'
// | K_isNUMERIC '(' gExpression ')'
// | gRegexExpression
// | gExistsFunc
// | gNotExistsFunc;
class BuiltInCall {
  public BuiltInCallTypeEnum type;
  public Expression gExpression;
  public Expression gExpression2;
  public Expression gExpression3;
}
enum BuiltInCallTypeEnum {
  STR, LANG, LANGMATCHES, DATATYPE, BOUND, IRI, URI, BNODE, RAND, ABS, CEIL, FLOOR, ROUND, CONCAT,
  STRLEN, UCASE, LCASE, ENCODE_FOR_URI, CONTAINS, STRSTARTS, STRENDS, STRBEFORE, STRAFTER, YEAR,
  MONTH, DAY, HOURS, MINUTES, SECONDS, TIMEZONE, TZ, NOW, UUID, STRUUID, MD5, SHA1, SHA256,
  SHA384, SHA512, COALESCE, IF, STRLANG, STRDT, sameTerm, isIRI, isURI, isBLANK, isLITERAL,
  isNUMERIC, gSubstringExpression, gStrReplaceExpression, gRegexExpression, gExistsFunc,
  gNotExistsFunc
}
//// [122] 正则表达式
// gRegexExpression: K_REGEX '(' gExpression ',' gExpression ( ',' gExpression )? ')';
class RegexExpression {
  public Expression first;
  public Expression second;
  public Expression third;
}
//// [123] 字符串子串SUBSTR函数
// gSubstringExpression: K_SUBSTR '(' gExpression ',' gExpression ( ',' gExpression )? ')';
class SubstringExpression {
  public Expression first;
  public Expression second;
  public Expression third;
}
//// [124] 字符串替换REPLACE函数
// gStrReplaceExpression: K_REPLACE '(' gExpression ',' gExpression ',' gExpression ( ',' gExpression )? ')';
class StrReplaceExpression{
  public Expression first;
  public Expression second;
  public Expression third;
  public Expression fourth;
}
//// [125] EXIST函数
// gExistsFunc: K_EXISTS gGroupGraphPattern;
class ExistsFunc {
  public GroupGraphPattern gGroupGraphPattern;
}
//// [126] NOT EXIST函数
// gNotExistsFunc: K_NOT K_EXISTS gGroupGraphPattern;
class NotExistsFunc {
  public GroupGraphPattern gGroupGraphPattern;
}
//// [127] 内建聚合调用: COUNT, SUM, MIN, MAX. AVG, SAMPLE, GROUP CONCAT
// gAggregate: K_COUNT '(' K_DISTINCT? ( '*' | gExpression ) ')'
// | K_SUM '(' K_DISTINCT? gExpression ')'
// | K_MIN '(' K_DISTINCT? gExpression ')'
// | K_MAX '(' K_DISTINCT? gExpression ')'
// | K_AVG '(' K_DISTINCT? gExpression ')'
// | K_SAMPLE '(' K_DISTINCT? gExpression ')'
// | K_GROUP_CONCAT '(' K_DISTINCT? gExpression ( ';' K_SEPARATOR '=' gString )? ')';
class Aggregate {
  public AggregateTypeEnum type;
  public boolean allExpression;
  public Expression gExpression;
  public String separator;
}
enum AggregateTypeEnum {
  COUNT, COUNT_DISTINCT, SUM, MIN, MAX, AVG, SAMPLE, GROUP_CONCAT, GROUP_CONCAT_DISTINCT
}
//// [128]
// giriOrFunction: giri gArgList?;
class iriOrFunction {
  public iri giri;
  public ArgList gArgList;
}
//// [129] RDF字面量
// gRDFLiteral: gString ( LANGTAG | ( '^^' giri ) )?;
class RDFLiteral {
  public String value;
  public boolean hasLANGTAG = false;
  public iri giri = null;
}
//// [130] 数值字面量
// gNumericLiteral: gNumericLiteralUnsigned | gNumericLiteralPositive | gNumericLiteralNegative;
class NumericLiteral {
  public Integer iValue;
  public BigDecimal bdValue;
  public Double dValue;
}
//// [131] 无符号数值字面量
// gNumericLiteralUnsigned: INTEGER | DECIMAL | DOUBLE;
//// [132] 正数值字面量
// gNumericLiteralPositive: INTEGER_POSITIVE | DECIMAL_POSITIVE | DOUBLE_POSITIVE;
//// [133] 负数值字面量
// gNumericLiteralNegative: INTEGER_NEGATIVE | DECIMAL_NEGATIVE | DOUBLE_NEGATIVE;
//// [134] 布尔字面量
// gBooleanLiteral: K_true | K_false;
class BooleanLiteral {
  public boolean value;
}
//// [135] 字符串: 可能包含空格
// gString: STRING_LITERAL1 | STRING_LITERAL2 | STRING_LITERAL_LONG1 | STRING_LITERAL_LONG2;
//// [136] IRI: IRI引用, 带前缀的名字(例foaf:knows)
// giri: IRIREF | gPrefixedName;
class iri {
  public String IRIREF;
  public PrefixedName gPrefixedName;
}
//// [137] 带前缀的名字
// gPrefixedName: PNAME_LN | PNAME_NS;
class PrefixedName {
  public String PNAME_LN;
  public String PNAME_NS;
}
//// [138] 空节点
// gBlankNode: BLANK_NODE_LABEL | ANON;
class BlankNode {
  public String BLANK_NODE_LABEL;
  public String ANON;
}

//// ---------------------------------------------------------------------------
//// lexer grammar
//// ---------------------------------------------------------------------------
//// ---------------------------------------------------------------------------
//// KEYWORDS
//// REF: https://github.com/antlr/antlr4/blob/master/doc/case-insensitive-lexing.md
//// ---------------------------------------------------------------------------
//
// fragment A : [aA]; // match either an 'a' or 'A'
// fragment B : [bB];
// fragment C : [cC];
// fragment D : [dD];
// fragment E : [eE];
// fragment F : [fF];
// fragment G : [gG];
// fragment H : [hH];
// fragment I : [iI];
// fragment J : [jJ];
// fragment K : [kK];
// fragment L : [lL];
// fragment M : [mM];
// fragment N : [nN];
// fragment O : [oO];
// fragment P : [pP];
// fragment Q : [qQ];
// fragment R : [rR];
// fragment S : [sS];
// fragment T : [tT];
// fragment U : [uU];
// fragment V : [vV];
// fragment W : [wW];
// fragment X : [xX];
// fragment Y : [yY];
// fragment Z : [zZ];
//
// K_OR: '||';
// K_AND: '&&';
// K_EQ: '=';
// K_NEQ: '!=';
// K_LT: '<';
// K_GT: '>';
// K_LTE: '<=';
// K_GTE: '>=';
// K_ABS: A B S ;
// K_ADD: A D D ;
// K_ALL: A L L ;
// K_AS: A S ;
// K_ASC: A S C ;
// K_ASK: A S K ;
// K_AVG: A V G ;
// K_BASE: B A S E ;
// K_BIND: B I N D ;
// K_BNODE: B N O D E ;
// K_BOUND: B O U N D ;
// K_BY: B Y ;
// K_CEIL: C E I L ;
// K_CLEAR: C L E A R ;
// K_COALESCE: C O A L E S C E ;
// K_CONCAT: C O N C A T ;
// K_CONSTRUCT: C O N S T R U C T ;
// K_CONTAINS: C O N T A I N S ;
// K_COPY: C O P Y ;
// K_COUNT: C O U N T ;
// K_CREATE: C R E A T E ;
// K_DATATYPE: D A T A T Y P E ;
// K_DAY: D A Y ;
// K_DEFAULT: D E F A U L T ;
// K_DELETE: D E L E T E ;
// K_DESC: D E S C ;
// K_DESCRIBE: D E S C R I B E ;
// K_DISTINCT: D I S T I N C T ;
// K_DROP: D R O P ;
// K_ENCODE_FOR_URI: E N C O D E '_' F O R '_' U R I ;
// K_EXISTS: E X I S T S ;
// K_FILTER: F I L T E R ;
// K_FLOOR: F L O O R ;
// K_FROM: F R O M ;
// K_GRAPH: G R A P H ;
// K_GROUP: G R O U P ;
// K_GROUP_CONCAT: G R O U P '_' C O N C A T ;
// K_HAVING: H A V I N G ;
// K_HOURS: H O U R S ;
// K_IF: I F ;
// K_IN: I N ;
// K_INSERT: I N S E R T ;
// K_INTO: I N T O ;
// K_IRI: I R I ;
// K_LANG: L A N G ;
// K_LANGMATCHES: L A N G M A T C H E S ;
// K_LCASE: L C A S E ;
// K_LIMIT: L I M I T ;
// K_LOAD: L O A D ;
// K_MAX: M A X ;
// K_MD5: M D '5' ;
// K_MIN: M I N ;
// K_MINUS: M I N U S ;
// K_MINUTES: M I N U T E S ;
// K_MONTH: M O N T H ;
// K_MOVE: M O V E ;
// K_NAMED: N A M E D ;
// K_NOT: N O T ;
// K_NOW: N O W ;
// K_OFFSET: O F F S E T ;
// K_OPTIONAL: O P T I O N A L ;
// K_ORDER: O R D E R ;
// K_PREFIX: P R E F I X ;
// K_RAND: R A N D ;
// K_REDUCED: R E D U C E D ;
// K_REGEX: R E G E X ;
// K_REPLACE: R E P L A C E ;
// K_ROUND: R O U N D ;
// K_SAMPLE: S A M P L E ;
// K_SECONDS: S E C O N D S ;
// K_SELECT: S E L E C T ;
// K_SEPARATOR: S E P A R A T O R ;
// K_SERVICE: S E R V I C E ;
// K_SHA1: S H A '1' ;
// K_SHA256: S H A '256' ;
// K_SHA384: S H A '384' ;
// K_SHA512: S H A '512' ;
// K_SILENT: S I L E N T ;
// K_STR: S T R ;
// K_STRAFTER: S T R A F T E R ;
// K_STRBEFORE: S T R B E F O R E ;
// K_STRDT: S T R D T ;
// K_STRENDS: S T R E N D S ;
// K_STRLANG: S T R L A N G ;
// K_STRLEN: S T R L E N ;
// K_STRSTARTS: S T R S T A R T S ;
// K_STRUUID: S T R U U I D ;
// K_SUBSTR: S U B S T R ;
// K_SUM: S U M ;
// K_TIMEZONE: T I M E Z O N E ;
// K_TO: T O ;
// K_TZ: T Z ;
// K_UCASE: U C A S E ;
// K_UNDEF: U N D E F ;
// K_UNION: U N I O N ;
// K_URI: U R I ;
// K_USING: U S I N G ;
// K_UUID: U U I D ;
// K_VALUES: V A L U E S ;
// K_WHERE: W H E R E ;
// K_WITH: W I T H ;
// K_YEAR: Y E A R ;
// K_A: A ;
// K_false: F A L S E ;
// K_isBLANK: I S B L A N K ;
// K_isIRI: I S I R I ;
// K_isLITERAL: I S L I T E R A L ;
// K_isNUMERIC: I S N U M E R I C ;
// K_isURI: I S U R I ;
// K_sameTerm: S A M E T E R M ;
// K_true: T R U E ;
// K_DATA: D A T A;
}