package com.soybeany.bdlib.basic.widget.output.tabbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.util.display.FragmentOperationUtils;
import com.soybeany.bdlib.util.display.ViewTransferUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 标准的标签栏辅助器，适用于需要添加标签栏的页面
 * <br>Created by Soybeany on 2017/3/27.
 */
public class StdTabBarHelper<TabBarItem extends StdTabBarItem> {

    private static final int CONTAINER_VIEW_ID = R.id.bd_tb_container_content_fl; // 容器视图id

    private FragmentActivity mActivity; // 活动页面
    private IOnClickListener<TabBarItem> mListener; // 监听器
    private Fragment[] mFragments; // 需要用于切换的片段页面

    private ViewGroup mTabBarView; // 标签栏视图

    private int mNormColor = R.color.colorBlack; // 正常状态下的颜色
    private int mSelColor = R.color.colorBlue; // 选中状态下的颜色

    private List<View> mTabs = new ArrayList<>(); // 标签列表

    private TabBarItem mSelectedItem; // 被选中的标签
    private Fragment mCurFragment; // 当前的片段页面

    private boolean mNeedDelay; // 是否需要延时加载
    private boolean[] mLoadArr; // 加载状态数组，长度与片段页面数组的相同，true表示此片段已加载

    /**
     * 构造器
     *
     * @param activity  活动页面
     * @param referView 参考视图，只会使用其布局参数
     */
    public StdTabBarHelper(FragmentActivity activity, View referView, IOnClickListener<TabBarItem> listener, TabBarItem[] items, Fragment[] fragments, boolean needDelay) {
        // 成员变量赋值
        mActivity = activity;
        mListener = listener;
        mFragments = fragments;
        mNeedDelay = needDelay;

        // 取代原视图
        ViewGroup containerView = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.bd_wdg_tb_container, null);
        mTabBarView = (ViewGroup) containerView.findViewById(R.id.bd_tb_container_tab_ll);
        ViewTransferUtils.replace(referView, containerView);

        // 标签栏添加标签
        addTabs(items);

        // 在活动页面中添加需要的片段页面并全部隐藏
        if (!mNeedDelay) {
            FragmentOperationUtils.addFragments(mActivity, CONTAINER_VIEW_ID, mFragments);
            FragmentOperationUtils.hideFragments(mActivity, mFragments);
        } else {
            mLoadArr = new boolean[mFragments.length];
        }
    }

    /**
     * 设置标签栏背景色
     */
    public StdTabBarHelper<TabBarItem> background(int color) {
        mTabBarView.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置正常状态下的颜色
     */
    public StdTabBarHelper<TabBarItem> normColor(int resId) {
        mNormColor = resId;
        return this;
    }

    /**
     * 设置选中状态下的颜色
     */
    public StdTabBarHelper<TabBarItem> selColor(int resId) {
        mSelColor = resId;
        return this;
    }

    /**
     * 点击标签
     *
     * @param index 标签的下标
     */
    public void click(int index) {
        // 只回调内部设置的点击监听器
        mTabs.get(index).callOnClick();
    }

    /**
     * 获得当前显示的片段页面
     */
    public Fragment getCurFragment() {
        return mCurFragment;
    }

    /**
     * 添加标签(一般的数目为3~5个)
     */
    private void addTabs(TabBarItem[] items) {
        // 创建监听器
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressWarnings("unchecked")
                TabBarItem item = (TabBarItem) v.getTag();
                // 若标签已被选中，则不再进行处理
                if (mSelectedItem == item) {
                    return;
                }
                // 更新被选中的标签
                mSelectedItem = item;
                // 回调监听者的回调
                if (!mListener.onClick(item)) {
                    selectTab(mSelectedItem);
                    int index = mListener.setupFragment(item); // 需要加载的片段页面下标
                    Fragment lastFragment = mCurFragment;
                    mCurFragment = mFragments[index];
                    if (mNeedDelay && !mLoadArr[index]) {
                        FragmentOperationUtils.addFragments(mActivity, CONTAINER_VIEW_ID, mCurFragment);
                        mLoadArr[index] = true;
                    }
                    FragmentOperationUtils.switchFragment(mActivity, lastFragment, mCurFragment);
                }
            }
        };
        // 循环创建标签，并添加监听器
        for (TabBarItem item : items) {
            View tab = item.getItemView(mTabBarView);
            tab.setTag(item);
            tab.setOnClickListener(listener);
            mTabBarView.addView(tab);
            mTabs.add(tab);
        }
    }

    /**
     * 重置全部标签
     */
    private void resetTab() {
        for (View tab : mTabs) {
            StdTabBarItem item = (StdTabBarItem) tab.getTag();
            item.tintColor(mNormColor, mSelColor);
            item.setSelect(false);
            item.refresh();
        }
    }

    /**
     * 选中指定标签(内部会先重置全部标签)
     */
    private void selectTab(StdTabBarItem item) {
        resetTab();
        item.setSelect(true);
        item.refresh();
    }

    /**
     * 标签被点击时的回调
     */
    public interface IOnClickListener<TabBarItem extends StdTabBarItem> {

        /**
         * 标签被点击时的回调
         *
         * @return true表示点击事件被处理，不会再引发后续回调
         */
        boolean onClick(TabBarItem item);

        /**
         * 设置需显示的片段页面
         *
         * @return 片段页面缓存数组中的下标
         */
        int setupFragment(TabBarItem item);

    }

}
