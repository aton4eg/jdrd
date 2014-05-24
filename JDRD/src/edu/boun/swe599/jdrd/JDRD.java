package edu.boun.swe599.jdrd;
/*
 *  Copyright ©2014 Canay ÖZEL (canay.ozel@gmail.com).
 */

import edu.boun.swe599.jdrd.data.FieldStateData;
import edu.boun.swe599.jdrd.util.JDRDUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Task 1 : instrument all acquire and release operations on locks : DONE<br/>
 * Task 2 : instrument load and stores of necessary variables: DONE<br/>
 * Task 3 : get all possible locks: DONE<br/>
 * Task 4 : implement algorithm DONE <br/>
 * Task 5 : implement user interface DONE<br/>
 * Task 6 : implement java agent DONE<br/>
 *
 * @author Canay ÖZEL (canay.ozel@gmail.com)
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
public abstract class JDRD {

    private static class ClassConstant {

        private static final Map<String, ClassConstant> classes = new HashMap<String, ClassConstant>();

        private final String className;

        private ClassConstant(String className) {
            this.className = className;
        }

        public static ClassConstant getInstance(String className) {
            ClassConstant constant;
            if ((constant = classes.get(className)) == null) {
                constant = new ClassConstant(className);
                classes.put(className, constant);
            }
            return constant;
        }

    }

    private static long threadCount = 0;
    private static long variableCount = 0;
    private static final Map<Object, Map<String, FieldStateData>> DATA_STATES;
    private static final Map<Thread, WeakHashMap<Object, Integer>> THREAD_LOCKS;

    static {
        THREAD_LOCKS = new WeakHashMap<>();
        DATA_STATES = new WeakHashMap<>();
    }

    public static synchronized void lockAcquired(String className) {
        lockAcquired(ClassConstant.getInstance(className));

    }

    public static synchronized void lockReleased(String className) {
        lockReleased(ClassConstant.getInstance(className));
    }

    public static synchronized void lockAcquired(Object lock) {
        WeakHashMap<Object, Integer> lockSet;
        Thread currentThread = Thread.currentThread();
        if ((lockSet = THREAD_LOCKS.get(currentThread)) == null) {
            THREAD_LOCKS.put(currentThread, lockSet = new WeakHashMap<>());
            threadCount++;
            if (JDRDConfiguration.isDebugEnabled()) {
                JDRDLogger.log("Thread count: " + threadCount);
            }
        }
        Integer count = lockSet.get(lock);
        count = count == null ? 1 : count + 1;
        lockSet.put(lock, count);
    }

    public static synchronized void lockReleased(Object lock) {
        Thread currentThread = Thread.currentThread();
        WeakHashMap<Object, Integer> lockSet;
        if ((lockSet = THREAD_LOCKS.get(currentThread)) != null) {
            Integer count = lockSet.get(lock);
            count = count == null ? 1 : count;
            if ((count -= 1) == 0) {
                lockSet.remove(lock);
            } else {
                lockSet.put(lock, count);
            }
        }
    }

    public static synchronized void fieldIsBeingRead(String className, String field, String methodName, int line) {
        fieldIsBeingRead(ClassConstant.getInstance(className), field, methodName, line);
    }

    public static synchronized void fieldIsBeingWritten(String className, String field, String methodName, int line) {
        fieldIsBeingWritten(ClassConstant.getInstance(className), field, methodName, line);
    }

    public static synchronized void fieldIsBeingRead(Object owner, String field, String methodName, int line) {
        if (JDRDConfiguration.isDebugEnabled()) {
            JDRDLogger.log("Field " + field + " is being read by " + Thread.currentThread().getName() + "#" + JDRDUtil.getCurrentThreadId() + " in " + owner.getClass().getName() + "@" + System.identityHashCode(owner) + "." + methodName + " at line " + line);
        }
        if (checkRaceCondition(owner, field, false)) {
            JDRDLogger.log("RACE DETECTED in " + owner.getClass().getName() + "." + methodName + "'s read operation on field " + field + " at line:" + line);
        }
    }

    public static synchronized void fieldIsBeingWritten(Object owner, String field, String methodName, int line) {
        if (JDRDConfiguration.isDebugEnabled()) {
            JDRDLogger.log("Field " + field + " is being written by " + Thread.currentThread().getName() + "#" + JDRDUtil.getCurrentThreadId() + " in " + owner.getClass().getName() + "@" + System.identityHashCode(owner) + "." + methodName + " at line " + line);
        }
        if (checkRaceCondition(owner, field, true)) {
            JDRDLogger.log("RACE DETECTED in " + owner.getClass().getName() + "." + methodName + "'s write operation on field " + field + " at line:" + line);
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
            variableCount++;
            if (JDRDConfiguration.isDebugEnabled()) {
                JDRDLogger.log("Variable count: " + variableCount);
            }
        }
        return fieldStateData;
    }

    private static WeakHashMap<Object, ?> getLocksHeld() {
        Thread currentThread = Thread.currentThread();
        WeakHashMap<Object, Integer> locksHeld;
        if ((locksHeld = THREAD_LOCKS.get(currentThread)) == null) {
            locksHeld = new WeakHashMap<>();
            THREAD_LOCKS.put(currentThread, locksHeld);
        }

        for (Iterator<Object> it = locksHeld.keySet().iterator(); it.hasNext();) {
            Object object = it.next();
            if (!Thread.holdsLock(object)) {
                it.remove();
            }
        }

        return locksHeld;
    }

    private static boolean checkRaceCondition(Object owner, String field, boolean isWrite) {
        FieldStateData fieldStateData = getFieldStateData(owner, field);
        return fieldStateData.signalAccess(getLocksHeld(), isWrite);
    }

}
