/*
 *  Copyright ©2014 Canay ÖZEL (canay.ozel@gmail.com).
 */
package edu.boun.swe599.jdrd;

import edu.boun.swe599.jdrd.util.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Canay ÖZEL (canay.ozel@gmail.com)
 * @version 1.0 created on May 13, 2014 11:07:48 PM
 */
@SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
public class JDRDConfiguration {

    private static final String NAMES_OF_CLASSES_TO_TRANSFORM = "jdrd.classes.transform";
    private static final String LOG_PATH = "jdrd.log.file.path";
    private static final String DEBUG = "jdrd.debug";
    private static final String VARIABLE_STATES = "jdrd.variables.states.enabled";
    private static final String CONSOLE_OUTPUT = "jdrd.console";
    private static final Properties JDRD_PROPERTIES = new Properties();

    static {
        try {
            JDRD_PROPERTIES.load(new FileInputStream(
                    JDRDConfiguration.class.getProtectionDomain().getCodeSource().getLocation().toString()
                    .replaceAll("JDRD.jar", "").replaceAll("file:/", "").concat("jdrd.properties")));
        } catch (Exception ex) {
        }
    }

    public static PrintStream getLogStream() {
        String path = JDRD_PROPERTIES.getProperty(LOG_PATH);
        if (StringUtil.hasText(path)) {
            try {
                path += path.endsWith("/") ? "" : "/";
                File file = new File(path);
                file.mkdirs();
                file = new File(path + "jdrd.log");
                file.createNewFile();
                return new PrintStream(file);
            } catch (IOException ex) {
                return null;
            }
        }
        return null;
    }

    public static Set<String> getNamesOfClassesToTransform() {
        Set<String> nameSet = new HashSet<>();
        String property = JDRD_PROPERTIES.getProperty(NAMES_OF_CLASSES_TO_TRANSFORM);
        if (StringUtil.hasText(property)) {
            String[] names = property.split(";");
            for (String name : names) {
                if (StringUtil.hasText(name)) {
                    name = name.replaceAll("\\*", "").replaceAll("\\.", "/");
                    nameSet.add(name);
                }
            }
        }
        return nameSet;
    }

    public static boolean isDebugEnabled() {
        String property = JDRD_PROPERTIES.getProperty(DEBUG);
        return StringUtil.hasText(property) && Boolean.TRUE.toString().equalsIgnoreCase(property);
    }

    public static boolean isFieldStateCheckEnabled() {
        String property = JDRD_PROPERTIES.getProperty(VARIABLE_STATES);
        return StringUtil.hasText(property) && Boolean.TRUE.toString().equalsIgnoreCase(property);
    }

    public static boolean isConsloleEnabled() {
        String property = JDRD_PROPERTIES.getProperty(CONSOLE_OUTPUT);
        return StringUtil.hasText(property) && Boolean.TRUE.toString().equalsIgnoreCase(property);
    }
}
