package com.xiaogenban1993.mocha.element;

import com.xiaogenban1993.mocha.antlr.MochaParser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Frank
 * @date 2025/2/4 20:46
 */


@AllArgsConstructor
@Data
public class FunctionElement extends Element {
    public List<String> params;

    public MochaParser.BlockStatementContext body;

    public Context closureCtx;

    public String toString() {
        return "function(" + String.join(",", params) + ")" + body.getText();
    }
}
