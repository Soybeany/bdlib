package com.soybeany.bdlib.basic.widget.output.listview.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 分组列表项数据
 * <br>Created by Soybeany on 2017/3/17.
 */
public class GroupItemData<Group, Child> {

    /**
     * 分组数据
     */
    private Group mGData;

    /**
     * 子项数据
     */
    private List<Child> mCDataList = new ArrayList<>();

    /**
     * 设置分组数据
     */
    public GroupItemData<Group, Child> gData(Group gData) {
        mGData = gData;
        return this;
    }

    /**
     * 设置子项列表
     */
    public GroupItemData<Group, Child> cDataList(List<Child> cDataList) {
        mCDataList.clear();
        if (null != cDataList) {
            mCDataList.addAll(cDataList);
        }
        return this;
    }

    /**
     * 复制数据
     */
    public GroupItemData<Group, Child> copy(GroupItemData<Group, Child> source) {
        gData(source.mGData);
        cDataList(source.mCDataList);
        return this;
    }

    /**
     * 获得分组数据
     */
    public Group getGData() {
        return mGData;
    }

    /**
     * 获得子项数据
     */
    public List<Child> getCDataList() {
        return mCDataList;
    }

}
