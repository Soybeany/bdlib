package com.soybeany.bdlib.basic.widget.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.util.context.BDContext;

/**
 * 标记工具类
 * <br>Created by Ben on 2016/5/20.
 */
public class BadgeUtils {

    /**
     * 标准的最小值
     */
    public static final int NORM_MIN = 1;

    /**
     * 标准的最大值
     */
    public static final int NORM_MAX = 99;

    /**
     * 标准的宽度，搭配R.dimen.text_detail使用
     */
    public static final int NORM_WIDTH = R.dimen.size_badge;

    /**
     * 小尺寸的宽度，搭配R.dimen.text_micro使用
     */
    public static final int SMALL_WIDTH = R.dimen.dimen_huge;

    /**
     * 标准的填充，搭配{@link #NORM_WIDTH}使用
     */
    public static final int NORM_PADDING = R.dimen.dimen_micro;

    /**
     * 小尺寸的填充，搭配{@link #SMALL_WIDTH}使用
     */
    public static final int SMALL_PADDING = R.dimen.dimen_mini;

    private BadgeUtils() {

    }

    /**
     * 设置标记的数目，可等于最小最大值
     *
     * @param useGone true为使用gone，false为使用invisible
     * @return true表示设置的值大于最大值
     */
    public static boolean setBadgeNum(TextView badge, int num, int min, int max, boolean useGone) {
        boolean isOut = false;
        if (max < num) {
            badge.setText(max + "+");
            isOut = true;
        } else if (min <= num) {
            badge.setText(num + "");
        } else {
            badge.setVisibility(useGone ? View.GONE : View.INVISIBLE);
            return false;
        }
        badge.setVisibility(View.VISIBLE);
        return isOut;
    }

    /**
     * 设置标记的数目，并自动调整文本视图的宽度
     *
     * @param widthResId   正常状态下的宽度，使用资源id
     * @param paddingResId 自动调整时的填充，使用资源id
     */
    public static void setBadgeNum(TextView badge, int num, int min, int max, boolean useGone, int widthResId, int paddingResId) {
        boolean isOut = setBadgeNum(badge, num, min, max, useGone);
        int width;
        int padding;
        if (isOut) {
            width = ViewGroup.LayoutParams.WRAP_CONTENT;
            padding = BDContext.getPixel(paddingResId);
        } else {
            width = BDContext.getPixel(widthResId);
            padding = 0;
        }
        badge.getLayoutParams().width = width;
        badge.setPadding(padding, 0, padding, 0);
    }

}
