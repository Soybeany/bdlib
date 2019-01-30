package com.soybeany.bdlib.basic.app.output.entity.std;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.soybeany.bdlib.basic.app.core.component.BaseFragmentCreator;
import com.soybeany.bdlib.util.reflect.GenericUtils;

/**
 * <br>Created by Soybeany on 2017/3/18.
 */
public class StdFragmentCreator extends BaseFragmentCreator {

    private Class<? extends Fragment> mFragmentClass; // 需创建的片段界面的类
    private Bundle mBundle; // 需传递给fragment的参数集

    public StdFragmentCreator(Class<? extends Fragment> fClass) {
        this(fClass, null);
    }

    public StdFragmentCreator(Class<? extends Fragment> fClass, Bundle bundle) {
        mFragmentClass = fClass;
        mBundle = bundle;
    }

    @Override
    public Fragment getNewFragment() {
        Fragment fragment = GenericUtils.getInstance(mFragmentClass);
        if (null != mBundle) {
            Bundle bundle = fragment.getArguments();
            if (null == bundle) {
                fragment.setArguments(bundle = new Bundle());
            }
            bundle.putAll(mBundle);
        }
        return fragment;
    }

    /**
     * 获得当中的片段页面类型
     */
    public Class<? extends Fragment> getFragmentClass() {
        return mFragmentClass;
    }

    /**
     * 获得数据集
     */
    public Bundle getBundle() {
        return mBundle;
    }

}
