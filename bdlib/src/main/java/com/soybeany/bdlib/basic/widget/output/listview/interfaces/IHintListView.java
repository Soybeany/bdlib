package com.soybeany.bdlib.basic.widget.output.listview.interfaces;

import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.basic.widget.interfaces.IContentPresenter;

/**
 * 提示语视图
 * <br>Created by Soybeany on 2017/3/18.
 */
public interface IHintListView {

    /**
     * 获得根视图
     */
    ViewGroup getRootView();


    // //////////////////////////////////提示语//////////////////////////////////

    /**
     * 设置文本描述
     */
    void setDesc(String desc);

    /**
     * 设置刷新按钮的文本
     */
    void setRefreshBtnText(String text);

    /**
     * 设置刷新按钮的监听器
     */
    void setRefreshBtnListener(View.OnClickListener listener);


    // //////////////////////////////////内容状态监听者//////////////////////////////////

    /**
     * 添加内容状态监听者
     */
    void addContentStateListener(IContentPresenter.IContentStateListener listener);

    /**
     * 移除内容状态监听者
     */
    void removeContentStateListener(IContentPresenter.IContentStateListener listener);

    /**
     * 清除全部内容状态监听者
     */
    void clearContentStateListener();


    // //////////////////////////////////刷新方法//////////////////////////////////

    /**
     * 重置listView的数据--重置成功（带分页时，使用refreshListView）
     */
    void resetListView(String hint);

    /**
     * 重置listView的数据--重置失败
     */
    void resetListView(String hint, boolean needRefreshButton);

    /**
     * 刷新listView的数据--刷新成功
     */
    void refreshListView(String hint);

    /**
     * 刷新listView的数据--刷新失败
     */
    void refreshListView(String hint, boolean needRefreshButton);

}
