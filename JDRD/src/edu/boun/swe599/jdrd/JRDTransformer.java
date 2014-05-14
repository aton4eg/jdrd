/*
 *  Copyright ©2014 Canay ÖZEL <canay.ozel@gmail.com>.
 */
package edu.boun.swe599.jdrd;

import edu.boun.swe599.jdrd.adpter.JDRDAdapter;
import edu.boun.swe599.jdrd.util.JDRDConfiguration;
import edu.boun.swe599.jdrd.util.StringUtil;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

/**
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
final class JRDTransformer implements ClassFileTransformer {

    private static final Set<String> NAMES_OF_CLASSES_TO_BE_TRANSFORMED = JDRDConfiguration.getNamesOfClassesToTransform();
    private static JRDTransformer transformer;

    public static JRDTransformer getInstance() {
        return transformer == null ? (transformer = new JRDTransformer()) : transformer;
    }

    private JRDTransformer() {
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;
        ClassVisitor classVisitor;
        if (willBeTransformed(className)) {
            try {
                ClassReader classReader = new ClassReader(byteCode);// Class reader to parse the class
                ClassWriter classWriter = new ClassWriter(classReader, 0);// class reader is passed for performence 

                classVisitor = new CheckClassAdapter(classWriter); // checks the correctness of the byte code
//                classVisitor = new TraceClassVisitor(classVisitor, new PrintWriter(System.out)); // prints class
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

    private static boolean willBeTransformed(String className) {
        if (StringUtil.hasText(className)) {
            for (String name : NAMES_OF_CLASSES_TO_BE_TRANSFORMED) {
                if (className.startsWith(name)) {
                    return !className.startsWith("edu/boun/swe599/jdrd/");
                }
            }
        }
        return false;
    }
}
