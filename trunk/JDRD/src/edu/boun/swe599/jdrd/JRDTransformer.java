/*
 *  Copyright ©2014 Canay ÖZEL <canay.ozel@gmail.com>.
 */
package edu.boun.swe599.jdrd;

import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.boun.swe599.jdrd.adpter.JDRDAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

/**
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
public final class JRDTransformer implements ClassFileTransformer {

    private static final String CLASSES_TO_BE_TRACED = ".";
    private static JRDTransformer transformer;

    public static JRDTransformer getInstance() {
        return transformer == null ? (transformer = new JRDTransformer()) : transformer;
    }

    private JRDTransformer() {
        System.out.println(ClassLoader.getSystemResource(""));
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;
        ClassVisitor classVisitor;
        if (className.startsWith("edu/boun/swe599/jdrd/test/")) {
            try {
                ClassReader classReader = new ClassReader(byteCode);// Class reader to parse the class
                ClassWriter classWriter = new ClassWriter(classReader, 0);// class reader is passed for performence 

                classVisitor = new CheckClassAdapter(classWriter); // checks the correctness of the byte code
                if (className.startsWith(CLASSES_TO_BE_TRACED)) {
                    classVisitor = new TraceClassVisitor(classVisitor, new PrintWriter(System.out)); // prints class
                }
                classVisitor = new JDRDAdapter(classVisitor); // adds time to the class

                classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
                // Get the result
                byteCode = classWriter.toByteArray();
            } catch (Exception ex) {
                Logger.getLogger(JRDTransformer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return byteCode;
    }
}
