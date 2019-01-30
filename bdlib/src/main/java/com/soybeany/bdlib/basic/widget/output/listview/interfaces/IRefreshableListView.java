package com.soybeany.bdlib.basic.widget.output.listview.interfaces;

/**
 * 可刷新的表视图
 * <br>Created by Soybeany on 2017/3/19.
 */
public interface IRefreshableListView {

    // //////////////////////////////////刷新提示语//////////////////////////////////

    /**
     * 默认状态
     */
    String R_IDLE = "空闲";

    /**
     * 显示“下拉刷新”时
     */
    String R_PULL = "下拉刷新";

    /**
     * 显示“松手刷新”时
     */
    String R_RELEASE = "松手刷新";

    /**
     * 刷新中
     */
    String R_REFRESHING = "刷新中...";


    // //////////////////////////////////加载更多的提示语//////////////////////////////////

    /**
     * 空闲
     */
    String L_IDLE = "松手开始加载";

    /**
     * 加载中
     */
    String L_LOADING = "正在加载...";

    /**
     * 失败
     */
    String L_FAIL = "加载失败,请点此刷新";

    /**
     * 结束
     */
    String L_END = "当前已经是最后一页";


    // //////////////////////////////////刷新的状态//////////////////////////////////

    /**
     * 默认状态
     */
    int REFRESH_IDLE = -1;

    /**
     * 显示“下拉刷新”时
     */
    int REFRESH_PULL = -2;

    /**
     * 显示“松手刷新”时
     */
    int REFRESH_RELEASE = -3;

    /**
     * 刷新中
     */
    int REFRESH_REFRESHING = -4;


    // //////////////////////////////////加载更多的状态//////////////////////////////////

    /**
     * 空闲
     */
    int LOAD_IDLE = 0;

    /**
     * 加载中
     */
    int LOAD_LOADING = 1;

    /**
     * 失败
     */
    int LOAD_FAIL = 2;

    /**
     * 结束
     */
    int LOAD_END = 3;

    /**
     * 可见
     */
    int LOAD_VISIBLE = 4;

    /**
     * 不可见
     */
    int LOAD_INVISIBLE = 5;

    /**
     * 去除
     */
    int LOAD_GONE = 6;


    // //////////////////////////////////api//////////////////////////////////

    /**
     * 刷新开始
     */
    void refreshStart();

    /**
     * 设置刷新完成
     */
    void refreshComplete();

    /**
     * 加载开始
     */
    void loadStart();

    /**
     * 加载成功
     */
    void loadSucceed();

    /**
     * 加载失败
     */
    void loadFail();

    /**
     * 加载最后
     */
    void loadEnd();

    /**
     * 是否允许刷新功能
     */
    void enableRefresh(boolean flag);

    /**
     * 是否允许加载功能
     */
    void enableLoad(boolean flag);


    // //////////////////////////////////监听器//////////////////////////////////

    /**
     * 刷新的监听器
     */
    interface IOnRefreshListener {
        /**
         * 手势识别为刷新时的回调
         */
        void onRefresh();
    }

    /**
     * 加载更多的监听器
     */
    interface IOnLoadListener {
        /**
         * 加载时的回调
         */
        void onLoad();
    }

}
