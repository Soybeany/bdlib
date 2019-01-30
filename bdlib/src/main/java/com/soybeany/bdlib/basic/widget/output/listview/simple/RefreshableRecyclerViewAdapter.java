package com.soybeany.bdlib.basic.widget.output.listview.simple;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.widget.output.layout.ViewPagerRefreshLayout;
import com.soybeany.bdlib.basic.widget.output.listview.base.BaseRecyclerViewAdapter;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IRefreshableListView;
import com.soybeany.bdlib.util.display.ViewTransferUtils;

import java.util.List;

/**
 * 可刷新的重用视图适配器(只适用于纵向系)
 * <br>Created by Soybeany on 2017/8/21.
 */
public class RefreshableRecyclerViewAdapter<Data> extends BaseRecyclerViewAdapter<Data> implements IRefreshableListView {

    private static final int TYPE_LOADING_ITEM = 12345; // 加载条目
    private static final int EX_COUNT_LOAD_DISABLE = 0; // 加载的额外数目（禁止）
    private static final int EX_COUNT_LOAD_ENABLE = 1; // 加载的额外数目（启用）

    private Context mContext; // 上下文
    private RecyclerView mRecyclerView; // 重用视图
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager; // 瀑布流
    private RecyclerView.OnScrollListener mOnScrollListener = new ScrollListenerForLoad(); // 重用视图滚动时的监听器
    private List<Data> mDataList; // 列表的数据
    private int mCurState = LOAD_IDLE; // 当前的状态

    private IOnRefreshListener mRefreshListener; // 刷新监听器
    private SwipeRefreshLayout mRefreshLayout; // 刷新布局

    private IOnLoadListener mLoadListener; // 加载监听器
    private LoadViewHolder mLoadVH; // 加载的VH
    private int mLoadExCount; // 加载的额外数目

