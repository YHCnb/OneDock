package AppTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskControler {
    public static List<String> getAllTask() {//获取后台进程
        List<String> processList = new ArrayList<String>();
        try {
            Process tasklist = new ProcessBuilder("tasklist").start();
            //这是第一种
            //      BufferedReader input = new BufferedReader(new InputStreamReader(tasklist.getInputStream()));
            //      String line = "";
            //      while ((line = input.readLine()) != null) {
            //          processList.add(line);
            //      }
            //      input.close();

            //这是第二种
            Scanner in = new Scanner(tasklist.getInputStream());
            while (in.hasNextLine()) {
                String p = in.nextLine();
                processList.add(p);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processList;
    }

    public static List filterTaskName(List<String> taskNames) {//筛选进程
        List filterTaskNames = new ArrayList();
        for (int i = 0; i < taskNames.size(); i++) {
            if (taskNames.get(i).indexOf(".EXE") != -1) {
                filterTaskNames.add(taskNames.get(i).substring(0, taskNames.get(i).indexOf(".EXE")));
            } else if (taskNames.get(i).indexOf(".exe") != -1) {
                filterTaskNames.add(taskNames.get(i).substring(0, taskNames.get(i).indexOf(".exe")));
            }
        }
        return filterTaskNames;
    }

    public static boolean isHavaTask(String taskName) {//检查是否含有需要查找的进程名
        List<String> processList = getAllTask();
        for (String s : processList) {
            if (s.length() < taskName.length()) continue;
            String tt = s.substring(0, taskName.length());
            if (tt.equals(taskName)) return true;
        }
        return false;
    }

    public static void killTask(String taskName) {//结束需要停止的进程
        if (!isHavaTask(taskName))
            return;
        try {
            Process p = Runtime.getRuntime().exec("taskkill /f /im " + taskName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
