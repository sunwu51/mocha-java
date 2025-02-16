package com.xiaogenban1993.mocha.element;

import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Stack;
import java.util.stream.Collectors;

public class ErrorElement extends Element {
    public ErrorElement(String msg, Stack<ErrorInfo> stack) {
        set("message", new StringElement(msg));
        set("stack", new ArrayElement(stack.stream().map(ErrorInfo::toElement).collect(Collectors.toList())));
    }


    @AllArgsConstructor
    public static class ErrorInfo {
        public String function;
        public int line;
        public int column;

        public Element toElement() {
            Element res = new Element();
            res.set("function", function == null ? Constants.NULL : new StringElement(function));
            res.set("line", new NumberElement(line));
            res.set("column", new NumberElement(column));
            return res;
        }

        public static ErrorInfo fromElement(Element element) {
            String function = element.get("function") == Constants.NULL ? null : element.get("function").toString();
            Number line = ((NumberElement)element.get("line")).value;
            Number column = ((NumberElement)element.get("column")).value;
            return new ErrorInfo(function, line.intValue(), column.intValue());
        }
    }

    public static class MochaError extends RuntimeException {
        public ErrorElement error;

        public MochaError(String message, ParserRuleContext ctx) {
            super(message);
            Stack<ErrorInfo> stack = new Stack<>();
            int line = ctx == null ? -1: ctx.getStart().getLine();
            int col =  ctx == null ? -1: ctx.getStart().getCharPositionInLine();
            stack.push(new ErrorInfo(null, line, col));
            ErrorElement error = new ErrorElement(message, stack);
            this.error = error;
        }

        public MochaError(ErrorElement error) {
            super(error.get("message").toString());
            this.error = error;
        }

        @Override
        public void printStackTrace() {
            ArrayElement stack = (ArrayElement)error.get("stack");
            System.err.println("Uncaught " + error.get("message").toString());
            stack.array.forEach(e -> {
                System.err.println("\tat " + (e.get("function") == Constants.NULL ? "": e.get("function").toString()) + " " + e.get("line").toString() + ":" + e.get("column").toString());
            });
        }

        public void pushStack(String function, int line, int column) {
            ArrayElement stack = (ArrayElement)error.get("stack");
            ErrorInfo info = new ErrorInfo(function, line, column);
            stack.array.add(info.toElement());
        }

        public void fillFunctionName(String function) {
            ArrayElement stack = (ArrayElement)error.get("stack");
            Element last = stack.array.get(stack.array.size() - 1);
            if (last.get("function") == Constants.NULL) {
                last.set("function", new StringElement(function));
            }
        }
    }
}
