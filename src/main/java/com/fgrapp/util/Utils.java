package com.fgrapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fgr
 * @date 2022-11-02 20:37
 **/
public class Utils {

    /**
     * 验证邮箱
     *
     * @param email email
     * @return flag
     */
    public static boolean checkEmail(String email) {
        boolean flag;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    /**
     * 验证手机号码
     *
     * @param mobileNumber mobileNumber
     * @return flag
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            String check = "^1[34578]\\d{9}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception ignored) {
        }
        return flag;
    }


    /**
     * 获取6位验证码
     * @return
     */
    public static String getCode(){
        return String.valueOf((int)((Math.random()*9+1)*100000));
    }
}
