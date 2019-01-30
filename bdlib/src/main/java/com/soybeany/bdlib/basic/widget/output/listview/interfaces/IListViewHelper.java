package com.soybeany.bdlib.basic.widget.output.listview.interfaces;

import android.view.ViewGroup;

/**
 * 列表视图辅助器接口
 * <br>Created by Soybeany on 2017/3/16.
 */
public interface IListViewHelper<LV extends ViewGroup, Adapter> {

    /**
     * 获得表视图，用于进行原生操作
     */
    LV getListView();

    /**
     * 获得适配器
     */
    Adapter getAdapter();

    /**
     * 刷新表视图(更新数据，停留位置不会发生变化)
     */
    void refreshListView();

    /**
     * 重置表视图(更新数据，停留位置重置为顶端)
     */
    void resetListView();

    /**
     * 列表是否为空
     */
    boolean isEmpty();

}