    public RefreshableRecyclerViewAdapter(RecyclerView recyclerView, IItemFactory<Data> factory, IOnRefreshListener refreshListener, IOnLoadListener loadListener) {
        super(factory);
        mContext = recyclerView.getContext();
        mRecyclerView = recyclerView;
        mRefreshListener = refreshListener;
        mLoadListener = loadListener;

        // 设置默认可刷新
        enableRefresh(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == TYPE_LOADING_ITEM ? mLoadVH : super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TYPE_LOADING_ITEM != getItemViewType(position)) {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position >= mDataList.size() ? TYPE_LOADING_ITEM : super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + mLoadExCount;
    }

    @Override
    public void setData(List<Data> dataList) {
        super.setData(dataList);
        mDataList = dataList;
    }

    @Override
    public void refreshStart() {
        if (null != mRefreshListener) {
            switchState(REFRESH_REFRESHING);
            mRefreshLayout.setRefreshing(true);
            mRefreshListener.onRefresh();
            // 禁用加载
            enableLoad(false);
        }
    }

    @Override
    public void refreshComplete() {
        if (null != mRefreshListener) {
            mRefreshLayout.setRefreshing(false);
            // 重新设置加载
            enableLoad(true);
        }
    }

    @Override
    public void loadStart() {
        if (null != mLoadListener) {
            switchState(LOAD_LOADING);
            mLoadListener.onLoad(); // 调用回调
            // 禁用刷新
            enableRefresh(false);
        }
    }

    @Override
    public void loadSucceed() {
        if (null != mLoadListener) {
            switchState(LOAD_IDLE);
            // 重新设置刷新
            enableRefresh(true);
        }
    }

    @Override
    public void loadFail() {
        if (null != mLoadListener) {
            switchState(LOAD_FAIL);
            // 重新设置刷新
            enableRefresh(true);
        }
    }

    @Override
    public void loadEnd() {
        if (null != mLoadListener) {
            switchState(LOAD_END);
            // 重新设置刷新
            enableRefresh(true);
        }
    }

    @Override
    public void enableRefresh(boolean flag) {
        if (null == mRefreshListener) {
            return;
        }
        // 转移布局
        if (null == mRefreshLayout) {
            mRefreshLayout = new ViewPagerRefreshLayout(mContext);
            mRefreshLayout.setOnRefreshListener(new InnerRefreshListener());
            ViewTransferUtils.transfer(mRecyclerView, mRefreshLayout, 0);
        }
        // 设置可用性
        mRefreshLayout.setEnabled(flag);
    }

    @Override
    public void enableLoad(boolean flag) {
        if (null == mLoadListener) {
            return;
        }
        // 设置加载列表项是否显示
        mLoadExCount = flag ? EX_COUNT_LOAD_ENABLE : EX_COUNT_LOAD_DISABLE;
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        if (flag) {
            if (null == mLoadVH) {
                mLoadVH = new LoadViewHolder(LayoutInflater.from(mContext).inflate(R.layout.bd_wdg_lv_item_load_more, mRecyclerView, false));
                switchState(LOAD_IDLE); // 初始化为空闲状态
            }
            mRecyclerView.addOnScrollListener(mOnScrollListener);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置加载列表项占据一行(布局管理器发生变动后需调用)
     */
    public void setupSpanCount() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();

        if (layoutManager == null) {
            throw new RuntimeException("LayoutManager 为空,请先设置 recycleView.setLayoutManager(...)");
        }

        if (layoutManager instanceof GridLayoutManager) { //网格布局
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_LOADING_ITEM ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        } else if (layoutManager instanceof StaggeredGridLayoutManager) { //瀑布流布局
            mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        }
    }

    /**
     * 切换状态
     */
    private void switchState(int state) {
        // 记录状态
        mCurState = state;
        // 切换视图持有器状态
        if (null == mLoadVH) {
            return;
        }
        switch (state) {
            case LOAD_IDLE:
                mLoadVH.setupVH(L_IDLE, false, false);
                break;
            case LOAD_LOADING:
                mLoadVH.setupVH(L_LOADING, true, false);
                break;
            case LOAD_FAIL:
                mLoadVH.setupVH(L_FAIL, false, true);
                break;
            case LOAD_END:
                mLoadVH.setupVH(L_END, false, false);
                break;
        }
    }

    /**
     * 加载用的滚动监听器
     */
    private class ScrollListenerForLoad extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (RecyclerView.SCROLL_STATE_IDLE == newState && isBottom(recyclerView) && LOAD_IDLE == mCurState) {
                loadStart();
            }
        }

        /**
         * 是否滚动到最底端
         */
        private boolean isBottom(RecyclerView recyclerView) {
            int delRange = recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollExtent();
            return mLoadVH.rootView.getHeight() > delRange - recyclerView.computeVerticalScrollOffset();
        }
    }

    /**
     * 内部用的刷新监听器
     */
    private class InnerRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refreshStart();
        }
    }

    /**
     * 加载的视图持有器
     */
    private class LoadViewHolder extends RecyclerView.ViewHolder {
        View rootView; // 根视图
        ProgressBar pBarV; // 加载圈
        TextView descV; // 说明文本

        LoadViewHolder(View view) {
            super(view);
            bindViews(view);
            init();
        }

        /**
         * 设置视图持有器
         */
        void setupVH(String desc, boolean needProgress, boolean clickable) {
            descV.setText(desc);
            pBarV.setVisibility(needProgress ? View.VISIBLE : View.GONE);
            rootView.setClickable(clickable);
        }

        /**
         * 绑定视图
         */
        private void bindViews(View view) {
            rootView = view;
            pBarV = (ProgressBar) rootView.findViewById(R.id.bd_lv_item_load_more_progress_pb);
            descV = (TextView) rootView.findViewById(R.id.bd_lv_item_load_more_desc_tv);
        }

        /**
         * 初始化
         */
        private void init() {
            // 设置点击监听器
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadStart();
                }
            });
            // 设置布局参数
            if (mStaggeredGridLayoutManager != null) {
                StaggeredGridLayoutManager.LayoutParams layoutParams =
                        new StaggeredGridLayoutManager.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setFullSpan(true);
                rootView.setLayoutParams(layoutParams);
            }
        }
    }
}