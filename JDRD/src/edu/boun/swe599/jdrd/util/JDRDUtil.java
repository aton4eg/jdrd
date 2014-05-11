/*
 *  Copyright ©2014 Canay ÖZEL <canay.ozel@gmail.com>.
 */
package edu.boun.swe599.jdrd.util;

import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.WeakHashMap;
import org.objectweb.asm.util.ASMifier;

/**
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
public abstract class JDRDUtil {

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

    public static long getCurrentThreadId() {
        return Thread.currentThread().getId();
    }

    public static long getCurrentProcessId() {
        String jvm = ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(jvm.substring(0, jvm.indexOf('@')));
    }

    public static <T> void printASMCode(Class<T> clazz) throws Exception {
        ASMifier.main(new String[]{clazz.getName()});
    }
}
