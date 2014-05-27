/*
 *  Copyright ©2014 Canay ÖZEL (canay.ozel@gmail.com).
 */
package edu.boun.swe599.test;

/**
 *
 * @author Canay ÖZEL (canay.ozel@gmail.com)
 * @version 1.0 created on May 12, 2014 10:50:37 PM
 */
public class SyncronizedTestThread implements Runnable {

    private final LoadStoreTest testData;

    public SyncronizedTestThread(LoadStoreTest testData) {
        this.testData = testData;
    }

    @Override
    public void run() {
        synchronized (testData) {
            this.testData.setObject(new Object());
        }
    }
}
