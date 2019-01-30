package com.soybeany.bdlib.basic.app.core;

import android.support.v4.app.Fragment;

import com.soybeany.bdlib.basic.app.annotation.BackType;
import com.soybeany.bdlib.basic.app.core.component.BaseFragmentCreator;
import com.soybeany.bdlib.basic.app.core.component.BaseFragmentManager;
import com.soybeany.bdlib.basic.app.core.component.BaseViewContainer;
import com.soybeany.bdlib.basic.app.core.component.ToolbarInfo;
import com.soybeany.bdlib.basic.app.core.interfaces.IFragmentResultListener;
import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;

/**
 * 标准Activity需遵循的协议
 * <br>Created by Ben on 2016/5/17.
 */
public interface IActivity extends IFragmentResultListener {

    /**
     * 设置视图容器
     */
    BaseViewContainer setupViewContainer();

    /**
     * 设置FragmentManager
     */
    BaseFragmentManager setupFragmentManager();

    /**
     * 设置toolbar管理器
     */
    IProgressDialogHolder setupDialogHolder();

    /**
     * 结束该activity（间接）
     */
    void finishThisActivity(@BackType int backType);

    /**
     * 推入fragment(普通)
     */
    void pushFragment(ToolbarInfo info, BaseFragmentCreator creator);

    /**
     * 推入fragment(带回调)
     */
    void pushFragment(ToolbarInfo info, BaseFragmentCreator creator, Integer requestCode);

    /**
     * 弹出fragment（直接）
     */
    boolean popFragment();

    /**
     * 附着fragment
     */
    void attachFragment(ToolbarInfo info, Fragment fragment);

}
