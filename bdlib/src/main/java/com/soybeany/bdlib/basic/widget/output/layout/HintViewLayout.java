package com.soybeany.bdlib.basic.widget.output.layout;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.widget.interfaces.IContentPresenter;
import com.soybeany.bdlib.basic.widget.util.WdgPresentUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 提示语视图布局(相对布局)
 * <br>Created by Soybeany on 2017/3/16.
 */
public class HintViewLayout extends RelativeLayout implements IContentPresenter {
    // TODO: 2017/4/20 改为链式传入提示语布局，内部在传入时再进行提示语渲染
    private View mHintView; // 提示语视图
    private TextView mHintTv; // 提示语文本框
    private Button mRefreshBtn; // 刷新按钮

    private boolean mIsContentHiding; // 标识内容是否已隐藏

    private Set<IContentStateListener> mListeners = new HashSet<>(); // 监听者集合

    public HintViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 加载提示语视图
        LayoutInflater.from(context).inflate(R.layout.bd_wdg_view_hint, this);
        // 绑定控件
        mHintView = findViewById(R.id.bd_view_hint_container_ll);
        mHintTv = (TextView) findViewById(R.id.bd_view_hint_desc_tv);
        mRefreshBtn = (Button) findViewById(R.id.bd_view_hint_refresh_btn);
    }


    // //////////////////////////////////IContentPresenter重写//////////////////////////////////

    @Override
    public void showContent() {
        handleContent(true);
    }

    @Override
    public void hideContent(String hint, boolean needRefreshBtn) {
        // 设置提示
        hint(hint);
        refreshBtnVisible(needRefreshBtn);
        // 隐藏内容
        handleContent(false);
    }


    // //////////////////////////////////自定义方法//////////////////////////////////

    /**
     * 设置弹簧长度
     *
     * @param length 弹簧长度 (单位：像素)
     */
    public HintViewLayout spring(int length) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.topMargin = length;
        return this;
    }

    /**
     * 设置提示语
     */
    public HintViewLayout hint(String hint) {
        mHintTv.setText(hint);
        return this;
    }

    /**
     * 设置刷新按钮可见性，需要提示语本身可见
     *
     * @see #showContent()
     */
    public HintViewLayout refreshBtnVisible(boolean flag) {
        WdgPresentUtils.setViewVisible(mRefreshBtn, flag);
        return this;
    }

    /**
     * 设置刷新按钮的监听者
     */
    public HintViewLayout refreshBtnListener(OnClickListener listener) {
        mRefreshBtn.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置刷新按钮文本
     */
    public HintViewLayout refreshBtnText(String text) {
        mRefreshBtn.setText(text);
        return this;
    }

    /**
     * 添加内容状态监听者
     */
    public HintViewLayout addContentStateListener(IContentStateListener listener) {
        mListeners.add(listener);
        return this;
    }

    /**
     * 移除内容状态监听者
     */
    public void removeContentStateListener(IContentStateListener listener) {
        mListeners.remove(listener);
    }

    /**
     * 清除全部内容状态监听者
     */
    public void clearContentStateListener() {
        mListeners.clear();
    }


    // //////////////////////////////////内部实现//////////////////////////////////

    /**
     * 处理内容
     *
     * @param visible 内容是否可见
     */
    private void handleContent(boolean visible) {
        if (setChildViewsVisible(visible)) {
            notifyContentStateListeners(visible);
        }
    }

    /**
     * 设置子视图的可见性
     *
     * @return 是否进行了操作
     */
    private boolean setChildViewsVisible(boolean flag) {
        boolean hasChanged = false;
        if (mIsContentHiding == flag) {
            for (int i = 0; i < getChildCount(); i++) {
                WdgPresentUtils.setViewVisible(getChildAt(i), flag); // 操作全部视图
            }
            mIsContentHiding = !flag;
            hasChanged = true;
        }
        // 单独操作提示视图
        WdgPresentUtils.setViewVisible(mHintView, !flag && !TextUtils.isEmpty(mHintTv.getText()));
        return hasChanged;
    }

    /**
     * 通知内容状态监听者
     */
    private void notifyContentStateListeners(boolean visible) {
        if (visible) {
            for (IContentStateListener listener : mListeners) {
                listener.onShow();
            }
        } else {
            for (IContentStateListener listener : mListeners) {
                listener.onHide();
            }
        }
    }

}
