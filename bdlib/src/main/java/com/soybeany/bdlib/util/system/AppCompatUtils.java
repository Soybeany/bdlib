package com.soybeany.bdlib.util.system;

import android.os.Build;
import android.widget.PopupWindow;

/**
 * 应用兼容工具类(兼容5.0以下系统)
 * <br>Created by Soybeany on 2017/12/8.
 */
public class AppCompatUtils {

    /**
     * 弹窗设置阴影
     */
    public static void setElevation(PopupWindow popupWindow) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(8f);
        }
    }

}
