package com.soybeany.bdlib.basic.widget.output.nav;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.util.async.AsyncUtils;
import com.soybeany.bdlib.util.async.impl.option.TimerAsyncOption;
import com.soybeany.bdlib.util.display.ViewTransferUtils;
import com.soybeany.bdlib.util.system.KeyboardUtils;

/**
 * 基础的导航辅助器，基于官方的DrawerLayout
 * <br>Created by Soybeany on 2017/9/4.
 */
public abstract class BaseNavHelper {

    private DrawerLayout mDrawerLayout; // 抽屉布局
    private ActionBarDrawerToggle mToggle; // 导航视图开关

    private LinearLayout mRootV; // 抽屉根视图
    private View mBodyV; // 主体视图
    private View mFixV; // 固定视图，位于最下方

    private boolean mHasDisabledDragMode; // 是否已禁用手势模式

    public BaseNavHelper(Activity activity, Toolbar toolbar, DrawerLayout drawerLayout, int bodyLayoutId) {
        mDrawerLayout = drawerLayout;
        mBodyV = LayoutInflater.from(activity).inflate(bodyLayoutId, drawerLayout, false);
        setupDrawerView(activity); // 需保证抽屉视图位于DrawerLayout的最后，不然会导致抽屉视图点击无反应
        setupDefaultListener(activity);
        setupToggle(activity, toolbar);
    }

    /**
     * 设置抽屉是否可用
     */
    public void setDrawerUsable(boolean flag) {
        mDrawerLayout.removeView(mRootV);
        if (flag) {
            mDrawerLayout.addView(mRootV);
        }
    }

    /**
     * 禁用手势打开抽屉
     */
    public void disableDragMode() {
        if (!mHasDisabledDragMode) {
            mDrawerLayout.addDrawerListener(new DragModeListener());
            mHasDisabledDragMode = true;
        }
    }

    /**
     * 开启导航栏
     *
     * @return 是否开启了抽屉
     */
    public boolean openDrawer() {
        boolean isOpen = false;
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            try {
                mDrawerLayout.openDrawer(GravityCompat.START);
                isOpen = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isOpen;
    }

    /**
     * 关闭导航栏
     * <br>人性化设计；在实体返回键的相关监听方法中补充使用
     *
     * @return 是否关闭了抽屉
     */
    public boolean closeDrawer() {
        boolean isClose = false;
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            isClose = true;
        }
        return isClose;
    }

    /**
     * 关闭导航栏(带延时)，常用于需要关闭并跳转页面的情况
     */
    public void closeDrawerWithDelay() {
        AsyncUtils.timer(100, true, new TimerAsyncOption() {
            @Override
            protected void onTimeUp() {
                closeDrawer();
            }
        });
    }

    /**
     * 将返回按钮变换为菜单按钮
     */
    public void switchToMenuBtn() {
        if (null != mToggle) {
            mToggle.syncState();
        }
    }

    /**
     * 添加表头视图
     */
    public void addHeaderView(View header) {
        mRootV.addView(header, 0);
    }

    /**
     * 获得抽屉根视图
     */
    public ViewGroup getRootView() {
        return mRootV;
    }

    /**
     * 获得主体视图
     */
    public View getBodyView() {
        return mBodyV;
    }

    /**
     * 设置固定视图
     *
     * @param view 需要添加的固定视图，可传入null进行重置
     */
    public void setFixView(View view) {
        if (null != mFixV) {
            mRootV.removeView(mFixV);
        }
        if (null != (mFixV = view)) {
            mRootV.addView(mFixV);
        }
    }

    /**
     * 设置抽屉的宽度(单位:px)
     */
    public void setWidth(int width) {
        mRootV.getLayoutParams().width = width;
    }

    /**
     * 设置抽屉视图
     */
    private void setupDrawerView(Context context) {
        mRootV = new LinearLayout(context);
        mRootV.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        ViewTransferUtils.transfer(mBodyV, mRootV, params, 0);
        setDrawerUsable(true);
    }

    /**
     * 设置常规监听器
     */
    private void setupDefaultListener(final Activity activity) {
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                KeyboardUtils.closeKeyboard(activity);
            }
        });
    }

    /**
     * 设置开关
     */
    private void setupToggle(Activity activity, Toolbar toolbar) {
        if (null == toolbar) {
            return;
        }
        mToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        switchToMenuBtn();
    }

    /**
     * 拖动模式监听器
     */
    private class DragModeListener extends DrawerLayout.SimpleDrawerListener {
        DragModeListener() {
            onDrawerClosed(null);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }
}
