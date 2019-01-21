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
            System.out.println("未指定提取规则！");
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

    public static void main(String[] args) {
        String s = "电话及传真：0531—66601130王AB1234电话号码8615953128866身份证号430528197810295353电子邮件地址jhwang@inspur.com座机0531-85105818银行卡4270300016729930，周小飞于2012-10-09购入一车，车牌号码鲁A5768N";
        System.out.println("mobileStr===" + getTelNumber(s));
        String t = "我要评论 2016年 05月 04日 04:36:16 来源。历下区派出所在2014年1月11日凌晨1时27分接到报警电话。2009-02-06武汉2月5日电。2013年12月29日早上8点41分。2013-12-29 14:08。12日14时28分。";
        String cnt = "公元四百八十二年是哪一个朝代？文化大革命从一九六六年开始到一九七六年结束。一九九七年十二月十五日。生于一九四○年十一月二十三日五点零三分三十三秒。九二年那个春天。北京时间6日下午四点五十四分。13胜6负。";
        String cnNum = "第二百三十一条：昨天工地要了一千五百吨水泥、一千吨沙子、一百零三吨石子。总共花销肆万零贰拾圆整。25日上午10点18分，成百上千只麻雀和数万只蜻蜓在麦田上空盘旋。工程款总额为五亿四千万。本届运动会共有有两百二十个人报名参加。第三两个人出行需并排。)二十九和五亿)。(1)二十九个人需申请。公园一九○八年。";
        String floatNum = "圆周率是3.14。(2013.06-)。O.J.-。";
        String perNum = "实验中学今年高考的升学率是百分之八十一点一九，即81.19%。山师附中今年高考的升学率是百分之七十点六，即70.6%。历城二中今年高考的升学率是百分之七十，即70%。本月销售额同比增长百分之两百零五，百分之235,235.108%。爸爸吃了二分之一，妈妈吃了八分之三，给波妞留了8分之1。这种食品中中的锌含量高达千分之30。13胜6负。4和6。001与003。1、23个人。在1989.01-1991.09间。参考文献：【2006〕44p。大约3%5%之间。范围在-0.6-1.60.0。在(3)二十九章内容。1948年8月。515元5毛。(1)二十九个人需申请。";
        String idNum = "登记身份证号如下：3701021982xxxx0591、37010219******059X、37010281****059X。13胜6负。";
        String carNum = "新J22088,的SUMMLY。13胜6负。";
        String emailStr = "邮箱是zhangxian@inspur.com。OJ的邮箱是：O.J-J";
        String urlStr = "O.J.-。ARISTOTLE384-322B.C。2013.06-1。在1977.01--1980.09期间。位于2.3-2.9之间。占比大约在80―90%。";
        String mobileStr = "(1)二十九个人需申请。五亿)。二十四)。";
        System.out.println("ID===" + getIDCard(idNum));
        System.out.println("Time===" + getTime(t));
        System.out.println("CNTime===" + getCNTime(cnt));
        System.out.println("cnNum===" + getCNNum(cnNum));
        System.out.println("folatNum===" + getFloatNum(floatNum));
        System.out.println("perNum===" + getPerNum(perNum));
        System.out.println("numbers===" + getNumbers(floatNum));
        System.out.println("carNum===" + getCarNum(carNum));
        System.out.println("emailStr===" + getEmail(emailStr));
        System.out.println("urlStr===" + getURL(urlStr));
        System.out.println("mobileStr===" + getMobile(s));
    }
}
