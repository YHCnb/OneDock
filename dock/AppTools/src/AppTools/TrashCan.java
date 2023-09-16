package AppTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TrashCan extends Shortcut{
    private boolean isOpen=false;
    private String sid;
    public TrashCan() {
        this.setOpen(false);
        this.setShortcutUrl("initialPad/TrashCan.lnk");
//        this.setName("废纸篓");
//        sidFlush();
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public void run() {
        try {
            List<String> list = new ArrayList<String>();
            list.add("cmd.exe");
            list.add("/c");
            list.add("explorer.exe ::{645FF040-5081-101B-9F08-00AA002F954E}");
            ProcessBuilder pBuilder = new ProcessBuilder(list);
            pBuilder.start();
            isOpen=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sidFlush(){//获取当前用户的sid
        try {
            List<String> list = new ArrayList<String>();
            list.add("cmd.exe");
            list.add("/c");
            list.add("whoami   /user");
            Process nowSid = new ProcessBuilder(list).start();
            Scanner in = new Scanner(nowSid.getInputStream());
            List<String> sidList = new ArrayList<String>();
            while (in.hasNextLine()) {
                String p = in.nextLine();
                sidList.add(p);
            }
            in.close();
            String s=sidList.get(sidList.size()-1);
            sid=s.split(" ")[1];
//            System.out.println(sid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUp(){
        try {
            new ProcessBuilder("src/JAVACourseCompletionDesign/tools/clean.exe").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            List<String> list = new ArrayList<String>();
//            list.add("cmd.exe");
//            list.add("/c");
//            list.add("rd /s %systemdrive%\\$Recycle.bin");
//            list.add("y");
//
//            ProcessBuilder pBuilder = new ProcessBuilder(list);
//            pBuilder.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
