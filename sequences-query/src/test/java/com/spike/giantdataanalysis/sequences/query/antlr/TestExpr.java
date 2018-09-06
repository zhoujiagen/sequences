package com.spike.giantdataanalysis.sequences.query.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestExpr {
    private static final Logger LOG = LoggerFactory.getLogger(TestExpr.class);

    public static void main(String[] args) throws Exception {
        ANTLRErrorStrategy errorStrategy = new DefaultErrorStrategy() {
        };

        ANTLRErrorListener errorListener = new BaseErrorListener() {
        };

        try {
            CharStream cs = CharStreams.fromString("100+3)2\n");//CharStreams.fromStream(System.in);
            ExprLexer exprLexer = new ExprLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(exprLexer);
            ExprParser exprParser = new ExprParser(tokens);
            exprParser.setErrorHandler(errorStrategy);
            exprLexer.addErrorListener(errorListener);

            ParseTree tree = exprParser.prog();
            System.out.println(tree.toStringTree(exprParser));
        } catch (Exception e) {

            LOG.error("parse failed: " + e.getMessage(), e);
        }
    }
}
