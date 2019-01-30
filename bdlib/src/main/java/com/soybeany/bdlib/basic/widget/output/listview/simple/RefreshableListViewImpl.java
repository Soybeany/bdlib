package com.soybeany.bdlib.basic.widget.output.listview.simple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IRefreshableListView;


/**
 * 列表视图的刷新辅助类，需放到setAdapter前
 * <br>Created by Ben on 2016/5/3.
 */
public class RefreshableListViewImpl implements IRefreshableListView {

    private static final int RATIO = 3; // 实际的padding的距离与界面上偏移距离的比例
    private static final float DISTANCE_RATIO = 1.5f; // 下拉与释放的边界比例（变换则越大需要拉得越远）

    // ***** 公共部分 *****
    private Context mContext; // 上下文
    private ListView mListView; // 所辅助的表格

    // ***** 官方监听器部分 *****
    private View.OnTouchListener mRefreshTouchListener = new TouchListenerForRefresh(); // 刷新的触摸监听器
    private AbsListView.OnScrollListener mLoadScrollListener = new ScrollListenerForLoad(); // 加载的滚动监听器

    // ***** 刷新部分 *****
    private IOnRefreshListener mRefreshListener; // 刷新监听器
    private ViewHolder mRefreshVH; // 刷新视图持有器
    private boolean mInRefreshMode; // 是否已进入刷新模式
    private int mRefreshState; // 刷新状态
    private int mStartY; // 刷新模式开始时手指在屏幕上的y坐标
    private View mRefreshView; // 刷新视图
    private int mRefreshViewHeight; // 刷新视图的高度

    // ***** 加载更多部分 *****
    private IOnLoadListener mLoadListener; // 加载监听器
    private ViewHolder mLoadVH; // 加载更多视图持有器
    private int mLoadState; // 加载更多状态


    public RefreshableListViewImpl(ListView listView, IOnRefreshListener refreshListener, IOnLoadListener loadListener) {
        mContext = listView.getContext();
        mListView = listView;
        mRefreshListener = refreshListener;
        mLoadListener = loadListener;

        boolean needFunc = true; // 默认需要

        if (null == mRefreshListener && null == mLoadListener) {
            needFunc = false;
        }

        if (needFunc) {
            // 设置刷新与加载更多功能
            if (null != mRefreshListener) {
                setupRefreshFunc(true);
            }
            if (null != mLoadListener) {
                setupLoadFunc(true);
            }
        }
    }


    // //////////////////////////////////公开的常用操作方法//////////////////////////////////

    @Override
    public void refreshStart() {
        if (null != mRefreshListener && REFRESH_REFRESHING != mRefreshState) {
            updateRefreshView(REFRESH_REFRESHING);
            mRefreshListener.onRefresh();
        }
    }

    @Override
    public void refreshComplete() {
        if (null != mRefreshListener) {
            updateRefreshView(REFRESH_IDLE);
        }
    }

    @Override
    public void loadStart() {
        if (null != mLoadListener && LOAD_LOADING != mLoadState) {
            switchState(LOAD_LOADING);
            mLoadListener.onLoad();
        }
    }

    @Override
    public void loadSucceed() {
        if (null != mLoadListener) {
            switchState(LOAD_IDLE);
        }
    }

    @Override
    public void loadFail() {
        if (null != mLoadListener) {
            switchState(LOAD_FAIL);
        }
    }

    @Override
    public void loadEnd() {
        if (null != mLoadListener) {
            switchState(LOAD_END);
        }
    }

    @Override
    public void enableRefresh(boolean flag) {
        if (null != mRefreshListener) {
            mListView.setOnTouchListener(flag ? mRefreshTouchListener : null);
        }
    }

    @Override
    public void enableLoad(boolean flag) {
        if (null != mLoadListener) {
            int state = LOAD_GONE;
            AbsListView.OnScrollListener listener = null;
            if (flag) {
                state = LOAD_VISIBLE;
                listener = mLoadScrollListener;
            }
            switchState(state);
            mListView.setOnScrollListener(listener);
        }
    }


    // //////////////////////////////////私有内部实现//////////////////////////////////

    /**
     * 设置刷新功能
     */
    private void setupRefreshFunc(boolean enable) {
        // 添加表头
        mRefreshView = ViewHolder.getConvertView(mContext);
        mListView.addHeaderView(mRefreshView);
//        mListView.setHeaderDividersEnabled(false);

        // 引用持有者及监听
        mRefreshVH = (ViewHolder) mRefreshView.getTag();
        mRefreshViewHeight = mRefreshVH.viewHeight;
        updateRefreshView(REFRESH_IDLE);

        // 设置手势监听,若在使用时设置了该监听，将会覆盖
        enableRefresh(enable);
    }

