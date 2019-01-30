package com.soybeany.bdlib.basic.widget.output.listview.interfaces;

import java.util.List;

/**
 * 列表视图适配器
 * <br>Created by Soybeany on 2017/3/16.
 */
public interface IListViewAdapter<Data> {

    /**
     * 设置数据
     */
    void setData(List<Data> dataList);

}
