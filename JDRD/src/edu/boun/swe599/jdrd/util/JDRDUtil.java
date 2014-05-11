package edu.boun.swe599.jdrd.util;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.objectweb.asm.util.ASMifier;

public abstract class JDRDUtil {

    private static HashMap<?, ?> hasher = new HashMap<>();
    private static int hashSeed = UUID.randomUUID().hashCode();

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

    public static void main(String... args) {
        String a = "";
        String b = new String("");
        String c = "";
        String d = new String("");
        HashMap<String, Object> map = new HashMap<>();
        map.put(a, 1);
        map.put(b, 2);
        System.out.println(System.identityHashCode(a));
        System.out.println(System.identityHashCode(b));
        System.out.println(System.identityHashCode(c));
        System.out.println(System.identityHashCode(d));
    }
}