    /**
     * 设置加载更多功能
     */
    private void setupLoadFunc(boolean enable) {
        // 添加表尾
        View footerView = ViewHolder.getConvertView(mContext);
        mListView.addFooterView(footerView);
        mListView.setFooterDividersEnabled(false);


        // 引用持有者及监听
        mLoadVH = (ViewHolder) footerView.getTag();
        mLoadVH.setHelper(this);
        switchState(LOAD_IDLE);

        // 设置滚动监听器
        enableLoad(enable);
    }

    /**
     * 转换表头/表尾状态
     */
    private void switchState(int state) {
        boolean isMatch; // 判断是否找到匹配

        if (null != mLoadVH) {
            isMatch = true;
            switch (state) {
                case LOAD_IDLE:
                    mLoadVH.setupItem(L_IDLE, false, null); // 加载空闲，没进度圈，不可点击
                    break;
                case LOAD_LOADING:
                    mLoadVH.setupItem(L_LOADING, true, null); // 加载中，有进度圈，不可点击
                    break;
                case LOAD_FAIL:
                    mLoadVH.setupItem(L_FAIL, false, mLoadListener); // 加载失败，没进度圈，可点击
                    break;
                case LOAD_END:
                    mLoadVH.setupItem(L_END, false, null); // 加载最后，没进度圈，不可点击
                    break;
                case LOAD_VISIBLE:
                    mLoadVH.switchVisibility(View.VISIBLE);
                    break;
                case LOAD_INVISIBLE:
                    mLoadVH.switchVisibility(View.INVISIBLE);
                    break;
                case LOAD_GONE: // 常用
                    mLoadVH.switchVisibility(View.GONE);
                    break;
                default:
                    isMatch = false;
                    break;
            }

            if (isMatch && LOAD_VISIBLE != state && LOAD_INVISIBLE != state && LOAD_GONE != state) {
                mLoadState = state; // 记录转换后的状态（不包含可视状态的转换）
            }

        }

        if (null != mRefreshVH) {
            isMatch = true;
            switch (state) {
                case REFRESH_IDLE:
                    mRefreshVH.setupItem(R_IDLE, false, null); // 刷新空闲，没进度圈，不可点击
                    break;
                case REFRESH_PULL:
                    mRefreshVH.setupItem(R_PULL, false, null); // 刷新继续下拉，没进度圈，不可点击
                    break;
                case REFRESH_RELEASE:
                    mRefreshVH.setupItem(R_RELEASE, false, null); // 刷新释放刷新，没进度圈，不可点击
                    break;
                case REFRESH_REFRESHING:
                    mRefreshVH.setupItem(R_REFRESHING, true, null); // 刷新中，有进度圈，不可点击
                    break;
                default:
                    isMatch = false;
                    break;
            }

            if (isMatch) {
                mRefreshState = state; // 记录转换后的状态
            }
        }
    }

    /**
     * 根据刷新状态更新界面
     */
    private void updateRefreshView(int state) {
        mRefreshState = state;
        switch (mRefreshState) {
            case REFRESH_RELEASE:
                switchState(REFRESH_RELEASE);
                break;
            case REFRESH_PULL:
                switchState(REFRESH_PULL);
                break;
            case REFRESH_REFRESHING:
                mRefreshView.setPadding(0, 0, 0, 0);
                switchState(REFRESH_REFRESHING);
                break;
            case REFRESH_IDLE:
                mRefreshView.setPadding(0, -mRefreshViewHeight, 0, 0);
                switchState(REFRESH_IDLE);
                break;
        }
    }


    // //////////////////////////////////刷新/加载视图持有器//////////////////////////////////

    /**
     * 刷新与加载更多的公共viewHolder
     */
    private static class ViewHolder {

        View rootView; // 根视图
        View contentView; // 内容视图
        ProgressBar progressBar; // 加载圈
        TextView description; // 说明文本

        int viewHeight;

        private RefreshableListViewImpl mHelper;

        private ViewHolder(View convertView) {
            init(convertView);

            // 测量并记录该view的高度
            measureView(convertView);
            viewHeight = convertView.getMeasuredHeight();
        }

        /**
         * 获取渲染后的视图，tag值为该对象
         */
        static View getConvertView(Context context) {
            View convertView = LayoutInflater.from(context).inflate(R.layout.bd_wdg_lv_item_load_more, null);
            convertView.setTag(new ViewHolder(convertView));
            return convertView;
        }

