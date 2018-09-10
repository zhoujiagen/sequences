package com.spike.giantdataanalysis.sequences.query.antlr;

import com.spike.giantdataanalysis.sequences.query.antlr.sparql.SPARQLQueryLexer;
import com.spike.giantdataanalysis.sequences.query.antlr.sparql.SPARQLQueryParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class TestSPARQLQuery {
    public static void main(String[] args) throws Exception {

        //CharStreams.fromStream(System.in);
        CharStream cs = CharStreams.fromString("?p*(1+?discount)");
        SPARQLQueryLexer exprLexer = new SPARQLQueryLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(exprLexer);
        SPARQLQueryParser exprParser = new SPARQLQueryParser(tokens);

        ParseTree tree = exprParser.gNumericExpression();
        System.out.println(tree.toStringTree(exprParser));
    }
}
