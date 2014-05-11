package jdrd;

import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdrd.util.JDRDUtil;

/**
 * Task 1 : instrument all acquire and release operations on locks : DONE<br/>
 * Task 2 : instrument load and stores of necessary variables: DONE<br/>
 * Task 3 : get all possible locks: DONE<br/>
 * Task 4 : implement algorithm <br/>
 * Task 5 : implement user interface <br/>
 * Task 6 : implement java agent <br/>
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 */
public class JDRD {

    private static final boolean DEBUG = true;

    private static final WeakHashMap<Object, Boolean> ALL_POSSIBLE_LOCKS;
    private static final WeakHashMap<Long, WeakHashMap<Object, Integer>> THREAD_LOCKS;

    static {
        THREAD_LOCKS = new WeakHashMap<>();
        ALL_POSSIBLE_LOCKS = new WeakHashMap<>();
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
        ALL_POSSIBLE_LOCKS.put(lock, Boolean.TRUE);
        long tid = JDRDUtil.getCurrentThreadId();
        WeakHashMap<Object, Integer> lockSet;
        if ((lockSet = THREAD_LOCKS.get(tid)) == null) {
            THREAD_LOCKS.put(tid, lockSet = new WeakHashMap<>());
        }
        Integer count = lockSet.get(lock);
        count = count == null ? 1 : count + 1;
        lockSet.put(lock, count);
        if (DEBUG) {
            printMessage("JDRD: Lock acquired: " + lock + " Thread.holdsLock(lock): " + Thread.holdsLock(lock));
        }
    }

    public static synchronized void lockReleased(Object lock) {
        long tid = JDRDUtil.getCurrentThreadId();
        WeakHashMap<Object, Integer> lockSet;
        if ((lockSet = THREAD_LOCKS.get(tid)) != null) {
            Integer count = lockSet.get(lock);
            count = count == null ? 1 : count;
            if ((count -= 1) == 0) {
                lockSet.remove(lock);
            } else {
                lockSet.put(lock, count);
            }
        }
        printMessage("JDRD: Lock released: " + lock + " Thread.holdsLock(lock): " + Thread.holdsLock(lock));
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
        printMessage("JDRD: Field " + field + " is being read in " + owner.getClass().getName() + "." + methodName + " at line " + line);
    }

    public static synchronized void fieldIsBeingWritten(Object owner, String field, String methodName, int line) {
        //TODO: implement
        printMessage("JDRD: Field " + field + " is being written in " + owner.getClass().getName() + ". " + methodName + " at line " + line);
    }

    private static void printMessage(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }
}
