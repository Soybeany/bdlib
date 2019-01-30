package com.soybeany.bdlib.util.async;

import android.os.AsyncTask;

import com.soybeany.bdlib.util.async.impl.dialog.SimpleProgressDialogWrapper;
import com.soybeany.bdlib.util.async.impl.option.DialogAsyncOption;
import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;

/**
 * 异步操作工具类
 * <br>Created by Soybeany on 2017/1/17.
 */
public class AsyncUtils {

    private AsyncUtils() {

    }

    /**
     * 休眠指定长度
     */
    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自动决定是否使用异步处理操作
     */
    public static <T> void auto(DialogAsyncOption<T> option) {
        timer(0, null == option.getDialogHolder(), option);
    }

    /**
     * 使用异步处理操作
     */
    public static <T> void run(DialogAsyncOption<T> option) {
        timer(0, false, option);
    }

    /**
     * 定时器
     *
     * @param delay          延时（单位：毫秒）
     * @param needMainThread 操作是否需要在主线程中进行
     * @param option         需要进行的操作
     */
    public static <T> TimerTask timer(long delay, boolean needMainThread, DialogAsyncOption<T> option) {
        return timer(delay, 0, 1, needMainThread, option);
    }

    /**
     * 重复定时器
     *
     * @param delay          延时
     * @param period         周期（单位：毫秒）
     * @param times          循环次数，小于0则表示不限次数
     * @param needMainThread 操作是否需要在主线程中进行
     * @param option         需要进行的操作
     */
    public static <T> TimerTask timer(long delay, long period, Integer times, boolean needMainThread, DialogAsyncOption<T> option) {
        return new TimerTask<>(delay, period, times, needMainThread, option);
    }

    /**
     * 延时任务
     */
    public static class TimerTask<T> extends AsyncTask<Object, Object, Object> {
        private static final int TYPE_RUN = 1; // 运行类型
        private static final int TYPE_END = 2; // 结束类型

        private long mDelay; // 延时
        private long mPeriod; // 周期
        private int mTimes; // 还需要循环的次数(小于0则不限次数)
        private boolean mNeedMainThread; // 是否需要在主线程中执行

        private DialogAsyncOption<T> mOption; // 操作(需释放)
        private TimerDialogWrapper mDialogHolder; // 弹窗(需释放)

        private boolean mIsCancel; // 是否已取消
        private boolean mHasFinish; // 标识是否已结束
        private T mResult; // 结果

        TimerTask(Long delay, long period, int times, boolean needMainThread, DialogAsyncOption<T> option) {
            mDelay = delay;
            mPeriod = period;
            mTimes = times;
            mNeedMainThread = needMainThread;
            mOption = option;
            mDialogHolder = new TimerDialogWrapper(mOption.getDialogHolder(), this);

            // 开始行程
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        @Override
        protected void onPreExecute() {
            mOption.beforeShowDialog();
            mDialogHolder.showDialog();
            mOption.onStart();
        }

        @Override
        protected Object doInBackground(Object... params) {
            int firstTime = mTimes - 1; // 首次对应的次数，因为会自减所以需减1
            while (!mIsCancel && 0 != mTimes--) {
                // 休眠适当的时间
                sleep(firstTime == mTimes ? mDelay : mPeriod);
                // 在合适线程中进行回调
                if (mNeedMainThread) {
                    publishProgress(TYPE_RUN); // 主线程
                } else {
                    onRun(); // 此子线程
                }
            }
            // 主线程中进行收尾回调
            publishProgress(TYPE_END);
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            if (mHasFinish) {
                return;
            }
            // 按类型调用
            int type = (int) values[0];
            if (TYPE_RUN == type) {
                onRun();
            } else if (TYPE_END == type) {
                onFinish();
                mHasFinish = true;
            }
        }

        /**
         * 标识任务已被取消
         */
        public void cancel() {
            mIsCancel = true;
            publishProgress(TYPE_END);
        }

        /**
         * 调用运行回调
         */
        private void onRun() {
            if (!mIsCancel) {
                mResult = mOption.run();
            }
        }

        /**
         * 调用结束回调
         */
        private void onFinish() {
            // 执行回调
            T result = !mIsCancel ? mResult : null;
            mOption.onFinish(mIsCancel, result);
            mDialogHolder.hideDialog();
            mOption.afterHideDialog(mIsCancel, result);
            // 释放引用
            mOption = null;
            mDialogHolder = null;
        }
    }

    /**
     * 弹窗包装器
     */
    private static class TimerDialogWrapper extends SimpleProgressDialogWrapper<TimerTask> {

        TimerDialogWrapper(IProgressDialogHolder holder, TimerTask task) {
            super(holder, task);
        }

        @Override
        protected void onCancel(TimerTask task) {
            super.onCancel(task);
            task.cancel();
        }
    }
}
