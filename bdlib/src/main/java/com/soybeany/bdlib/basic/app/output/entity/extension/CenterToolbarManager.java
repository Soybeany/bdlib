package com.soybeany.bdlib.basic.app.output.entity.extension;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.app.output.entity.std.StdToolbarManager;


/**
 * <br>Created by Ben on 2016/7/21.
 */
public class CenterToolbarManager extends StdToolbarManager {

    private TextView mTitleView; // 标题视图
    private TextView mSubtitleView; // 副标题视图

    public CenterToolbarManager(AppCompatActivity activity) {
        super(activity);
        Toolbar toolbar = getToolbar();
        activity.getLayoutInflater().inflate(R.layout.bd_app_layout_toolbar_center, toolbar);
        mTitleView = (TextView) toolbar.findViewById(R.id.bd_toolbar_title);
        mSubtitleView = (TextView) toolbar.findViewById(R.id.bd_toolbar_subtitle);
    }

    @Override
    protected void updateTitle(String title) {
        if (null != title) {
            mTitleView.setText(title);
            mTitleView.setVisibility(View.VISIBLE);

            // 设置标题显示的行数
            if (View.GONE == mSubtitleView.getVisibility()) {
                mTitleView.setMaxLines(2);
            } else {
                mTitleView.setSingleLine();
            }
        } else {
            mTitleView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void updateSubtitle(String subtitle) {
        if (null != subtitle) {
            mSubtitleView.setText(subtitle);
            mSubtitleView.setVisibility(View.VISIBLE);
        } else {
            mSubtitleView.setVisibility(View.GONE);
        }
    }

    /**
     * 获得标题视图
     */
    public TextView getTitleView() {
        return mTitleView;
    }

    /**
     * 获得副标题视图
     */
    public TextView getSubtitleView() {
        return mSubtitleView;
    }
}
