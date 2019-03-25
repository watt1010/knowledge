//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.watt.core.nlp.cosinesimlarity;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IDExtract {
    public IDExtract() {
    }

    private static Map<Integer, String> getStr(String regex_code, String param) {
        if (regex_code != null && !"".equals(regex_code)) {
            if (param != null && !"".equals(param)) {
                new ArrayList();
                Map<Integer, String> map = new TreeMap();
                Pattern p = Pattern.compile(regex_code);
                Matcher m = p.matcher(param);

                while(m.find()) {
                    map.put(m.start(), m.group());
                }

                return map;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Map<Integer, String> getLetters(String param) {
        String regex_code = "[A-Za-z]+";
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getNumbers(String param) {
        String regex_code = "(\\+|\\-)?\\d+(\\.\\d+)?";
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getEmail(String param) {
        String regex_code = "([a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,})|([1-9]\\d{4,10}@qq.com)";
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getMobile(String param) {
        String regex_code = "(?<!\\d)(?:(?:1[345678]\\d{9})|(?:861[345678]\\d{9}))(?!\\d)";
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getTelNumber(String param) {
        String regex_code = "((\\(0[1-9][0-9]{1,2}\\))|((?<!\\d)0[1-9][0-9]{1,2}))?[-—]?((?<!\\d)[1-9][0-9]{6,7}(?!\\d))";
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getIDCard(String param) {
        String regex_code = "(\\d{6})(18|19|20)?((\\d{2})|(\\*\\*)|(XX)|(xx)|(\\?\\?)|(\\？\\？))(([01]\\d)|(\\*\\*)|(XX)|(xx)|(\\?\\?)|(\\？\\？))(([0123]\\d)|(\\*\\*)|(XX)|(xx)|(\\?\\?)|(\\？\\？))(((\\d{3})(\\d|X|x)?)|(\\*\\*\\*\\*)|(XXXX)|(xxxx)|(\\?\\?\\?\\?)|(\\？\\？\\？\\？))";
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getIPAddr(String param) {
        String regex_code = "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])";
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getQQ(String param) {
        String regex_code = "[1-9]\\d{4,10}";
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getTime(String param) {
        String regex_code1 = "((\\d{4}-\\d{1,2}-\\d{1,2})|(\\d{2,4}\\u5E74\\d{1,2}\\u6708\\d{1,2}\\u65E5))";
        String regex_code2 = "((\\d{2}:\\d{2}:\\d{2})|(((\\d{1,2}\\u65F6)|(\\d{1,2}\\u70b9))(\\d{1,2}\\u5206(\\d{1,2}\\u79D2)?)?))";
        String regex_code3 = "((\\d{4}-\\d{1,2}-\\d{1,2})|(\\d{2,4}\\u5E74\\d{1,2}\\u6708\\d{1,2}\\u65E5)|(\\d{2,4}\\u5E74\\d{1,2}\\u6708)|\\d{1,2}\\u6708\\d{1,2}\\u65E5|\\d{2,4}\\u5E74|\\d{1,2}\\u6708|\\d{1,2}\\u65E5)";
        String regex_code4 = "(\\d{2}:\\d{2}:\\d{2})|(\\d{2}:\\d{2})|(((\\d{1,2}\\u65F6)|(\\d{1,2}\\u70b9))\\d{1,2}\\u5206(\\d{1,2}\\u79D2)?)|(\\d{1,2}\\u65F6)";
        String regex_code = regex_code1 + "(\\s)?" + regex_code2 + "|" + regex_code3 + "|" + regex_code4;
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getCNTime(String param) {
        String regex_code1 = "(([零○一二两三四五六七八九十百千万亿]{2,}\\u5E74([一二三四五六七八九十]|(十(一|二)))\\u6708([一二三四五六七八九十]{1,})\\u65E5)((\\s)?((([零○一二两三四五六七八九十]{1,}\\u65F6)|([零○一二两三四五六七八九十]{1,}\\u70b9))[零○一二两三四五六七八九十]{1,}\\u5206[零○一二两三四五六七八九十]{1,}\\u79D2))*)";
        String regex_code2 = "(([零○一二两三四五六七八九十百千万亿]{2,}\\u5E74([一二三四五六七八九十]|(十(一|二)))\\u6708([一二三四五六七八九十]{1,})\\u65E5)*((\\s)?((([零○一二两三四五六七八九十]{1,}\\u65F6)|([零○一二两三四五六七八九十]{1,}\\u70b9))[零○一二两三四五六七八九十]{1,}\\u5206[零○一二两三四五六七八九十]{1,}\\u79D2)))";
        String regex_code3 = "((([零○一二两三四五六七八九十百千万亿]{2,}\\u5E74([一二三四五六七八九十]|(十(一|二)))\\u6708)|(([一二三四五六七八九十]|(十(一|二)))\\u6708([一二三四五六七八九十]{1,})\\u65E5)|([(零|○)一(二|两)三四五六七八九十百千万亿]{2,}\\u5E74)|(([一二三四五六七八九十]|(十(一|二)))\\u6708)|(([一二三四五六七八九十]{1,})\\u65E5)))";
        String regex_code4 = "((([零○一二两三四五六七八九十]{1,}\\u65F6)|([零○一二两三四五六七八九十]{1,}\\u70b9))[零○一二两三四五六七八九十]{1,}\\u5206|(([零○一二两三四五六七八九十]{1,}\\u65F6)|([零○一二两三四五六七八九十]{1,}\\u70b9)))";
        String regex_code = regex_code1 + "|" + regex_code2 + "|" + regex_code3 + "|" + regex_code4;
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getURL(String param) {
        String regex_code = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getCarNum(String param) {
        String regex_code = "[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}";
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getBankCard(String param) {
        String regex_code = "(\\d{16}|\\d{19})";
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getCNNum(String param) {
        String regex_code = "(第[零○一二两三四五六七八九十廿卅百千万亿]{1,})|((几|数)(十|百|千|万|(十万)|(百万)|(千万)|亿))|((成|上)(百|千|万|(十万)|(百万)|(千万)|亿))|([零○一二两三四五六七八九十廿卅百千万亿]{2,})|([零壹贰叁肆伍陆柒捌玖拾佰仟万亿]{2,})";
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getPerNum(String param) {
        String regex_code1 = "((\\d{1,})(\\.\\d{1,})?%|(百分之(([○零一二两三四五六七八九十廿卅百]{1,})(点[○零一二两三四五六七八九十廿卅]{1,})?)|((\\d{1,})(\\.\\d{1,})?)))";
        String regex_code2 = "((\\d{1,})(\\.\\d{1,})?‰|(千分之(([○零一二两三四五六七八九十廿卅百千]{1,})(点[○零一二两三四五六七八九十廿卅]{1,})?))|((\\d{1,})(\\.\\d{1,})?))";
        String regex_code3 = "((([○零一二两三四五六七八九十廿卅百千万亿]{1,})|(\\d{1,}))分之(([○零一二两三四五六七八九十廿卅百千万亿]{1,})|(\\d{1,})))";
        String regex_code = regex_code3 + "|" + regex_code1 + "|" + regex_code2;
        return getStr(regex_code, param);
    }

    public static Map<Integer, String> getFloatNum(String param) {
        String regex_code = "([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)";
        return getStr(regex_code, param);
    }
}
