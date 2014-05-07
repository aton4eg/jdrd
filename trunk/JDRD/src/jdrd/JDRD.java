package jdrd;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdrd.util.JDRDUtil;

/**
 * Task 1 : instrument all acquire and release operations on locks : DONE<br/>
 * Task 2 : instrument load and stores of necessary variables: DONE<br/>
 * Task 3 : identify objects : DONE <br/>
 * Task 4 : get all possible locks  <br/>
 * Task 5 : implement algorithm <br/>
 * Task 6 : implement interface <br/>
 * Task 6 : implement java agent <br/>
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 */
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
        //TODO: implement
        System.out.println("JDRD: Lock acquired: " + lock + " Thread.holdsLock(lock): " + Thread.holdsLock(lock));
    }

    public static synchronized void lockReleased(Object lock) {
        //TODO: implement (remember counter 0)
        System.out.println("JDRD: Lock released: " + lock + " Thread.holdsLock(lock): " + Thread.holdsLock(lock));
    }

    public static synchronized void fieldIsBeingRead(String className, String field, String methodName, int line) {
        try {
            fieldIsBeingRead(Class.forName(className), field, methodName, line);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JDRD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized void fieldIsBeingWritten(String className, String field, String methodName, int line) {
        try {
            fieldIsBeingWritten(Class.forName(className), field, methodName, line);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JDRD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized void fieldIsBeingRead(Object owner, String field, String methodName, int line) {
        //TODO: implement
        System.out.println("JDRD: Field " + field + " is being read in " + owner.getClass().getName() + "." + methodName + " at line " + line);
    }

    public static synchronized void fieldIsBeingWritten(Object owner, String field, String methodName, int line) {
        //TODO: implement
        System.out.println("JDRD: Field " + field + " is being written in " + owner.getClass().getName() + ". " + methodName + " at line " + line);
    }
}
