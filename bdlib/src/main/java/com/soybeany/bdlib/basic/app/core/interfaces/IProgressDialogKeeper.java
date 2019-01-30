package com.soybeany.bdlib.basic.app.core.interfaces;

import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;

/**
 * 进度弹窗持有器
 * <br>Created by Soybeany on 2017/3/13.
 */
public interface IProgressDialogKeeper {

    /**
     * 获得进度弹窗
     */
    IProgressDialogHolder getProgressDialogHolder();

    /**
     * 显示进度弹窗
     */
    boolean showProgressDialog(String msg);

    /**
     * 隐藏进度弹窗
     */
    boolean hideProgressDialog();

    /**
     * 清除进度弹窗
     */
    void clearProgressDialog();

}
