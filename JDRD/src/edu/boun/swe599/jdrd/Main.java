/*
 *  Copyright ©2014 Canay ÖZEL (canay.ozel@gmail.com).
 */
package edu.boun.swe599.jdrd;

import edu.boun.swe599.jdrd.util.JDRDUtil;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Canay ÖZEL (canay.ozel@gmail.com)
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
public class Main {

    public static void main(String... args) throws Exception {

        JarFile source;
        if (args == null || args.length < 1) {
            //Create a file chooser
            JFileChooser fc = new JFileChooser();
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".jar");
                }

                @Override
                public String getDescription() {
                    return "";
                }
            });

            int returnVal = fc.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                source = new JarFile(fc.getSelectedFile());
            } else {
                return;
            }
        } else {
            source = new JarFile(args[0]);
        }

        try (JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(source.getName() + "_jdrd"))) {
            Enumeration<? extends JarEntry> entries = source.entries();
            while (entries.hasMoreElements()) {
                JarEntry e = entries.nextElement();
                byte[] bytes = new byte[(int) e.getSize()];
                new DataInputStream(source.getInputStream(e)).readFully(bytes);
                if (e.getName().endsWith(".class")) {
                    bytes = JDRDUtil.convertClassData(bytes);
                }
                e = new JarEntry(e.getName());
                jarOutputStream.putNextEntry(e);
                jarOutputStream.write(bytes);
                jarOutputStream.closeEntry();
            }
        }
    }
}
