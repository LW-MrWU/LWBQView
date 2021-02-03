package vip.gameclub.lwbqview.model;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义变量基础服务
 *
 * @author LW-MrWU
 * @date 创建时间 2021/2/2 16:49
 */
public class BaseVariableUtil {
    public static String getVariable(String str, String keyWord){
        String regex = "\\$\\{(?<var>.*?)" + keyWord + "\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return matcher.group("var");
        }
        return "";
    }

    public static boolean isContains(String str, String keyWord){
        return StringUtils.isNotEmpty(getVariable(str, keyWord));
    }

    public static String replaceVariable(String str, String keyWord, String value){
        String regex = "\\$\\{(?<var>.*?)" + keyWord + "\\}";
        String strNew = str.replaceAll(regex, value);
        return strNew;
    }
}
