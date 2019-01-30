package com.soybeany.bdlib.util.context;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.TypedValue;

import com.soybeany.bdlib.R;

import java.util.UUID;

/**
 * 应用上下文，供没有界面但需要上下文的类使用（一般为类库内部使用）
 * <br>Created by Soybeany on 2017/1/20.
 */
public class BDContext {

    private static Context mContext; // 应用的上下文

    private BDContext() {

    }

    /**
     * 设置应用上下文
     */
    public static void setContext(Context context) {
        mContext = context;
    }

    /**
     * 获得应用上下文
     */
    public static Context getContext() {
        if (null == mContext) {
            throw new RuntimeException("需要先设置Context");
        }
        return mContext;
    }

    /**
     * 获得唯一码
     */
    public static String getUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获得应用的资源
     */
    public static Resources getResources() {
        return mContext.getResources();
    }

    /**
     * 获得资源管理器
     */
    public static AssetManager getAssets() {
        return mContext.getAssets();
    }

    /**
     * 获得资源的像素值
     */
    public static int getPixel(int resId) {
        return mContext.getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获得应用主色调
     *
     * @return 颜色值
     */
    public static int getColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

}
