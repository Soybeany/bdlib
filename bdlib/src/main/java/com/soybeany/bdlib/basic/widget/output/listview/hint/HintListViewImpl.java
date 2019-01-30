package com.soybeany.bdlib.basic.widget.output.listview.hint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.widget.interfaces.IContentPresenter;
import com.soybeany.bdlib.basic.widget.output.layout.HintViewLayout;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IHintListView;
import com.soybeany.bdlib.basic.widget.util.WdgPresentUtils;
import com.soybeany.bdlib.util.display.ViewTransferUtils;

/**
 * 提示语处理者
 * <br>Created by Soybeany on 2017/3/18.
 */
public class HintListViewImpl implements IHintListView {

    /**
     * 默认的提示语
     */
    public static String DEFAULT_HINT = "暂无内容";

    private IHintListListener mListener; // 重置监听者

    private ViewGroup mRootView; // 根视图
    private TextView mDescView; // 描述文本视图
    private HintViewLayout mHintLayout; // 提示语布局


    HintListViewImpl(IHintListListener listener) {
        mListener = listener;
    }


    // //////////////////////////////////协议实现//////////////////////////////////

    @Override
    public ViewGroup getRootView() {
        return mRootView;
    }

    @Override
    public void setDesc(String desc) {
        WdgPresentUtils.autoShowTextView(mDescView, desc);
    }

    @Override
    public void setRefreshBtnText(String text) {
        mHintLayout.refreshBtnText(text);
    }

    @Override
    public void setRefreshBtnListener(View.OnClickListener listener) {
        mHintLayout.refreshBtnListener(listener);
    }

    @Override
    public void addContentStateListener(IContentPresenter.IContentStateListener listener) {
        mHintLayout.addContentStateListener(listener);
    }

    @Override
    public void removeContentStateListener(IContentPresenter.IContentStateListener listener) {
        mHintLayout.removeContentStateListener(listener);
    }

    @Override
    public void clearContentStateListener() {
        mHintLayout.clearContentStateListener();
    }

    @Override
    public void resetListView(String hint) {
        resetListView(hint, false);
    }

    @Override
    public void resetListView(String hint, boolean needRefreshButton) {
        mListener.onReset();
        switchContentVisibility(hint, needRefreshButton);
    }

    @Override
    public void refreshListView(String hint) {
        refreshListView(hint, false);
    }

    @Override
    public void refreshListView(String hint, boolean needRefreshButton) {
        mListener.onRefresh();
        switchContentVisibility(hint, needRefreshButton);
    }


    // //////////////////////////////////共享方法//////////////////////////////////

    /**
     * 重置列表(默认提示:{@link #DEFAULT_HINT})
     */
    void resetListView() {
        resetListView(DEFAULT_HINT);
    }

    /**
     * 刷新列表(默认提示:{@link #DEFAULT_HINT})
     */
    void refreshListView() {
        refreshListView(DEFAULT_HINT);
    }

    /**
     * 将listView转移到提示语列表中，并使用其替代listView原来的位置
     */
    void transfer(Context context, View listView) {
        // 渲染提示语列表
        mRootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.bd_wdg_lv_hint, (ViewGroup) listView.getParent(), false);
        // 绑定控件
        mDescView = (TextView) mRootView.findViewById(R.id.bd_lv_hint_desc_tv);
        mHintLayout = (HintViewLayout) mRootView.findViewById(R.id.bd_lv_hint_container_hvl);
        // 转移listView
        RelativeLayout.LayoutParams params = ViewTransferUtils.getRelativeLayoutParams();
        params.addRule(RelativeLayout.BELOW, R.id.bd_lv_hint_desc_tv);
        ViewTransferUtils.transfer(listView, mRootView, params, 1);
    }


    // //////////////////////////////////私有方法//////////////////////////////////

    /**
     * 切换内容可见性
     */
    private void switchContentVisibility(String hint, boolean needRefreshButton) {
        if (mListener.hasContent()) {
            mHintLayout.showContent();
        } else {
            mHintLayout.hideContent(hint, needRefreshButton);
        }
    }


    // //////////////////////////////////协议//////////////////////////////////

    /**
     * 提示语列表监听器
     */
    interface IHintListListener {
        /**
         * 重置时的回调
         */
        void onReset();

        /**
         * 刷新时的回调
         */
        void onRefresh();

        /**
         * 列表是否有内容
         */
        boolean hasContent();
    }

}
