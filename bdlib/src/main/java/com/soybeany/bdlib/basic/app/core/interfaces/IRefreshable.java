package com.soybeany.bdlib.basic.app.core.interfaces;

/**
 * 页面可刷新接口
 * <br>Created by Soybeany on 2017/3/14.
 */
public interface IRefreshable {

    /**
     * 请求刷新当前页面，可选延时或立刻
     */
    void requireToRefresh(boolean isNow);

    /**
     * 需要刷新当前页面时的操作
     */
    void onRefresh();

}
