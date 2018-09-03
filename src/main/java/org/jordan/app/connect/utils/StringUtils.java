package org.jordan.app.connect.utils;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/23下午10:59
 */
public class StringUtils {

    public static boolean isNotBlank(String parma) {
        if (parma == null) {
            return false;
        }
        if (parma.trim().length() ==0) {
            return false;
        }
        return true;
    }

    public static boolean isBlank(String param) {
        if (param == null) {
            return true;
        }
        if (param.trim().length() == 0) {
            return true;
        }
        return false;
    }
}
