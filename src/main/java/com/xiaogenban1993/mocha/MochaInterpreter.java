package com.xiaogenban1993.mocha;

import com.xiaogenban1993.mocha.antlr.MochaBaseVisitor;
import com.xiaogenban1993.mocha.antlr.MochaLexer;
import com.xiaogenban1993.mocha.antlr.MochaParser;
import com.xiaogenban1993.mocha.element.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.*;
import org.apache.commons.text.StringEscapeUtils;
import com.xiaogenban1993.mocha.sdk.Sdk;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Frank
 * @date 2025/2/16 15:09
 */
public class MochaInterpreter extends MochaBaseVisitor<Element> {

    // 放置自定义上下文的栈，当前上下文位于栈顶
    Stack<Context> mctxStack = new Stack<>();

    // 当前要解释的语法，用于抛出异常时候定位位置
    ParserRuleContext curRuleCtx;

    // 默认把内建的sdk引入
    {
        mctxStack.push(new Context(Sdk.getBuildInContext()));
    }

    // 当前上下文位于栈顶
    public Context getMctx() {
        return mctxStack.peek();
    }


    /**
     * var语句，将【变量名-变量值】设置到上下文中
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitVarStatement(MochaParser.VarStatementContext ctx) {
        curRuleCtx = ctx;
        String key = ctx.IDENTIFIER().getText();
        Element value = visitExpression(ctx.expression());
        getMctx().set(key, value);
        return Constants.NULL;
    }

    /**
     * return语句，设置上下文的return值，并返回null
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitReturnStatement(MochaParser.ReturnStatementContext ctx) {
        curRuleCtx = ctx;
        Element res = visit(ctx.expression());
        getMctx().setReturnElement(res);
        return Constants.NULL;
    }

    /**
     * throw语句，抛出异常
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitThrowStatement(MochaParser.ThrowStatementContext ctx) {
        curRuleCtx = ctx;
        Element err = visitExpression(ctx.expression());
        if (err instanceof ErrorElement) {
            ErrorElement.MochaError mochaError = new ErrorElement.MochaError((ErrorElement)err);
            mochaError.pushStack(null, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
            throw mochaError;
        }
        throw new ErrorElement.MochaError("throw error", ctx);
    }

    /**
     * try-catch语句，捕捉自定义异常，将异常封装设置到catch块变量中
     * @param ctx the parse tree
     * @return
     */

    @Override
    public Element visitTryCatchStatement(MochaParser.TryCatchStatementContext ctx) {
        curRuleCtx = ctx;
        try {
            Context tryCtx = new Context(getMctx());
            return visitBlockStatement(ctx.tryBody, tryCtx);
        } catch (ErrorElement.MochaError e) {
            Context catchCtx = new Context(getMctx());
            catchCtx.set(ctx.IDENTIFIER().getText(), e.error);
            visitBlockStatement(ctx.catchBody, catchCtx);
            return Constants.NULL;
        }
    }

    /**
     * if语句，满足cond运行ifBody，否则运行elseBody
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitIfStatement(MochaParser.IfStatementContext ctx) {
        curRuleCtx = ctx;
        Element cond = visit(ctx.cond);
        if (cond.toBooleanElement() == Constants.TRUE) {
            visitBlockStatement(ctx.ifBody);
        } else if (ctx.elseBody != null){
            visitBlockStatement(ctx.elseBody);
        }
        return Constants.NULL;
    }


    /**
     * 表达式语句，运行表达式返回结果。
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitExpresstionStatement(MochaParser.ExpresstionStatementContext ctx) {
        curRuleCtx = ctx;
        return visit(ctx.expression());
    }

    /**
     * for循环中设置break
     * @param ctx the parse tree
     * @return
     */

    @Override
    public Element visitBreakStatement(MochaParser.BreakStatementContext ctx) {
        curRuleCtx = ctx;
        getMctx().setBreak();
        return Constants.NULL;
    }

