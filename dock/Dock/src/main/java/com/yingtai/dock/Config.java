package com.yingtai.dock;

import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Config {
    private static final String configIconPath="config/icon.ini";
    private static final String configSettingPath="config/config.ini";

    public static List<DockItem> readIconConfig() {
        List<DockItem> dockItemList=new ArrayList<>();
        File file=new File(configIconPath);

        InputStream inputStream=Config.class.getResourceAsStream("config/icon.ini");
        if(file.exists()&&file.length()!=0){
            //默认图标文件
            try {
                inputStream=new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            String tag=null;
            String realPath=null;
            //添加dockItem
            while((line=bufferedReader.readLine())!=null){
                if(line.startsWith("tag=")){
                    tag=line.substring(4);
                }
                else if(line.startsWith("realpath=")){
                    realPath=line.substring(9);
                }
                else if(line.equals("[end]")&&tag!=null&&realPath!=null){
                    dockItemList.add(new Icon(tag,realPath,IconImage.getIconImage(realPath)));
                }
                else if(line.equals("[clock]")){
                    dockItemList.add(new Clock());
                }
                else if(line.equals("[explorer]")){
                    dockItemList.add(new Explorer());
                }
                else if(line.equals("[trashcan]")){
                    dockItemList.add(new TrashCan());
                }
                else if(line.equals("[separator]")){
                    dockItemList.add(new Separator());
                }
            }
            if(inputStream!=null){
                inputStream.close();
            }

            return dockItemList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void renewIconConfig(List<DockItem> iconList) {
        File file=new File(configIconPath);
        if(!file.exists()){
            //目录不存在时无法直接创建文件
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configIconPath))) {
            String line1="[icon";
            String line2="tag=";
            String line3="realpath=";
            int counter=1;
            for(DockItem item:iconList){
                if(item instanceof Separator){
                    bufferedWriter.write("[separator]");
                    bufferedWriter.newLine();
                }
                else if(item instanceof Clock){
                    bufferedWriter.write("[clock]");
                    bufferedWriter.newLine();
                }
                else if (item instanceof Explorer){
                    bufferedWriter.write("[explorer]");
                    bufferedWriter.newLine();
                }
                else if(item instanceof TrashCan){
                    bufferedWriter.write("[trashcan]");
                    bufferedWriter.newLine();
                }
                else if(item instanceof Icon){
                    bufferedWriter.write(line1+counter+"]");
                    bufferedWriter.newLine();
                    bufferedWriter.write(line2+((Icon)item).getTag());
                    bufferedWriter.newLine();
                    bufferedWriter.write(line3+((Icon)item).getRealPath());
                    bufferedWriter.newLine();
                    bufferedWriter.write("[end]");
                    bufferedWriter.newLine();
                    counter++;
                }
            }
            System.out.println("图标文件已更新");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void readSettingConfig(){
        File file=new File(configSettingPath);
        if(!file.exists()||file.length()==0){
            return;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(configSettingPath))) {
            String line;
            while((line=bufferedReader.readLine())!=null){
                switch(line){
                    case "iconWidth":
                        Integer iconWidth=Integer.valueOf(bufferedReader.readLine().trim());
                        Parament.iconWidth.setValue(iconWidth);
                        break;
                    case "iconEnlargedWidth":
                        Integer iconEnlargedWidth=Integer.valueOf(bufferedReader.readLine().trim());
                        Parament.iconEnlargedWidth.setValue(iconEnlargedWidth);
                        break;
                    case "dockArcWidth":
                        Integer dockArcWidth=Integer.valueOf(bufferedReader.readLine().trim());
                        Parament.dockArcWidth.setValue(dockArcWidth);
                        break;
                    case "iconSpacing":
                        Integer iconSpacing=Integer.valueOf(bufferedReader.readLine().trim());
                        Parament.iconSpacing.setValue(iconSpacing);
                        break;
                    case "dockToBottom":
                        Integer dockToBottom=Integer.valueOf(bufferedReader.readLine().trim());
                        Parament.dockToBottom.setValue(dockToBottom);
                        break;
                    case "glassColor":
                        Integer r,g,b;
                        Double o;
                        String nextLine= bufferedReader.readLine().trim();

                        r=Integer.valueOf(nextLine.substring(0,4).trim());
                        g=Integer.valueOf(nextLine.substring(4,8).trim());
                        b=Integer.valueOf(nextLine.substring(8,12).trim());
                        o=Double.valueOf(nextLine.substring(12,16).trim());
                        Parament.glassColor.set(Color.rgb(r,g,b,o));
                        break;
                    case "isSetAutoDark":
                        Boolean isSetAutoDark=Boolean.valueOf(bufferedReader.readLine().trim());
                        Parament.isSetAutoDark.setValue(isSetAutoDark);
                        break;
                    case "timeBeforeHiding":
                        Integer timeBeforeHiding=Integer.valueOf(bufferedReader.readLine().trim());
                        Parament.timeBeforeHiding.setValue(timeBeforeHiding);
                        break;
                    case "dateModel":
                        Integer dateModel=Integer.valueOf(bufferedReader.readLine().trim());
                        Parament.dateModel.setValue(dateModel);
                        break;
                    case "isIconTag":
                        Boolean isIconTag=Boolean.valueOf(bufferedReader.readLine().trim());
                        Parament.isIconTag.setValue(isIconTag);
                        break;
                    case "isIconAnimation":
                        Boolean isIconAnimation=Boolean.valueOf(bufferedReader.readLine().trim());
                        Parament.isIconAnimation.setValue(isIconAnimation);
                        break;
                    default:
                        System.out.println("未知的设置项");
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void renewSettingConfig(){
        File file=new File(configSettingPath);
        if(!file.exists()){
            //目录不存在时无法直接创建文件
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configSettingPath))) {
            bufferedWriter.write("iconWidth");
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf((int)Parament.iconWidth.get()));
            bufferedWriter.newLine();
            bufferedWriter.write("iconEnlargedWidth");
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf((int)Parament.iconEnlargedWidth.get()));
            bufferedWriter.newLine();
            bufferedWriter.write("dockArcWidth");
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf((int)Parament.dockArcWidth.get()));
            bufferedWriter.newLine();
            bufferedWriter.write("iconSpacing");
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf((int)Parament.iconSpacing.get()));
            bufferedWriter.newLine();
            bufferedWriter.write("dockToBottom");
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf((int)Parament.dockToBottom.get()));
            bufferedWriter.newLine();
            bufferedWriter.write("glassColor");
            bufferedWriter.newLine();
            Color color=Parament.glassColor.get();
            StringBuilder str=new StringBuilder();
            str.replace(0,4,String.format("%-4d",(int)(255* color.getRed())));
            str.replace(4,8,String.format("%-4d",(int)(255* color.getGreen())));
            str.replace(8,12,String.format("%-4d",(int)(255* color.getBlue())));
            str.replace(12,16,String.format("%-4.2f",color.getOpacity()));
            bufferedWriter.write(str.toString());
            bufferedWriter.newLine();
            bufferedWriter.write("isSetAutoDark");
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(Parament.isSetAutoDark.get()));
            bufferedWriter.newLine();
            bufferedWriter.write("timeBeforeHiding");
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf((int)Parament.timeBeforeHiding.get()));
            bufferedWriter.newLine();
            bufferedWriter.write("dateModel");
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf((int)Parament.dateModel.get()));
            bufferedWriter.newLine();
            bufferedWriter.write("isIconTag");
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(Parament.isIconTag.get()));
            bufferedWriter.newLine();
            bufferedWriter.write("isIconAnimation");
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(Parament.isIconAnimation.get()));
            bufferedWriter.newLine();
            System.out.println("设置文件已更新");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
