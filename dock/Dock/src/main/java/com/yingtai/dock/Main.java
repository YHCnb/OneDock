package com.yingtai.dock;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static <List> void main(String[] args) {
        File file=new File("config/tool/");
        if(!file.exists()){
            file.mkdirs();
        }
        ArrayList<String> names=new ArrayList<String>();
        names.add("IconExtractor.exe");
        names.add("hide.exe");
        names.add("clean.exe");
        names.add("run.exe");
        for(var name:names){
            try (InputStream inputStream = Main.class.getResourceAsStream("tool/"+name);
                 FileOutputStream fileOutputStream = new FileOutputStream("config/tool/"+name);){
                byte[] buf = new byte[4096];
                int r;
                while(-1 != (r = inputStream.read(buf))) {
                    fileOutputStream.write(buf, 0, r);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        App.main(args);
    }
}
