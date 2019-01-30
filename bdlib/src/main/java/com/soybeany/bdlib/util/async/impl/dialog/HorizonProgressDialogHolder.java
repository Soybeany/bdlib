package com.soybeany.bdlib.util.async.impl.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * <br>Created by Soybeany on 2018/1/22.
 */
public class HorizonProgressDialogHolder extends SimpleProgressDialogHolder {

    public HorizonProgressDialogHolder(Context context) {
        super(context);
    }

    @Override
    protected void initDialog(Context context) {
        super.initDialog(context);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setProgressNumberFormat(null);
    }

    /**
     * 设置最大值
     */
    public HorizonProgressDialogHolder max(int max) {
        mDialog.setMax(max);
        return this;
    }

    /**
     * 设置进度
     */
    public void setProgress(int value) {
        mDialog.setProgress(value);
    }
}
