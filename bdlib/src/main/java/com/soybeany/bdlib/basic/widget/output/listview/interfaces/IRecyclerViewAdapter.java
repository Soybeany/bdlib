package com.soybeany.bdlib.basic.widget.output.listview.interfaces;

/**
 * 重用视图适配器
 * <br>Created by Soybeany on 2017/8/19.
 */
public interface IRecyclerViewAdapter<Data> extends IListViewAdapter<Data> {

    /**
     * 设置监听器
     */
    void setListener(IRecyclerViewItemListener<Data> listener);

    /**
     * 获得数据数目(只包含数据部分，排除表头表尾)
     */
    int getDataCount();

}
