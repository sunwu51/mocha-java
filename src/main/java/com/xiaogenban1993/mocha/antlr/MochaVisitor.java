// Generated from C:/Users/sunwu/Desktop/code/mocha-java/src/main/resources/Mocha.g4 by ANTLR 4.13.2
package com.xiaogenban1993.mocha.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MochaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MochaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MochaParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(MochaParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(MochaParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#varStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarStatement(MochaParser.VarStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(MochaParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#throwStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowStatement(MochaParser.ThrowStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#expresstionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpresstionStatement(MochaParser.ExpresstionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(MochaParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(MochaParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(MochaParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(MochaParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#continueStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(MochaParser.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#tryCatchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryCatchStatement(MochaParser.TryCatchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#classStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassStatement(MochaParser.ClassStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#classBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBody(MochaParser.ClassBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#classElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassElement(MochaParser.ClassElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDeclaration(MochaParser.FieldDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#methodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaration(MochaParser.MethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorDeclaration(MochaParser.ConstructorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#constructorBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorBody(MochaParser.ConstructorBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#superCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuperCall(MochaParser.SuperCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(MochaParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndInAssign}
	 * labeled alternative in {@link MochaParser#assignExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndInAssign(MochaParser.AndInAssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link MochaParser#assignExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(MochaParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndInOr}
	 * labeled alternative in {@link MochaParser#orExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndInOr(MochaParser.AndInOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Or}
	 * labeled alternative in {@link MochaParser#orExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(MochaParser.OrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code And}
	 * labeled alternative in {@link MochaParser#andExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(MochaParser.AndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqInAnd}
	 * labeled alternative in {@link MochaParser#andExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqInAnd(MochaParser.EqInAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Eq}
	 * labeled alternative in {@link MochaParser#eqExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEq(MochaParser.EqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CompInEq}
	 * labeled alternative in {@link MochaParser#eqExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompInEq(MochaParser.CompInEqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddInComp}
	 * labeled alternative in {@link MochaParser#compExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddInComp(MochaParser.AddInCompContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Comp}
	 * labeled alternative in {@link MochaParser#compExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp(MochaParser.CompContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Add}
	 * labeled alternative in {@link MochaParser#additionExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd(MochaParser.AddContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulInAdd}
	 * labeled alternative in {@link MochaParser#additionExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulInAdd(MochaParser.MulInAddContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Mul}
	 * labeled alternative in {@link MochaParser#multiplicationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMul(MochaParser.MulContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PreInMul}
	 * labeled alternative in {@link MochaParser#multiplicationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreInMul(MochaParser.PreInMulContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PostfixInPrefix}
	 * labeled alternative in {@link MochaParser#prefixExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixInPrefix(MochaParser.PostfixInPrefixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Prefix}
	 * labeled alternative in {@link MochaParser#prefixExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefix(MochaParser.PrefixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunCallInPost}
	 * labeled alternative in {@link MochaParser#postfixExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunCallInPost(MochaParser.FunCallInPostContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Postfix}
	 * labeled alternative in {@link MochaParser#postfixExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfix(MochaParser.PostfixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PropertyExpr}
	 * labeled alternative in {@link MochaParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyExpr(MochaParser.PropertyExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CallExpr}
	 * labeled alternative in {@link MochaParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallExpr(MochaParser.CallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryExpr}
	 * labeled alternative in {@link MochaParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(MochaParser.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IndexExpr}
	 * labeled alternative in {@link MochaParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexExpr(MochaParser.IndexExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(MochaParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code String}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(MochaParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Null}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull(MochaParser.NullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code True}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrue(MochaParser.TrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code False}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalse(MochaParser.FalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Super}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuper(MochaParser.SuperContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Ident}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(MochaParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrInUnary}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrInUnary(MochaParser.ArrInUnaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Gro}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGro(MochaParser.GroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ObjInUnary}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjInUnary(MochaParser.ObjInUnaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunInUnary}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunInUnary(MochaParser.FunInUnaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NewInUnary}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewInUnary(MochaParser.NewInUnaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup(MochaParser.GroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(MochaParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject(MochaParser.ObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair(MochaParser.PairContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(MochaParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParams(MochaParser.ParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#new}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew(MochaParser.NewContext ctx);
	/**
	 * Visit a parse tree produced by {@link MochaParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(MochaParser.ArgumentsContext ctx);
}