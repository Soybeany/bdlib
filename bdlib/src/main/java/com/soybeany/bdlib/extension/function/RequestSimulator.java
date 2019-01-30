package com.soybeany.bdlib.extension.function;

import com.soybeany.bdlib.basic.network.interfaces.IRequest;
import com.soybeany.bdlib.basic.network.interfaces.IRequestCallback;
import com.soybeany.bdlib.util.async.AsyncUtils;
import com.soybeany.bdlib.util.async.impl.option.TimerAsyncOption;

/**
 * 请求生成模拟器
 * <br>Created by Soybeany on 2017/4/22.
 */
public class RequestSimulator {

    /**
     * 使用get方式请求
     */
    public static <T> SimulatedRequest get(T response, IRequestCallback<T> callback) {
        return new SimulatedRequest<>(response, callback);
    }

    /**
     * 模拟的请求
     */
    public static class SimulatedRequest<T> implements IRequest {

        private T mResponse; // 响应语
        private IRequestCallback<T> mCallback; // 回调

        private String mHint; // 提示语
        private Object mTag; // 自定义标识

        private long mDelay; // 延时
        private long mPeriod; // 周期
        private int mTimes; // 循环次数
        private AsyncUtils.TimerTask mTask; // 定时任务

        SimulatedRequest(T response, IRequestCallback<T> callback) {
            mResponse = response;
            mCallback = callback;
        }

        @Override
        public SimulatedRequest stdHint(String stdHint) {
            mHint = stdHint;
            return this;
        }

        @Override
        public String getStdHint() {
            return mHint;
        }

        @Override
        public SimulatedRequest timeout(long timeout) {
            return this;
        }

        @Override
        public SimulatedRequest requestTag(Object requestTag) {
            return this;
        }

        @Override
        public SimulatedRequest tag(Object tag) {
            mTag = tag;
            return this;
        }

        @Override
        public void startRequest() {
            mTask = AsyncUtils.timer(mDelay, mPeriod, mTimes, true, new Option());
        }

        @Override
        public boolean cancelRequest() {
            mTask.cancel();
            return true;
        }

        /**
         * 设置计划
         */
        public SimulatedRequest schedule(long delay, long period, int times) {
            mDelay = delay;
            mPeriod = period;
            mTimes = times;
            return this;
        }

        /**
         * 底层的异步操作
         */
        private class Option extends TimerAsyncOption {

            private float progress;

            @Override
            public void onStart() {
                super.onStart();
                mCallback.onStart(SimulatedRequest.this, mTag);
            }

            @Override
            protected void onTimeUp() {
                mCallback.inProgress(progress += 1.0f / mTimes, 100, mTag);
            }

            @Override
            public void onFinish(boolean isCancel, Object tag) {
                super.onFinish(isCancel, tag);
                mCallback.onResponse(mResponse, mTag);
                mCallback.onFinish(mTag);
                mCallback.onFinal(mTag);
            }
        }
    }

}
