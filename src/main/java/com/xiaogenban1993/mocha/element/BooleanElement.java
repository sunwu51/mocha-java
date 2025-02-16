package com.xiaogenban1993.mocha.element;

import lombok.AllArgsConstructor;

/**
 * @author Frank
 * @date 2025/2/4 20:47
 */
@AllArgsConstructor
public class BooleanElement extends Element {
    public boolean value;

    @Override
    public String toString() {
        return value + "";
    }
}