package com.soybeany.bdlib.basic.widget.output.listview.std;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewItem;
import com.soybeany.bdlib.basic.widget.util.BadgeUtils;
import com.soybeany.bdlib.basic.widget.util.DrawableUtils;

/**
 * 标准的视图列表Item（普通）
 * <br>Created by Ben on 2016/5/26.
 */
public class StdListViewItem implements IListViewItem<StdListViewData> {

    private View mConvertView; // 转换视图
    private ImageView mIconV; // 图标
    private TextView mNameV; // 文本
    private TextView mBadgeV; // 标记
    private ImageView mArrowIV; // 箭头

    private boolean mNeedSwitchArrow; // 切换箭头的标识
    private boolean mNeedDefBg;

    @Override
    public int setupItemResId() {
        return R.layout.bd_wdg_lv_item_std;
    }

    @Override
    public void bindViews(View convertView) {
        mConvertView = convertView;
        mIconV = (ImageView) mConvertView.findViewById(R.id.bd_lv_item_std_icon_iv);
        mNameV = (TextView) mConvertView.findViewById(R.id.bd_lv_item_std_text_tv);
        mBadgeV = (TextView) mConvertView.findViewById(R.id.bd_lv_item_std_badge_tv);
        mArrowIV = (ImageView) mConvertView.findViewById(R.id.bd_lv_item_std_arrow_iv);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void setupView(int position, boolean flag, StdListViewData data) {
        // 设置icon
        int imgResId = data.icon;
        if (0 != imgResId) {
            mIconV.setVisibility(View.VISIBLE);
            mIconV.setImageResource(imgResId);
        } else {
            mIconV.setVisibility(View.GONE);
        }

        // 设置文本
        mNameV.setText(data.name);

        // 设置标记
        BadgeUtils.setBadgeNum(mBadgeV, data.badge, BadgeUtils.NORM_MIN, BadgeUtils.NORM_MAX, true, BadgeUtils.NORM_WIDTH, BadgeUtils.NORM_PADDING);

        // 设置箭头颜色
        int colorResId = data.arrowColorResId;
        if (colorResId > 0) {
            DrawableUtils.setColor(mArrowIV.getDrawable(), colorResId);
        }

        // 设置箭头
        if (data.needArrow) {
            mArrowIV.setVisibility(View.VISIBLE);
            if (mNeedSwitchArrow) {
                mArrowIV.setImageResource(flag ? R.drawable.bd_ic_expand_less : R.drawable.bd_ic_expand_more);
            }
        } else {
            mArrowIV.setVisibility(View.GONE);
        }

        // 设置是否被选中
        mConvertView.setBackgroundResource(data.needSelected ? data.selBackgroundResId :
                0 == data.backgroundResId && mNeedDefBg ? R.drawable.bd_sl_bg2ck_p2trans2gray : data.backgroundResId);
    }

    /**
     * 需要切换箭头
     */
    public StdListViewItem needSwitchArrow() {
        mNeedSwitchArrow = true;
        return this;
    }

    /**
     * 需要默认背景
     */
    public StdListViewItem needDefBg() {
        mNeedDefBg = true;
        return this;
    }

}
