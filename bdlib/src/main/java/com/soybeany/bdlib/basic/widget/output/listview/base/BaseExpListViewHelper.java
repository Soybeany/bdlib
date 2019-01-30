package com.soybeany.bdlib.basic.widget.output.listview.base;

import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.soybeany.bdlib.basic.widget.output.listview.entity.GroupItemData;
import com.soybeany.bdlib.basic.widget.output.listview.entity.ItemIndex;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewAdapter;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewHelper;
import com.soybeany.bdlib.basic.widget.util.BoundsValidationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础的可展开的列表视图辅助器
 * <br>Created by Soybeany on 2017/3/17.
 */
public class BaseExpListViewHelper<
        Group, Child,
        Adapter extends BaseExpandableListAdapter & IListViewAdapter<GroupItemData<Group, Child>>
        > implements IListViewHelper<ExpandableListView, Adapter> {

    // data, childData StdListView SimpleListView

    private static final int OTB_NONE = 0; // 没有越界
    private static final int OTB_GROUP = 1; // 分组越界
    private static final int OTB_CHILD = 1 << 1; // 子项越界

    private ExpandableListView mListView; // 被辅助的列表视图
    private Adapter mAdapter; // 列表视图使用的适配器

    private List<GroupItemData<Group, Child>> mDataList = new ArrayList<>(); // 列表的数据


    public BaseExpListViewHelper(ExpandableListView listView) {
        mListView = listView;
    }


    // //////////////////////////////////IListViewHelper重写//////////////////////////////////

    @Override
    public ExpandableListView getListView() {
        return mListView;
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void refreshListView() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void resetListView() {
        setAdapter(mAdapter);
    }

    @Override
    public boolean isEmpty() {
        return 0 == getAdapter().getGroupCount();
    }


    // //////////////////////////////////数据操作方法//////////////////////////////////

    /**
     * 增加数据(末尾位置，添加列表项数据列表)
     */
    public void addData(List<GroupItemData<Group, Child>> iDataList) {
        if (null != iDataList) {
            for (GroupItemData<Group, Child> data : iDataList) {
                addData(data);
            }
        }
    }

    /**
     * 增加数据（末尾位置，添加列表项数据）
     */
    public void addData(GroupItemData<Group, Child> iData) {
        addData(-1, iData);
    }

    /**
     * 增加数据（指定分组位置，添加列表项数据）
     */
    public void addData(int gPosition, GroupItemData<Group, Child> iData) {
        GroupItemData<Group, Child> data = new GroupItemData<Group, Child>().copy(iData);
        if (isGroupPositionValid(gPosition)) {
            mDataList.add(gPosition, data);
        } else {
            mDataList.add(data);
        }
    }

    /**
     * 增加数据（指定具体位置，添加子项数据）
     */
    public void addData(ItemIndex index, Child cData) {
        switch (isOutOfBounds(index)) {
            case OTB_GROUP: // 分组越界，则先在表尾创建一个列表项数据，再为其子项添加数据
                mDataList.add(new GroupItemData<Group, Child>());
                mDataList.get(mDataList.size() - 1).getCDataList().add(cData);
                break;
            case OTB_CHILD: // 子项越界，则在子项表尾添加数据
                mDataList.get(index.gPosition).getCDataList().add(cData);
                break;
            default: // 没有越界，则在指定位置添加数据
                mDataList.get(index.gPosition).getCDataList().add(index.cPosition, cData);
                break;
        }
    }

    /**
     * 移除列表项数据
     */
    public boolean removeData(int gPosition) {
        if (!isGroupPositionValid(gPosition)) {
            return false;
        }
        mDataList.remove(gPosition);
        return true;
    }

    /**
     * 移除子项数据
     */
    public boolean removeData(ItemIndex index) {
        if (!isIndexValid(index)) {
            return false;
        }
        mDataList.get(index.gPosition).getCDataList().remove(index.cPosition);
        return true;
    }

    /**
     * 清空全部数据
     */
    public void clearData() {
        mDataList.clear();
    }

    /**
     * 设置数据（指定分组位置，设置列表项数据）
     */
    public boolean setData(int gPosition, GroupItemData<Group, Child> iData) {
        return setData(gPosition, iData.getGData()) && setData(gPosition, iData.getCDataList());
    }

    /**
     * 设置数据（指定分组位置，设置分组）
     */
    public boolean setData(int gPosition, Group gData) {
        if (!isGroupPositionValid(gPosition)) {
            return false;
        }
        mDataList.get(gPosition).gData(gData);
        return true;
    }

    /**
     * 设置数据（指定分组位置，设置子项列表）
     */
    public boolean setData(int gPosition, List<Child> cDataList) {
        if (!isGroupPositionValid(gPosition)) {
            return false;
        }
        mDataList.get(gPosition).cDataList(cDataList);
        return true;
    }

    /**
     * 设置数据（指定具体位置，设置子项数据）
     */
    public boolean setData(ItemIndex index, Child data) {
        if (!isIndexValid(index)) {
            return false;
        }
        mDataList.get(index.gPosition).getCDataList().set(index.cPosition, data);
        return true;
    }

    /**
     * 获得分组数据列表
     */
    public List<Group> getGroupDataList() {
        ArrayList<Group> groupDataList = new ArrayList<>();
        for (GroupItemData<Group, Child> data : mDataList) {
            groupDataList.add(data.getGData());
        }
        return groupDataList;
    }

    /**
     * 获得分组数据
     */
    public Group getGroupData(int gPosition) {
        return mDataList.get(gPosition).getGData();
    }

    /**
     * 获得子项数据列表
     */
    public List<Child> getChildDataList(int gPosition) {
        return mDataList.get(gPosition).getCDataList();
    }

    /**
     * 获得子项数据
     */
    public Child getChildData(ItemIndex index) {
        return mDataList.get(index.gPosition).getCDataList().get(index.cPosition);
    }


    // //////////////////////////////////分组收展方法//////////////////////////////////

    /**
     * 展开指定分组
     */
    public void expand(int gPosition, boolean needRefresh) {
        if (needRefresh) {
            refreshListView();
        }
        mListView.expandGroup(gPosition);
    }

    /**
     * 收起指定分组
     */
    public void collapse(int gPosition, boolean needRefresh) {
        if (needRefresh) {
            refreshListView();
        }
        mListView.collapseGroup(gPosition);
    }

    /**
     * 展开指定分组
     */
    public void expand(int gPosition) {
        expand(gPosition, true);
    }

    /**
     * 收起指定分组
     */
    public void collapse(int gPosition) {
        collapse(gPosition, true);
    }

    /**
     * 展开全部分组
     */
    public void expandAll() {
        refreshListView();
        int groupCount = mListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            expand(i, false);
        }
    }

    /**
     * 收起全部分组
     */
    public void collapseAll() {
        refreshListView();
        int groupCount = mListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            collapse(i, false);
        }
    }


    // //////////////////////////////////其它方法//////////////////////////////////

    /**
     * 设置适配器
     */
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        mAdapter.setData(mDataList);
        mListView.setAdapter(mAdapter);
    }


    // //////////////////////////////////内部实现//////////////////////////////////

    /**
     * 判断分组位置是否有效
     */
    private boolean isGroupPositionValid(int gPosition) {
        return OTB_GROUP != isOutOfBounds(ItemIndex.get(gPosition, -1));
    }

    /**
     * 判断下标是否有效
     */
    private boolean isIndexValid(ItemIndex index) {
        return OTB_NONE == isOutOfBounds(index);
    }

    /**
     * 检测下标是否越界
     */
    private int isOutOfBounds(ItemIndex index) {
        if (BoundsValidationUtils.isOut(index.gPosition, mDataList)) {
            return OTB_GROUP;
        } else if (BoundsValidationUtils.isOut(index.cPosition, mDataList.get(index.gPosition).getCDataList())) {
            return OTB_CHILD;
        }
        return OTB_NONE;
    }

}
