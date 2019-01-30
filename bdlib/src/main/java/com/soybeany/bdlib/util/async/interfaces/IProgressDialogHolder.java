package com.soybeany.bdlib.util.async.interfaces;

import android.support.annotation.Nullable;

import java.util.Set;

/**
 * 进度弹窗持有者
 * <br>Created by Soybeany on 2017/8/29.
 */
public interface IProgressDialogHolder {

    /**
     * 设置是否可取消
     */
    IProgressDialogHolder cancelable(boolean flag);

    /**
     * 设置提示语
     */
    IProgressDialogHolder hint(String hint);

    /**
     * 显示进度弹窗(带监听)
     *
     * @param id 对应显示时使用的id
     * @return 是否需要显示
     */
    boolean showDialog(String id, @Nullable Set<Listener> listeners);

    /**
     * 显示进度弹窗(使用默认id)
     *
     * @return 是否需要显示
     */
    boolean showDialog();

    /**
     * 隐藏进度弹窗
     *
     * @param id 对应显示时使用的id
     * @return 是否需要隐藏
     */
    boolean hideDialog(String id);

    /**
     * 隐藏进度弹窗(使用默认id)
     *
     * @return 是否需要隐藏
     */
    boolean hideDialog();

    /**
     * 清除进度弹窗(全部id)
     */
    void clearDialog();

    /**
     * 带标识的监听器
     */
    class Listener<T> {
        public T tag; // 标识
        public IDialogListener<T> listener; // 监听器

        public Listener(T tag, IDialogListener<T> listener) {
            this.tag = tag;
            this.listener = listener;
        }

        @Override
        public int hashCode() {
            return listener.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Listener) {
                return ((Listener) obj).listener.equals(listener);
            }
            return super.equals(obj);
        }
    }

}
