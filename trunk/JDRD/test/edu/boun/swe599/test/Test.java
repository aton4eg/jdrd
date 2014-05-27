/*
 *  Copyright ©2014 Canay ÖZEL (canay.ozel@gmail.com).
 */
package edu.boun.swe599.test;

/**
 *
 * @author Canay ÖZEL (canay.ozel@gmail.com)
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
public class Test {

    public static void main(String[] args) {
        test4();
    }

    public static void test1() {
        LoadStoreTest data = new LoadStoreTest();
        new Thread(new TestThread(data)).start();
        data.getObject();
    }

    public static void test1_1() {
        LoadStoreTest data = new LoadStoreTest();
        data.getObject();
        new Thread(new TestThread(data)).start();
    }

    public static void test1_2() throws InterruptedException {
        LoadStoreTest data = new LoadStoreTest();
        Thread thread1 = new Thread(new TestThread(data));
        thread1.start();
        thread1.join();
        data.getObject();
    }

    public static void test1_3() {
        LoadStoreTest data = new LoadStoreTest();
        Thread thread1 = new Thread(new SyncronizedTestThread(data));
        thread1.start();
        data.getObject();
    }

    public static void test2() {
        LoadStoreTest data = new LoadStoreTest();
        Thread thread1 = new Thread(new TestThread(data));
        Thread thread2 = new Thread(new TestThread(data));
        thread1.start();
        thread2.start();
    }

    public static void test3() {
        LoadStoreTest data = new LoadStoreTest();
        Thread thread1 = new Thread(new SyncronizedTestThread(data));
        Thread thread2 = new Thread(new SyncronizedTestThread(data));
        thread1.start();
        thread2.start();
    }

    public static void test4() {
        LoadStoreTest data = new LoadStoreTest();
        Thread thread1 = new Thread(new TestThread(data));
        Thread thread2 = new Thread(new SyncronizedTestThread(data));
        thread1.start();
        thread2.start();
    }
}
