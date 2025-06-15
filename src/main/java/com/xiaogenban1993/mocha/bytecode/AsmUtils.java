package com.xiaogenban1993.mocha.bytecode;

import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.xiaogenban1993.mocha.element.Element;

/**
 * @author Frank
 * @date 2025/5/31 12:52
 */
public class AsmUtils implements Opcodes {
    public static MethodVisitor generateMainClass(ClassWriter classWriter) {

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, "com/xiaogenban1993/mocha/Script", null, "java/lang/Object", null);
        MethodVisitor methodVisitor;

        // 空参构造方法
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLocalVariable("this", "Lcom/xiaogenban1993/mocha/Script;", null, label0, label1, 0);
            methodVisitor.visitMaxs(0, 0);
            methodVisitor.visitEnd();
        }

        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            methodVisitor.visitCode();
        }
        return methodVisitor;
    }

    public static void setLocalVariable(MethodVisitor methodVisitor, String key, int index, String className) {
//        Label l1 = new Label();
//        methodVisitor.visitLineNumber(1, l1);
//        methodVisitor.visitLabel(l1);
        methodVisitor.visitVarInsn(ASTORE, index);
//        Label l2 = new Label();
//        methodVisitor.visitLabel(l2);
//        methodVisitor.visitLocalVariable(key, "L" + className + ";", null, l1, l2, index);
    }




    public static void main(String[] args) throws Exception {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        MethodVisitor methodVisitor = generateMainClass(classWriter);


        classWriter.visitEnd();
        byte[] byteCode = classWriter.toByteArray();

        Files.write(Path.of(".", "Script.class"), byteCode, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }
}
