package com.soybeany.bdlib.extension.module.common;

import android.app.Activity;

import com.soybeany.bdlib.basic.app.core.interfaces.IDestroyListener;
import com.soybeany.bdlib.util.display.ToastUtils;
import com.soybeany.bdlib.util.system.TimeUtils;

/**
 * 双击退出模块
 * <br>用法：
 * <br>1.(推荐)放在{@link IDestroyListener#shouldDestroy(int)}中，调用{@link #simpleUse(Activity)}，并返回false
 * <br>2.创建实例，调用{@link #autoCheck(Activity)}
 * <br>3.(不推荐)创建实例，调用{@link #hasDoubleClicked()}判断，再调用{@link #exit(Activity)}结束应用
 * <br>Created by Soybeany on 2017/3/15.
 */
public class DoubleExitModule {

    private static DoubleExitModule mInstance = new DoubleExitModule(); // 单例

    private long mLastClickTime = 0; // 上一次点击返回的时间
    private long mInterval = 2 * TimeUtils.SECOND; // 检测间隔，默认2秒
    private String mHint = "再按一次退出"; // 提示

    /**
     * 最简单地调用（使用内部单例）
     */
    public static void simpleUse(Activity activity) {
        mInstance.autoCheck(activity);
    }

    /**
     * 获得单例
     */
    public static DoubleExitModule getInstance() {
        return mInstance;
    }

    /**
     * 退出应用
     */
    public static void exit(Activity activity) {
        activity.finish();
    }

    /**
     * 设置检测间隔
     *
     * @param interval 间隔，单位：毫秒（1秒->1000）
     */
    public DoubleExitModule interval(long interval) {
        mInterval = interval;
        return this;
    }

    /**
     * 设置未满足双击时的提示语
     */
    public DoubleExitModule hint(String hint) {
        mHint = hint;
        return this;
    }

    /**
     * 检测是否双击了
     */
    public boolean hasDoubleClicked() {
        boolean hasDoubleClicked = false;
        long currentTime = TimeUtils.getCurrentTimeStamp();
        if (currentTime - mLastClickTime < mInterval) {
            hasDoubleClicked = true;
            ToastUtils.cancel();
        } else {
            mLastClickTime = currentTime;
            ToastUtils.show(mHint);
        }
        return hasDoubleClicked;
    }

    /**
     * 自动检测
     */
    public void autoCheck(Activity activity) {
        if (hasDoubleClicked()) {
            exit(activity);
        }
    }

}
