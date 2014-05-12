package edu.boun.swe599.jdrd;
/*
 *  Copyright ©2014 Canay ÖZEL <canay.ozel@gmail.com>.
 */

import edu.boun.swe599.jdrd.data.FieldStateData;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.boun.swe599.jdrd.util.JDRDUtil;
import edu.boun.swe599.jdrd.data.FieldState;
import java.util.HashMap;
import java.util.Map;

/**
 * Task 1 : instrument all acquire and release operations on locks : DONE<br/>
 * Task 2 : instrument load and stores of necessary variables: DONE<br/>
 * Task 3 : get all possible locks: DONE<br/>
 * Task 4 : implement algorithm DONE <br/>
 * Task 5 : implement user interface <br/>
 * Task 6 : implement java agent <br/>
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
public class JDRD {

    public static final boolean DEBUG = true;

    private static final WeakHashMap<Object, Map<String, FieldStateData>> DATA_STATES;
    private static final Map<Long, WeakHashMap<Object, Integer>> THREAD_LOCKS;

    static {
        THREAD_LOCKS = new WeakHashMap<>();
        DATA_STATES = new WeakHashMap<>();
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
        printMessage("JDRD: Field " + field + " is being read by " + Thread.currentThread().getName() + "#" + JDRDUtil.getCurrentThreadId() + " in " + owner.getClass().getName() + "@" + System.identityHashCode(owner) + "." + methodName + " at line " + line);
        if (checkRaceCondition(owner, field, false)) {
            System.err.println("JDRD: Race detected in " + owner.getClass().getName() + "." + methodName + " on field " + field + " at line:" + line);
        }
    }

    public static synchronized void fieldIsBeingWritten(Object owner, String field, String methodName, int line) {
        printMessage("JDRD: Field " + field + " is being written by " + Thread.currentThread().getName() + "#" + JDRDUtil.getCurrentThreadId() + " in " + owner.getClass().getName() + "@" + System.identityHashCode(owner) + "." + methodName + " at line " + line);
        if (checkRaceCondition(owner, field, true)) {
            System.err.println("JDRD: Race detected in " + owner.getClass().getName() + "." + methodName + " on field " + field + " at line:" + line);
        }
    }

    private static void printMessage(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }

    private static FieldStateData getFieldStateData(Object owner, String field) {
        Map<String, FieldStateData> map;
        if ((map = DATA_STATES.get(owner)) == null) {
            map = new HashMap<>();
            DATA_STATES.put(owner, map);
        }
        FieldStateData fieldStateData;
        if ((fieldStateData = map.get(field)) == null) {
            fieldStateData = new FieldStateData();
            map.put(field, fieldStateData);
        }
        return fieldStateData;
    }

    private static WeakHashMap<Object, ?> getLocksHeld() {
        long tid = JDRDUtil.getCurrentThreadId();
        WeakHashMap<Object, Integer> locksHeld;
        if ((locksHeld = THREAD_LOCKS.get(tid)) == null) {
            locksHeld = new WeakHashMap<>();
            THREAD_LOCKS.put(tid, locksHeld);
        }
        return locksHeld;
    }

    private static boolean checkRaceCondition(Object owner, String field, boolean isWrite) {
        FieldStateData fieldStateData = getFieldStateData(owner, field);
        fieldStateData.signalAccess(getLocksHeld(), isWrite);
        return fieldStateData.getState() == FieldState.SHARED_MODIFIED && fieldStateData.getLockSetSize() == 0;
    }

}
