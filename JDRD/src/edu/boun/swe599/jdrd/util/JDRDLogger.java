/*
 *  Copyright ©2014 Canay ÖZEL <canay.ozel@gmail.com>.
 */
package edu.boun.swe599.jdrd.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Canay ÖZEL <canay.ozel@gmail.com>
 * @version 1.0 created on May 13, 2014 11:49:17 PM
 */
public class JDRDLogger {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final PrintStream out;

    static {
        PrintStream printStream = JDRDConfiguration.getLogStream();
        if (printStream == null) {
            out = System.out;
            System.err.println(getHeader() + "Log file cannot be created System.out will be used to log JDRD messages.");
        } else {
            out = printStream;
        }
    }

    private static String getHeader() {
        return "JDRD: " + formatter.format(Calendar.getInstance().getTime()) + " - " + JDRDUtil.getCurrentThreadName() + "#" + JDRDUtil.getCurrentThreadId() + " - ";
    }

    public static void log(String message) {
        out.println(getHeader() + message);
        out.flush();
    }

}
