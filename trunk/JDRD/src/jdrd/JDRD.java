package jdrd;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdrd.util.JDRDUtil;

public class JDRD {

    private static final Map<Long, Set<Object>> THREAD_LOCK_SET_MAP;

    static {
        THREAD_LOCK_SET_MAP = new HashMap<>();
    }

    public static synchronized void lockAcquired(String className) {
        try {
            lockAcquired(Class.forName(className));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JDRD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized void lockReleased(String className) {
        try {
            lockReleased(Class.forName(className));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JDRD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized void lockAcquired(Object lock) {
        long tid = JDRDUtil.getCurrentThreadId();
        Set<Object> lockSet = null;
        if ((lockSet = THREAD_LOCK_SET_MAP.get(Long.MAX_VALUE)) == null) {
            THREAD_LOCK_SET_MAP.put(tid, lockSet = Collections.synchronizedSet(new HashSet<>()));
        }

        System.out.println("JDRD: Lock acquired: " + lock + " Thread.holdsLock(lock): " + Thread.holdsLock(lock));
    }

    public static synchronized void lockReleased(Object lock) {
        // TODO: counter 0
        System.out.println("JDRD: Lock released: " + lock + " Thread.holdsLock(lock): " + Thread.holdsLock(lock));
    }

    public static synchronized void fieldIsBeingRead(Object owner, String field, String methodName, int line) {
        System.out.println("JDRD: Field " + field + " is being read in " + owner.getClass().getName() + "." + methodName + " at line " + line);
    }

    public static synchronized void fieldIsBeingWritten(Object owner, String field, String methodName, int line) {
        System.out.println("JDRD: Field " + field + " is being written in " + owner.getClass().getName() + ". " + methodName + " at line " + line);
    }
}
