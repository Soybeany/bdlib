package com.soybeany.bdlib.basic.app.output.viewholder;

import android.view.View;

import com.soybeany.bdlib.basic.app.core.component.BaseViewHolder;
import com.soybeany.bdlib.basic.widget.util.DrawableUtils;

/**
 * 支持背景的视图持有器
 * <br>Created by Soybeany on 2017/9/5.
 */
public abstract class BgViewHolder extends BaseViewHolder {

    private View mRootView; // 根视图

    @Override
    public void bindViews(View rootView) {
        mRootView = rootView;
    }

    @Override
    public void initViews() {
        setupViewBackground(mRootView, setupBg());
    }

    /**
     * 设置视图的背景
     */
    protected void setupViewBackground(View view, Integer resId) {
        if (null != view && null != resId) {
            view.setBackground(DrawableUtils.start(resId).build());
        }
    }

    /**
     * 设置背景
     */
    protected abstract Integer setupBg();

}
