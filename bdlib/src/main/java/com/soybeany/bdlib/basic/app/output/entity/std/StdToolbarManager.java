package com.soybeany.bdlib.basic.app.output.entity.std;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.app.core.component.BaseToolbarManager;
import com.soybeany.bdlib.basic.app.core.component.ToolbarInfo;

/**
 * 工具栏管理器，负责工具栏内容变化的具体执行
 * <br> Created by Soybeany on 16/1/25.
 */
public class StdToolbarManager extends BaseToolbarManager {

    private AppCompatActivity mActivity; // 活动页面

    private AppBarLayout mAppBarLayout; // 整个appbar布局
    private Toolbar mToolbar; // 整个工具栏

    private int mDefaultContentInsetLeft; // 默认左边距
    private int mDefaultContentInsetRight; // 默认右边距

    public StdToolbarManager(AppCompatActivity activity) {
        super(activity);
        mActivity = activity;
        initToolbar();
    }

    @Override
    public void update(ToolbarInfo info) {
        // 设置工具栏可见性
        updateToolbarVisibility(info.isToolbarVisible);
        // 设置返回按钮可见性
        updateBackItemVisibility(info.isBackItemVisible);
        // 设置自定义视图
        updateCustomViewResId(info.customViewResId);
        // 设置副标题
        updateSubtitle(info.subtitle);
        // 设置标题
        updateTitle(info.title);
        // 更新菜单Items
        updateMenuItems();
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * 禁用悬浮效果
     */
    public void disableElevation() {
        mAppBarLayout.setTargetElevation(0);
    }

    /**
     * 更新工具栏可见性
     */
    protected void updateToolbarVisibility(boolean visible) {
        mAppBarLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置返回列表项可见性
     */
    protected void updateBackItemVisibility(boolean visible) {
        getActionbar().setDisplayHomeAsUpEnabled(visible); // 显示向上一层箭头
    }

    /**
     * 设置自定义视图资源id(根视图会被默认赋予一个id值)
     */
    protected View updateCustomViewResId(int resId) {
        View view = null;
        int id = R.id.bd_toolbar_custom;
        // 移除旧视图
        View preView = mToolbar.findViewById(id);
        if (null != preView) {
            mToolbar.removeView(preView);
        }
        if (0 < resId) {
            mToolbar.setContentInsetsAbsolute(0, 0);
            // 添加新视图
            view = mActivity.getLayoutInflater().inflate(resId, mToolbar, false);
            view.setId(id);
            mToolbar.addView(view);
        } else {
            mToolbar.setContentInsetsAbsolute(mDefaultContentInsetLeft, mDefaultContentInsetRight);
        }
        return view;
    }

    /**
     * 设置标题
     */
    protected void updateTitle(String title) {
        if (null != title) {
            ActionBar actionBar = getActionbar();
            actionBar.setTitle(title);
            actionBar.setDisplayShowTitleEnabled(true);
        } else {
            getActionbar().setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * 设置副标题
     */
    protected void updateSubtitle(String subtitle) {
        getActionbar().setSubtitle(subtitle);
    }

    /**
     * 更新菜单项
     */
    protected void updateMenuItems() {
        mActivity.invalidateOptionsMenu();
    }

    /**
     * 获得功能栏
     */
    protected ActionBar getActionbar() {
        return mActivity.getSupportActionBar();
    }

    /**
     * 初始化toolBar
     */
    private void initToolbar() {
        // 绑定视图
        mAppBarLayout = (AppBarLayout) mActivity.findViewById(R.id.bd_appbar);
        mToolbar = (Toolbar) mAppBarLayout.findViewById(R.id.bd_toolbar);

        // 记录默认边距
        mDefaultContentInsetLeft = mToolbar.getContentInsetLeft();
        mDefaultContentInsetRight = mToolbar.getContentInsetRight();

        // 设置Appbar背景
//        mAppBarLayout.setBackgroundResource(R.color.colorBlack);

        // 初始化为功能栏
        mActivity.setSupportActionBar(mToolbar);

        // ActionBar的特殊设置
        ActionBar actionBar = getActionbar();

        // 设置原生标题隐藏,即不显示靠左的标题
        actionBar.setDisplayShowTitleEnabled(false);
    }

}
