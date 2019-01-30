package com.soybeany.bdlib.basic.app.core.interfaces;

import com.soybeany.bdlib.basic.app.annotation.BackType;

/**
 * 销毁监听者，监听页面销毁相关的信息
 * <br>Created by Soybeany on 2017/3/14.
 */
public interface IDestroyListener {

    /**
     * 销毁前的回调，true为销毁，false为不销毁
     */
    boolean shouldDestroy(@BackType int backType);

    /**
     * 判断当前页面是否已经被销毁
     */
    boolean isDestroyed();

}
