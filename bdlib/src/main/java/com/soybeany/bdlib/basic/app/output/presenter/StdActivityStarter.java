package com.soybeany.bdlib.basic.app.output.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.soybeany.bdlib.basic.app.annotation.ResultCodeType;
import com.soybeany.bdlib.basic.app.core.IFragment;
import com.soybeany.bdlib.basic.app.core.component.BaseViewHolder;
import com.soybeany.bdlib.basic.app.core.component.ToolbarInfo;
import com.soybeany.bdlib.basic.app.output.entity.std.StdFragmentCreator;
import com.soybeany.bdlib.basic.app.util.GlobalInfoTransferUtils;

/**
 * 标准的活动页面启动器，使用fragment创建activity
 * <br>Created by Soybeany on 2017/5/2.
 */
public class StdActivityStarter {

    private static String KEY_FRAGMENT_CLASS = "key_fragment_class"; // 片段页面类型键
    private static String KEY_BUNDLE = "key_bundle"; // 数据集键

    private Fragment mFragment; // 粘附的片段

    /**
     * 开始一个新的活动界面(普通)
     */
    public static void startActivity(Activity activity, Class desActivity, ToolbarInfo info, StdFragmentCreator creator) {
        activity.startActivity(getItemActivityIntent(activity, desActivity, info, creator));
    }

    /**
     * 开始一个新的列表项活动界面(普通)
     */
    public static void startActivity(Fragment fragment, Class desActivity, ToolbarInfo info, StdFragmentCreator creator) {
        fragment.startActivity(getItemActivityIntent(fragment.getActivity(), desActivity, info, creator));
    }

    /**
     * 开始一个新的活动界面(带回调)
     */
    public static void startActivityForResult(Activity activity, Class desActivity, ToolbarInfo info, StdFragmentCreator creator, int requestCode) {
        activity.startActivityForResult(getItemActivityIntent(activity, desActivity, info, creator), requestCode);
    }

    /**
     * 开始一个新的列表项活动界面(带回调)
     */
    public static void startActivityForResult(Fragment fragment, Class desActivity, ToolbarInfo info, StdFragmentCreator creator, int requestCode) {
        fragment.startActivityForResult(getItemActivityIntent(fragment.getActivity(), desActivity, info, creator), requestCode);
    }

    /**
     * 获得启动列表项活动视图的意图
     */
    private static Intent getItemActivityIntent(Context context, Class desActivity, ToolbarInfo info, StdFragmentCreator creator) {
        Intent intent = new Intent(context, desActivity);
        intent.putExtra(KEY_FRAGMENT_CLASS, creator.getFragmentClass());
        intent.putExtra(KEY_BUNDLE, creator.getBundle());
        GlobalInfoTransferUtils.saveToolbarInfo(intent, info);
        return intent;
    }


    // //////////////////////////////////便捷设置//////////////////////////////////


    /**
     * 不需要设置布局
     */
    public int setupLayoutResId() {
        return 0;
    }

    /**
     * 不需要设置视图持有器
     */
    public BaseViewHolder setupViewHolder() {
        return null;
    }

    /**
     * 粘贴片段页面
     */
    @SuppressWarnings("unchecked")
    public void doBusiness(BaseActivity activity) {
        Intent intent = activity.getIntent();
        ToolbarInfo info = GlobalInfoTransferUtils.loadToolbarInfo(intent);
        StdFragmentCreator creator = new StdFragmentCreator((Class<? extends Fragment>) intent.getSerializableExtra(KEY_FRAGMENT_CLASS), intent.getBundleExtra(KEY_BUNDLE));
        activity.attachFragment(info, mFragment = creator.getNewFragment());
    }

    /**
     * 片段页面结果(在Activity的相应方法中调用)
     */
    public void onFragmentResult(int requestCode, @ResultCodeType int resultCode, Bundle bundle) {
        if (mFragment instanceof IFragment) {
            ((IFragment) mFragment).onFragmentResult(requestCode, resultCode, bundle);
        }
    }
}
