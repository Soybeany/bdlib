package com.soybeany.bdlib.basic.network.interfaces;

import android.support.annotation.UiThread;

/**
 * 网络请求的回调（若修改此类的泛型顺序，需保证response类型的泛型位于首位，除非修改BDRequest中的相关反射;
 * 目前实现只允许泛型在直接子类中指定）
 * <br>Created by Soybeany on 2017/1/24.
 */
public interface IRequestCallback<T> extends IProcessor<T> {

    /**
     * 获得目标文件夹
     */
    String getDestFileDir();

    /**
     * 获得目标文件名
     */
    String getDestFileName();

    /**
     * 网络请求开始前的回调
     */
    @UiThread
    void onStart(IRequest request, Object tag);

    /**
     * 收到服务器响应时的回调，用于处理原始的响应
     */
    @UiThread
    void onReceive(IResponse response, Object tag);

    /**
     * 进度回调(该方法在使用部分Request时才会回调，如okHttp)
     */
    @UiThread
    void inProgress(float progress, long total, Object tag);

    /**
     * 收到服务器响应时的回调，可以用来集中处理某些事务，例如强转tag类型
     */
    @UiThread
    void onResponse(T response, Object tag);

    /**
     * 错误后的回调(服务器没有响应)
     */
    @UiThread
    void onErrorResponse(boolean isCancel, String errMsg, Object tag);

    /**
     * 网络请求完成后(弹窗消失前)的回调(一定会调用)
     */
    @UiThread
    void onFinish(Object tag);

    /**
     * 网络请求结束后(弹窗消失后)的回调(一定会调用)
     */
    @UiThread
    void onFinal(Object tag);

}
