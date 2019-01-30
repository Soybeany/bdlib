package com.soybeany.bdlib.basic.widget.output.nav;

import android.view.View;
import android.widget.TextView;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewItem;
import com.soybeany.bdlib.basic.widget.util.WdgPresentUtils;

/**
 * 标准的导航栏分组栏
 * <br>Created by Soybeany on 2017/3/19.
 */
public class StdNavGroupItem implements IListViewItem<StdNavItemData> {

    private TextView mNameV; // 名字视图

    @Override
    public int setupItemResId() {
        return R.layout.bd_wdg_lv_item_nav;
    }

    @Override
    public void bindViews(View convertView) {
        mNameV = (TextView) convertView.findViewById(R.id.bd_lv_item_nav_name_tv);

    }

    @Override
    public void initViews() {

    }

    @Override
    public void setupView(int position, boolean flag, StdNavItemData data) {
        WdgPresentUtils.autoShowTextView(mNameV, null != data ? data.name : null);
    }

}
