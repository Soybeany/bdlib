package com.soybeany.bdlib.basic.widget.output.nav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.soybeany.bdlib.R;

/**
 * 标准的导航栏表头
 * <br>Created by Soybeany on 2017/3/20.
 */
public class StdNavHeader {

    private View mHeaderView; // 表头视图
    private ImageView mPortraitView; // 头像视图
    private TextView mTitleView; // 标题视图
    private TextView mSubtitleView; // 副标题视图

    public StdNavHeader(Context context) {
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.bd_wdg_lv_item_nav_header, null);
        mPortraitView = (ImageView) mHeaderView.findViewById(R.id.bd_lv_item_nav_header_portrait_iv);
        mTitleView = (TextView) mHeaderView.findViewById(R.id.bd_lv_item_nav_header_title_tv);
        mSubtitleView = (TextView) mHeaderView.findViewById(R.id.bd_lv_item_nav_header_subtitle_tv);
    }

    /**
     * 设置头像
     */
    public StdNavHeader portrait(int resId) {
        mPortraitView.setImageResource(resId);
        return this;
    }

    /**
     * 设置标题
     */
    public StdNavHeader title(String text) {
        mTitleView.setText(text);
        return this;
    }

    /**
     * 设置副标题
     */
    public StdNavHeader subtitle(String text) {
        mSubtitleView.setText(text);
        return this;
    }

    /**
     * 获得表头视图
     */
    public View getHeaderView() {
        return mHeaderView;
    }

}
