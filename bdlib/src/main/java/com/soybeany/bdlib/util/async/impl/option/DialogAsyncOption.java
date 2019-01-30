package com.soybeany.bdlib.util.async.impl.option;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;

/**
 * 带弹窗的异步操作实现
 * <br>Created by Soybeany on 2017/11/23.
 */
public abstract class DialogAsyncOption<T> extends SimpleAsyncOption<T> {

    private IProgressDialogHolder mDialogHolder;

    protected DialogAsyncOption() {
        this(null);
    }

    protected DialogAsyncOption(@Nullable IProgressDialogHolder dialogHolder) {
        mDialogHolder = dialogHolder;
    }

    /**
     * 获得对应的弹窗
     */
    @Nullable
    public IProgressDialogHolder getDialogHolder() {
        return mDialogHolder;
    }

    /**
     * 弹窗显示前的回调
     */
    public void beforeShowDialog() {
        // 留空，子类实现
    }

    /**
     * 弹窗消失后的回调
     */
    public void afterHideDialog(boolean isCanceled, T tag) {
        // 留空，子类实现
    }

}
