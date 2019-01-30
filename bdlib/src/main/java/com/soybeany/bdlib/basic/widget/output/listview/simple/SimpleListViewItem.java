package com.soybeany.bdlib.basic.widget.output.listview.simple;

import android.view.View;

import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewItem;

/**
 * 简易的列表项
 * <br>Created by Soybeany on 2017/10/19.
 */
public abstract class SimpleListViewItem<Data> implements IListViewItem<Data> {

    @Override
    public void bindViews(View convertView) {
        // 留空
    }

    @Override
    public void initViews() {
        // 留空
    }

    @Override
    public void setupView(int position, boolean flag, Data data) {
        // 留空
    }
}
