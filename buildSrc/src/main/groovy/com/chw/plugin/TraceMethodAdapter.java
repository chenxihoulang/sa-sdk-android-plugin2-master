package com.chw.plugin;

import groovyjarjarasm.asm.Opcodes;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author ChaiHongwei
 * @date 2020-12-02 15:58
 */
class TraceMethodAdapter extends AdviceAdapter {
    private String className;
    private String methodName;

    public TraceMethodAdapter(int api, MethodVisitor methodVisitor, int access, String name,
                              String desc, String className) {
        super(api, methodVisitor, access, name, desc);
        this.className = className;
        this.methodName = name;
    }

    private int timeLocalIndex = 0;

    @Override
    protected void onMethodEnter() {
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        // 1
        timeLocalIndex = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, timeLocalIndex);
    }

    @Override
    protected void onMethodExit(int opcode) {
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitVarInsn(LLOAD, timeLocalIndex);
        // 此处的值在栈顶
        mv.visitInsn(LSUB);
        // 因为后面要用到这个值所以先将其保存到本地变量表中
        mv.visitVarInsn(LSTORE, timeLocalIndex);

        int stringBuilderIndex = newLocal(Type.getType("java/lang/StringBuilder"));
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        // 需要将栈顶的 stringbuilder 指针保存起来否则后面找不到了
        mv.visitVarInsn(Opcodes.ASTORE, stringBuilderIndex);
        mv.visitVarInsn(Opcodes.ALOAD, stringBuilderIndex);
        mv.visitLdcInsn(className + "." + methodName + " time:");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitInsn(Opcodes.POP);
        mv.visitVarInsn(Opcodes.ALOAD, stringBuilderIndex);
        mv.visitVarInsn(Opcodes.LLOAD, timeLocalIndex);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
        mv.visitInsn(Opcodes.POP);
        mv.visitLdcInsn("Geek");
        mv.visitVarInsn(Opcodes.ALOAD, stringBuilderIndex);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        // 注意： Log.d 方法是有返回值的，需要 pop 出去
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        // 2
        mv.visitInsn(Opcodes.POP);
    }
}
