package com.xiaogenban1993.mocha.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class NativeFunctionElement extends FunctionElement{
    private Function<List<Element>, Element> function;

    public NativeFunctionElement(Function<List<Element>, Element> function) {
        super(null, null, null);
        this.function = function;
    }


    public Element call(Element _this, List<Element> args) {
        List<Element> input = new ArrayList<>();
        input.add(_this);
        input.addAll(args);
        return function.apply(input);
    }

    @Override
    public String toString() {
        return "function(<native>);";
    }
}
