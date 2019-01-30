package com.soybeany.bdlib.basic.widget.output.listview.interfaces;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 列表项工厂
 * <br>Created by Soybeany on 2017/3/16.
 */
public interface IItemFactory<Data> {

    /**
     * 获得上下文
     */
    Context getContext();

    /**
     * 获得Item类型的数目
     */
    int getItemTypeCount();

    /**
     * 获得Item的类型
     */
    int getItemType(int position, Data data);

    /**
     * 获得转换视图
     */
    View getConvertView(int type, ViewGroup parent);

    /**
     * 设置转换视图
     */
    void setupConvertView(int position, boolean isExpanded, View convertView, Data data);

}
