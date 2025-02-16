package com.xiaogenban1993.mocha.element;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Frank
 * @date 2025/2/4 20:46
 */


public class ArrayElement extends Element {
    public List<Element> array;

    public ArrayElement(List<Element> array) {
        this.array = array;
        this.setPrototype(arrayProto);
    }
    @Override
    public String toString() {
        return array.toString();
    }

    static ProtoElement arrayProto;

    static {
        arrayProto = new ProtoElement("array");
        arrayProto.set("at", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            ArrayElement arrayElement = (ArrayElement) list.get(0);
            NumberElement indexElement = (NumberElement) list.get(1);
            return arrayElement.array.get(Double.valueOf(indexElement.value).intValue());
        }));
        arrayProto.set("length", new NativeFunctionElement((list) -> {
            ArrayElement arrayElement = (ArrayElement) list.get(0);
            return new NumberElement(arrayElement.array.size());
        }));
        arrayProto.set("push", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            ArrayElement arrayElement = (ArrayElement) list.get(0);
            Element element = list.get(1);
            arrayElement.array.add(element);
            return Constants.NULL;
        }));
        arrayProto.set("pop", new NativeFunctionElement((list) -> {
            ArrayElement arrayElement = (ArrayElement) list.get(0);
            return arrayElement.array.remove(arrayElement.array.size() - 1);
        }));
        arrayProto.set("shift", new NativeFunctionElement((list) -> {
            ArrayElement arrayElement = (ArrayElement) list.get(0);
            return arrayElement.array.remove(0);
        }));
        arrayProto.set("unshift", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            ArrayElement arrayElement = (ArrayElement) list.get(0);
            Element element = list.get(1);
            arrayElement.array.add(0, element);
            return Constants.NULL;
        }));
        arrayProto.set("join", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            ArrayElement arrayElement = (ArrayElement) list.get(0);
            StringElement stringElement = (StringElement) list.get(1);
            return new StringElement(arrayElement.array.stream().map(Element::toString).collect(Collectors.joining(stringElement.value)));
        }));
    }
}