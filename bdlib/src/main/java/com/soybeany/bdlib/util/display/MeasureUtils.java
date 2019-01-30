package com.soybeany.bdlib.util.display;

import android.view.View;

/**
 * 测量工具类
 * <br>Created by Ben on 2016/7/6.
 */
public class MeasureUtils {

    /**
     * 标准的测量空间
     */
    public static final int STD_MEASURE_SPEC = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

    /**
     * 测量视图
     */
    public static void measureSize(View view) {
        measureSize(view, STD_MEASURE_SPEC, STD_MEASURE_SPEC);
    }

    /**
     * 测量视图
     */
    public static void measureSize(View view, int widthMeasureSpec, int heightMeasureSpec) {
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

}
