package com.soybeany.bdlib.basic.database.util;


import com.soybeany.bdlib.util.log.LogUtils;

/**
 * <br>Created by Soybeany on 2017/1/18.
 */
public class DBLogUtils {

    public static boolean NEED_LGO = false; // 是否需要日志
    private static final String TAG = "BDDatabase"; // 日志标识
    private static Class CLAZZ = DBLogUtils.class;

    public static void i(String msg) {
        if (NEED_LGO) {
            LogUtils.i(CLAZZ, TAG, msg);
        }
    }

    public static void e(String msg) {
        if (NEED_LGO) {
            LogUtils.e(CLAZZ, TAG, msg);
        }
    }

}
