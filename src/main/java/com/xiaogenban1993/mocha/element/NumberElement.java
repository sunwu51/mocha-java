package com.xiaogenban1993.mocha.element;
;

import lombok.AllArgsConstructor;

/**
 * @author Frank
 * @date 2025/2/4 20:46
 */
@AllArgsConstructor
public class NumberElement extends Element {
    public double value;

    @Override
    public String toString() {
        if (value % 1 == 0) {
            return (long)value + "";
        }
        return value + "";
    }
}