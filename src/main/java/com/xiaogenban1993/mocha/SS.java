package com.xiaogenban1993.mocha;

import com.xiaogenban1993.mocha.element.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Frank
 * @date 2025/5/31 22:21
 */
public class SS {
    public static void main(String[] var0) {
        Element[] r = new Element[10];
        r[1] = Constants.NULL;
        FuncApi funcApi = (list) -> {
            System.out.println(list[0]);
            return Constants.NULL;
        };

        funcApi.run(Constants.TRUE);
    }
}
