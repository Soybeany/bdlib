package com.soybeany.bdlib.basic.widget.output.listview.std;

import android.view.View;
import android.widget.TextView;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewItem;
import com.soybeany.bdlib.basic.widget.util.WdgPresentUtils;

/**
 * 标准的视图列表Item（分组）
 * <br>Created by Soybeany on 2017/3/20.
 */
public class StdListViewGroupItem implements IListViewItem<StdListViewData> {

    private boolean mIsGroupClickable = true; // 分组是否可点击
    private View mConvertView; // 转换视图
    private TextView mNameV; // 名字视图

    @Override
    public int setupItemResId() {
        return R.layout.bd_wdg_lv_item_std_group;
    }

    @Override
    public void bindViews(View convertView) {
        mNameV = (TextView) (mConvertView = convertView).findViewById(R.id.bd_lv_item_std_group_name_tv);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void setupView(int position, boolean flag, StdListViewData data) {
        clickable(mIsGroupClickable);
        WdgPresentUtils.autoShowTextView(mNameV, null != data ? data.name : null);
    }

    /**
     * 设置该Item是否可被点击
     */
    public StdListViewGroupItem clickable(boolean flag) {
        mIsGroupClickable = flag;
        if (null != mConvertView) {
            mConvertView.setClickable(!mIsGroupClickable);
        }
        return this;
    }

}
