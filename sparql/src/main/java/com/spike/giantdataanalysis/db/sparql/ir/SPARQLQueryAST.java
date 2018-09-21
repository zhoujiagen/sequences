package com.spike.giantdataanalysis.db.sparql.ir;

import java.util.List;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.db.query.sparql.SPARQLQueryParser.*;
import com.spike.giantdataanalysis.db.sparql.exception.ASTParseException;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.db.query.sparql.SPARQLQueryParser;
import com.spike.giantdataanalysis.db.query.sparql.SPARQLQueryVisitor;

/**
 * IR generator for SPARQL Query Language.
 * <p>
 * NOTE: IR is rather complicated.
 */
public class SPARQLQueryAST implements SPARQLQueryVisitor<Void>, SPARQLQuery {
  private static final Logger LOG = LoggerFactory.getLogger(SPARQLQueryAST.class);

  private final SPARQLQueryParser parser;

  public SPARQLQueryAST(SPARQLQueryParser parser) {
    this.parser = parser;
  }

  @Override
  public Void visit(ParseTree tree) {
    LOG.debug("visit: {}", tree.getText());
    if (tree instanceof GQueryUnitContext) {
      return visitGQueryUnit((GQueryUnitContext) tree);
    }
    throw ASTParseException.newE("Support SAPRQL Query only currently!");
  }

  @Override
  public Void visitChildren(RuleNode node) {
    LOG.debug("visitChildren: {}", node);
    return null;
  }

  @Override
  public Void visitTerminal(TerminalNode node) {
    LOG.debug("visitTerminal: {}", node);
    return null;
  }

  @Override
  public Void visitErrorNode(ErrorNode node) {
    LOG.debug("visitErrorNode: {}", node);
    return null;
  }

  @Override
  public Void visitGQueryUnit(GQueryUnitContext ctx) {
    LOG.debug("visitGQueryUnit: {}", ctx.toStringTree(parser));

    ParseTree gQueryContext = ctx.getChild(0);
    Preconditions.checkState(gQueryContext instanceof GQueryContext);
    return visitGQuery((GQueryContext) gQueryContext);
  }

  @Override
  public Void visitGQuery(GQueryContext ctx) {
    LOG.debug("visitGQuery: {}", ctx.toStringTree(parser));

    List<ParseTree> children = ctx.children;
    Preconditions.checkArgument(children.size() == 3);
    Preconditions.checkArgument(children.get(0) instanceof GPrologueContext);
    Preconditions.checkArgument(children.get(1) instanceof GSelectQueryContext
        || children.get(1) instanceof GConstructQueryContext
        || children.get(1) instanceof GDescribeQueryContext
        || children.get(1) instanceof GAskQueryContext);
    Preconditions.checkArgument(children.get(2) instanceof GValuesClauseContext);
    return null;
  }

