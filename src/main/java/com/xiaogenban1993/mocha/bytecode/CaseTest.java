package com.xiaogenban1993.mocha.bytecode;

import org.objectweb.asm.Opcodes;

import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * @author Frank
 * @date 2025/6/1 21:50
 */
public class CaseTest {
    public static void main(String[] args) throws Exception {
        ByteCodeGenerater compiler = new ByteCodeGenerater();
        // var语句
        // 字面量：数字 字符串 布尔 数组 对象
        // 二元操作符： = +-*/% <> <= >= == !=
        // 单元操作符： ++ -- ! + -
        // 分组 ()
        // 对象的点运算符
        // todo 函数实现 闭包函数
        // todo return block


        byte[] byteCode = compiler.generate("" +
//                "var a = null;" +
//                "var b = 1 + 2 * 3 / 4 - 5 % 10;" +
//                "print(b++);" +
//                "print(b);" +
//                "print(++b);" +
//                "print(a);" +
//                "print(a != b);" +
//
//                "var arr = [1, 'aaa', true, {'age': 10}];" +
//                "print(arr);" +
//                "print(arr[3]);" +
//                "a = 1; b = 1;" +
//                "print(a>=b++);" +
//                "print(a>=b);" +
//                "print(-a);" +
//                "print(!a);" +
//
//                "var sam = {name:'sam', age: 10};" +
//                "print(sam);" +
//                "print(sam.age * 2);" +
//
//                "var tim = {name:'tim', age: 40};" +
//                "sam.parent = tim;" +
//                "print(sam);" +
//                "print(sam.parent.age);" +
//                "print(sam['parent'].age);" +
//                "a = 'hello';" +
//                "print(a);" +
//                "" +
//                "{ var a = 'world';" +
//                "  print(a);" +
//                "}" +
//                "print(a);" +
                "var add = function(a,b) { return a + b;};" +
                "var res = add(1, 2);" +
                "print(res);");
        Files.write(Paths.get("./target/classes/com/xiaogenban1993/mocha", "Script.class"), byteCode);
    }
}
