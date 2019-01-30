package com.soybeany.bdlib.util.async.impl.dialog;

import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder.Listener;

import java.util.Map;
import java.util.Set;

/**
 * 弹窗监听管理器
 * <br>Created by Soybeany on 2017/11/27.
 */
class DialogListenerManager {

    private DialogListenerHolder mHolder = new DialogListenerHolder(); // 弹窗监听器持有器

    /**
     * 判断此id是否有效
     */
    boolean isValid(String id) {
        return mHolder.isValid(id);
    }

    /**
     * 缓存大小
     */
    int size() {
        return mHolder.size();
    }

    /**
     * 添加监听器
     */
    @SuppressWarnings("ConstantConditions")
    void addListeners(String id, Set<Listener> listeners) {
        mHolder.push(id, listeners);
    }

    /**
     * 移除监听器
     */
    void removeListeners(String id) {
        mHolder.pop(id);
    }

    /**
     * 通知监听器显示
     */
    @SuppressWarnings("unchecked")
    void notifyOnShow(String id, boolean isShowing) {
        Set<Listener> set = mHolder.peek(id);
        if (null != set) {
            for (Listener listener : set) {
                listener.listener.onShow(isShowing, mHolder.keyPeek(id), listener.tag);
            }
        }
    }

    /**
     * 通知监听器隐藏
     */
    void notifyOnHide(String id, boolean isHiding, boolean isCancel) {
        notifyOnHide(mHolder.keyPeek(id), mHolder.peek(id), isHiding, isCancel);
    }

    /**
     * 通知全部监听器已取消
     */
    void notifyAllOnCancel() {
        for (Map.Entry<String, Set<Listener>> entry : mHolder.clear().entrySet()) {
            notifyOnHide(entry.getKey(), entry.getValue(), true, true);
        }
    }

    /**
     * 通知监听器隐藏(内部)
     */
    @SuppressWarnings("unchecked")
    private void notifyOnHide(String id, Set<Listener> listeners, boolean isHiding, boolean isCancel) {
        if (null != listeners) {
            for (Listener listener : listeners) {
                listener.listener.onHide(isHiding, id, listener.tag, isCancel);
            }
        }
    }

}