  @Override
  public Void visitGUpdateUnit(GUpdateUnitContext ctx) {
    LOG.debug("visitGUpdateUnit: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPrologue(GPrologueContext ctx) {
    LOG.debug("visitGPrologue: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGBaseDecl(GBaseDeclContext ctx) {
    LOG.debug("visitGBaseDecl: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPrefixDecl(GPrefixDeclContext ctx) {
    LOG.debug("visitGPrefixDecl: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGSelectQuery(GSelectQueryContext ctx) {
    LOG.debug("visitGSelectQuery: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGSubSelect(GSubSelectContext ctx) {
    LOG.debug("visitGSubSelect: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGSelectClause(GSelectClauseContext ctx) {
    LOG.debug("visitGSelectClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGConstructQuery(GConstructQueryContext ctx) {
    LOG.debug("visitGConstructQuery: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGDescribeQuery(GDescribeQueryContext ctx) {
    LOG.debug("visitGDescribeQuery: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGAskQuery(GAskQueryContext ctx) {
    LOG.debug("visitGAskQuery: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGDatasetClause(GDatasetClauseContext ctx) {
    LOG.debug("visitGDatasetClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGDefaultGraphClause(GDefaultGraphClauseContext ctx) {
    LOG.debug("visitGDefaultGraphClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGNamedGraphClause(GNamedGraphClauseContext ctx) {
    LOG.debug("visitGNamedGraphClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGSourceSelector(GSourceSelectorContext ctx) {
    LOG.debug("visitGSourceSelector: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGWhereClause(GWhereClauseContext ctx) {
    LOG.debug("visitGWhereClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGSolutionModifier(GSolutionModifierContext ctx) {
    LOG.debug("visitGSolutionModifier: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGroupClause(GGroupClauseContext ctx) {
    LOG.debug("visitGGroupClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGroupCondition(GGroupConditionContext ctx) {
    LOG.debug("visitGGroupCondition: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGHavingClause(GHavingClauseContext ctx) {
    LOG.debug("visitGHavingClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGHavingCondition(GHavingConditionContext ctx) {
    LOG.debug("visitGHavingCondition: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGOrderClause(GOrderClauseContext ctx) {
    LOG.debug("visitGOrderClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGOrderCondition(GOrderConditionContext ctx) {
    LOG.debug("visitGOrderCondition: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGLimitOffsetClauses(GLimitOffsetClausesContext ctx) {
    LOG.debug("visitGLimitOffsetClauses: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGLimitClause(GLimitClauseContext ctx) {
    LOG.debug("visitGLimitClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGOffsetClause(GOffsetClauseContext ctx) {
    LOG.debug("visitGOffsetClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGValuesClause(GValuesClauseContext ctx) {
    LOG.debug("visitGValuesClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGUpdate(GUpdateContext ctx) {
    LOG.debug("visitGUpdate: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGUpdate1(GUpdate1Context ctx) {
    LOG.debug("visitGUpdate1: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGLoad(GLoadContext ctx) {
    LOG.debug("visitGLoad: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGClear(GClearContext ctx) {
    LOG.debug("visitGClear: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGDrop(GDropContext ctx) {
    LOG.debug("visitGDrop: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGCreate(GCreateContext ctx) {
    LOG.debug("visitGCreate: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGAdd(GAddContext ctx) {
    LOG.debug("visitGAdd: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGMove(GMoveContext ctx) {
    LOG.debug("visitGMove: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGCopy(GCopyContext ctx) {
    LOG.debug("visitGCopy: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGInsertData(GInsertDataContext ctx) {
    LOG.debug("visitGInsertData: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGDeleteData(GDeleteDataContext ctx) {
    LOG.debug("visitGDeleteData: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGDeleteWhere(GDeleteWhereContext ctx) {
    LOG.debug("visitGDeleteWhere: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGModify(GModifyContext ctx) {
    LOG.debug("visitGModify: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGDeleteClause(GDeleteClauseContext ctx) {
    LOG.debug("visitGDeleteClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGInsertClause(GInsertClauseContext ctx) {
    LOG.debug("visitGInsertClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGUsingClause(GUsingClauseContext ctx) {
    LOG.debug("visitGUsingClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGraphOrDefault(GGraphOrDefaultContext ctx) {
    LOG.debug("visitGGraphOrDefault: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGraphRef(GGraphRefContext ctx) {
    LOG.debug("visitGGraphRef: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGraphRefAll(GGraphRefAllContext ctx) {
    LOG.debug("visitGGraphRefAll: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGQuadPattern(GQuadPatternContext ctx) {
    LOG.debug("visitGQuadPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGQuadData(GQuadDataContext ctx) {
    LOG.debug("visitGQuadData: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGQuads(GQuadsContext ctx) {
    LOG.debug("visitGQuads: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGQuadsNotTriples(GQuadsNotTriplesContext ctx) {
    LOG.debug("visitGQuadsNotTriples: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGTriplesTemplate(GTriplesTemplateContext ctx) {
    LOG.debug("visitGTriplesTemplate: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGroupGraphPattern(GGroupGraphPatternContext ctx) {
    LOG.debug("visitGGroupGraphPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGroupGraphPatternSub(GGroupGraphPatternSubContext ctx) {
    LOG.debug("visitGGroupGraphPatternSub: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGTriplesBlock(GTriplesBlockContext ctx) {
    LOG.debug("visitGTriplesBlock: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGraphPatternNotTriples(GGraphPatternNotTriplesContext ctx) {
    LOG.debug("visitGGraphPatternNotTriples: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGOptionalGraphPattern(GOptionalGraphPatternContext ctx) {
    LOG.debug("visitGOptionalGraphPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGraphGraphPattern(GGraphGraphPatternContext ctx) {
    LOG.debug("visitGGraphGraphPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGServiceGraphPattern(GServiceGraphPatternContext ctx) {
    LOG.debug("visitGServiceGraphPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGBind(GBindContext ctx) {
    LOG.debug("visitGBind: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGInlineData(GInlineDataContext ctx) {
    LOG.debug("visitGInlineData: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGDataBlock(GDataBlockContext ctx) {
    LOG.debug("visitGDataBlock: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGInlineDataOneVar(GInlineDataOneVarContext ctx) {
    LOG.debug("visitGInlineDataOneVar: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGInlineDataFull(GInlineDataFullContext ctx) {
    LOG.debug("visitGInlineDataFull: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGDataBlockValue(GDataBlockValueContext ctx) {
    LOG.debug("visitGDataBlockValue: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGMinusGraphPattern(GMinusGraphPatternContext ctx) {
    LOG.debug("visitGMinusGraphPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGroupOrUnionGraphPattern(GGroupOrUnionGraphPatternContext ctx) {
    LOG.debug("visitGGroupOrUnionGraphPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGFilter(GFilterContext ctx) {
    LOG.debug("visitGFilter: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGConstraint(GConstraintContext ctx) {
    LOG.debug("visitGConstraint: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGFunctionCall(GFunctionCallContext ctx) {
    LOG.debug("visitGFunctionCall: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGArgList(GArgListContext ctx) {
    LOG.debug("visitGArgList: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGExpressionList(GExpressionListContext ctx) {
    LOG.debug("visitGExpressionList: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGConstructTemplate(GConstructTemplateContext ctx) {
    LOG.debug("visitGConstructTemplate: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGConstructTriples(GConstructTriplesContext ctx) {
    LOG.debug("visitGConstructTriples: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGTriplesSameSubject(GTriplesSameSubjectContext ctx) {
    LOG.debug("visitGTriplesSameSubject: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPropertyList(GPropertyListContext ctx) {
    LOG.debug("visitGPropertyList: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPropertyListNotEmpty(GPropertyListNotEmptyContext ctx) {
    LOG.debug("visitGPropertyListNotEmpty: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGVerb(GVerbContext ctx) {
    LOG.debug("visitGVerb: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGObjectList(GObjectListContext ctx) {
    LOG.debug("visitGObjectList: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGObject(GObjectContext ctx) {
    LOG.debug("visitGObject: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGTriplesSameSubjectPath(GTriplesSameSubjectPathContext ctx) {
    LOG.debug("visitGTriplesSameSubjectPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPropertyListPath(GPropertyListPathContext ctx) {
    LOG.debug("visitGPropertyListPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPropertyListPathNotEmpty(GPropertyListPathNotEmptyContext ctx) {
    LOG.debug("visitGPropertyListPathNotEmpty: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGVerbPath(GVerbPathContext ctx) {
    LOG.debug("visitGVerbPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGVerbSimple(GVerbSimpleContext ctx) {
    LOG.debug("visitGVerbSimple: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGObjectListPath(GObjectListPathContext ctx) {
    LOG.debug("visitGObjectListPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGObjectPath(GObjectPathContext ctx) {
    LOG.debug("visitGObjectPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPath(GPathContext ctx) {
    LOG.debug("visitGPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPathAlternative(GPathAlternativeContext ctx) {
    LOG.debug("visitGPathAlternative: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPathSequence(GPathSequenceContext ctx) {
    LOG.debug("visitGPathSequence: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPathElt(GPathEltContext ctx) {
    LOG.debug("visitGPathElt: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPathEltOrInverse(GPathEltOrInverseContext ctx) {
    LOG.debug("visitGPathEltOrInverse: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPathMod(GPathModContext ctx) {
    LOG.debug("visitGPathMod: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPathPrimary(GPathPrimaryContext ctx) {
    LOG.debug("visitGPathPrimary: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPathNegatedPropertySet(GPathNegatedPropertySetContext ctx) {
    LOG.debug("visitGPathNegatedPropertySet: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPathOneInPropertySet(GPathOneInPropertySetContext ctx) {
    LOG.debug("visitGPathOneInPropertySet: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGInteger(GIntegerContext ctx) {
    LOG.debug("visitGInteger: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGTriplesNode(GTriplesNodeContext ctx) {
    LOG.debug("visitGTriplesNode: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGBlankNodePropertyList(GBlankNodePropertyListContext ctx) {
    LOG.debug("visitGBlankNodePropertyList: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGTriplesNodePath(GTriplesNodePathContext ctx) {
    LOG.debug("visitGTriplesNodePath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGBlankNodePropertyListPath(GBlankNodePropertyListPathContext ctx) {
    LOG.debug("visitGBlankNodePropertyListPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGCollection(GCollectionContext ctx) {
    LOG.debug("visitGCollection: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGCollectionPath(GCollectionPathContext ctx) {
    LOG.debug("visitGCollectionPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGraphNode(GGraphNodeContext ctx) {
    LOG.debug("visitGGraphNode: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGraphNodePath(GGraphNodePathContext ctx) {
    LOG.debug("visitGGraphNodePath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGVarOrTerm(GVarOrTermContext ctx) {
    LOG.debug("visitGVarOrTerm: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGVarOrIri(GVarOrIriContext ctx) {
    LOG.debug("visitGVarOrIri: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGVar(GVarContext ctx) {
    LOG.debug("visitGVar: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGGraphTerm(GGraphTermContext ctx) {
    LOG.debug("visitGGraphTerm: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGExpression(GExpressionContext ctx) {
    LOG.debug("visitGExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGConditionalOrExpression(GConditionalOrExpressionContext ctx) {
    LOG.debug("visitGConditionalOrExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGConditionalAndExpression(GConditionalAndExpressionContext ctx) {
    LOG.debug("visitGConditionalAndExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGValueLogical(GValueLogicalContext ctx) {
    LOG.debug("visitGValueLogical: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGRelationalExpression(GRelationalExpressionContext ctx) {
    LOG.debug("visitGRelationalExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGRelationalExpression2(GRelationalExpression2Context ctx) {
    LOG.debug("visitGRelationalExpression2: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGNumericExpression(GNumericExpressionContext ctx) {
    LOG.debug("visitGNumericExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGAdditiveExpression(GAdditiveExpressionContext ctx) {
    LOG.debug("visitGAdditiveExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGAdditiveExpression2(GAdditiveExpression2Context ctx) {
    LOG.debug("visitGAdditiveExpression2: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGMultiplicativeExpression(GMultiplicativeExpressionContext ctx) {
    LOG.debug("visitGMultiplicativeExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGUnaryExpression(GUnaryExpressionContext ctx) {
    LOG.debug("visitGUnaryExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPrimaryExpression(GPrimaryExpressionContext ctx) {
    LOG.debug("visitGPrimaryExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGBrackettedExpression(GBrackettedExpressionContext ctx) {
    LOG.debug("visitGBrackettedExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGBuiltInCall(GBuiltInCallContext ctx) {
    LOG.debug("visitGBuiltInCall: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGRegexExpression(GRegexExpressionContext ctx) {
    LOG.debug("visitGRegexExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGSubstringExpression(GSubstringExpressionContext ctx) {
    LOG.debug("visitGSubstringExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGStrReplaceExpression(GStrReplaceExpressionContext ctx) {
    LOG.debug("visitGStrReplaceExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGExistsFunc(GExistsFuncContext ctx) {
    LOG.debug("visitGExistsFunc: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGNotExistsFunc(GNotExistsFuncContext ctx) {
    LOG.debug("visitGNotExistsFunc: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGAggregate(GAggregateContext ctx) {
    LOG.debug("visitGAggregate: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGiriOrFunction(GiriOrFunctionContext ctx) {
    LOG.debug("visitGiriOrFunction: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGRDFLiteral(GRDFLiteralContext ctx) {
    LOG.debug("visitGRDFLiteral: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGNumericLiteral(GNumericLiteralContext ctx) {
    LOG.debug("visitGNumericLiteral: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGNumericLiteralUnsigned(GNumericLiteralUnsignedContext ctx) {
    LOG.debug("visitGNumericLiteralUnsigned: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGNumericLiteralPositive(GNumericLiteralPositiveContext ctx) {
    LOG.debug("visitGNumericLiteralPositive: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGNumericLiteralNegative(GNumericLiteralNegativeContext ctx) {
    LOG.debug("visitGNumericLiteralNegative: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGBooleanLiteral(GBooleanLiteralContext ctx) {
    LOG.debug("visitGBooleanLiteral: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGString(GStringContext ctx) {
    LOG.debug("visitGString: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGiri(GiriContext ctx) {
    LOG.debug("visitGiri: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGPrefixedName(GPrefixedNameContext ctx) {
    LOG.debug("visitGPrefixedName: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGBlankNode(GBlankNodeContext ctx) {
    LOG.debug("visitGBlankNode: {}", ctx.toStringTree(parser));
    return null;
  }

}
