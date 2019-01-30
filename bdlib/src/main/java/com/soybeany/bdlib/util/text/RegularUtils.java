package com.soybeany.bdlib.util.text;


import com.soybeany.bdlib.util.log.LogUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式公用类
 * Created by Mocona on 16/8/5.
 * Copyright (c) www.hemingsoft.com.cn.  All rights reserved.
 */
public class RegularUtils {

    /**
     * 验证邮箱的正则表达式
     */
    public static final String REGULAR_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 验证手机号码的正则表达式
     */
    public static final String REGULAR_TELEPHONE = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";

    /**
     * 验证数字的正则表达式
     */
    public static final String REGULAR_NUMERIC = "[0-9]*";

    /**
     * 检查字符串是否符合规范
     *
     * @param input 需要检查的字符串
     * @param regex 规则
     * @return true or false
     */
    public static boolean isMatchRegular(CharSequence input, String regex) {
        if (null == input || null == regex) {
            LogUtils.system("传入了null值, 验证失败");
            return false;
        }
        return find(input, regex).find();
    }

    /**
     * 查找范围
     */
    public static Matcher find(CharSequence input, String regex) {
        return Pattern.compile(regex).matcher(input);
    }

}
