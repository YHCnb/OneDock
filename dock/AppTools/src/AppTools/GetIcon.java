//package AppTools;
//
//import javax.swing.*;
//import java.io.File;
//import java.io.FileNotFoundException;
//
//public class GetIcon {
//    public static Icon getBigIcon(File f) {
//        if (f!=null && f.exists()) {
//            try {
//                sun.awt.shell.ShellFolder sf = sun.awt.shell.ShellFolder.getShellFolder(f);
//                return new ImageIcon(sf.getIcon(true));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//}
