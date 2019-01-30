package com.soybeany.bdlib.util.system;

import android.app.Activity;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * 系统设置工具类
 * <br>Created by Soybeany on 2017/3/13.
 */
public class SystemSettingUtils {

    private SystemSettingUtils() {

    }

    /**
     * 隐藏状态栏
     */
    public static void hideStatusBar(Activity activity) {
        //隐藏标题栏
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = activity.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }

    /**
     * 设置操作菜单悬浮
     */
    public static void setOptionMenuOverlay(AppCompatActivity activity) {
        activity.supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_MODE_OVERLAY);
    }
}
