package com.yingtai.dock;

import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.sf.image4j.codec.ico.ICODecoder;

import javax.imageio.ImageIO;

public class IconImage {
    public static Image getIconImage(String realPath) {
        String imgPath = "config/img/";
        File file = new File(imgPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        File realPathFile=new File(realPath);
        if(realPathFile.isDirectory()){
            return new Image(IconImage.class.getResourceAsStream("img/文件夹.png"));
        }

        int end = realPath.length() - 1;
        while (realPath.charAt(end) != '.') end--;
        int start = end;
        while (realPath.charAt(start) != '\\' && realPath.charAt(start) != '/') start--;
        String name = realPath.substring(start + 1, end);

        String imgP = imgPath + name + ".png";
        File icon=new File(imgP);



        String suffix=realPath.substring( realPath.length()-4,  realPath.length());
        if(suffix.equals(".lnk")){
            realPath=lnkToRealPath(realPath);
            Process obj = null;
            try {
                obj = new ProcessBuilder("config/tool/IconExtractor.exe", realPath, imgPath).start();
//                System.out.println(IconImage.class.getResource("tool/IconExtractor.exe").getPath());
            } catch (IOException e) {
                System.out.println("IconExtractor异常");
                throw new RuntimeException(e);
            }
            while (obj.isAlive()) ;
            icon=new File(imgP);

            List<BufferedImage> images = null;
            try {
                images = ICODecoder.read(new File(imgPath + name + ".ico"));
                ImageIO.write(images.get(0), "png", icon);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Image image;
            try {
                if(icon.exists())
                    image = new Image("file:" + icon.getCanonicalPath());
                else {
                    image=new Image(IconImage.class.getResourceAsStream("img/默认图标文件.png"));
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return image;
        }
        else if(suffix.equals(".exe")){
            Process obj = null;
            try {
                obj = new ProcessBuilder("config/tool/IconExtractor.exe", realPath, imgPath).start();
            } catch (IOException e) {
                System.out.println("IconExtractor异常");
                throw new RuntimeException(e);
            }
            while (obj.isAlive()) ;
            icon=new File(imgP);

            List<BufferedImage> images = null;
            try {
                images = ICODecoder.read(new File(imgPath + name + ".ico"));
                ImageIO.write(images.get(0), "png", icon);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Image image = null;
            try {
                if(icon.exists())
                    image = new Image("file:" + icon.getCanonicalPath());
                else {
                    image=new Image(IconImage.class.getResourceAsStream("img/默认图标文件.png"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return image;
        }
        else if(suffix.equals(".jpg")||suffix.equals(".png")||suffix.equals(".gif")||suffix.equals(".bmp")){
            return new Image(realPath);
        }
        else if(suffix.equals(".txt")||suffix.equals(".java")||suffix.equals(".c")||suffix.equals(".cpp")){
            return new Image(IconImage.class.getResourceAsStream("img/记事本.png"));
        }
        else {
            return new Image(IconImage.class.getResourceAsStream("img/默认图标文件.png"));
        }
    }

    public static String lnkToRealPath(String filePath) {
        File file = new File(filePath);
        WindowsShortcut link;
        try {
            link = new WindowsShortcut(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return link.getRealFilename();
    }

}