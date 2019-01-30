package com.soybeany.bdlib.basic.app.output.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.soybeany.bdlib.basic.app.core.component.BaseBusiness;
import com.soybeany.bdlib.basic.app.core.component.BaseViewHolder;
import com.soybeany.bdlib.basic.app.core.component.ToolbarInfo;
import com.soybeany.bdlib.basic.app.output.entity.std.StdSystemConfig;
import com.soybeany.bdlib.basic.app.util.GlobalInfoTransferUtils;
import com.soybeany.bdlib.util.display.AlertDialogUtils;

/**
 * 简易的活动页面
 * <br>Created by Soybeany on 2017/3/14.
 */
public abstract class SimpleActivity<
        Activity extends SimpleActivity,
        ViewHolder extends BaseViewHolder,
        Business extends BaseBusiness,
        SystemConfig extends StdSystemConfig
        > extends BaseActivity<Activity, ViewHolder, Business, SystemConfig> {


    // //////////////////////////////////便捷功能//////////////////////////////////

    /**
     * 开始一个新的活动界面(普通)
     */
    public void startActivity(ToolbarInfo info, Intent intent) {
        GlobalInfoTransferUtils.saveToolbarInfo(intent, info);
        super.startActivity(intent);
    }

    /**
     * 开始一个新的活动界面(带回调)
     */
    public void startActivityForResult(ToolbarInfo info, Intent intent, int requestCode) {
        GlobalInfoTransferUtils.saveToolbarInfo(intent, info);
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * 获得新的工具栏信息
     */
    protected ToolbarInfo getNewToolbarInfo(String title) {
        return new ToolbarInfo(title);
    }

    /**
     * 显示提示内容的弹窗
     */
    protected AlertDialog showInfoDialog(String msg, DialogInterface.OnDismissListener listener) {
        return AlertDialogUtils.showInfoDialog(mActivity, AlertDialogUtils.STD_TITLE, msg, listener);
    }

    /**
     * 显示询问内容的弹窗
     */
    protected AlertDialog showQueryDialog(String msg, DialogInterface.OnClickListener listener) {
        return AlertDialogUtils.showQueryDialog(mActivity, AlertDialogUtils.STD_TITLE, msg, listener);
    }

}