        /**
         * 测量视图尺寸
         */
        private static void measureView(View child) {
            ViewGroup.LayoutParams params = child.getLayoutParams();
            if (null == params) {
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, params.width);
            int lpHeight = params.height;
            int childHeightSpec;
            if (lpHeight > 0) {
                childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            }
            child.measure(childWidthSpec, childHeightSpec);
        }

        /**
         * 对成员变量赋值
         */
        private void init(View rootView) {
            this.rootView = rootView;
            contentView = rootView.findViewById(R.id.bd_lv_item_load_more_content_ll);
            progressBar = (ProgressBar) rootView.findViewById(R.id.bd_lv_item_load_more_progress_pb);
            description = (TextView) rootView.findViewById(R.id.bd_lv_item_load_more_desc_tv);
        }

        /**
         * 设置刷新辅助器
         */
        void setHelper(RefreshableListViewImpl helper) {
            mHelper = helper;
        }

        /**
         * 设置item
         */
        void setupItem(String desc, boolean proBarVisible, final IOnLoadListener listener) {
            // 设置文本
            description.setText(desc);

            // 设置进度圈可见
            progressBar.setVisibility(proBarVisible ? View.VISIBLE : View.GONE);

            // 设置是否可点击及点击监听
            if (null == listener) {
                setRootViewClickable(false);
            } else {
                setRootViewClickable(true);
                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHelper.switchState(LOAD_LOADING);
                        listener.onLoad();
                    }
                });
            }
        }

        /**
         * 转换可见状态
         */
        void switchVisibility(int visibility) {
            contentView.setVisibility(visibility);
        }

        /**
         * 设置根视图是否可点击
         */
        private void setRootViewClickable(boolean clickable) {
            rootView.setClickable(clickable);
        }
    }


    // //////////////////////////////////手势监听者//////////////////////////////////

    /**
     * 用于刷新功能的触摸监听者
     */
    private class TouchListenerForRefresh implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartY = getCurY(event); // 按下时需记录y坐标，不然某些情况下会跳动
                    break;

                case MotionEvent.ACTION_UP:
                    if (mInRefreshMode) {
                        switch (mRefreshState) {
                            case REFRESH_RELEASE:
                                refreshStart();
                                break;
                            case REFRESH_PULL:
                                refreshComplete();
                                mInRefreshMode = false;
                                break;
                        }
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    int curY = getCurY(event);
                    mInRefreshMode = (0 == mListView.getFirstVisiblePosition() && REFRESH_REFRESHING != mRefreshState);

                    if (!mInRefreshMode) {
                        mStartY = curY; // 如果从列表中的某个位置开始拖动，需要在位置0处更新Y坐标，不然会有跳动现象
                    }

                    if (mInRefreshMode) {
                        if (REFRESH_IDLE != mRefreshState) {
                            mListView.setSelection(0); // 在往上拖时，让列表差速的关键代码

                            if (mRefreshViewHeight <= (getOffset(curY) / (DISTANCE_RATIO * RATIO))) {
                                mRefreshState = REFRESH_RELEASE;
                            } else if (0 < getOffset(curY)) {
                                mRefreshState = REFRESH_PULL; // 普通上拖，将状态设置为继续下拉
                            } else {
                                mRefreshState = REFRESH_IDLE; // 过度上拖，需要将其状态设置为空闲
                            }

                        } else if (0 < getOffset(curY)) {
                            mRefreshState = REFRESH_PULL;
                        }

                        switchState(mRefreshState); // 根据偏移位置切换为对应的状态
                        mRefreshView.setPadding(0, getOffset(curY) / RATIO - mRefreshViewHeight, 0, 0); // 动态调整表头高度的关键代码
                    }
                    break;
            }
            return false;
        }

        /**
         * 获得当前的Y值
         */
        private int getCurY(MotionEvent event) {
            return (int) event.getRawY();
        }

        /**
         * 获取偏移值
         */
        private int getOffset(int curY) {
            return (curY - mStartY);
        }
    }

    /**
     * 用于加载功能的滚动监听者
     */
    private class ScrollListenerForLoad implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (null != mLoadListener && !isEmpty() && isIdle(scrollState) && isBottom()) {
                loadStart();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }

        /**
         * 判断listView内容是否为空
         */
        private boolean isEmpty() {
            ListAdapter adapter = mListView.getAdapter();
            return null == adapter || 0 >= adapter.getCount();
        }

        /**
         * 是否空闲状态
         */
        private boolean isIdle(int scrollState) {
            return SCROLL_STATE_IDLE == scrollState && LOAD_IDLE == mLoadState;
        }

        /**
         * 是否已滚到最下方
         */
        private boolean isBottom() {
            return mListView.getLastVisiblePosition() == mListView.getCount() - 1;
        }
    }

}
