package com.soybeany.bdlib.util.message;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 信息中心，适用于应用的数据通讯
 * 支持在任意线程中提交信息，并在任意线程中响应
 * <br>Created by Soybeany on 2018/2/22.
 */
public class MessageCenter {

    private static Handler DISPATCH_HANDLER = HandlerHolder.getNew(null).handler; // 分发处理器
    private static Map<String, HandlerHolder> HANDLER_MAP = new HashMap<>(); // 处理器映射
    private static Map<String, Set<Callback>> LISTENER_MAP = new HashMap<>(); // 监听者映射

    /**
     * 注册
     */
    @SuppressWarnings("ConstantConditions")
    public static void register(@NonNull String key, @NonNull Callback callback) {
        Set<Callback> listeners = getCallbacks(key, true);
        listeners.add(callback);
    }

    /**
     * 注销(根据键)
     */
    public static void unregister(@NonNull String key) {
        LISTENER_MAP.remove(key);
    }

    /**
     * 注销(根据监听器)
     */
    public static void unregister(@NonNull Callback callback) {
        for (Set<Callback> set : LISTENER_MAP.values()) {
            if (set.remove(callback)) {
                return;
            }
        }
    }

    /**
     * 通知(指定回调)
     */
    public static <Data> void notify(final Data data, long delayMills, final Callback... callbacks) {
        DISPATCH_HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != callbacks) {
                    for (final Callback callback : callbacks) {
                        notifyCallback(data, callback);
                    }
                }
            }
        }, delayMills);
    }

    /**
     * 通知（指定键）
     */
    public static <Data> void notify(final String key, final Data data, long delayMills) {
        DISPATCH_HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                Set<Callback> callbacks = getCallbacks(key, false);
                if (null != callbacks) {
                    for (final Callback callback : callbacks) {
                        notifyCallback(data, callback);
                    }
                }
            }
        }, delayMills);
    }

    /**
     * 停止指定名称的线程
     */
    public static void stopThread(String name) {
        HandlerHolder holder = HANDLER_MAP.remove(name);
        if (null != holder && holder.thread instanceof HandlerThread) {
            ((HandlerThread) holder.thread).quitSafely();
        }
    }

    /**
     * 通知回调
     */
    private static <Data> void notifyCallback(final Data data, final Callback callback) {
        Handler handler = getHandler(callback);
        handler.post(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                callback.onHandle(data);
                if (callback.needDelete) {
                    stopThread(callback.threadName);
                }
            }
        });
    }

    /**
     * 获得监听者集合
     */
    @Nullable
    private static Set<Callback> getCallbacks(String key, boolean needCreate) {
        Set<Callback> set = LISTENER_MAP.get(key);
        if (null == set && needCreate) {
            LISTENER_MAP.put(key, set = new HashSet<>());
        }
        return set;
    }

    /**
     * 获得处理器
     */
    private static Handler getHandler(Callback callback) {
        if (!HANDLER_MAP.containsKey(callback.threadName)) {
            HANDLER_MAP.put(callback.threadName, HandlerHolder.getNew(callback.isMainThread ? Looper.getMainLooper() : null));
        }
        return HANDLER_MAP.get(callback.threadName).handler;
    }

    /**
     * 处理回调
     */
    public static abstract class Callback<Data> {

        String threadName; // 线程名
        boolean needDelete; // 是否需要删除线程
        boolean isMainThread; // 是否为主线程

        public Callback() {
            useWorkThread(); // 默认使用工作线程
        }

        public Callback<Data> useUIThread() {
            isMainThread = true;
            return useThread("thread_ui");
        }

        public Callback<Data> useWorkThread() {
            return useThread("thread_work");
        }

        public Callback<Data> useMsgThread() {
            return useThread("thread_msg");
        }

        public Callback<Data> useThread(String name) {
            return useThread(name, false);
        }

        public Callback<Data> useTmpThread(String name) {
            return useThread(name, true);
        }

        private Callback<Data> useThread(String name, boolean onlyOnce) {
            threadName = name;
            needDelete = onlyOnce;
            return this;
        }

        /**
         * 处理时的回调
         */
        protected abstract void onHandle(Data data);
    }

    /**
     * 处理器持有器
     */
    private static class HandlerHolder {

        private static int mThreadInitNumber; // 线程编号

        Thread thread; // 线程
        Handler handler; // 处理器

        private static synchronized int nextThreadNum() {
            return mThreadInitNumber++;
        }

        /**
         * 获得新处理器
         */
        static HandlerHolder getNew(Looper looper) {
            if (null == looper) {
                HandlerThread thread = new HandlerThread("HandlerThread-" + nextThreadNum());
                thread.start();
                return new HandlerHolder(thread, new Handler(thread.getLooper()));
            } else {
                return new HandlerHolder(looper.getThread(), new Handler(looper));
            }
        }

        private HandlerHolder(Thread thread, Handler handler) {
            this.thread = thread;
            this.handler = handler;
        }
    }
}
