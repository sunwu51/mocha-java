package com.xiaogenban1993.mocha.element;
;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Frank
 * @date 2025/2/4 20:46
 */


public class StringElement extends Element {
    public String value;

    public StringElement(String value) {
        this.value = value;
        this.setPrototype(stringProto);
    }

    @Override
    public String toString() {
        return value;
    }

    static ProtoElement stringProto;

    static {
        stringProto = new ProtoElement("string");
        stringProto.set("length", new NativeFunctionElement((list) -> {
            StringElement stringElement = (StringElement) list.get(0);
            return new NumberElement(stringElement.value.length());
        }));
        stringProto.set("split", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            StringElement splitElement = (StringElement) list.get(1);
            List<Element> result = new ArrayList<>();
            for (String s : stringElement.value.split(splitElement.value)) {
                result.add(new StringElement(s));
            }
            return new ArrayElement(result);
        }));
        stringProto.set("charAt", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            NumberElement indexElement = (NumberElement) list.get(1);
            return new StringElement(stringElement.value.charAt(Double.valueOf(indexElement.value).intValue()) + "");
        }));
        stringProto.set("indexOf", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            StringElement indexElement = (StringElement) list.get(1);
            return new NumberElement(stringElement.value.indexOf(indexElement.value));
        }));
        stringProto.set("startsWith", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            StringElement indexElement = (StringElement) list.get(1);
            return stringElement.value.startsWith(indexElement.value) ? Constants.TRUE : Constants.FALSE;
        }));
        stringProto.set("endsWith", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            StringElement indexElement = (StringElement) list.get(1);
            return stringElement.value.endsWith(indexElement.value) ? Constants.TRUE : Constants.FALSE;
        }));
        stringProto.set("replaceAll", new NativeFunctionElement((list) -> {
            if (list.size() != 3) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            StringElement originElement = (StringElement) list.get(1);
            StringElement replaceElement = (StringElement) list.get(2);
            return new StringElement(stringElement.value.replaceAll(originElement.value, replaceElement.value));
        }));
        stringProto.set("toUpperCase", new NativeFunctionElement((list) -> {
            if (list.size() != 1) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            return new StringElement(stringElement.value.toUpperCase());
        }));
        stringProto.set("toLowerCase", new NativeFunctionElement((list) -> {
            if (list.size() != 1) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            return new StringElement(stringElement.value.toLowerCase());
        }));
        stringProto.set("trim", new NativeFunctionElement((list) -> {
            if (list.size() != 1) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            return new StringElement(stringElement.value.trim());
        }));
        stringProto.set("trimLeft", new NativeFunctionElement((list) -> {
            if (list.size() != 1) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            return new StringElement(stringElement.value.trim());
        }));
        stringProto.set("trimRight", new NativeFunctionElement((list) -> {
            if (list.size() != 1) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            return new StringElement(stringElement.value.trim());
        }));
        stringProto.set("toNumber", new NativeFunctionElement((list) -> {
            if (list.size() != 1) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            return new NumberElement(Double.parseDouble(stringElement.value));
        }));
        stringProto.set("substring", new NativeFunctionElement((list) -> {
            if (list.size() != 3) {
                throw new RuntimeException("参数不对");
            }
            StringElement stringElement = (StringElement) list.get(0);
            NumberElement start = (NumberElement) list.get(1);
            NumberElement end = (NumberElement) list.get(2);
            return new StringElement(stringElement.value.substring(Double.valueOf(start.value).intValue(), Double.valueOf(end.value).intValue()));
        }));

    }
}