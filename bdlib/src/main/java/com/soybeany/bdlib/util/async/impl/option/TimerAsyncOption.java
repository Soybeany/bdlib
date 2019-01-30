package com.soybeany.bdlib.util.async.impl.option;

/**
 * 定时器的异步操作实现
 * <br>Created by Soybeany on 2017/3/21.
 */
public abstract class TimerAsyncOption extends DialogAsyncOption<Object> {

    @Override
    public Object run() {
        onTimeUp();
        return null;
    }

    /**
     * 时间到时的操作
     */
    protected abstract void onTimeUp();

}
