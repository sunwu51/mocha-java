package com.xiaogenban1993.mocha.bytecode;

import com.xiaogenban1993.mocha.antlr.MochaBaseVisitor;
import com.xiaogenban1993.mocha.antlr.MochaLexer;
import com.xiaogenban1993.mocha.antlr.MochaParser;
import com.xiaogenban1993.mocha.element.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.*;
import org.apache.commons.text.StringEscapeUtils;
import org.objectweb.asm.*;

import com.xiaogenban1993.mocha.sdk.Sdk;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author Frank
 * @date 2025/5/31 12:50
 */
public class ByteCodeGenerater extends MochaBaseVisitor<String> implements Opcodes {
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

    MethodVisitor methodVisitor = AsmUtils.generateMainClass(classWriter);

    int localVariableIndex = 1;

    int tempMethodIndex = 0;

    static final String ELEMENT_TYPE = Element.class.getName().replace(".", "/");
    static final String NUMBER_TYPE = NumberElement.class.getName().replace(".", "/");
    static final String STRING_TYPE = StringElement.class.getName().replace(".", "/");
    static final String BOOLEAN_TYPE = BooleanElement.class.getName().replace(".", "/");
    static final String NULL_TYPE = NullElement.class.getName().replace(".", "/");
    static final String ARRAY_TYPE = ArrayElement.class.getName().replace(".", "/");
    static final String FUNCTION_TYPE = FunctionElement.class.getName().replace(".", "/");

    static final String CONSTANTS = "com/xiaogenban1993/mocha/element/Constants";


    Stack<Context> ctxStack = new Stack<>();

    {
        ctxStack.push(new Context());
    }