    /**
     * for循环中设置continue
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitContinueStatement(MochaParser.ContinueStatementContext ctx) {
        getMctx().setContinue();
        return Constants.NULL;
    }

    /**
     * for循环语句
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitForStatement(MochaParser.ForStatementContext ctx) {
        curRuleCtx = ctx;
        // 1 运行init语句
        visit(ctx.init);
        while(true) {
            // 2 判断是否满足cond
            Element cond = visit(ctx.cond);
            if (cond.toBooleanElement() == Constants.FALSE) {
                break;
            }
            // 3 满足cond运行body
            Context newCtx = new Context(getMctx());
            newCtx.forCtx.inFor = true; // 设置inFor在for循环中的标识
            visitBlockStatement(ctx.body, newCtx);
            if (newCtx.funCtx.returnElement != null || newCtx.forCtx.isBreak) {
                break; // 如果设置了return或break，则结束循环
            }
            // 4 运行后，指定step
            visit(ctx.step);
        }
        return  Constants.NULL;
    }

    /**
     * 块语句，默认是新创建一个上下文，来运行内部的语句
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitBlockStatement(MochaParser.BlockStatementContext ctx) {
        curRuleCtx = ctx;
        return visitBlockStatement(ctx, new Context(getMctx()));
    }

    /**
     * class声明的语句，创建一个ProtoElement放置到上下文中
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitClassStatement(MochaParser.ClassStatementContext ctx) {
        curRuleCtx = ctx;
        String className = ctx.className.getText();
        String parentName = ctx.parentName == null ? null : ctx.parentName.getText();
        ProtoElement parent =  null;
        if (parentName != null) {
            Element parentElement = getMctx().get(parentName);
            if (parentElement == null) {
                throw new ErrorElement.MochaError("Class " + className + " extends undefined class " + parentName, ctx);
            }
            if (!(parentElement instanceof ProtoElement)) {
                throw new ErrorElement.MochaError("Class " + className + " extends " + parentName + " which is not a class", ctx);
            }
            parent = (ProtoElement)parentElement;
        }

        ProtoElement protoElement = new ProtoElement(className);

        // 方法
        MochaParser.ConstructorDeclarationContext consCtx = ctx.classBody().constructorDeclaration();

        StringBuilder fieldsCode = new StringBuilder();
        for (MochaParser.ClassElementContext classElementContext : ctx.classBody().classElement()) {
            // 普通方法
            if (classElementContext.methodDeclaration() != null) {
                MochaParser.MethodDeclarationContext methodDeclarationContext = classElementContext.methodDeclaration();
                String key = methodDeclarationContext.IDENTIFIER().getText();
                MochaParser.ParamsContext paramsCtx = methodDeclarationContext.params();
                List<String> params = paramsCtx.IDENTIFIER().stream().map(ParseTree::getText).collect(Collectors.toList());
                MochaParser.BlockStatementContext blockStatementCtx = methodDeclarationContext.blockStatement();
                protoElement.set(key, new FunctionElement(params, blockStatementCtx, getMctx()));
                continue;
            }
            // 属性
            if (classElementContext.fieldDeclaration() != null) {
                // 去糖之后的句子
                String newCode = ("this." + classElementContext.fieldDeclaration().IDENTIFIER().getText()) + (classElementContext.fieldDeclaration().ASSGIN() == null ? ";" :  "=" + classElementContext.fieldDeclaration().expression().getText() + ";");
                fieldsCode.append("\n").append(newCode);
            }
        }
        String constructorCode = String.format("{%s%s}", consCtx == null ? "super();\n" : consCtx.constructorBody().getText(), fieldsCode);
        MochaLexer lexer = new MochaLexer(CharStreams.fromString(constructorCode));
        // 重新设置行号列号
        lexer.setTokenFactory(new PositionAdjustingTokenFactory(consCtx != null ? consCtx.getStart().getLine() : ctx.getStart().getLine(),
                consCtx != null ? consCtx.getStart().getCharPositionInLine() : ctx.getStart().getCharPositionInLine()));
        MochaParser parser = new MochaParser(new CommonTokenStream(lexer));
        MochaParser.BlockStatementContext newConstructorBody = parser.blockStatement();
        List<String> params = consCtx == null ? Collections.emptyList() : consCtx.params().IDENTIFIER().stream().map(ParseTree::getText).collect(Collectors.toList());
        FunctionElement constructor = new FunctionElement(consCtx == null ? Collections.emptyList() : params, newConstructorBody, getMctx());
        protoElement.set("constructor", constructor);

        if (parent != null) {
            protoElement.map.put("$$pro$$", parent);
        }
        getMctx().set(className, protoElement);
        return Constants.NULL;
    }

    /**
     * 最低优先级 = 操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitAssign(MochaParser.AssignContext ctx) {
        curRuleCtx = ctx;
        Element right = visit(ctx.right);
        MochaParser.LeftValueContext leftCtx = ctx.leftValue();
        // 变量引用
        if (leftCtx instanceof MochaParser.LeftUnaryContext) {
            MochaParser.LeftUnaryContext variableRefCtx = (MochaParser.LeftUnaryContext)leftCtx;
            if (variableRefCtx.unary() instanceof MochaParser.IdentContext) {
                getMctx().set(((MochaParser.IdentContext)variableRefCtx.unary()).IDENTIFIER().getText(), right);
            } else {
                throw new ErrorElement.MochaError("not support syntax", ctx);
            }
            return right;
        }

        // 点属性访问
        if (leftCtx instanceof MochaParser.PropertyAccessContext) {
            MochaParser.PropertyAccessContext propertyAccessCtx = (MochaParser.PropertyAccessContext)leftCtx;
            Element _this = visit(propertyAccessCtx.leftValue());
            _this.set(propertyAccessCtx.IDENTIFIER().getText(), right);
            return right;
        }
        // []属性访问
        if (leftCtx instanceof MochaParser.ArrayAccessContext) {
            MochaParser.ArrayAccessContext arrayAccessCtx = (MochaParser.ArrayAccessContext)leftCtx;
            Element _this = visit(arrayAccessCtx.leftValue());
            Element index = visit(arrayAccessCtx.expression());
            if (index instanceof StringElement) {
                _this.set(index.toString(), right);
            } else if (index instanceof NumberElement) {
                double v = ((NumberElement) index).value;
                int iv = (int) v;
                if (v % 1 != 0) {
                    throw new ErrorElement.MochaError("数组索引只能是整数", ctx);
                }
                if (_this instanceof ArrayElement) {
                    ArrayElement arr = (ArrayElement) _this;
                    arr.array.set(iv, right);
                }
            } else {
                throw new ErrorElement.MochaError("not support", ctx);
            }
            return right;
        }
        if (leftCtx instanceof MochaParser.PropertyAccessInFunCallContext) {
            MochaParser.PropertyAccessInFunCallContext pctx = (MochaParser.PropertyAccessInFunCallContext)leftCtx;
            String code = pctx.leftValue().getText() + "(" + pctx.expression().stream().map(RuleContext::getText).collect(Collectors.joining(",")) + ")";
            MochaLexer lexer = new MochaLexer(CharStreams.fromString(code));
            lexer.setTokenFactory(new PositionAdjustingTokenFactory(pctx.getStart().getLine(), pctx.getStart().getCharPositionInLine()));
            MochaParser parser = new MochaParser(new CommonTokenStream(lexer));
            MochaParser.FunctionCallOrPointExpressionContext funcOrPointExpressionContext = parser.functionCallOrPointExpression();
            Element _this = visit(funcOrPointExpressionContext);
            _this.set(pctx.IDENTIFIER().getText(), right);
            return right;
        }
        if (leftCtx instanceof MochaParser.ArrayAccessInFunCallContext) {
            MochaParser.ArrayAccessInFunCallContext actx = (MochaParser.ArrayAccessInFunCallContext)leftCtx;
            String code = actx.leftValue().getText() + "(" + actx.expression().stream().map(RuleContext::getText).collect(Collectors.joining(",")) + ")";
            MochaLexer lexer = new MochaLexer(CharStreams.fromString(code));
            lexer.setTokenFactory(new PositionAdjustingTokenFactory(actx.getStart().getLine(), actx.getStart().getCharPositionInLine()));
            MochaParser parser = new MochaParser(new CommonTokenStream(lexer));
            MochaParser.FunctionCallOrPointExpressionContext funcOrPointExpressionContext = parser.functionCallOrPointExpression();
            Element _this = visit(funcOrPointExpressionContext);
            Element index = visit(actx.index);
            if (index instanceof StringElement) {
                _this.set(index.toString(), right);
            } else if (index instanceof NumberElement) {
                double v = ((NumberElement) index).value;
                int iv = (int) v;
                if (v % 1 != 0) {
                    throw new ErrorElement.MochaError("数组索引只能是整数", ctx);
                }
                if (_this instanceof ArrayElement) {
                    ArrayElement arr = (ArrayElement) _this;
                    arr.array.set(iv, right);
                }
            } else {
                throw new ErrorElement.MochaError("not support", ctx);
            }
            return right;
        }
        throw new ErrorElement.MochaError("not support", ctx);
    }

    /**
     * 赋值语句中的左值为属性访问的情况，如a.b.c
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitPropertyAccess(MochaParser.PropertyAccessContext ctx) {
        curRuleCtx = ctx;
        Element _this = visit(ctx.leftValue());
        String key = ctx.IDENTIFIER().getText();
        return _this.get(key);
    }

    /**
     * 赋值语句中左值为数组访问属性的情况，如a["name"]
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitArrayAccess(MochaParser.ArrayAccessContext ctx) {
        curRuleCtx = ctx;
        Element _this = visit(ctx.leftValue());
        Element index = visit(ctx.expression());
        if (index instanceof StringElement) {
            return _this.get(((StringElement)index).value);
        }
        if (index instanceof NumberElement) {
            double v = ((NumberElement) index).value;
            int iv = (int) v;
            if (v % 1 != 0) {
                throw new ErrorElement.MochaError("数组索引只能是整数", ctx);
            }
            if (_this instanceof ArrayElement) {
                ArrayElement arr = (ArrayElement) _this;
                return arr.array.get(iv);
            }
        }
        throw new ErrorElement.MochaError("not support", ctx);
    }

    /**
     * 赋值语句中左值为函数调用结果的属性访问如add(a, b).name
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitPropertyAccessInFunCall(MochaParser.PropertyAccessInFunCallContext ctx) {
        curRuleCtx = ctx;
        MochaLexer lexer = new MochaLexer(CharStreams.fromString(ctx.getText()));
        lexer.setTokenFactory(new PositionAdjustingTokenFactory(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine()));
        MochaParser parser = new MochaParser(new CommonTokenStream(lexer));
        return visit(parser.functionCallOrPointExpression());
    }

    /**
     * 赋值语句中左值为函数返回值的数组访问属性，add(a,b)["name"]
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitArrayAccessInFunCall(MochaParser.ArrayAccessInFunCallContext ctx) {
        curRuleCtx = ctx;
        MochaLexer lexer = new MochaLexer(CharStreams.fromString(ctx.getText()));
        lexer.setTokenFactory(new PositionAdjustingTokenFactory(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine()));
        MochaParser parser = new MochaParser(new CommonTokenStream(lexer));
        return visit(parser.functionCallOrPointExpression());
    }


    /**
     * && || 操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitAnd(MochaParser.AndContext ctx) {
        curRuleCtx = ctx;
        Element left = visit(ctx.left).toBooleanElement();

        if (ctx.op.getType() == MochaParser.AND) {
            if (left == Constants.FALSE ||
                    visit(ctx.right).toBooleanElement() == Constants.FALSE) {
                return Constants.FALSE;
            }
            return Constants.TRUE;
        } else if (ctx.op.getType() == MochaParser.OR) {
            if (left == Constants.TRUE ||
                    visit(ctx.right).toBooleanElement() == Constants.TRUE) {
                return Constants.TRUE;
            }
            return Constants.FALSE;
        }
        throw new ErrorElement.MochaError("not support", ctx);
    }

    /**
     * == != 操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitEq(MochaParser.EqContext ctx) {
        curRuleCtx = ctx;
        Element left = visit(ctx.left);
        Element right = visit(ctx.right);
        boolean ok;
        if (left instanceof NumberElement && right instanceof NumberElement) {
            ok = ((NumberElement)left).value == ((NumberElement)right).value;
        } else if (left instanceof StringElement && right instanceof StringElement) {
            ok = Objects.equals(((StringElement) left).value, ((StringElement) right).value);
        } else {
            ok = left == right;
        }
        return ok ? Constants.TRUE : Constants.FALSE;
    }

    /**
     * 比较操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitComp(MochaParser.CompContext ctx) {
        curRuleCtx = ctx;
        Element left = visit(ctx.left);
        Element right = visit(ctx.right);
        if (!(left instanceof NumberElement && right instanceof NumberElement)) {
            throw new ErrorElement.MochaError("非数字", ctx);
        }
        NumberElement l = (NumberElement) left, r = (NumberElement) right;
        switch (ctx.op.getType()) {
            case MochaParser.GT:
                return l.value > r.value ? Constants.TRUE : Constants.FALSE;
            case MochaParser.GTE:
                return l.value >= r.value ? Constants.TRUE : Constants.FALSE;
            case MochaParser.LT:
                return l.value < r.value ? Constants.TRUE : Constants.FALSE;
            case MochaParser.LTE:
                return l.value <= r.value ? Constants.TRUE : Constants.FALSE;
        }
        throw new ErrorElement.MochaError("not support", ctx);
    }

    /**
     *  + - 操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitAdd(MochaParser.AddContext ctx) {
        curRuleCtx = ctx;
        Element left = visit(ctx.left);
        Element right = visit(ctx.right);
        if (ctx.op.getType() == MochaParser.PLUS) {
            if ((left instanceof StringElement || right instanceof StringElement)) {
                return new StringElement(left.toString() + right.toString());
            } else if (!(left instanceof NumberElement) || !(right instanceof NumberElement)) {
                throw new IllegalStateException("+ should be used for number or string");
            }
            NumberElement l = (NumberElement) left, r = (NumberElement) right;
            return new NumberElement(l.value + r.value);
        }

        if (ctx.op.getType() == MochaParser.MINUS) {
            if (!(left instanceof NumberElement) || !(right instanceof NumberElement)) {
                throw new IllegalStateException("- should be used for number");
            }
            NumberElement l = (NumberElement) left, r = (NumberElement) right;
            return new NumberElement(l.value - r.value);
        }
        throw new ErrorElement.MochaError("暂时不支持", ctx);
    }

    /**
     * * / 操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitMul(MochaParser.MulContext ctx) {
        curRuleCtx = ctx;
        Element left = visit(ctx.left);
        Element right = visit(ctx.right);

        if (!(left instanceof NumberElement && right instanceof NumberElement)) {
            throw new ErrorElement.MochaError("非数字", ctx);
        }
        NumberElement l = (NumberElement) left, r = (NumberElement) right;

        if (ctx.op.getType() == MochaParser.MULTIPLY) {
            return new NumberElement(l.value * r.value);
        }

        if (ctx.op.getType() == MochaParser.DIVIDE) {
            return new NumberElement(l.value / r.value);
        }

        if (ctx.op.getType() == MochaParser.MODULUS) {
            return new NumberElement(l.value % r.value);
        }

        throw new ErrorElement.MochaError("暂时不支持", ctx);
    }

    /**
     * 前缀操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitPrefix(MochaParser.PrefixContext ctx) {
        curRuleCtx = ctx;
        // right
        Element right = visit(ctx.right);
        switch (ctx.op.getType()) {
            case MochaParser.INCREMENT:
                if (right instanceof NumberElement) {
                    return new NumberElement(++((NumberElement)right).value);
                } else {
                    throw new ErrorElement.MochaError("not support", ctx);
                }
            case MochaParser.DECREMENT:
                if (right instanceof NumberElement) {
                    return new NumberElement(--((NumberElement)right).value);
                } else {
                    throw new ErrorElement.MochaError("not support", ctx);
                }
            case MochaParser.PLUS:
                if (right instanceof NumberElement) {
                    return new NumberElement(((NumberElement)right).value);
                } else {
                    throw new ErrorElement.MochaError("not support", ctx);
                }
            case MochaParser.MINUS:
                if (right instanceof NumberElement) {
                    return new NumberElement(-((NumberElement)right).value);
                } else {
                    throw new ErrorElement.MochaError("not support", ctx);
                }
            case MochaParser.NOT:
                return right.toBooleanElement() == Constants.TRUE ? Constants.FALSE : Constants.TRUE;
            default:
                throw new ErrorElement.MochaError("not support", ctx);
        }
    }

    /**
     * 后缀操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitPostfix(MochaParser.PostfixContext ctx) {
        curRuleCtx = ctx;
        // left
        Element left = visit(ctx.left);
        if (!(left instanceof NumberElement)) {
            throw new ErrorElement.MochaError("postfix should use in number", ctx);
        }
        NumberElement num = (NumberElement) left;
        switch (ctx.op.getType()) {
            case MochaParser.INCREMENT:
                return new NumberElement(num.value ++);
            case MochaParser.DECREMENT:
                return new NumberElement(num.value --);
            default:
                throw new ErrorElement.MochaError("not support", ctx);
        }
    }

    /**
     * 直接函数调用 如 func() 或 func()()等
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitFunCall(MochaParser.FunCallContext ctx) {
        curRuleCtx = ctx;
        // 求出func，直接调用
        if (ctx.left.getText().equals("print")) {
            System.out.println(ctx.expression().stream().map(it -> visit(it).toString()).collect(Collectors.joining(", ")));
            return Constants.NULL;
        }
        if (ctx.left.getText().equals("error")) {
            return new ErrorElement(visit(ctx.expression().get(0)).toString(), new Stack<>());
        }
        if (ctx.left.getText().equals("super")) {
            if (getMctx().get("super") == Constants.NULL) {
                return Constants.NULL;
            }

            ProtoElement _super_ = (ProtoElement) getMctx().get("super");
            functionCall((FunctionElement) _super_.map.get("constructor"), "constructor", ctx.expression().stream().map(this::visitExpression).collect(Collectors.toList()),
                    getMctx().get("this"),
                    _super_.getPrototype() == null ? Constants.NULL : _super_.getPrototype(), ctx);
            return Constants.NULL;
        }
        Element func = visit(ctx.left);
        if (func instanceof FunctionElement) {
            FunctionElement fun = (FunctionElement) func;
            List<Element> args = ctx.expression().stream().map(this::visitExpression).collect(Collectors.toList());
            return functionCall(fun, ctx.left.getText(), args, Constants.NULL, Constants.NULL, ctx);
        }
        throw new ErrorElement.MochaError("not a function", ctx);
    }

    /**
     * 对象的方法调用最后用点操作符， obj.func() 或 func().func() 等
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitPointFunCall(MochaParser.PointFunCallContext ctx) {
        curRuleCtx = ctx;
        // 求出_this和func，调用
        Element _this = visit(ctx.left);
        if (ctx.left.getText().equals("super")) {
            _this = getMctx().get("this");
        }
        Element func = _this.get(ctx.IDENTIFIER().getText());
        if (func instanceof FunctionElement) {
            FunctionElement fun = (FunctionElement) func;
            List<Element> args = ctx.expression().stream().map(this::visitExpression).collect(Collectors.toList());
            Element _super = Constants.NULL;
            if (_this.getPrototype() != null) {
                _super = _this.getPrototype().getPrototype() == null ? Constants.NULL : _this.getPrototype().getPrototype();
            }
            return functionCall(fun, ctx.IDENTIFIER().getText(), args, _this, _super, ctx);
        }
        throw new ErrorElement.MochaError("not a function", ctx);
    }


    /**
     * . 操作符，如a.b 或 a.b().c 等
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitPointProperty(MochaParser.PointPropertyContext ctx) {
        curRuleCtx = ctx;
        // 求出_this
        Element _this = visit(ctx.left);
        return _this.get(ctx.IDENTIFIER().getText());
    }

    /**
     * 对象的方法调用最后用["xx"]，如a["func"]()
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitIndexFunCall(MochaParser.IndexFunCallContext ctx) {
        curRuleCtx = ctx;
        // 求出_this和func，调用
        Element _this = visit(ctx.left);
        Element index = visit(ctx.index);
        if (index instanceof StringElement) {
            Element func =  _this.get(((StringElement)index).value);
            if (func instanceof FunctionElement) {
                FunctionElement fun = (FunctionElement) func;
                List<Element> args = ctx.expression().stream().map(this::visitExpression).collect(Collectors.toList());
                Element _super = Constants.NULL;
                if (_this.getPrototype() != null) {
                    _super = _this.getPrototype().getPrototype() == null ? Constants.NULL : _this.getPrototype().getPrototype();
                }
                return functionCall(fun, ((StringElement)index).value, args, _this, _super, ctx);
            }
            throw new ErrorElement.MochaError("not a function", ctx);
        }
        if (index instanceof NumberElement) {
            double v = ((NumberElement) index).value;
            int iv = (int) v;
            if (v % 1 != 0) {
                throw new ErrorElement.MochaError("数组索引只能是整数", ctx);
            }
            if (_this instanceof ArrayElement) {
                ArrayElement arr = (ArrayElement) _this;
                Element func = arr.array.get(iv);
                if (func instanceof FunctionElement) {
                    FunctionElement fun = (FunctionElement) func;
                    List<Element> args = ctx.expression().stream().map(this::visitExpression).collect(Collectors.toList());
                    Element _super = Constants.NULL;
                    if (_this.getPrototype() != null) {
                        _super = _this.getPrototype().getPrototype() == null ? Constants.NULL : _this.getPrototype().getPrototype();
                    }
                    return functionCall(fun, "<anonymous>", args, _this, _super, ctx);
                }
            }
        }
        throw new ErrorElement.MochaError("not support", ctx);
    }


    /**
     * 数组索引访问属性如arr[1] 或 obj["name"] 等
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitIndexProperty(MochaParser.IndexPropertyContext ctx) {
        curRuleCtx = ctx;
        // 求出_this
        Element _this = visit(ctx.left);
        Element index = visit(ctx.index);
        if (index instanceof StringElement) {
            return _this.get(((StringElement)index).value);
        }
        if (index instanceof NumberElement) {
            double v = ((NumberElement) index).value;
            int iv = (int) v;
            if (v % 1 != 0) {
                throw new ErrorElement.MochaError("数组索引只能是整数", ctx);
            }
            if (_this instanceof ArrayElement) {
                ArrayElement arr = (ArrayElement) _this;
                return arr.array.get(iv);
            }
            if (_this instanceof StringElement) {
                String str = ((StringElement) _this).value;
                if (iv < 0 || iv >= str.length()) {
                    throw new ErrorElement.MochaError("字符串索引越界", ctx);
                }
                return new StringElement(str.substring(iv, iv + 1));
            }
        }
        throw new ErrorElement.MochaError("not support", ctx);
    }


    /**
     * 单元-数字
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitNumber(MochaParser.NumberContext ctx) {
        curRuleCtx = ctx;
        return new NumberElement(Double.parseDouble(ctx.NUMBER().getText()));
    }

    /**
     * 单元-字符串
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitString(MochaParser.StringContext ctx) {
        curRuleCtx = ctx;
        // 反转义例如 "\n" =》 回车
        String str = StringEscapeUtils.unescapeJava(ctx.STRING().getText());
        return new StringElement(str.substring(1, str.length() - 1));
    }

    /**
     * 单元-null
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitNull(MochaParser.NullContext ctx) {
        curRuleCtx = ctx;
        return Constants.NULL;
    }

    /**
     * 单元-false
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitFalse(MochaParser.FalseContext ctx) {
        curRuleCtx = ctx;
        return Constants.FALSE;
    }

    /**
     * 单元-true
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitTrue(MochaParser.TrueContext ctx) {
        curRuleCtx = ctx;
        return Constants.TRUE;
    }

    /**
     * 单元-数组字面量 [1, true, "2", null]
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitArray(MochaParser.ArrayContext ctx) {
        curRuleCtx = ctx;
        List<MochaParser.ExpressionContext> exps = ctx.expression();
        List<Element> array = exps.stream().map(this::visitExpression).collect(Collectors.toList());
        return new ArrayElement(array);
    }

    /**
     * 单元-对象字面量 {"name": "zhang", age: 10}
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitObject(MochaParser.ObjectContext ctx) {
        curRuleCtx = ctx;
        List<MochaParser.PairContext> pairContexts = ctx.pair();
        Map<String, Element> map = new HashMap<>();
        pairContexts.forEach(item -> {
            String key = null;
            if (item.key.getType() == MochaParser.STRING) {
                key = item.key.getText().substring(1, item.key.getText().length() - 1);
            } else {
                key = item.key.getText();
            }
            Element val = visitExpression(item.value);
            map.put(key, val);
        });
        Element e = new Element();
        e.map = map;
        return e;
    }

    /**
     * 单元-分组 (1 + 3 * b)
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitGroup(MochaParser.GroupContext ctx) {
        curRuleCtx = ctx;
        return visitExpression(ctx.expression());
    }

    /**
     * 单元-函数声明 function(a,b){return a+b;}
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitFunction(MochaParser.FunctionContext ctx) {
        curRuleCtx = ctx;
        List<String> params = ctx.params().IDENTIFIER().stream().map(ParseTree::getText).collect(Collectors.toList());
        MochaParser.BlockStatementContext blockStatementContext = ctx.blockStatement();
        return new FunctionElement(params, blockStatementContext, getMctx());
    }

    /**
     * 单元-标识符 a
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitIdent(MochaParser.IdentContext ctx) {
        curRuleCtx = ctx;
        String name = ctx.IDENTIFIER().getText();
        return getMctx().get(name);
    }

    /**
     * 单元-特殊字符super
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitSuper(MochaParser.SuperContext ctx) {
        return getMctx().get("super");
    }

    /**
     * 单元-new对象 new Person("xx")
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Element visitNew(MochaParser.NewContext ctx) {
        curRuleCtx = ctx;
        String clsName = ctx.IDENTIFIER().getText();
        Element cls = getMctx().get(clsName);
        if (cls instanceof ProtoElement) {
            ProtoElement curPrototype = (ProtoElement)cls;
            Element obj = new Element();
            List<Element> args = ctx.expression().stream().map(this::visitExpression).collect(Collectors.toList());
            Element parent = curPrototype.getPrototype();
            functionCall((FunctionElement) curPrototype.map.get("constructor"), "constructor", args, obj, parent == null ? Constants.NULL : parent, ctx);
            obj.setPrototype(curPrototype);
            return obj;
        }
        throw new ErrorElement.MochaError("class " + clsName + " not exist", ctx);
    }


    // 函数调用中有块语句，需要包装上下文
    private Element functionCall(FunctionElement ele, String name, List<Element> args, Element _this, Element _super, ParserRuleContext tree) {
        if (ele instanceof NativeFunctionElement) {
            try {
                return ((NativeFunctionElement) ele).call(_this, args);
            } catch (Exception e) {
                throw new ErrorElement.MochaError(e.getMessage(), tree);
            }
        }

        // 闭包上下文
        Context newCtx = new Context(ele.getClosureCtx());
        for (int i=0; i<ele.getParams().size(); i++) {
            String param = ele.getParams().get(i);
            if (i < args.size()) {
                newCtx.set(param, args.get(i));
            } else {
                newCtx.set(param, Constants.NULL);
            }
        }
        newCtx.set("this", _this);
        newCtx.set("super", _super);
        newCtx.funCtx.name = name;
        mctxStack.push(newCtx);
        try {
            visitBlockStatement(ele.getBody(), newCtx);
        } catch (ErrorElement.MochaError e) {
            e.fillFunctionName(name);
            e.pushStack(null, tree.getStart().getLine(), tree.getStart().getCharPositionInLine());
            throw e;
        } finally {
            mctxStack.pop();
        }
        return newCtx.funCtx.returnElement == null ? Constants.NULL : newCtx.funCtx.returnElement;
    }


    private Element visitBlockStatement(MochaParser.BlockStatementContext ctx, Context mctx) {
        mctxStack.push(mctx);
        try {
            Element res = Constants.NULL;
            for (MochaParser.StatementContext statementContext : ctx.statement()) {
                if (getMctx().funCtx.returnElement != null || getMctx().forCtx.isBreak || getMctx().forCtx.isContinue)
                    break;
                res = visitStatement(statementContext);
            }
            return res;
        } finally {
            mctxStack.pop();
        }
    }

    public static void main(String[] args) throws Exception {
        MochaInterpreter visitor = new MochaInterpreter();
        if (args != null && args.length > 0) {
            // 第一个参数为文件名
            String input = Files.readString(Path.of(args[0]));
            try {
                Element result = visitor.visit(parseTree(input));
            } catch (ErrorElement.MochaError e) {
                e.printStackTrace();
                System.exit(1);
            } catch (Exception e) {
                ErrorElement.MochaError mochaError = new ErrorElement.MochaError(e.getMessage(), visitor.curRuleCtx);
                mochaError.printStackTrace();
                System.exit(1);
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            // repl
            while (true) {
                System.out.print(">> ");
                String input = scanner.nextLine();
                try {
                    if (!input.endsWith(";")) {
                        input += ";";
                    }
                    Element result = visitor.visit(parseTree(input));
                    System.out.println(result);
                } catch (ErrorElement.MochaError e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    ErrorElement.MochaError mochaError = new ErrorElement.MochaError(e.getMessage(), visitor.curRuleCtx);
                    mochaError.printStackTrace();
                }
            }
        }

    }

    private static ParseTree parseTree(String input) {
        MochaLexer lexer = new MochaLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ThrowingErrorListener());
        MochaParser parser = new MochaParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(new ThrowingErrorListener());
        ParseTree tree = parser.program();
        return tree;
    }

    public static class ThrowingErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line,
                                int charPositionInLine,
                                String msg,
                                RecognitionException e) {
            throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg);
        }
    }


}
