package com.soybeany.bdlib.basic.app.output.presenter;

import com.soybeany.bdlib.basic.app.core.component.BaseBusiness;
import com.soybeany.bdlib.basic.app.core.component.BaseFragmentManager;
import com.soybeany.bdlib.basic.app.core.component.BaseViewContainer;
import com.soybeany.bdlib.basic.app.core.component.BaseViewHolder;
import com.soybeany.bdlib.basic.app.output.entity.std.StdFragmentManager;
import com.soybeany.bdlib.basic.app.output.entity.std.StdSystemConfig;
import com.soybeany.bdlib.basic.app.output.entity.std.StdViewContainer;
import com.soybeany.bdlib.util.async.impl.dialog.SimpleProgressDialogHolder;
import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;

/**
 * 标准的活动页面
 * <br>Created by Soybeany on 2017/3/14.
 */
public abstract class StdActivity<
        Activity extends StdActivity,
        ViewHolder extends BaseViewHolder,
        Business extends BaseBusiness
        > extends SimpleActivity<
        Activity, ViewHolder, Business, StdSystemConfig> {


    // //////////////////////////////////默认实现//////////////////////////////////

    @Override
    public BaseViewContainer setupViewContainer() {
        return new StdViewContainer(mActivity);
    }

    @Override
    public IProgressDialogHolder setupDialogHolder() {
        return new SimpleProgressDialogHolder(this);
    }

    @Override
    public BaseFragmentManager setupFragmentManager() {
        return new StdFragmentManager(this, mVC.getFragmentContainerId(), this);
    }

    @Override
    protected StdSystemConfig setupSystemConfig() {
        return new StdSystemConfig();
    }

}
