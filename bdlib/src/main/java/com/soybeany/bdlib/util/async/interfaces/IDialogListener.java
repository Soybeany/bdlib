package com.soybeany.bdlib.util.async.interfaces;

import android.support.annotation.Nullable;

/**
 * 弹窗监听器
 * <br>Created by Soybeany on 2017/8/29.
 */
public interface IDialogListener<T> {

    /**
     * 显示时的回调
     *
     * @param id 调用者id
     */
    void onShow(boolean isShowing, @Nullable String id, T tag);

    /**
     * 隐藏时的回调
     *
     * @param id 调用者id，cancel时为null
     */
    void onHide(boolean isHiding, @Nullable String id, T tag, boolean isCancel);

}
