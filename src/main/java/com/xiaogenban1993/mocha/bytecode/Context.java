package com.xiaogenban1993.mocha.bytecode;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Frank
 * @date 2025/6/1 12:49
 */
public class Context {

    Context parent;

    boolean returned;

    Map<String, VarInfo> varInfoMap = new HashMap<>();


    @AllArgsConstructor
    public static class VarInfo {
        int index;
        String type;
    }

    public VarInfo get(String name) {
        VarInfo res = null;
        if ((res = varInfoMap.get(name)) != null || parent == null) {
            return res;
        }
        return parent.get(name);
    }

    public void set(String name, int index, String type) {
        varInfoMap.put(name, new VarInfo(index, type));
    }
}
