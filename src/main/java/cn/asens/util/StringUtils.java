package cn.asens.util;

/**
 * Created by Asens on 2017/8/24.
 */
public class StringUtils {
    public static boolean isBlank(String s){
        return s==null||s.equals("");
    }

    public static boolean isNotBlank(String s){
        return !isBlank(s);
    }
}
