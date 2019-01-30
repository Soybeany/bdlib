package com.soybeany.bdlib.basic.widget.output.listview.simple;

import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.soybeany.bdlib.basic.widget.output.listview.base.BaseExpListViewAdapter;
import com.soybeany.bdlib.basic.widget.output.listview.base.BaseExpListViewHelper;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IExpItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IRefreshableListView;

/**
 * 简单的可展开的列表视图辅助器，使用时不需要再调用{@link #setAdapter(BaseExpandableListAdapter)}，默认使用{@link BaseExpListViewAdapter}
 * <br>Created by Soybeany on 2017/3/17.
 */
public class SimpleExpListViewHelper<Group, Child> extends BaseExpListViewHelper<Group, Child, BaseExpListViewAdapter<Group, Child>> implements IRefreshableListView {

    private RefreshableListViewImpl mHelper; // 刷新辅助器

    public SimpleExpListViewHelper(ExpandableListView listView, IExpItemFactory<Group, Child> factory) {
        this(listView, factory, null, null);
    }

    public SimpleExpListViewHelper(ExpandableListView listView, IExpItemFactory<Group, Child> factory, IOnRefreshListener refreshListener, IOnLoadListener loadListener) {
        super(listView);
        mHelper = new RefreshableListViewImpl(listView, refreshListener, loadListener);
        setAdapter(new BaseExpListViewAdapter<>(factory));
    }

    @Override
    public void refreshListView() {
        super.refreshListView();
        enableLoad(!isEmpty());
    }

    @Override
    public void resetListView() {
        super.resetListView();
        enableLoad(!isEmpty());
    }

    @Override
    public void refreshStart() {
        mHelper.refreshStart();
    }

    @Override
    public void refreshComplete() {
        mHelper.refreshComplete();
    }

    @Override
    public void loadStart() {
        mHelper.loadStart();
    }

    @Override
    public void loadSucceed() {
        mHelper.loadSucceed();
    }

    @Override
    public void loadFail() {
        mHelper.loadFail();
    }

    @Override
    public void loadEnd() {
        mHelper.loadEnd();
    }

    @Override
    public void enableRefresh(boolean flag) {
        mHelper.enableRefresh(flag);
    }

    @Override
    public void enableLoad(boolean flag) {
        mHelper.enableLoad(flag);
    }

    @Override
    public boolean isEmpty() {
        return 0 == getAdapter().getGroupCount();
    }

}
