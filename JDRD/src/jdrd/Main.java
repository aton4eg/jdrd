package jdrd;

import jdrd.test.LoadStoreTest;

public class Main {

    public static void main(String[] args) throws Exception {
        LoadStoreTest test = new LoadStoreTest();
        test.primitiveFieldTest(3);
        test.objectFieldTest(new Object());

    }
}
