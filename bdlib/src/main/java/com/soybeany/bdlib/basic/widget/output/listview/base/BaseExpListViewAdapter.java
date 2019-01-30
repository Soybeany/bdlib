package com.soybeany.bdlib.basic.widget.output.listview.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.soybeany.bdlib.basic.widget.output.listview.entity.GroupItemData;
import com.soybeany.bdlib.basic.widget.output.listview.entity.ItemIndex;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IExpItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewAdapter;

import java.util.List;

/**
 * 基础的可展开的列表视图适配器
 * <br>Created by Soybeany on 2017/3/17.
 */
public class BaseExpListViewAdapter<Group, Child> extends BaseExpandableListAdapter implements IListViewAdapter<GroupItemData<Group, Child>> {

    private static final int BASE = 10000; // 基数

    private IExpItemFactory<Group, Child> mFactory; // 列表项工厂
    private List<GroupItemData<Group, Child>> mDataList; // 列表的数据

    public BaseExpListViewAdapter(IExpItemFactory<Group, Child> factory) {
        mFactory = factory;
    }

    @Override
    public void setData(List<GroupItemData<Group, Child>> dataList) {
        mDataList = dataList;
    }

    @Override
    public int getGroupCount() {
        return mDataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int groupSize = mDataList.size();
        if (groupSize <= groupPosition) {
            return 0;
        }
        return mDataList.get(groupPosition).getCDataList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDataList.get(groupPosition).getCDataList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * BASE + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getGroupTypeCount() {
        return mFactory.getItemTypeCount();
    }

    @Override
    public int getChildTypeCount() {
        return mFactory.getChildTypeCount();
    }

    @Override
    public int getGroupType(int groupPosition) {
        return mFactory.getItemType(groupPosition, mDataList.get(groupPosition).getGData());
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return mFactory.getChildType(ItemIndex.get(groupPosition, childPosition),
                mDataList.get(groupPosition).getCDataList().get(childPosition));
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // 若视图为空，则创建
        if (null == convertView) {
            convertView = mFactory.getConvertView(getGroupType(groupPosition), parent);
        }
        // 设置视图内容
        mFactory.setupConvertView(groupPosition, isExpanded, convertView, mDataList.get(groupPosition).getGData());
        // 返回视图
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // 若视图为空，则创建
        if (null == convertView) {
            convertView = mFactory.getChildConvertView(getGroupType(groupPosition), parent);
        }
        // 设置视图内容
        mFactory.setupChildConvertView(groupPosition, isLastChild, convertView, mDataList.get(groupPosition).getCDataList().get(childPosition));
        // 返回视图
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true; // 由列表项自身控制是否可被点击
    }

}
