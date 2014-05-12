/*
 *  Copyright ©2014 Canay ÖZEL <canay.ozel@gmail.com>.
 */
package edu.boun.swe599.jdrd.test;

/**
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 * @version 1.0 created on May 12, 2014 10:50:37 PM
 */
public class TestThread implements Runnable {

    private LoadStoreTest testData;

    public TestThread(LoadStoreTest testData) {
        this.testData = testData;
    }

    @Override
    public void run() {
        this.testData.objectFieldTest(new Object());
        synchronized (testData) {
            this.testData.objectFieldTest(new Object());
        }
    }

}
