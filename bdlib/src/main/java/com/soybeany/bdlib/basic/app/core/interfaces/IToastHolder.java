package com.soybeany.bdlib.basic.app.core.interfaces;

/**
 * 吐司持有器
 * <br>Created by Soybeany on 2017/3/13.
 */
public interface IToastHolder {

    /**
     * 显示吐司
     */
    void showToast(String msg);

    /**
     * 取消吐司的显示
     */
    void cancelToast();

}
