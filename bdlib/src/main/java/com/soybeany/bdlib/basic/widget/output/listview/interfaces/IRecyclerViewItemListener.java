package com.soybeany.bdlib.basic.widget.output.listview.interfaces;

import android.view.View;

/**
 * 重用视图列表项监听器
 * <br>Created by Soybeany on 2017/8/19.
 */
public interface IRecyclerViewItemListener<T> {

    /**
     * 列表项被点击
     */
    void onItemClick(View view, int position, T data);

    /**
     * 列表项被长点击
     */
    boolean onItemLongClick(View view, int position, T data);

}
