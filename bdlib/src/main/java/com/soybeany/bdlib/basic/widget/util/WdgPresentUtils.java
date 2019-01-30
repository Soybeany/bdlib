package com.soybeany.bdlib.basic.widget.util;

import android.view.View;
import android.widget.TextView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 视图展示工具类
 * <br>Created by Soybeany on 2017/3/16.
 */
public class WdgPresentUtils {

    private WdgPresentUtils() {

    }

    /**
     * 自动显示文本视图
     */
    public static void autoShowTextView(TextView textView, String text) {
        boolean hasText = (null != text);
        if (hasText) {
            textView.setText(text);
        }
        setViewVisible(textView, hasText);
    }

    /**
     * 设置视图可见性
     */
    public static void setViewVisible(View view, boolean flag) {
        view.setVisibility(flag ? VISIBLE : GONE);
    }

}
