package com.soybeany.bdlib.util.async;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 任务计时器，在计时结束时可选择在主线程或后台进行任务回调(仅供参考，不作调用)
 * <br>Created by Ben on 2016/5/18.
 */
class ScheduleTimer {

    /**
     * 不需要周期回调
     */
    public static final long PERIOD_NONE = -1;

    private Handler handler; // 句柄处理器
    private Scheduler mScheduler; // 计划执行者
    private long mDelay; // 延时
    private long mPeriod; // 周期
    private Timer mTimer; // 计时器

    public ScheduleTimer(Scheduler scheduler, long delay) {
        this(scheduler, delay, PERIOD_NONE);
    }

    public ScheduleTimer(Scheduler scheduler, long delay, long period) {
        handler = new TimerHandler(this);
        mScheduler = scheduler;
        mDelay = delay;
        mPeriod = period;
    }

    /**
     * 开始计时
     */
    public void start() {
        mTimer = new Timer();
        TimerTask task = new InnerTimerTask();
        if (mPeriod > 0) {
            mTimer.schedule(task, mDelay, mPeriod);
        } else {
            mTimer.schedule(task, mDelay);
        }
    }

    /**
     * 取消计时
     */
    public void cancel() {
        if (null != mTimer) {
            mTimer.cancel();
        }
        mScheduler.onCancel();
    }

    /**
     * 布置的计划
     */
    public static abstract class Scheduler {

        /**
         * 指定特定行程是否需要主线程
         */
        protected boolean needMainThread(long times) {
            return true;
        }

        /**
         * 取消时的回调
         */
        protected void onCancel() {
            // 留空
        }

        /**
         * 到达时间后的计划
         */
        protected abstract void onSchedule(ScheduleTimer timer);
    }

    /**
     * 计时器句柄
     */
    private static class TimerHandler extends Handler {

        private ScheduleTimer mScheduleTimer;

        TimerHandler(ScheduleTimer scheduleTimer) {
            mScheduleTimer = scheduleTimer;
        }

        @Override
        public void handleMessage(Message msg) {
            mScheduleTimer.mScheduler.onSchedule(mScheduleTimer);
        }
    }

    /**
     * 计时器任务
     */
    private class InnerTimerTask extends TimerTask {

        private long mTimes; // 运行的次数

        @Override
        public void run() {
            if (mScheduler.needMainThread(mTimes++)) {
                handler.sendMessage(new Message());
            } else {
                mScheduler.onSchedule(ScheduleTimer.this);
            }
        }
    }
}
