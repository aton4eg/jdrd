/*
 *  Copyright ©2014 Canay ÖZEL (canay.ozel@gmail.com).
 */
package edu.boun.swe599.jdrd;

import java.lang.instrument.Instrumentation;

/**
 *
 * @author Canay ÖZEL (canay.ozel@gmail.com)
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
class JDRDPreMain {

    public static void premain(String agentArgument, Instrumentation instrumentation) {
        if (instrumentation != null) {
            instrumentation.addTransformer(new JRDTransformer(instrumentation));
            JDRDLogger.log("JDRD Trasnformer has been successfully attached to the classloader.");
        }
    }

}
