package jdrd.util;

import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.objectweb.asm.util.ASMifier;

public abstract class JDRDUtil {

    private static HashMap<?, ?> hasher = new HashMap<>();
    private static int hashSeed = UUID.randomUUID().hashCode();
    //   private static int hash(Object k) {
//        int h = 0;
//        if (k instanceof String) {
//            return sun.misc.Hashing.stringHash32((String) k);
//        }
//        h = hashSeed;
//
//        h ^= k.hashCode();
//        h ^= (h >>> 20) ^ (h >>> 12);
//        return h ^ (h >>> 7) ^ (h >>> 4);
//    }

    public static int hash(Object object) {
        try {
            if (object == null) {
                return 0;
            }
            Method method = hasher.getClass().getDeclaredMethod("hash", Object.class);
            if (method != null) {
                method.setAccessible(true);
                return (int) method.invoke(hasher, object);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(JDRDUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
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
        int i = 256;
        Integer a = 256;
        Integer b = new Integer("5");
        Integer c = new Integer(5);
        Integer d = i;
        System.out.println(System.identityHashCode(a));
        System.out.println(System.identityHashCode(b));
        System.out.println(System.identityHashCode(c));
        System.out.println(System.identityHashCode(d));
    }
}
