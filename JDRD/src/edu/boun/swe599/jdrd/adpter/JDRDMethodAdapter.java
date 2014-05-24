/*
 *  Copyright ©2014 Canay ÖZEL <canay.ozel@gmail.com>.
 */
package edu.boun.swe599.jdrd.adpter;

import edu.boun.swe599.jdrd.JDRD;
import java.lang.reflect.Modifier;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
public class JDRDMethodAdapter extends MethodVisitor {

    private final String ownerName;
    private final String methodName;
    private int lastLineNumber;
    private int additionalStackSize = 0;
    private final boolean isStatic;
    private final boolean isSynchronized;
    private int lastALOADIndex;
    private int lastASTOREIndex;

    public JDRDMethodAdapter(MethodVisitor methodVisitor, String owner, int access, String name, String desc) {
        super(Opcodes.ASM4, methodVisitor);
        this.ownerName = owner;
        this.methodName = name;
        this.isStatic = Modifier.isStatic(access);
        this.isSynchronized = Modifier.isSynchronized(access);
        if (methodVisitor == null) {
            throw new IllegalArgumentException("Parameter methodVisitor cacnot be null!");
        }
    }

    @Override
    public void visitCode() {
        mv.visitCode();
        if (this.isSynchronized) {
            if (this.isStatic) {
                mv.visitLdcInsn(this.ownerName.replaceAll("/", "."));
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(JDRD.class).getInternalName(), "lockAcquired", "(Ljava/lang/String;)V", false);
            } else {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(JDRD.class).getInternalName(), "lockAcquired", "(Ljava/lang/Object;)V", false);
            }
        }
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        if (opcode == Opcodes.GETFIELD) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);// owner this
            mv.visitLdcInsn(name); // field name
            mv.visitLdcInsn(this.methodName); // method name
            mv.visitLdcInsn(this.lastLineNumber); // line number
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(JDRD.class).getInternalName(), "fieldIsBeingRead", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;I)V", false);
            this.additionalStackSize = 4;
        } else if (opcode == Opcodes.PUTFIELD) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);// owner this
            mv.visitLdcInsn(name); // field name
            mv.visitLdcInsn(this.methodName); // method name
            mv.visitLdcInsn(this.lastLineNumber); // line number
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(JDRD.class).getInternalName(), "fieldIsBeingWritten", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;I)V", false);
            this.additionalStackSize = 4;
        } else if (opcode == Opcodes.GETSTATIC) {
            mv.visitLdcInsn(this.ownerName.replaceAll("/", ".")); // owner name
            mv.visitLdcInsn(name); // field name
            mv.visitLdcInsn(this.methodName); // method name
            mv.visitLdcInsn(this.lastLineNumber); // line number
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(JDRD.class).getInternalName(), "fieldIsBeingRead", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V", false);
            this.additionalStackSize = 4;
        } else if (opcode == Opcodes.PUTSTATIC) {
            mv.visitLdcInsn(this.ownerName.replaceAll("/", ".")); // owner name
            mv.visitLdcInsn(name); // field name
            mv.visitLdcInsn(this.methodName); // method name
            mv.visitLdcInsn(this.lastLineNumber); // line number
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(JDRD.class).getInternalName(), "fieldIsBeingWritten", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V", false);
            this.additionalStackSize = 4;
        }
        mv.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
            if (this.isSynchronized) {
                if (this.isStatic) {
                    mv.visitLdcInsn(ownerName.replaceAll("/", "."));
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(JDRD.class).getInternalName(), "lockReleased", "(Ljava/lang/String;)V", false);
                } else {
                    mv.visitVarInsn(Opcodes.ALOAD, 0);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(JDRD.class).getInternalName(), "lockAcquired", "(Ljava/lang/Object;)V", false);
                }
            }
            mv.visitInsn(opcode);
        } else if (opcode == Opcodes.MONITORENTER) {
            mv.visitInsn(opcode);
            mv.visitVarInsn(Opcodes.ALOAD, this.lastASTOREIndex);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(JDRD.class).getInternalName(), "lockAcquired", "(Ljava/lang/Object;)V", false);
        } else if (opcode == Opcodes.MONITOREXIT) {
            mv.visitInsn(opcode);
            mv.visitVarInsn(Opcodes.ALOAD, this.lastALOADIndex);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(JDRD.class).getInternalName(), "lockReleased", "(Ljava/lang/Object;)V", false);
        } else {
            mv.visitInsn(opcode);
        }
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        mv.visitMaxs(maxStack + this.additionalStackSize, maxLocals);
    }

    @Override
    public void visitVarInsn(int opcode, int index) {
        mv.visitVarInsn(opcode, index);
        if (opcode == Opcodes.ALOAD) {
            this.lastALOADIndex = index;
        } else if (opcode == Opcodes.ASTORE) {
            this.lastASTOREIndex = index;
        }
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        this.lastLineNumber = line;
        mv.visitLineNumber(line, start);
    }
}