    /**
     * var语句，将【变量名-变量值】设置到上下文中
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitVarStatement(MochaParser.VarStatementContext ctx) {
        String key = ctx.IDENTIFIER().getText();
        // 此时栈顶为表达式结果
        String className = visitExpression(ctx.expression());
        AsmUtils.setLocalVariable(methodVisitor, key, localVariableIndex, className);
        ctxStack.peek().set(key, localVariableIndex);
        localVariableIndex++;
        return null;
    }

    /**
     * return语句，设置上下文的return值，并返回null
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitReturnStatement(MochaParser.ReturnStatementContext ctx) {
        visit(ctx.expression());
        methodVisitor.visitInsn(ARETURN);
        return null;
    }

    /**
     * 表达式语句，运行表达式返回结果。
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitExpresstionStatement(MochaParser.ExpresstionStatementContext ctx) {
        visit(ctx.expression());
        methodVisitor.visitInsn(POP);
        return null;
    }

    /**
     * 块语句，默认是新创建一个上下文，来运行内部的语句
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitBlockStatement(MochaParser.BlockStatementContext ctx) {
        return visitBlockStatement(ctx, new Context());
    }

    /**
     * 最低优先级 = 操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitAssign(MochaParser.AssignContext ctx) {
        visit(ctx.right);
        methodVisitor.visitInsn(DUP);

        MochaParser.PrimaryContext leftCtx = ctx.left;
        // 变量引用
        if (leftCtx instanceof MochaParser.UnaryExprContext) {
            MochaParser.UnaryExprContext variableRefCtx = (MochaParser.UnaryExprContext)leftCtx;
            if (variableRefCtx.unary() instanceof MochaParser.IdentContext) {
                String name = ((MochaParser.IdentContext)variableRefCtx.unary()).IDENTIFIER().getText();
                Integer index = ctxStack.peek().get(name);
                if (index == null) {
                    throw new RuntimeException("编译错误，变量不存在:" + name);
                }
                AsmUtils.setLocalVariable(methodVisitor, name, index, ELEMENT_TYPE);
            } else {
                throw new ErrorElement.MochaError("not support syntax", ctx);
            }
            return ELEMENT_TYPE;
        }
        // 点属性访问
        if (leftCtx instanceof MochaParser.PropertyExprContext) {
            MochaParser.PropertyExprContext propertyExprContext = (MochaParser.PropertyExprContext)leftCtx;
            visit(propertyExprContext.left);
            methodVisitor.visitInsn(SWAP); // h v [top]
            String fieldName = propertyExprContext.IDENTIFIER().getText();
            methodVisitor.visitLdcInsn(fieldName); // h v k [top]
            methodVisitor.visitInsn(SWAP); // h k v [top]
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, ELEMENT_TYPE, "set", "(Ljava/lang/String;L"+ELEMENT_TYPE+";)V", false);
//            _this.set(propertyAccessCtx.IDENTIFIER().getText(), right);
            return ELEMENT_TYPE;
        }
        // []属性访问
        if (leftCtx instanceof MochaParser.IndexExprContext) {
            MochaParser.IndexExprContext indexExprContext = (MochaParser.IndexExprContext)leftCtx;
            visit(indexExprContext.left);
            methodVisitor.visitInsn(SWAP); // h v [top]

            visit(indexExprContext.expression()); // h v k [top]
            methodVisitor.visitInsn(SWAP); // h k v [top]
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, ELEMENT_TYPE, "set", "(L"+ELEMENT_TYPE+";L"+ELEMENT_TYPE+";)V", false);
            return ELEMENT_TYPE;
        }
        throw new ErrorElement.MochaError("not support", ctx);
    }


    /**
     * ||操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitOr(MochaParser.OrContext ctx) {
        visit(ctx.left);
        visit(ctx.right);
        methodVisitor.visitLdcInsn(ctx.op.getText());
        methodVisitor.visitMethodInsn(INVOKESTATIC, ELEMENT_TYPE, "option",
                "(" + typeDesc(ELEMENT_TYPE) + typeDesc(ELEMENT_TYPE) + "Ljava/lang/String;)" + typeDesc(ELEMENT_TYPE), false);
        methodVisitor.visitTypeInsn(CHECKCAST, BOOLEAN_TYPE);
        // 栈顶的类型是booleanElement
        return BOOLEAN_TYPE;
    }

    /**
     * &&操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitAnd(MochaParser.AndContext ctx) {
        visit(ctx.left);
        visit(ctx.right);
        methodVisitor.visitLdcInsn(ctx.op.getText());
        methodVisitor.visitMethodInsn(INVOKESTATIC, ELEMENT_TYPE, "option",
                "(" + typeDesc(ELEMENT_TYPE) + typeDesc(ELEMENT_TYPE) + "Ljava/lang/String;)" + typeDesc(ELEMENT_TYPE), false);
        methodVisitor.visitTypeInsn(CHECKCAST, BOOLEAN_TYPE);
        // 栈顶的类型是booleanElement
        return BOOLEAN_TYPE;
    }

    /**
     * == != 操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitEq(MochaParser.EqContext ctx) {
        visit(ctx.left);
        visit(ctx.right);
        methodVisitor.visitLdcInsn(ctx.op.getText());

        methodVisitor.visitMethodInsn(INVOKESTATIC, ELEMENT_TYPE, "option",
                "(" + typeDesc(ELEMENT_TYPE) + typeDesc(ELEMENT_TYPE) + "Ljava/lang/String;)" + typeDesc(ELEMENT_TYPE), false);
        methodVisitor.visitTypeInsn(CHECKCAST, BOOLEAN_TYPE);
        return BOOLEAN_TYPE;
    }

    /**
     * 比较操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitComp(MochaParser.CompContext ctx) {
        visit(ctx.left);
        visit(ctx.right);
        methodVisitor.visitLdcInsn(ctx.op.getText());
        methodVisitor.visitMethodInsn(INVOKESTATIC, ELEMENT_TYPE, "option",
                "(" + typeDesc(ELEMENT_TYPE) + typeDesc(ELEMENT_TYPE) + "Ljava/lang/String;)" + typeDesc(ELEMENT_TYPE), false);
        methodVisitor.visitTypeInsn(CHECKCAST, BOOLEAN_TYPE);
        return BOOLEAN_TYPE;
    }

    /**
     *  + - 操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitAdd(MochaParser.AddContext ctx) {
        visit(ctx.left);
        visit(ctx.right);
        methodVisitor.visitLdcInsn(ctx.op.getText());
        methodVisitor.visitMethodInsn(INVOKESTATIC, ELEMENT_TYPE, "option",
                "(" + typeDesc(ELEMENT_TYPE) + typeDesc(ELEMENT_TYPE) + "Ljava/lang/String;)" + typeDesc(ELEMENT_TYPE), false);
        return ELEMENT_TYPE;
    }

    /**
     * * / 操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitMul(MochaParser.MulContext ctx) {
        visit(ctx.left);
        visit(ctx.right);
        methodVisitor.visitLdcInsn(ctx.op.getText());

        methodVisitor.visitMethodInsn(INVOKESTATIC, ELEMENT_TYPE, "option",
                "(" + typeDesc(ELEMENT_TYPE) + typeDesc(ELEMENT_TYPE) + "Ljava/lang/String;)" + typeDesc(ELEMENT_TYPE), false);
        methodVisitor.visitTypeInsn(CHECKCAST, NUMBER_TYPE);

        return NUMBER_TYPE;
    }

    /**
     * 前缀操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitPrefix(MochaParser.PrefixContext ctx) {

        methodVisitor.visitInsn(ACONST_NULL);
        // right
        visit(ctx.right);


        methodVisitor.visitLdcInsn(ctx.op.getText());

        methodVisitor.visitMethodInsn(INVOKESTATIC, ELEMENT_TYPE, "option",
                "(" + typeDesc(ELEMENT_TYPE) + typeDesc(ELEMENT_TYPE) + "Ljava/lang/String;)" + typeDesc(ELEMENT_TYPE), false);
        return ELEMENT_TYPE;
    }

    /**
     * 后缀操作符
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitPostfix(MochaParser.PostfixContext ctx) {
        // left
        visit(ctx.left);
        methodVisitor.visitInsn(ACONST_NULL);
        methodVisitor.visitLdcInsn(ctx.op.getText());
        methodVisitor.visitMethodInsn(INVOKESTATIC, ELEMENT_TYPE, "option",
                "(" + typeDesc(ELEMENT_TYPE) + typeDesc(ELEMENT_TYPE) + "Ljava/lang/String;)" + typeDesc(ELEMENT_TYPE), false);

        methodVisitor.visitTypeInsn(CHECKCAST, NUMBER_TYPE);
        return NUMBER_TYPE;
    }

    /**
     * . 操作符，如a.b 或 a.b().c 等
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitPropertyExpr(MochaParser.PropertyExprContext ctx) {
        // 求出_this type
        String _this = visit(ctx.left);
        String fieldName = ctx.IDENTIFIER().getText();
        methodVisitor.visitLdcInsn(fieldName);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, _this, "get", "(Ljava/lang/String;)L" + ELEMENT_TYPE + ";", false);
        return ELEMENT_TYPE;
    }

    /**
     * 数组索引访问属性如arr[1] 或 obj["name"] 等
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitIndexExpr(MochaParser.IndexExprContext ctx) {
        // _this index [top]
        visit(ctx.left);
        visit(ctx.expression());
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, ELEMENT_TYPE, "get", "(L" + ELEMENT_TYPE +";)L" + ELEMENT_TYPE + ";", false);

        return ELEMENT_TYPE;
    }

    /**
     * 函数调用 a() 或者 a.b()等
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitCallExpr(MochaParser.CallExprContext ctx) {
        // 求出func，直接调用
        if (ctx.left.getText().equals("print")) {

            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

            methodVisitor.visitTypeInsn(NEW, "java/lang/StringBuilder");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);

            for (MochaParser.ExpressionContext expressionContext : ctx.arguments().expression()) {
                visit(expressionContext);
                methodVisitor.visitMethodInsn(INVOKESTATIC, "java/util/Objects", "toString", "(Ljava/lang/Object;)Ljava/lang/String;", false);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            }
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            methodVisitor.visitFieldInsn(GETSTATIC, "com/xiaogenban1993/mocha/element/Constants", "NULL", typeDesc(ELEMENT_TYPE));
            return NULL_TYPE;
        }

        // 栈顶 functionElement
        visit(ctx.left);
        methodVisitor.visitTypeInsn(CHECKCAST, FUNCTION_TYPE);

        methodVisitor.visitLdcInsn(ctx.arguments().expression().size());
        methodVisitor.visitTypeInsn(ANEWARRAY, ELEMENT_TYPE);
//        methodVisitor.visitTypeInsn(NEW, "[" + typeDesc(ELEMENT_TYPE));
//        methodVisitor.visitLdcInsn();

        for (int i = 0; i<ctx.arguments().expression().size(); i++) {
            MochaParser.ExpressionContext expressionContext = ctx.arguments().expression().get(i);
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitLdcInsn(i);
            visit(expressionContext);
            methodVisitor.visitInsn(AASTORE);
        }

        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, FUNCTION_TYPE, "call", "(["+typeDesc(ELEMENT_TYPE)+")"+typeDesc(ELEMENT_TYPE), false);
        return ELEMENT_TYPE;
    }

    /**
     * 单元-数字
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitNumber(MochaParser.NumberContext ctx) {
        double num = Double.parseDouble(ctx.NUMBER().getText());
        return newNumber(num);
    }

    /**
     * 单元-字符串
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitString(MochaParser.StringContext ctx) {
        // 反转义例如 "\n" =》 回车
        String str = StringEscapeUtils.unescapeJava(ctx.STRING().getText());
        return newString(str.substring(1, str.length() - 1));
    }
    private String newNumber(double num) {
        methodVisitor.visitTypeInsn(NEW, NUMBER_TYPE);
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitLdcInsn(num);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, NUMBER_TYPE, "<init>", "(D)V", false);
        return NUMBER_TYPE;
    }
    private String newString(String str) {
        methodVisitor.visitTypeInsn(NEW, STRING_TYPE);
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitLdcInsn(str);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, STRING_TYPE, "<init>", "(Ljava/lang/String;)V", false);
        return STRING_TYPE;
    }

    /**
     * 单元-null
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitNull(MochaParser.NullContext ctx) {
        methodVisitor.visitFieldInsn(
                Opcodes.GETSTATIC,                            // 操作码：获取静态字段
                CONSTANTS,                                    // 类名(使用/分隔而非.)
                "NULL",                                       // 字段名
                typeDesc(ELEMENT_TYPE)                        // 字段描述符
        );
        return NULL_TYPE;
    }

    /**
     * 单元-false
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitFalse(MochaParser.FalseContext ctx) {
        methodVisitor.visitInsn(ICONST_0);
        return peekToBooleanElement();
    }

    /**
     * 单元-true
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitTrue(MochaParser.TrueContext ctx) {
        methodVisitor.visitInsn(ICONST_1);
        return peekToBooleanElement();
    }

    /**
     * 单元-数组字面量 [1, true, "2", null]
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitArray(MochaParser.ArrayContext ctx) {
        methodVisitor.visitTypeInsn(NEW, ARRAY_TYPE);
        methodVisitor.visitInsn(DUP);

        methodVisitor.visitTypeInsn(NEW, "java/util/ArrayList");
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
        List<MochaParser.ExpressionContext> exps = ctx.expression();

        for (MochaParser.ExpressionContext exp : exps) {
            methodVisitor.visitInsn(DUP);
            visitExpression(exp);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false);
            methodVisitor.visitInsn(POP);
        }
        methodVisitor.visitMethodInsn(INVOKESPECIAL, ARRAY_TYPE, "<init>", "(Ljava/util/List;)V", false);
        return ARRAY_TYPE;
    }

    /**
     * 单元-对象字面量 {"name": "zhang", age: 10}
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitObject(MochaParser.ObjectContext ctx) {
        methodVisitor.visitTypeInsn(NEW, ELEMENT_TYPE);
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, ELEMENT_TYPE, "<init>", "()V", false);

        List<MochaParser.PairContext> pairContexts = ctx.pair();
        methodVisitor.visitTypeInsn(NEW, "java/util/HashMap");
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);


        pairContexts.forEach(item -> {
            methodVisitor.visitInsn(DUP);
            String key = null;
            if (item.key.getType() == MochaParser.STRING) {
                key = item.key.getText().substring(1, item.key.getText().length() - 1);
            } else {
                key = item.key.getText();
            }
            // k
            methodVisitor.visitLdcInsn(key);
            // v
            visitExpression(item.value);
            // put
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);
            methodVisitor.visitInsn(POP);
        });

        methodVisitor.visitFieldInsn(PUTFIELD, ELEMENT_TYPE, "map", "Ljava/util/Map;");
        return ELEMENT_TYPE;
    }


    /**
     * 单元-分组 (1 + 3 * b)
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitGroup(MochaParser.GroupContext ctx) {
        return visitExpression(ctx.expression());
    }


    /**
     * 单元-函数声明 function(a,b){return a+b;}
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitFunction(MochaParser.FunctionContext ctx) {
        List<String> params = ctx.params().IDENTIFIER().stream().map(ParseTree::getText).collect(Collectors.toList());
        MochaParser.BlockStatementContext blockStatementContext = ctx.blockStatement();
        // 先看是不是有捕捉上下文变量的
        VarRecordVisitor varRecordVisitor = new VarRecordVisitor();
        for (MochaParser.StatementContext statementContext : blockStatementContext.statement()) {
            varRecordVisitor.visit(statementContext);
        }
        Map<String, Integer> catched = new TreeMap<>();
        // 创建的合成方法，入参是捕捉的上下文变量，然后是Element[params.length]
        for (String outerVar : varRecordVisitor.outerVars) {
            Integer varInfo = null;
            if ((varInfo = ctxStack.peek().varInfoMap.get(outerVar)) != null &&
                    !params.contains(outerVar)
            ) {
                catched.put(outerVar, varInfo);
            }
        }

        methodVisitor.visitTypeInsn(NEW, FUNCTION_TYPE);
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, FUNCTION_TYPE, "<init>", "()V", false);
        methodVisitor.visitInsn(DUP);

        String methodName = "temp$" + tempMethodIndex++;
        // 如果没有闭包的话，函数声明就是创建一个临时函数。
        MethodVisitor tempMv = methodVisitor;
        int tempIndex = localVariableIndex;

        // 生成一个临时函数
        methodVisitor = classWriter.visitMethod(ACC_PRIVATE | ACC_STATIC | ACC_SYNTHETIC, methodName,
                "("+typeDesc(ELEMENT_TYPE).repeat(catched.size())+"[" + typeDesc(ELEMENT_TYPE) + ")" + typeDesc(ELEMENT_TYPE), null, null);
        methodVisitor.visitCode();
        localVariableIndex = params.size() + catched.size() + 1;
        Context newCtx = new Context();
        int i = 0;
        for (i = 0; i<params.size(); i++) {
            // ELement[] 入参位于catched之后，所以下标就是其size

            // psize array.length [top]
            methodVisitor.visitLdcInsn(i);
            methodVisitor.visitVarInsn(ALOAD,  catched.size());
            methodVisitor.visitInsn(ARRAYLENGTH);

            Label label = new Label();
            methodVisitor.visitJumpInsn(IF_ICMPGE, label);

            // if(i < array.length)  array[i]
            methodVisitor.visitVarInsn(ALOAD,  catched.size());
            methodVisitor.visitLdcInsn(i);
            methodVisitor.visitInsn(AALOAD);
            Label end = new Label();
            methodVisitor.visitJumpInsn(GOTO, end);

            // else NULL
            methodVisitor.visitLabel(label);
            methodVisitor.visitFieldInsn(GETSTATIC, "com/xiaogenban1993/mocha/element/Constants", "NULL", typeDesc(ELEMENT_TYPE));

            methodVisitor.visitLabel(end);

            AsmUtils.setLocalVariable(methodVisitor, params.get(i), catched.size() + i + 1, ELEMENT_TYPE);
            newCtx.set(params.get(i), catched.size() + i + 1);
        }
        i = 0;
        for (Map.Entry<String, Integer> stringVarInfoEntry : catched.entrySet()) {
            String k = stringVarInfoEntry.getKey();
            newCtx.set(k, i);
            i++;
        }


        ctxStack.push(newCtx);
        blockStatementContext.statement().forEach(this::visit);

        // 补一个return NULL;在最后，防止没有返回值，或者多分支中部分分支没有返回。
        methodVisitor.visitFieldInsn(GETSTATIC, "com/xiaogenban1993/mocha/element/Constants", "NULL", typeDesc(ELEMENT_TYPE));
        methodVisitor.visitInsn(ARETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
        ctxStack.pop();
        methodVisitor = tempMv;
        localVariableIndex = tempIndex;

        for (Map.Entry<String, Integer> stringVarInfoEntry : catched.entrySet()) {
            int outerIndex = stringVarInfoEntry.getValue();
            methodVisitor.visitVarInsn(ALOAD,  outerIndex);
        }
        // lambda 指向当前生成的函数
        methodVisitor.visitInvokeDynamicInsn("run", "(" +typeDesc(ELEMENT_TYPE).repeat(catched.size()) + ")Lcom/xiaogenban1993/mocha/element/FuncApi;",
                new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false),
                Type.getType("(["+typeDesc(ELEMENT_TYPE)+")"+typeDesc(ELEMENT_TYPE)),
                new Handle(Opcodes.H_INVOKESTATIC, "com/xiaogenban1993/mocha/Script", methodName, "(" + typeDesc(ELEMENT_TYPE).repeat(catched.size()) + "["+typeDesc(ELEMENT_TYPE)+")"+typeDesc(ELEMENT_TYPE), false),
                Type.getType("(["+typeDesc(ELEMENT_TYPE)+")"+typeDesc(ELEMENT_TYPE)));

        methodVisitor.visitFieldInsn(PUTFIELD, FUNCTION_TYPE, "api", "Lcom/xiaogenban1993/mocha/element/FuncApi;");
        return FUNCTION_TYPE;
    }

    /**
     * 单元-标识符 a
     * @param ctx the parse tree
     * @return
     */
    @Override
    public String visitIdent(MochaParser.IdentContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        Integer index = ctxStack.peek().get(name);
        if (index == null) {
            throw new RuntimeException("变量未声明" + name);
        }
        methodVisitor.visitVarInsn(ALOAD, index);
        return ELEMENT_TYPE;
    }

