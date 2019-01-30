package com.soybeany.bdlib.basic.widget.output.listview.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IRecyclerViewAdapter;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IRecyclerViewItemListener;

import java.util.List;

/**
 * 基础的重用视图适配器
 * <br>Created by Soybeany on 2017/8/19.
 */
public class BaseRecyclerViewAdapter<Data> extends RecyclerView.Adapter implements IRecyclerViewAdapter<Data> {

    private IItemFactory<Data> mFactory; // 列表项工厂
    private List<Data> mDataList; // 列表的数据

    private IRecyclerViewItemListener<Data> mListener; // 外部监听器
    private IRecyclerViewItemListener<Data> mInnerListener = new IRecyclerViewItemListener<Data>() {
        @Override
        public void onItemClick(View view, int position, Data data) {
            if (null != mListener) {
                mListener.onItemClick(view, position, data);
            }
        }

        @Override
        public boolean onItemLongClick(View view, int position, Data data) {
            return null != mListener && mListener.onItemLongClick(view, position, data);
        }
    }; // 内部监听器

    public BaseRecyclerViewAdapter(IItemFactory<Data> factory) {
        mFactory = factory;
    }

    @Override
    public void setData(List<Data> dataList) {
        mDataList = dataList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mFactory.getItemType(position, mDataList.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mFactory.getConvertView(viewType, parent);
        convertView.setClickable(true);
        return new ViewHolder(convertView);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Data data = mDataList.get(position);
        mFactory.setupConvertView(position, false, ((ViewHolder) holder).position(position).itemView, data);
    }

    @Override
    public void setListener(IRecyclerViewItemListener<Data> listener) {
        mListener = listener;
    }

    @Override
    public int getDataCount() {
        return mDataList.size();
    }

    /**
     * 内部使用的视图持有器
     */
    private class ViewHolder extends RecyclerView.ViewHolder {
        private int mPosition; // 当前位置
        private Data mData; // 当前数据

        ViewHolder(View itemView) {
            super(itemView);
            setupListeners(itemView);
        }

        /**
         * 设置当前位置
         */
        ViewHolder position(int position) {
            mPosition = position;
            mData = mDataList.get(mPosition);
            return this;
        }

        /**
         * 设置监听器
         */
        private void setupListeners(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mInnerListener.onItemClick(v, mPosition, mData);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mInnerListener.onItemLongClick(v, mPosition, mData);
                }
            });
        }
    }

}
