package com.cuking.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cuking on 2017/4/18.
 */
public class PhoneNoUtils {
    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147
     * 15+任意数
     * 166
     * 17+除9的任意数
     * 18+任意数
     * 198,199
     */
    public static boolean isChinaPhoneLegal(String str) {
        String regExp = "^((13[0-9])|(145)|(147)|(15[0-9])|(166)|(17[0-8])|(18[0-9])|(19[8-9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 手机号加密
     *
     * @param phoneNo
     * @return
     */
    public static String formatPhone(String phoneNo) {
        return new StringBuilder(phoneNo.substring(0, 3)).append("****").append(phoneNo.substring(phoneNo.length() - 4, phoneNo.length())).toString();
    }


}
