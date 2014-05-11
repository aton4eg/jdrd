package edu.boun.swe599.jdrd.test;

import edu.boun.swe599.jdrd.JDRD;

public class SynchronizationTest {

    private final Object lock = new Object();

    public SynchronizationTest() {
//        super();
//        synchronized (this) {
//            System.out.println("This is a synchronized initialization.");
//        }
    }

    public synchronized void synchronizedMethod() {
        JDRD.lockAcquired("java.lang.String");
        System.out.println("This is a staticsynchronized method.");
    }

    public synchronized static void synchronizedStaticMethod() {
        System.out.println("This is a synchronized static method.");
    }

    public void statementSynchronizedMethod() {
        synchronized (lock) {
            System.out.println("This is a synchronized statement 1.");
            synchronized (lock) {
                System.out.println("This is a synchronized statement 2.");
            }
        }
    }
}
