package com.soybeany.bdlib.basic.widget.output.listview.hint;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.soybeany.bdlib.basic.widget.interfaces.IContentPresenter;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IExpItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IHintListView;
import com.soybeany.bdlib.basic.widget.output.listview.simple.SimpleExpListViewHelper;

/**
 * 带提示的可展开的列表视图辅助器，继承自{@link SimpleExpListViewHelper}，添加了提示语功能
 * <br>注意：listView需先添加到某个viewGroup中
 * <br>Created by Soybeany on 2017/3/18.
 */
public class HintExpListViewHelper<Group, Child> extends SimpleExpListViewHelper<Group, Child> implements IHintListView, HintListViewImpl.IHintListListener {

    private HintListViewImpl mHintListViewImpl; // 提示语处理者

    public HintExpListViewHelper(ExpandableListView listView, IExpItemFactory<Group, Child> factory) {
        this(listView, factory, null, null);
    }

    public HintExpListViewHelper(ExpandableListView listView, IExpItemFactory<Group, Child> factory, IOnRefreshListener refreshListener, IOnLoadListener loadListener) {
        super(listView, factory, refreshListener, loadListener);
        mHintListViewImpl = new HintListViewImpl(this);
        mHintListViewImpl.transfer(factory.getContext(), listView);
    }

    @Override
    public ViewGroup getRootView() {
        return mHintListViewImpl.getRootView();
    }

    @Override
    public void setDesc(String desc) {
        mHintListViewImpl.setDesc(desc);
    }

    @Override
    public void setRefreshBtnText(String text) {
        mHintListViewImpl.setRefreshBtnText(text);
    }

    @Override
    public void setRefreshBtnListener(View.OnClickListener listener) {
        mHintListViewImpl.setRefreshBtnListener(listener);
    }

    @Override
    public void addContentStateListener(IContentPresenter.IContentStateListener listener) {
        mHintListViewImpl.addContentStateListener(listener);
    }

    @Override
    public void removeContentStateListener(IContentPresenter.IContentStateListener listener) {
        mHintListViewImpl.removeContentStateListener(listener);
    }

    @Override
    public void clearContentStateListener() {
        mHintListViewImpl.clearContentStateListener();
    }

    @Override
    public void resetListView() {
        mHintListViewImpl.resetListView();
    }

    @Override
    public void resetListView(String hint) {
        mHintListViewImpl.resetListView(hint);
    }

    @Override
    public void resetListView(String hint, boolean needRefreshButton) {
        mHintListViewImpl.resetListView(hint, needRefreshButton);
    }

    @Override
    public void refreshListView() {
        mHintListViewImpl.refreshListView();
    }

    @Override
    public void refreshListView(String hint) {
        mHintListViewImpl.refreshListView(hint);
    }

    @Override
    public void refreshListView(String hint, boolean needRefreshButton) {
        mHintListViewImpl.refreshListView(hint, needRefreshButton);
    }

    @Override
    public void onReset() {
        super.resetListView();
    }

    @Override
    public void onRefresh() {
        super.refreshListView();
    }

    @Override
    public boolean hasContent() {
        return !isEmpty();
    }

}
