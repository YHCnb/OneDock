package AppTools;

import java.io.*;

public class GetRealUrl {//parseLink获取lnk的真实地址
    public static String parseLink(File f) throws FileNotFoundException, IOException {
        FileInputStream fin = new FileInputStream(f);
        byte[] link = new byte[(int) f.length()];
//读取文件中的内容到link[]数组
        fin.read(link);
        fin.close();
// 判断当前文件是否为快捷方式
        if (!isLnkFile(link)) {
            return null;
        }
// 获得flags信息
        byte flags = link[0x14];
        int shell_len = 0;
// 0000 0000 xxxx xxxx & 0000 0000 0000 0001(判断是否包含shell item id list段)
        if ((flags & 0x1) > 0) {
// 如果存在，则获取shell item id list段的总长度，加2是为了将link[0x4c]本身的长度计算在内
            shell_len = bytes2short(link, 0x4c) + 2;
        }
// 获得文件位置信息段的开始位置=shell item id list段的开始位置+shell item id list段的总长度
        int file_start = 0x4c + shell_len;
// 获取本地路径信息的偏移
        int local_sys_off = link[file_start + 0x10] + file_start;
        String real_file = getNullDelimitedString(link, local_sys_off);
        return real_file;
    }
    private static boolean isLnkFile(byte[] link) {
        if (link[0x00] == 0x4c) {// 76,L,0x4c代表lnk文件格式
            return true;
        }
        return false;
    }
    /**
     * 将两个字节转换为short
     * <p>
     * 注意，因为仅限英特尔操作系统，所以这是小端字节
     */
    private static int bytes2short(byte[] bytes, int off) {
        return bytes[off] | (bytes[off + 1] << 8);
    }

    /**
     * 获得从偏移位置off到以‘0’为结尾分割字符串
     *
     * @param bytes 源数组
     * @param off   偏移位置
     * @return 字符串
     */
    private static String getNullDelimitedString(byte[] bytes, int off) {
        int len = 0;
// 计算字符串占用数组的真实长度
        while (true) {
            if (bytes[off + len] == 0) {
                break;
            }
            len++;
        }
        byte[] results = new byte[len];
        for (int i = off, j = 0; i < off + len; i++, j++) {
            results[j] = bytes[i];
        }
        try {
            return new String(bytes, off, len, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
