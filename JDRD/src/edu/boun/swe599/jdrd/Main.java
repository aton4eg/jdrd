/*
 *  Copyright ©2014 Canay ÖZEL <canay.ozel@gmail.com>.
 */
package edu.boun.swe599.jdrd;

import edu.boun.swe599.jdrd.test.LoadStoreTest;
import edu.boun.swe599.jdrd.test.SynchronizationTest;
import edu.boun.swe599.jdrd.test.TestThread;

/**
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
public class Main {

//    public static void main(String[] args) {
//        // -- Fill a weak hash map with one entry
//        WeakHashMap<Data, String> map = new WeakHashMap<>();
//        Data someDataObject = new Data("foo");
//        map.put(someDataObject, someDataObject.value);
//        System.out.println("map contains someDataObject ? " + map.containsKey(someDataObject));
//
//        Set<Data> a = new HashSet<>(map.keySet());
//        a.retainAll(a);
//        System.out.println(map.size());
//        // -- now make someDataObject elligible for garbage collection...
//        someDataObject = null;
//        a = null;
//
//        for (int i = 0; i < 10000; i++) {
//            if (map.size() != 0) {
//                System.out.println("At iteration " + i + " the map still holds the reference on someDataObject");
//            } else {
//                System.out.println("somDataObject has finally been garbage collected at iteration " + i + ", hence the map is now empty");
//                break;
//            }
//        }
//    }
//
//    static class Data {
//
//        String value;
//
//        Data(String value) {
//            this.value = value;
//        }
//    }

    public static void main(String[] args) throws Exception {
        LoadStoreTest data = new LoadStoreTest();
        new Thread(new TestThread(data)).start();
        new Thread(new TestThread(data)).start();
    }
}
