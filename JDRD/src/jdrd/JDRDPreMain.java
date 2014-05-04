package jdrd;

import java.lang.instrument.Instrumentation;

public class JDRDPreMain {

    public static void premain(String agentArgument, Instrumentation instrumentation) {
        if (instrumentation != null) {
            instrumentation.addTransformer(JRDTransformer.getInstance());
        }
    }
}