    private String visitBlockStatement(MochaParser.BlockStatementContext ctx, Context newCtx) {
        newCtx.parent = ctxStack.peek();
        ctxStack.push(newCtx);
        try {
            // 依次运行每个语句
            for (MochaParser.StatementContext statementContext : ctx.statement()) {
                visitStatement(statementContext);
            }
            return null;
        } finally {
            ctxStack.pop();
        }
    }

    public byte[] generate(String code) {
        visit(parseTree(code));
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
        classWriter.visitEnd();
        return classWriter.toByteArray();
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

    private static void validate(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException(message);
        }
    }

    private static String typeDesc(String longType) {
        return "L" + longType + ";";
    }

    public String peekToBooleanElement() {
        Label lableF = new Label();
        // 栈顶是false直接跳转到labelF，把false放到操作数栈
        methodVisitor.visitJumpInsn(IFEQ, lableF);

        // 否则，true放到栈顶
        methodVisitor.visitFieldInsn(
                Opcodes.GETSTATIC,                            // 操作码：获取静态字段
                CONSTANTS,                                    // 类名(使用/分隔而非.)
                "TRUE",                                       // 字段名
                typeDesc(ELEMENT_TYPE)                        // 字段描述符
        );

        Label labelEnd = new Label();
        methodVisitor.visitJumpInsn(GOTO, labelEnd);

        methodVisitor.visitLabel(lableF);
        methodVisitor.visitFieldInsn(
                Opcodes.GETSTATIC,                            // 操作码：获取静态字段
                CONSTANTS,                                    // 类名(使用/分隔而非.)
                "FALSE",                                       // 字段名
                typeDesc(ELEMENT_TYPE)                        // 字段描述符
        );
        methodVisitor.visitLabel(labelEnd);

        return BOOLEAN_TYPE;
    }

    public String peekToNumberElement() {
        methodVisitor.visitTypeInsn(NEW, NUMBER_TYPE);
        methodVisitor.visitInsn(DUP_X2); // 复制栈顶元素，插入到栈顶往下2个
        methodVisitor.visitInsn(DUP_X2); // 复制栈顶元素，插入到栈顶往下2个
        // 此时栈状态：..., ref, ref, doubleValue, ref
        methodVisitor.visitInsn(Opcodes.POP);
        //  此时栈状态: ...,ref, ref, doubleValue
        methodVisitor.visitMethodInsn(INVOKESPECIAL, NUMBER_TYPE, "<init>", "(D)V", false);
        //  此时栈状态: ..., ref
        return NUMBER_TYPE;
    }
}
