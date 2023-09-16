package AppTools;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Shortcut {
    private boolean isOpen =false;//是否被打开
    private String name;//指向文件的名字
    private String shortcutUrl;//快捷方式地址
    private String realUrl;//真实地址
    private Image image;//图标
//    private Icon icon;//图标
    public Shortcut(String name,String shortcutUrl, String realUrl, Image image) {
        this.name=name;
        this.shortcutUrl = shortcutUrl;
        this.realUrl = realUrl;
        this.image = image;
    }

    public Shortcut() {
    }

    public String getShortcutUrl() {
        return shortcutUrl;
    }

    public void setShortcutUrl(String shortcutUrl) {
        this.shortcutUrl = shortcutUrl;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    void runInManagerMode(){//以管理者模式运行
        File file=new File(shortcutUrl);
        if(!file.isFile()){
            System.out.println("这不是一个有效的文件！！！！");//可以加提醒框
            return;
        }
        try {
            String programName = file.getName();
            List<String> list = new ArrayList<String>();
            list.add("cmd.exe");
            list.add("/c");
            list.add("start");
            list.add("\"" + programName + "\"");
            list.add("\"" + file.getPath() + "\"");
            ProcessBuilder pBuilder = new ProcessBuilder(list);
            pBuilder.start();
            isOpen=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void run(){//一般形式运行
        try {
            new ProcessBuilder(shortcutUrl).start();
            isOpen=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void openFolder(){//打开所在文件夹
        int index=realUrl.length()-1;
        while (index>=0){
            if(realUrl.charAt(index)==File.separatorChar)
                break;
            index--;
        }
        String target =realUrl.substring(0,index);
        try {
            Desktop.getDesktop().open(new File(target));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void quit(){//退出已经打开的程序
        if(!isOpen) return;
        TaskControler.killTask(name);
        isOpen=false;
    }

    @Override
    public String toString() {
        return "Shortcut{" +
                "isOpen=" + isOpen +
                ", name='" + name + '\'' +
                ", shortcutUrl='" + shortcutUrl + '\'' +
                ", realUrl='" + realUrl + '\'' +
                '}';
    }
}

