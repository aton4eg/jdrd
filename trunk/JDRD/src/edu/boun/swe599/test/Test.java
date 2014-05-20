/*
 *  Copyright ©2014 Canay ÖZEL <canay.ozel@gmail.com>.
 */
package edu.boun.swe599.test;

/**
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
public class Test {

    public static void main(String[] args) {
        LoadStoreTest data = new LoadStoreTest();
        Thread thread1 = new Thread(new TestThread(data));

        thread1.start();
        data.getObject();
    }
}
