// Generated from C:/Users/sunwu/Desktop/code/test/src/main/resources/Mocha.g4 by ANTLR 4.13.2
package com.xiaogenban1993.mocha.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MochaParser}.
 */
public interface MochaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MochaParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MochaParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MochaParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(MochaParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(MochaParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#varStatement}.
	 * @param ctx the parse tree
	 */
	void enterVarStatement(MochaParser.VarStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#varStatement}.
	 * @param ctx the parse tree
	 */
	void exitVarStatement(MochaParser.VarStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(MochaParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(MochaParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#throwStatement}.
	 * @param ctx the parse tree
	 */
	void enterThrowStatement(MochaParser.ThrowStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#throwStatement}.
	 * @param ctx the parse tree
	 */
	void exitThrowStatement(MochaParser.ThrowStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#expresstionStatement}.
	 * @param ctx the parse tree
	 */
	void enterExpresstionStatement(MochaParser.ExpresstionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#expresstionStatement}.
	 * @param ctx the parse tree
	 */
	void exitExpresstionStatement(MochaParser.ExpresstionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(MochaParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(MochaParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(MochaParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(MochaParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(MochaParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(MochaParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(MochaParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(MochaParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(MochaParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(MochaParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#tryCatchStatement}.
	 * @param ctx the parse tree
	 */
	void enterTryCatchStatement(MochaParser.TryCatchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#tryCatchStatement}.
	 * @param ctx the parse tree
	 */
	void exitTryCatchStatement(MochaParser.TryCatchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#classStatement}.
	 * @param ctx the parse tree
	 */
	void enterClassStatement(MochaParser.ClassStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#classStatement}.
	 * @param ctx the parse tree
	 */
	void exitClassStatement(MochaParser.ClassStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(MochaParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(MochaParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#classElement}.
	 * @param ctx the parse tree
	 */
	void enterClassElement(MochaParser.ClassElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#classElement}.
	 * @param ctx the parse tree
	 */
	void exitClassElement(MochaParser.ClassElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFieldDeclaration(MochaParser.FieldDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFieldDeclaration(MochaParser.FieldDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(MochaParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(MochaParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDeclaration(MochaParser.ConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDeclaration(MochaParser.ConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#constructorBody}.
	 * @param ctx the parse tree
	 */
	void enterConstructorBody(MochaParser.ConstructorBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#constructorBody}.
	 * @param ctx the parse tree
	 */
	void exitConstructorBody(MochaParser.ConstructorBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#superCall}.
	 * @param ctx the parse tree
	 */
	void enterSuperCall(MochaParser.SuperCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#superCall}.
	 * @param ctx the parse tree
	 */
	void exitSuperCall(MochaParser.SuperCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(MochaParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(MochaParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndInAssign}
	 * labeled alternative in {@link MochaParser#assignExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndInAssign(MochaParser.AndInAssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndInAssign}
	 * labeled alternative in {@link MochaParser#assignExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndInAssign(MochaParser.AndInAssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link MochaParser#assignExpression}.
	 * @param ctx the parse tree
	 */
	void enterAssign(MochaParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link MochaParser#assignExpression}.
	 * @param ctx the parse tree
	 */
	void exitAssign(MochaParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayAccess}
	 * labeled alternative in {@link MochaParser#leftValue}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccess(MochaParser.ArrayAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayAccess}
	 * labeled alternative in {@link MochaParser#leftValue}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccess(MochaParser.ArrayAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PropertyAccessInFunCall}
	 * labeled alternative in {@link MochaParser#leftValue}.
	 * @param ctx the parse tree
	 */
	void enterPropertyAccessInFunCall(MochaParser.PropertyAccessInFunCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PropertyAccessInFunCall}
	 * labeled alternative in {@link MochaParser#leftValue}.
	 * @param ctx the parse tree
	 */
	void exitPropertyAccessInFunCall(MochaParser.PropertyAccessInFunCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayAccessInFunCall}
	 * labeled alternative in {@link MochaParser#leftValue}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccessInFunCall(MochaParser.ArrayAccessInFunCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayAccessInFunCall}
	 * labeled alternative in {@link MochaParser#leftValue}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccessInFunCall(MochaParser.ArrayAccessInFunCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LeftUnary}
	 * labeled alternative in {@link MochaParser#leftValue}.
	 * @param ctx the parse tree
	 */
	void enterLeftUnary(MochaParser.LeftUnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LeftUnary}
	 * labeled alternative in {@link MochaParser#leftValue}.
	 * @param ctx the parse tree
	 */
	void exitLeftUnary(MochaParser.LeftUnaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PropertyAccess}
	 * labeled alternative in {@link MochaParser#leftValue}.
	 * @param ctx the parse tree
	 */
	void enterPropertyAccess(MochaParser.PropertyAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PropertyAccess}
	 * labeled alternative in {@link MochaParser#leftValue}.
	 * @param ctx the parse tree
	 */
	void exitPropertyAccess(MochaParser.PropertyAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code And}
	 * labeled alternative in {@link MochaParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAnd(MochaParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code And}
	 * labeled alternative in {@link MochaParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAnd(MochaParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqInAnd}
	 * labeled alternative in {@link MochaParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqInAnd(MochaParser.EqInAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqInAnd}
	 * labeled alternative in {@link MochaParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqInAnd(MochaParser.EqInAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Eq}
	 * labeled alternative in {@link MochaParser#eqExpression}.
	 * @param ctx the parse tree
	 */
	void enterEq(MochaParser.EqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Eq}
	 * labeled alternative in {@link MochaParser#eqExpression}.
	 * @param ctx the parse tree
	 */
	void exitEq(MochaParser.EqContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CompInEq}
	 * labeled alternative in {@link MochaParser#eqExpression}.
	 * @param ctx the parse tree
	 */
	void enterCompInEq(MochaParser.CompInEqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CompInEq}
	 * labeled alternative in {@link MochaParser#eqExpression}.
	 * @param ctx the parse tree
	 */
	void exitCompInEq(MochaParser.CompInEqContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddInComp}
	 * labeled alternative in {@link MochaParser#compExpression}.
	 * @param ctx the parse tree
	 */
	void enterAddInComp(MochaParser.AddInCompContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddInComp}
	 * labeled alternative in {@link MochaParser#compExpression}.
	 * @param ctx the parse tree
	 */
	void exitAddInComp(MochaParser.AddInCompContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Comp}
	 * labeled alternative in {@link MochaParser#compExpression}.
	 * @param ctx the parse tree
	 */
	void enterComp(MochaParser.CompContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Comp}
	 * labeled alternative in {@link MochaParser#compExpression}.
	 * @param ctx the parse tree
	 */
	void exitComp(MochaParser.CompContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Add}
	 * labeled alternative in {@link MochaParser#additionExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdd(MochaParser.AddContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Add}
	 * labeled alternative in {@link MochaParser#additionExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdd(MochaParser.AddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulInAdd}
	 * labeled alternative in {@link MochaParser#additionExpression}.
	 * @param ctx the parse tree
	 */
	void enterMulInAdd(MochaParser.MulInAddContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulInAdd}
	 * labeled alternative in {@link MochaParser#additionExpression}.
	 * @param ctx the parse tree
	 */
	void exitMulInAdd(MochaParser.MulInAddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Mul}
	 * labeled alternative in {@link MochaParser#multiplicationExpression}.
	 * @param ctx the parse tree
	 */
	void enterMul(MochaParser.MulContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Mul}
	 * labeled alternative in {@link MochaParser#multiplicationExpression}.
	 * @param ctx the parse tree
	 */
	void exitMul(MochaParser.MulContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PreInMul}
	 * labeled alternative in {@link MochaParser#multiplicationExpression}.
	 * @param ctx the parse tree
	 */
	void enterPreInMul(MochaParser.PreInMulContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PreInMul}
	 * labeled alternative in {@link MochaParser#multiplicationExpression}.
	 * @param ctx the parse tree
	 */
	void exitPreInMul(MochaParser.PreInMulContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PostfixInPrefix}
	 * labeled alternative in {@link MochaParser#prefixExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixInPrefix(MochaParser.PostfixInPrefixContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PostfixInPrefix}
	 * labeled alternative in {@link MochaParser#prefixExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixInPrefix(MochaParser.PostfixInPrefixContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Prefix}
	 * labeled alternative in {@link MochaParser#prefixExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrefix(MochaParser.PrefixContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Prefix}
	 * labeled alternative in {@link MochaParser#prefixExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrefix(MochaParser.PrefixContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunCallInPost}
	 * labeled alternative in {@link MochaParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunCallInPost(MochaParser.FunCallInPostContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunCallInPost}
	 * labeled alternative in {@link MochaParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunCallInPost(MochaParser.FunCallInPostContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Postfix}
	 * labeled alternative in {@link MochaParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostfix(MochaParser.PostfixContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Postfix}
	 * labeled alternative in {@link MochaParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostfix(MochaParser.PostfixContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunCall}
	 * labeled alternative in {@link MochaParser#functionCallOrPointExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunCall(MochaParser.FunCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunCall}
	 * labeled alternative in {@link MochaParser#functionCallOrPointExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunCall(MochaParser.FunCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PointFunCall}
	 * labeled alternative in {@link MochaParser#functionCallOrPointExpression}.
	 * @param ctx the parse tree
	 */
	void enterPointFunCall(MochaParser.PointFunCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PointFunCall}
	 * labeled alternative in {@link MochaParser#functionCallOrPointExpression}.
	 * @param ctx the parse tree
	 */
	void exitPointFunCall(MochaParser.PointFunCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IndexFunCall}
	 * labeled alternative in {@link MochaParser#functionCallOrPointExpression}.
	 * @param ctx the parse tree
	 */
	void enterIndexFunCall(MochaParser.IndexFunCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IndexFunCall}
	 * labeled alternative in {@link MochaParser#functionCallOrPointExpression}.
	 * @param ctx the parse tree
	 */
	void exitIndexFunCall(MochaParser.IndexFunCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PointProperty}
	 * labeled alternative in {@link MochaParser#functionCallOrPointExpression}.
	 * @param ctx the parse tree
	 */
	void enterPointProperty(MochaParser.PointPropertyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PointProperty}
	 * labeled alternative in {@link MochaParser#functionCallOrPointExpression}.
	 * @param ctx the parse tree
	 */
	void exitPointProperty(MochaParser.PointPropertyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IndexProperty}
	 * labeled alternative in {@link MochaParser#functionCallOrPointExpression}.
	 * @param ctx the parse tree
	 */
	void enterIndexProperty(MochaParser.IndexPropertyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IndexProperty}
	 * labeled alternative in {@link MochaParser#functionCallOrPointExpression}.
	 * @param ctx the parse tree
	 */
	void exitIndexProperty(MochaParser.IndexPropertyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryInFunCallOrPoint}
	 * labeled alternative in {@link MochaParser#functionCallOrPointExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryInFunCallOrPoint(MochaParser.UnaryInFunCallOrPointContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryInFunCallOrPoint}
	 * labeled alternative in {@link MochaParser#functionCallOrPointExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryInFunCallOrPoint(MochaParser.UnaryInFunCallOrPointContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterNumber(MochaParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitNumber(MochaParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterString(MochaParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitString(MochaParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Null}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterNull(MochaParser.NullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Null}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitNull(MochaParser.NullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code True}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterTrue(MochaParser.TrueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code True}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitTrue(MochaParser.TrueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code False}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterFalse(MochaParser.FalseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code False}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitFalse(MochaParser.FalseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Super}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterSuper(MochaParser.SuperContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Super}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitSuper(MochaParser.SuperContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Ident}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterIdent(MochaParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Ident}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitIdent(MochaParser.IdentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrInUnary}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterArrInUnary(MochaParser.ArrInUnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrInUnary}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitArrInUnary(MochaParser.ArrInUnaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Gro}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterGro(MochaParser.GroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Gro}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitGro(MochaParser.GroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ObjInUnary}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterObjInUnary(MochaParser.ObjInUnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ObjInUnary}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitObjInUnary(MochaParser.ObjInUnaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunInUnary}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterFunInUnary(MochaParser.FunInUnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunInUnary}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitFunInUnary(MochaParser.FunInUnaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NewInUnary}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterNewInUnary(MochaParser.NewInUnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NewInUnary}
	 * labeled alternative in {@link MochaParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitNewInUnary(MochaParser.NewInUnaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#group}.
	 * @param ctx the parse tree
	 */
	void enterGroup(MochaParser.GroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#group}.
	 * @param ctx the parse tree
	 */
	void exitGroup(MochaParser.GroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(MochaParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(MochaParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#object}.
	 * @param ctx the parse tree
	 */
	void enterObject(MochaParser.ObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#object}.
	 * @param ctx the parse tree
	 */
	void exitObject(MochaParser.ObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterPair(MochaParser.PairContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitPair(MochaParser.PairContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(MochaParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(MochaParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#params}.
	 * @param ctx the parse tree
	 */
	void enterParams(MochaParser.ParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#params}.
	 * @param ctx the parse tree
	 */
	void exitParams(MochaParser.ParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MochaParser#new}.
	 * @param ctx the parse tree
	 */
	void enterNew(MochaParser.NewContext ctx);
	/**
	 * Exit a parse tree produced by {@link MochaParser#new}.
	 * @param ctx the parse tree
	 */
	void exitNew(MochaParser.NewContext ctx);
}