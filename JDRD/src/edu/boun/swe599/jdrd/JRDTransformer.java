/*
 *  Copyright ©2014 Canay ÖZEL <canay.ozel@gmail.com>.
 */
package edu.boun.swe599.jdrd;

import edu.boun.swe599.jdrd.util.JDRDUtil;
import edu.boun.swe599.jdrd.util.StringUtil;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
final class JRDTransformer implements ClassFileTransformer {

    private static final Set<String> NAMES_OF_CLASSES_TO_BE_TRANSFORMED = JDRDConfiguration.getNamesOfClassesToTransform();
    private static JRDTransformer transformer;
    private final Instrumentation instrumentation;

    JRDTransformer(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;
        if (willBeTransformed(className)) {
            try {
                byteCode = JDRDUtil.convertClassData(byteCode);
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
