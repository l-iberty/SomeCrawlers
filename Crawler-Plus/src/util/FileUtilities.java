package util;

import java.io.*;
import java.util.StringTokenizer;

public class FileUtilities {
    public static void createFile(String pathname, String extensionName) {
        StringTokenizer token = new StringTokenizer(pathname, "/");
        String path = "";
        File filepath;

        try {
            while (token.hasMoreTokens()) {
                path = path + token.nextToken() + "\\";
                filepath = new File(path);
                if (!filepath.exists()) {
                    if (path.endsWith(extensionName + "\\")) {
                        filepath.createNewFile();
                    } else {
                        filepath.mkdir();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPathname(String pathname) {
        return pathname.replace('/', '\\');
    }

    public static boolean isImageFile(String filename) {
        return filename.endsWith(".jpg") || filename.endsWith(".JPG") ||
                filename.endsWith(".jpeg") || filename.endsWith(".JPEG") ||
                filename.endsWith(".png") || filename.endsWith(".PNG") ||
                filename.endsWith(".ico") || filename.endsWith(".ICO") ||
                filename.endsWith(".gif") || filename.endsWith(".GIF") ||
                filename.endsWith(".jpe") || filename.endsWith(".JPE") ||
                filename.endsWith(".jfif") || filename.endsWith(".JFIF") ||
                filename.endsWith(".fax") || filename.endsWith(".FAX") ||
                filename.endsWith(".tif") || filename.endsWith(".TIF");
    }

    public static boolean isXXXFile(String filename, String extensionName) {
        return filename.endsWith(extensionName)
                || filename.endsWith(extensionName.toUpperCase());
    }

    public static String getExtensionName(String filename) {
        int startPos = filename.lastIndexOf('.');
        return filename.substring(startPos);
    }
}
