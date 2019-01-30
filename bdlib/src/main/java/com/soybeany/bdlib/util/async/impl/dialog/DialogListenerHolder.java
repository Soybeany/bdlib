package com.soybeany.bdlib.util.async.impl.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder.Listener;
import com.soybeany.bdlib.util.context.BDContext;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * 弹窗id持有器
 * <br>Created by Soybeany on 2017/11/27.
 */
class DialogListenerHolder {

    private final Map<String, Set<Listener>> mCacheMap = new HashMap<>(); // 缓存映射
    private final LinkedList<String> mOrderList = new LinkedList<>(); // 记录顺序以便出栈


    // //////////////////////////////////查阅区//////////////////////////////////

    /**
     * 指定id是否有效
     */
    boolean isValid(String id) {
        return mOrderList.contains(keyPeek(id));
    }

    /**
     * 缓存大小
     */
    int size() {
        return mOrderList.size();
    }


    // //////////////////////////////////键操作区//////////////////////////////////

    /**
     * 推入键
     */
    String keyPush(@Nullable String id) {
        id = (null != id ? id : BDContext.getUID());
        mOrderList.addFirst(id);
        return id;
    }

    /**
     * 获得键
     */
    String keyPeek(@Nullable String id) {
        return null != id ? id : mOrderList.peekFirst();
    }

    /**
     * 弹出键
     */
    String keyPop(@Nullable String id) {
        if (null != id) {
            mOrderList.remove(id);
            return id;
        }
        return mOrderList.pollFirst();
    }


    // //////////////////////////////////监听器操作区//////////////////////////////////

    /**
     * 推入一组监听器
     */
    void push(@Nullable String id, Set<Listener> listeners) {
        mCacheMap.put(keyPush(id), listeners);
    }

    /**
     * 获得一组监听器
     */
    @Nullable
    Set<Listener> peek(@Nullable String id) {
        return mCacheMap.get(keyPeek(id));
    }

    /**
     * 弹出一组监听器
     */
    @Nullable
    Set<Listener> pop(@Nullable String id) {
        return mCacheMap.remove(keyPop(id));
    }

    /**
     * 清空缓存
     *
     * @return 被清除的监听器映射
     */
    @NonNull
    Map<String, Set<Listener>> clear() {
        Map<String, Set<Listener>> result = new HashMap<>();
        for (Map.Entry<String, Set<Listener>> entry : mCacheMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        mCacheMap.clear();
        mOrderList.clear();
        return result;
    }

}
