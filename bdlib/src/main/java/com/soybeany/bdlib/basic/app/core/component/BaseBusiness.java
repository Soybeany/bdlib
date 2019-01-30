package com.soybeany.bdlib.basic.app.core.component;

import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;

/**
 * 业务逻辑，MVP模式中的M层
 * <br>内置对象：1.mDH
 * <br>Created by Soybeany on 2017/12/7.
 */
public class BaseBusiness {

    protected IProgressDialogHolder mDH;
    private boolean mIsBinding; // 是否绑定中

    /**
     * 绑定P层(框架内调用)
     */
    public void bind(IProgressDialogHolder dh) {
        if (!mIsBinding) {
            mDH = dh;
            onBind();
            mIsBinding = true;
        }
    }

    /**
     * 解绑P层(框架内调用)
     */
    public void unbind() {
        if (mIsBinding) {
            onUnbind();
            mDH = null;
            mIsBinding = false;
        }
    }

    /**
     * 绑定时的回调
     */
    protected void onBind() {
        // 留空，子类实现
    }

    /**
     * 解绑时的回调
     */
    protected void onUnbind() {
        // 留空，子类实现
    }

}
