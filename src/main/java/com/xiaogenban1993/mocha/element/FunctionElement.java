package com.xiaogenban1993.mocha.element;

import com.xiaogenban1993.mocha.antlr.MochaParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Frank
 * @date 2025/2/4 20:46
 */


@AllArgsConstructor
@NoArgsConstructor
@Data
public class FunctionElement extends Element {
    public List<String> params;

    public MochaParser.BlockStatementContext body;

    public Context closureCtx;

    public FuncApi api;

    public FunctionElement(List<String> params, MochaParser.BlockStatementContext body, Context closureCtx) {
        this.params = params;
        this.body = body;
        this.closureCtx = closureCtx;
    }


    public Element call(Element[] elements) {
        return api.run(elements);
    }
    public String toString() {
        return "function(" + String.join(",", params) + ")" + body.getText();
    }
}
