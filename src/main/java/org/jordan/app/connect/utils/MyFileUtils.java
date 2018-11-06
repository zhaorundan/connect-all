package org.jordan.app.connect.utils;

import java.io.File;
import java.net.URL;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/24下午8:09
 */
public class MyFileUtils {
    /**
     * 获取当前类路径
     * @return
     */
    public static String getJarPath() {
        String path = System.getProperty("java.class.path");
        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) +1;
        path = path.substring(firstIndex, lastIndex);
        return path;
    }

    public static String getUserDir() {
        return System.getProperty("user.dir");
    }

    public static String getClasspath() {
        URL defaultUrl = MyFileUtils.class.getResource("/");
        return defaultUrl.getPath();
    }
}
