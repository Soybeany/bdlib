package com.soybeany.bdlib.basic.widget.output.listview.interfaces;

import android.view.View;

/**
 * 列表视图列表项接口
 * <br>Created by Soybeany on 2017/3/16.
 */
public interface IListViewItem<Data> {

    /**
     * 设置视图资源id
     */
    int setupItemResId();

    /**
     * 绑定视图
     */
    void bindViews(View convertView);

    /**
     * 对控件进行一些初始化操作，例如设置监听器等
     */
    void initViews();

    /**
     * 设置视图
     *
     * @param flag 多用途标识，需自行根据使用环境进行判断
     */
    void setupView(int position, boolean flag, Data data);

}
