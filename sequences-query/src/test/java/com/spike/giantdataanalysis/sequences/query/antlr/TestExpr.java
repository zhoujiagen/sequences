package com.spike.giantdataanalysis.sequences.query.antlr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestExpr {
    public static void main(String[] args) throws Exception {
        String regex = "[^\\^]+";

        Pattern pattern = Pattern.compile(regex);
        String a = "42342";
        Matcher matcher = pattern.matcher(a);
        System.out.print(matcher.matches());

//        CharStream cs = CharStreams.fromStream(System.in);
//        ExprLexer exprLexer = new com.spike.giantdataanalysis.sequences.query.antlr.ExprLexer(cs);
//        CommonTokenStream tokens = new CommonTokenStream(exprLexer);
//        ExprParser exprParser = new ExprParser(tokens);
//
//        ParseTree tree = exprParser.prog();
//        System.out.println(tree.toStringTree(exprParser));
    }
}
