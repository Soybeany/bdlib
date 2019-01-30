package com.soybeany.bdlib.util.async.impl.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;
import com.soybeany.bdlib.util.text.StdHintUtils;

import java.util.Set;

/**
 * 简易的进度弹窗持有器(带计数功能)
 * <br>Created by Soybeany on 2017/8/29.
 */
public class SimpleProgressDialogHolder implements IProgressDialogHolder {

    protected DialogListenerManager mManager = new DialogListenerManager(); // 弹窗监听管理器
    protected ProgressDialog mDialog; // 实际的进度弹窗

    public SimpleProgressDialogHolder(Context context) {
        initDialog(context);
    }

    @Override
    public SimpleProgressDialogHolder cancelable(boolean flag) {
        mDialog.setCancelable(flag);
        return this;
    }

    @Override
    public SimpleProgressDialogHolder hint(String hint) {
        mDialog.setMessage(StdHintUtils.STD_LOADING_PREFIX + hint + StdHintUtils.STD_LOADING_SUFFIX);
        return this;
    }

    @Override
    public boolean showDialog() {
        return showDialog(null, null);
    }

    @Override
    public boolean hideDialog() {
        return hideDialog(null);
    }

    @Override
    public boolean showDialog(String id, @Nullable Set<Listener> listeners) {
        boolean needToShow = !mDialog.isShowing();
        // 按需显示弹窗
        if (needToShow) {
            mDialog.show();
        }
        // 添加监听器
        mManager.addListeners(id, listeners);
        // 执行监听器的回调
        mManager.notifyOnShow(id, needToShow);
        return needToShow;
    }

    @Override
    public boolean hideDialog(String id) {
        if (!mManager.isValid(id)) {
            return false;
        }
        // id有效才继续执行
        boolean needToHide = (mManager.size() == 1);
        // 执行监听器的回调
        mManager.notifyOnHide(id, needToHide, false);
        // 移除监听器
        mManager.removeListeners(id);
        // 按需隐藏弹窗
        if (needToHide) {
            clearDialog();
        }
        return needToHide;
    }

    @Override
    public void clearDialog() {
        mDialog.dismiss();
    }

    /**
     * 初始化内部持有的弹窗
     */
    protected void initDialog(Context context) {
        mDialog = new ProgressDialog(context);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                clearDialog();
            }
        });
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mManager.notifyAllOnCancel();
                cancelable(true);
            }
        });
    }
}
