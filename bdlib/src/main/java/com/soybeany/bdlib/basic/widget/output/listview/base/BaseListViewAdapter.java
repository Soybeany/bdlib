package com.soybeany.bdlib.basic.widget.output.listview.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewAdapter;

import java.util.List;

/**
 * 基础的列表视图适配器
 * <br>Created by Soybeany on 2017/3/16.
 */
public class BaseListViewAdapter<Data> extends BaseAdapter implements IListViewAdapter<Data> {

    private IItemFactory<Data> mFactory; // 列表项工厂
    private List<Data> mDataList; // 列表的数据

    public BaseListViewAdapter(IItemFactory<Data> factory) {
        mFactory = factory;
    }

    @Override
    public void setData(List<Data> dataList) {
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Data getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return mFactory.getItemTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        return mFactory.getItemType(position, mDataList.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 若视图为空，则创建
        if (null == convertView) {
            convertView = mFactory.getConvertView(getItemViewType(position), parent);
        }
        // 设置视图内容
        mFactory.setupConvertView(position, false, convertView, mDataList.get(position));
        // 返回视图
        return convertView;
    }
}
