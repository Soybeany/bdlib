package com.soybeany.bdlib.basic.widget.output.listview.base;

import android.support.v7.widget.RecyclerView;

import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewHelper;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IRecyclerViewAdapter;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IRecyclerViewItemListener;
import com.soybeany.bdlib.basic.widget.util.BoundsValidationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>Created by Soybeany on 2017/8/19.
 */
public class BaseRecyclerViewHelper<Data, Adapter extends RecyclerView.Adapter & IRecyclerViewAdapter<Data>>
        implements IListViewHelper<RecyclerView, Adapter> {

    private RecyclerView mListView; // 被辅助的列表视图
    private Adapter mAdapter; // 列表视图使用的适配器

    private List<Data> mDataList = new ArrayList<>(); // 列表的数据

    private IRecyclerViewItemListener<Data> mListener; // 点击监听器


    public BaseRecyclerViewHelper(RecyclerView listView) {
        mListView = listView;
    }


    // //////////////////////////////////IListViewHelper重写//////////////////////////////////

    @Override
    public RecyclerView getListView() {
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
        return 0 == mAdapter.getDataCount();
    }


    // //////////////////////////////////数据操作方法//////////////////////////////////

    /**
     * 增加多条数据
     */
    public void addData(List<? extends Data> data) {
        if (null != data) {
            for (Data itemData : data) {
                addData(itemData);
            }
        }
    }

    /**
     * 增加数据
     */
    public void addData(Data data) {
        addData(-1, data);
    }

    /**
     * 增加数据，在指定位置
     */
    public void addData(int position, Data data) {
        if (isOutOfBounds(position)) {
            mDataList.add(data);
        } else {
            mDataList.add(position, data);
        }
    }

    /**
     * 移除数据，在指定位置
     */
    public boolean removeData(int position) {
        if (isOutOfBounds(position)) {
            return false;
        }
        mDataList.remove(position);
        return true;
    }

    /**
     * 清空全部数据
     */
    public void clearData() {
        mDataList.clear();
    }

    /**
     * 设置数据，在指定位置
     */
    public void setData(int position, Data data) {
        if (isOutOfBounds(position)) {
            return;
        }
        mDataList.set(position, data);
    }

    /**
     * 设置数据列表
     */
    public void setData(List<? extends Data> dataList) {
        clearData();
        addData(dataList);
    }

    /**
     * 获得数据
     */
    public Data getData(int position) {
        return mDataList.get(position);
    }


    // //////////////////////////////////其它方法//////////////////////////////////

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        mAdapter.setData(mDataList);
        mListView.setAdapter(mAdapter);
        setupAdapterListener(mListener);
    }

    /**
     * 设置列表项监听器
     */
    public void setListener(IRecyclerViewItemListener<Data> listener) {
        setupAdapterListener(mListener = listener);
    }


    // //////////////////////////////////内部实现//////////////////////////////////

    /**
     * item是否越界
     */
    private boolean isOutOfBounds(int position) {
        return BoundsValidationUtils.isOut(position, mDataList);
    }

    /**
     * 设置监听器
     */
    private void setupAdapterListener(IRecyclerViewItemListener<Data> listener) {
        if (null != mAdapter) {
            mAdapter.setListener(listener);
        }
    }

}
