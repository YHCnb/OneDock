package AppTools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LnkParser {

    public LnkParser(File f) throws IOException {
        parse(f);
    }

    private boolean isDirectory;
    private boolean isLocal;

    public boolean isDirectory() {
        return isDirectory;
    }

    private String real_file;

    public String getRealFilename() {
        return real_file;
    }

    private void parse(File f) throws IOException {
        FileInputStream fin = new FileInputStream(f);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buff = new byte[256];
        while (true) {
            int n = fin.read(buff);
            if (n == -1) {
                break;
            }
            bout.write(buff, 0, n);
        }
        fin.close();
        byte[] link = bout.toByteArray();

        parseLink(link);
    }

    private void parseLink(byte[] link) {
        byte flags = link[0x14];

        final int file_atts_offset = 0x18;
        byte file_atts = link[file_atts_offset];
        byte is_dir_mask = (byte)0x10;
        if ((file_atts & is_dir_mask) > 0) {
            isDirectory = true;
        } else {
            isDirectory = false;
        }

        final int shell_offset = 0x4c;
        final byte has_shell_mask = (byte)0x01;
        int shell_len = 0;
        if ((flags & has_shell_mask) > 0) {
            shell_len = bytes2short(link, shell_offset) + 2;
        }

        int file_start = 0x4c + shell_len;

        final int file_location_info_flag_offset_offset = 0x08;
        int file_location_info_flag = link[file_start + file_location_info_flag_offset_offset];
        isLocal = (file_location_info_flag & 2) == 0;
        final int basename_offset_offset = 0x10;
        final int networkVolumeTable_offset_offset = 0x14;
        final int finalname_offset_offset = 0x18;
        int finalname_offset = link[file_start + finalname_offset_offset] + file_start;
        String finalname = getNullDelimitedString(link, finalname_offset);
        if (isLocal) {
            int basename_offset = link[file_start + basename_offset_offset] + file_start;
            String basename = getNullDelimitedString(link, basename_offset);
            real_file = basename + finalname;
        } else {
            int networkVolumeTable_offset = link[file_start + networkVolumeTable_offset_offset] + file_start;
            int shareName_offset_offset = 0x08;
            int shareName_offset = link[networkVolumeTable_offset + shareName_offset_offset]
                    + networkVolumeTable_offset;
            String shareName = getNullDelimitedString(link, shareName_offset);
            real_file = shareName +"\\" + finalname;
        }
    }

    private static String getNullDelimitedString(byte[] bytes, int off) {
        int len = 0;
        while (true) {
            if (bytes[off + len] == 0) {
                break;
            }
            len++;
        }
        return new String(bytes, off, len);
    }

    private static int bytes2short(byte[] bytes, int off) {
        return ((bytes[off + 1] & 0xff) << 8) | (bytes[off] & 0xff);
    }


    public boolean isLocal() {
        return isLocal;
    }
}
