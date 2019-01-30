package com.soybeany.bdlib.basic.widget.output.listview.simple;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.soybeany.bdlib.basic.widget.output.listview.base.BaseRecyclerViewAdapter;
import com.soybeany.bdlib.basic.widget.output.listview.base.BaseRecyclerViewHelper;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IRefreshableListView;

/**
 * 简单的重用视图辅助器，使用时不需要再调用{@link #setAdapter(RecyclerView.Adapter)}，默认使用{@link BaseRecyclerViewAdapter}
 * <br>Created by Soybeany on 2017/8/19.
 */
public class SimpleRecyclerViewHelper<Data> extends BaseRecyclerViewHelper<Data, BaseRecyclerViewAdapter<Data>> implements IRefreshableListView {

    private RecyclerView mRecyclerView; // 重用视图

    private RecyclerView.ItemDecoration mDecoration; // 分隔符

    private RefreshableRecyclerViewAdapter<Data> mRefreshableAdapter; // 刷新辅助器

    public SimpleRecyclerViewHelper(RecyclerView recyclerView, IItemFactory<Data> factory) {
        this(recyclerView, factory, null, null);
    }

    public SimpleRecyclerViewHelper(RecyclerView recyclerView, IItemFactory<Data> factory, IOnRefreshListener refreshListener, IOnLoadListener loadListener) {
        super(recyclerView);
        mRecyclerView = recyclerView;
        mRefreshableAdapter = new RefreshableRecyclerViewAdapter<>(mRecyclerView, factory, refreshListener, loadListener);
        setAdapter(mRefreshableAdapter);
        applyLinearLayout(LinearLayout.VERTICAL, true);
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
        mRefreshableAdapter.refreshStart();
    }

    @Override
    public void refreshComplete() {
        mRefreshableAdapter.refreshComplete();
    }

    @Override
    public void loadStart() {
        mRefreshableAdapter.loadStart();
    }

    @Override
    public void loadSucceed() {
        mRefreshableAdapter.loadSucceed();
    }

    @Override
    public void loadFail() {
        mRefreshableAdapter.loadFail();
    }

    @Override
    public void loadEnd() {
        mRefreshableAdapter.loadEnd();
    }

    @Override
    public void enableRefresh(boolean flag) {
        mRefreshableAdapter.enableRefresh(flag);
    }

    @Override
    public void enableLoad(boolean flag) {
        mRefreshableAdapter.enableLoad(flag);
    }

    /**
     * 应用线性布局
     *
     * @param orientation 使用{@link LinearLayout#VERTICAL}与{@link LinearLayout#HORIZONTAL}
     */
    public void applyLinearLayout(int orientation, boolean needDivider) {
        Context context = mRecyclerView.getContext();
        applyLayout(new LinearLayoutManager(context, orientation, false), needDivider ? new DividerItemDecoration(context, orientation) : null);
    }

    /**
     * 应用指定布局
     */
    public void applyLayout(RecyclerView.LayoutManager manager, RecyclerView.ItemDecoration decoration) {
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.removeItemDecoration(mDecoration);
        if (null != (mDecoration = decoration)) {
            mRecyclerView.addItemDecoration(mDecoration);
        }
        mRefreshableAdapter.setupSpanCount();
    }

}
