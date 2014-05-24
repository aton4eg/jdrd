/*
 *  Copyright ©2014 Canay ÖZEL (canay.ozel@gmail.com).
 */
package edu.boun.swe599.jdrd.util;

import edu.boun.swe599.jdrd.adpter.JDRDAdapter;
import java.lang.management.ManagementFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.CheckClassAdapter;

/**
 *
 * @author Canay ÖZEL (canay.ozel@gmail.com)
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
public abstract class JDRDUtil {

    public static byte[] convertClassData(byte[] byteCode) {
        ClassReader classReader = new ClassReader(byteCode);// Class reader to parse the class
        ClassWriter classWriter = new ClassWriter(classReader, 0);// class reader is passed for performence 

        ClassVisitor classVisitor = new CheckClassAdapter(classWriter); // checks the correctness of the byte code
//                classVisitor = new TraceClassVisitor(classVisitor, new PrintWriter(System.out)); // prints class
        classVisitor = new JDRDAdapter(classVisitor);// JDRD instrumentation

        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
        // Get the result
        return classWriter.toByteArray();
    }

    public static long getCurrentProcessId() {
        String jvm = ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(jvm.substring(0, jvm.indexOf('@')));
    }

    public static long getCurrentThreadId() {
        return Thread.currentThread().getId();
    }

    public static String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }

    public static long getIdentity(Object object) {
        return System.identityHashCode(object);
    }

    public static boolean in(Object objectToControl, Object... values) {
        if (objectToControl != null && values != null) {
            for (Object value : values) {
                if (objectToControl.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> void printASMCode(Class<T> clazz) throws Exception {
        ASMifier.main(new String[]{clazz.getName()});
    }

}
