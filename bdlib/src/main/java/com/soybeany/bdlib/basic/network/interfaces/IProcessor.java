package com.soybeany.bdlib.basic.network.interfaces;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

/**
 * 处理器，用于异步处理数据
 * <br>Created by Soybeany on 2017/9/5.
 */
interface IProcessor<T> {

    /**
     * 开始前的准备(异步调用)
     */
    @WorkerThread
    void onPreStart(Object tag);

    /**
     * 预处理的回调(异步调用)
     *
     * @return 作为后续回调的响应
     */
    @WorkerThread
    T onPreProcess(boolean hasResponse, @Nullable T response, Object tag);

    /**
     * 完成处理的回调(异步调用)
     */
    @WorkerThread
    void onPostProcess(boolean isSmooth, T response, Object tag);

    /**
     * 标识此处理是否顺利，影响{@link #onPostProcess(boolean, Object, Object)}中相应参数的值
     */
    boolean isSmooth();

}
