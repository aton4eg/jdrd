/*
 *  Copyright ©2014 Canay ÖZEL <canay.ozel@gmail.com>.
 */
package edu.boun.swe599.jdrd.test;

import edu.boun.swe599.jdrd.JDRD;

/**
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
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
