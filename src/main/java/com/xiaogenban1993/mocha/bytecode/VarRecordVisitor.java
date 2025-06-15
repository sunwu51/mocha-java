package com.xiaogenban1993.mocha.bytecode;

import com.xiaogenban1993.mocha.antlr.MochaBaseVisitor;
import com.xiaogenban1993.mocha.antlr.MochaLexer;
import com.xiaogenban1993.mocha.antlr.MochaParser;
import com.xiaogenban1993.mocha.element.ErrorElement;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Frank
 * @date 2025/6/2 14:09
 */
public class VarRecordVisitor extends MochaBaseVisitor<String> {
    Set<String> localVars = new HashSet<>();
    Set<String> outerVars = new HashSet<>();

//    @Override
//    public String visitAssign(MochaParser.AssignContext ctx) {
//
//        MochaParser.PrimaryContext leftCtx = ctx.left;
//        // 变量引用
//        if (leftCtx instanceof MochaParser.UnaryExprContext) {
//            MochaParser.UnaryExprContext variableRefCtx = (MochaParser.UnaryExprContext)leftCtx;
//            if (variableRefCtx.unary() instanceof MochaParser.IdentContext) {
//                String name = ((MochaParser.IdentContext)variableRefCtx.unary()).IDENTIFIER().getText();
//                localVars.add(name);
//            } else {
//                throw new ErrorElement.MochaError("not support syntax", ctx);
//            }
//        }
//        return null;
//    }

    @Override
    public String visitIdent(MochaParser.IdentContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        // 不是本地声明的变量，则认为是外部捕捉的变量
        if (!localVars.contains(name)) {
            outerVars.add(name);
        }

        return null;
    }

    public ParseTree parseTree(String input) {
        MochaLexer lexer = new MochaLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ByteCodeGenerater.ThrowingErrorListener());
        MochaParser parser = new MochaParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(new ByteCodeGenerater.ThrowingErrorListener());
        ParseTree tree = parser.program();
        return tree;
    }
}
