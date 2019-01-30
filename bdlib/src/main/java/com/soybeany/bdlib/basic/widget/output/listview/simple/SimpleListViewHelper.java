package com.soybeany.bdlib.basic.widget.output.listview.simple;

import android.widget.BaseAdapter;
import android.widget.ListView;

import com.soybeany.bdlib.basic.widget.output.listview.base.BaseListViewAdapter;
import com.soybeany.bdlib.basic.widget.output.listview.base.BaseListViewHelper;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IRefreshableListView;

/**
 * 简单的列表视图辅助器，使用时不需要再调用{@link #setAdapter(BaseAdapter)}，默认使用{@link BaseListViewAdapter}
 * <br>Created by Soybeany on 2017/3/16.
 */
public class SimpleListViewHelper<Data> extends BaseListViewHelper<Data, BaseListViewAdapter<Data>> implements IRefreshableListView {

    private RefreshableListViewImpl mHelper; // 刷新辅助器

    public SimpleListViewHelper(ListView listView, IItemFactory<Data> factory) {
        this(listView, factory, null, null);
    }

    public SimpleListViewHelper(ListView listView, IItemFactory<Data> factory, IOnRefreshListener refreshListener, IOnLoadListener loadListener) {
        super(listView);
        mHelper = new RefreshableListViewImpl(listView, refreshListener, loadListener);
        setAdapter(new BaseListViewAdapter<>(factory));
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
        return 0 == getAdapter().getCount();
    }

}
