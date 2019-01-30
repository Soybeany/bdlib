package com.soybeany.bdlib.util.async.impl.option;

import com.soybeany.bdlib.util.async.interfaces.IAsyncOption;

/**
 * 简单的异步操作实现
 * <br>Created by Soybeany on 2017/1/26.
 */
public abstract class SimpleAsyncOption<T> implements IAsyncOption<T> {

    @Override
    public void onStart() {

    }

    @Override
    public void onFinish(boolean isCanceled, T tag) {

    }
}
