package jdrd;

import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdrd.adpter.JDRDAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

public final class JRDTransformer implements ClassFileTransformer {

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
        if (className.startsWith("jdrd/test/S")) {
            try {
                ClassReader classReader = new ClassReader(byteCode);// Class reader to parse the class
                ClassWriter classWriter = new ClassWriter(classReader, 0);// class reader is passed for performence 

                classVisitor = new CheckClassAdapter(classWriter); // checks the correctness of the byte code
                classVisitor = new TraceClassVisitor(classVisitor, new PrintWriter(System.out)); // prints class
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
