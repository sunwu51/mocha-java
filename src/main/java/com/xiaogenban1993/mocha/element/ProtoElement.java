package com.xiaogenban1993.mocha.element;

/**
 * @author Frank
 * @date 2025/2/4 21:29
 */
public class ProtoElement extends Element {
    public ProtoElement(String type) {
        set("type", new StringElement(type));
    }

}