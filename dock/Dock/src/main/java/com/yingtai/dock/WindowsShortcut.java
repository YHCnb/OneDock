package com.yingtai.dock;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

/*
* 本代码是从网上搜索而来，并非本人原创
* 来源：https://github.com/codebling/WindowsShortcuts
* */

public class WindowsShortcut
{
    private boolean isDirectory;
    private boolean isLocal;
    private String real_file;
    private String description;
    private String relative_path;
    private String working_directory;
    private String command_line_arguments;


    public static boolean isPotentialValidLink(final File file) throws IOException {
        final int minimum_length = 0x64;
        final InputStream fis = new FileInputStream(file);
        boolean isPotentiallyValid = false;
        try {
            isPotentiallyValid = file.isFile()
                    && file.getName().toLowerCase().endsWith(".lnk")
                    && fis.available() >= minimum_length
                    && isMagicPresent(getBytes(fis, 32));
        } finally {
            fis.close();
        }
        return isPotentiallyValid;
    }

    public WindowsShortcut(final File file) throws IOException, ParseException {
        final InputStream in = new FileInputStream(file);
        try {
            parseLink(getBytes(in));
        } finally {
            in.close();
        }
    }


    public String getRealFilename() {
        return real_file;
    }


    public String getDescription() {
        return description;
    }


    public String getRelativePath() {
        return relative_path;
    }


    public String getWorkingDirectory() {
        return working_directory;
    }


    public String getCommandLineArguments() {
        return command_line_arguments;
    }


    public boolean isLocal() {
        return isLocal;
    }


    public boolean isDirectory() {
        return isDirectory;
    }


    private static byte[] getBytes(final InputStream in) throws IOException {
        return getBytes(in, null);
    }


    private static byte[] getBytes(final InputStream in, Integer max) throws IOException {

        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final byte[] buff = new byte[256];
        while (max == null || max > 0) {
            final int n = in.read(buff);
            if (n == -1) {
                break;
            }
            bout.write(buff, 0, n);
            if (max != null)
                max -= n;
        }
        in.close();
        return bout.toByteArray();
    }

    private static boolean isMagicPresent(final byte[] link) {
        final int magic = 0x0000004C;
        final int magic_offset = 0x00;
        return link.length >= 32 && bytesToDword(link, magic_offset) == magic;
    }


    private void parseLink(final byte[] link) throws ParseException {
        try {
            if (!isMagicPresent(link))
                throw new ParseException("Invalid shortcut; magic is missing", 0);


            final byte flags = link[0x14];


            final int file_atts_offset = 0x18;
            final byte file_atts = link[file_atts_offset];
            final byte is_dir_mask = (byte)0x10;
            if ((file_atts & is_dir_mask) > 0) {
                isDirectory = true;
            } else {
                isDirectory = false;
            }


            final int shell_offset = 0x4c;
            final byte has_shell_mask = (byte)0x01;
            int shell_len = 0;
            if ((flags & has_shell_mask) > 0) {

                shell_len = bytesToWord(link, shell_offset) + 2;
            }


            final int file_start = 0x4c + shell_len;

            final int file_location_info_flag_offset_offset = 0x08;
            final int file_location_info_flag = link[file_start + file_location_info_flag_offset_offset];
            isLocal = (file_location_info_flag & 1) == 1;

            final int basename_offset_offset = 0x10;
            final int networkVolumeTable_offset_offset = 0x14;
            final int finalname_offset_offset = 0x18;
            final int finalname_offset = bytesToDword(link,file_start + finalname_offset_offset) + file_start;
            final String finalname = getNullDelimitedString(link, finalname_offset);
            if (isLocal) {
                final int basename_offset = bytesToDword(link,file_start + basename_offset_offset) + file_start;
                final String basename = getNullDelimitedString(link, basename_offset);
                real_file = basename + finalname;
            } else {
                final int networkVolumeTable_offset = link[file_start + networkVolumeTable_offset_offset] + file_start;
                final int shareName_offset_offset = 0x08;
                final int shareName_offset = link[networkVolumeTable_offset + shareName_offset_offset]
                        + networkVolumeTable_offset;
                final String shareName = getNullDelimitedString(link, shareName_offset);
                real_file = shareName + "\\" + finalname;
            }


            final int file_location_size = bytesToDword(link, file_start);
            int next_string_start = file_start + file_location_size;

            final byte has_description				= (byte)0b00000100;
            final byte has_relative_path			= (byte)0b00001000;
            final byte has_working_directory		= (byte)0b00010000;
            final byte has_command_line_arguments	= (byte)0b00100000;


            if ((flags & has_description) > 0) {
                final int string_len = bytesToWord(link, next_string_start) * 2; // times 2 because UTF-16
                description = getUTF16String(link, next_string_start + 2, string_len);
                next_string_start = next_string_start + string_len + 2;
            }


            if ((flags & has_relative_path) > 0) {
                final int string_len = bytesToWord(link, next_string_start) * 2; // times 2 because UTF-16
                relative_path = getUTF16String(link, next_string_start + 2, string_len);
                next_string_start = next_string_start + string_len + 2;
            }


            if ((flags & has_working_directory) > 0) {
                final int string_len = bytesToWord(link, next_string_start) * 2; // times 2 because UTF-16
                working_directory = getUTF16String(link, next_string_start + 2, string_len);
                next_string_start = next_string_start + string_len + 2;
            }


            if ((flags & has_command_line_arguments) > 0) {
                final int string_len = bytesToWord(link, next_string_start) * 2; // times 2 because UTF-16
                command_line_arguments = getUTF16String(link, next_string_start + 2, string_len);
                next_string_start = next_string_start + string_len + 2;
            }

        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new ParseException("Could not be parsed, probably not a valid WindowsShortcut", 0);
        }
    }

    private static String getNullDelimitedString(final byte[] bytes, final int off) {
        int len = 0;

        while (true) {
            if (bytes[off + len] == 0) {
                break;
            }
            len++;
        }
        return new String(bytes, off, len);
    }

    private static String getUTF16String(final byte[] bytes, final int off, final int len) {
        return new String(bytes, off, len, StandardCharsets.UTF_16LE);
    }

    private static int bytesToWord(final byte[] bytes, final int off) {
        return ((bytes[off + 1] & 0xff) << 8) | (bytes[off] & 0xff);
    }

    private static int bytesToDword(final byte[] bytes, final int off) {
        return (bytesToWord(bytes, off + 2) << 16) | bytesToWord(bytes, off);
    }

}
