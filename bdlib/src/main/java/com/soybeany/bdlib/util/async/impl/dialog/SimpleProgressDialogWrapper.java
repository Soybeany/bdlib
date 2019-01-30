package com.soybeany.bdlib.util.async.impl.dialog;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.util.async.interfaces.IDialogListener;
import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;

import java.util.HashSet;
import java.util.Set;

/**
 * 简易的进度弹窗包装器
 * <br>Created by Soybeany on 2017/8/29.
 */
public abstract class SimpleProgressDialogWrapper<Tag> implements IProgressDialogHolder, IDialogListener<Tag> {

    @Nullable
    private IProgressDialogHolder mHolder; // 被装饰的持有器
    private Set<Listener> mInnerListenerSet = new HashSet<>(); // 内部用的监听器，实则为自身

    private DialogListenerHolder mListenerHolder = new DialogListenerHolder(); // 监听者持有器

    public SimpleProgressDialogWrapper(@Nullable IProgressDialogHolder holder, Tag tag) {
        mHolder = holder;
        mInnerListenerSet.add(new Listener<>(tag, this));
    }

    @Override
    public SimpleProgressDialogWrapper cancelable(boolean flag) {
        if (null != mHolder) {
            mHolder.cancelable(flag);
        }
        return this;
    }

    @Override
    public SimpleProgressDialogWrapper hint(String hint) {
        if (null != mHolder) {
            mHolder.hint(hint);
        }
        return this;
    }

    @Override
    public boolean showDialog(String id, @Nullable Set<Listener> listeners) {
        return null != mHolder && mHolder.showDialog(id, listeners);
    }

    @Override
    public boolean showDialog() {
        return showDialog(mListenerHolder.keyPush(null), mInnerListenerSet);
    }

    @Override
    public boolean hideDialog(String id) {
        return null != mHolder && mHolder.hideDialog(id);
    }

    @Override
    public boolean hideDialog() {
        return hideDialog(mListenerHolder.keyPop(null));
    }

    @Override
    public void clearDialog() {
        mListenerHolder.clear();
        if (null != mHolder) {
            mHolder.clearDialog();
        }
    }

    @Override
    public void onShow(boolean isShowing, @Nullable String id, Tag tag) {
        // 留空
    }

    @Override
    public void onHide(boolean isHiding, @Nullable String id, Tag tag, boolean isCancel) {
        if (isCancel) {
            onCancel(tag);
        }
    }

    /**
     * 取消时的回调({@link #onHide(boolean, String, Object, boolean)}的延伸)
     */
    protected void onCancel(Tag tag) {
        // 留空
    }

}
