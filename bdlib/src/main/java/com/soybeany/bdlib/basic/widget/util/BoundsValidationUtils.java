package com.soybeany.bdlib.basic.widget.util;

import java.util.List;

/**
 * 边界验证工具类，验证是否越界
 * <br>Created by Ben on 2016/5/25.
 */
public class BoundsValidationUtils {

    private BoundsValidationUtils() {

    }

    /**
     * 判断是否越界
     */
    public static <T> boolean isOut(int index, T[] arr) {
        return 0 > index || index >= arr.length;
    }

    /**
     * 判断是否越界
     */
    public static boolean isOut(int index, List list) {
        return isOut(index, list, 0);
    }

    /**
     * 判断是否越界
     *
     * @param delta 列表数目的修正量，增大(正数)或减少(负数)判断值
     */
    public static boolean isOut(int index, List list, int delta) {
        return 0 > index || index >= list.size() + delta;
    }
}
