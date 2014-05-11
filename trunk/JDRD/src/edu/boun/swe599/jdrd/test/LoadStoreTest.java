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
public class LoadStoreTest {

    private int primitive = 5;//GETFIELD & PUTFIELD: may fall into race condition
    private final int finalPrimitive = 5; // LDC or xCONST: no race condition
    private Object object = new Object();//GETFIELD & PUTFIELD: may fall into race condition
    private final Object finalObject = new Object();// GETFIELD: may fall into race condition if it is not immutable
    public static int staticPrimitive = 5; // GETSTATIC & PUTSTATIC: may fall into race condition
    public static final int staticFinalPrimitive = 5;//LDC or xCONST: no race condition
    public static Object staticObject = new Object();// GETSTATIC & PUTSTATIC: may fall into race condition
    public static final Object staticFinalObject = new Object();// GETSTATIC: may fall into race condition if it is not immutable

    public void test() {
        JDRD.fieldIsBeingRead(object, null, null, primitive);
    }

    public int primitiveFieldTest(int parameter) {
        int a = finalPrimitive;
        primitive = parameter;
        return primitive;
    }

    public Object objectFieldTest(Object parameter) {
        Object a = finalObject;
        object = parameter;
        return object;
    }

    public int staticPrimitiveTest(int parameter) {
        int a = staticFinalPrimitive;
        staticPrimitive = parameter;
        return staticPrimitive;
    }

    public Object staticObhectTest(Object parameter) {
        Object a = staticFinalObject;
        staticObject = parameter;
        return staticObject;
    }
}
