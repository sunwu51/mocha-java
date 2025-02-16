package com.xiaogenban1993.mocha;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.TokenFactory;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.misc.Pair;

/**
 * @author Frank
 * @date 2025/2/16 15:09
 */

class PositionAdjustingTokenFactory implements TokenFactory<CommonToken> {
    private final int lineOffset;
    private final int columnOffset;

    public PositionAdjustingTokenFactory(int lineOffset, int columnOffset) {
        this.lineOffset = lineOffset;
        this.columnOffset = columnOffset;
    }

    @Override
    public CommonToken create(int type, String text) {
        CommonToken token = new CommonToken(type, text);
        adjustPosition(token);
        return token;
    }

    @Override
    public CommonToken create(Pair<TokenSource, CharStream> source, int type,
                              String text, int channel, int start, int stop,
                              int line, int charPositionInLine) {
        CommonToken token = new CommonToken(source, type, channel, start, stop);
        token.setText(text);
        token.setLine(line + lineOffset);
        token.setCharPositionInLine(charPositionInLine + columnOffset);
        return token;
    }

    private void adjustPosition(CommonToken token) {
        if (token.getLine() > 0) {
            token.setLine(token.getLine() + lineOffset);
            token.setCharPositionInLine(token.getCharPositionInLine() + columnOffset);
        }
    }
}
