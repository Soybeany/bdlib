package com.soybeany.bdlib.util.async.interfaces;

/**
 * 异步操作接口
 * <br>Created by Soybeany on 2017/1/18.
 */
public interface IAsyncOption<T> {

    /**
     * 开始
     */
    void onStart();

    /**
     * 需要异步执行的代码
     */
    T run();

    /**
     * 结束
     */
    void onFinish(boolean isCancel, T tag);

}
