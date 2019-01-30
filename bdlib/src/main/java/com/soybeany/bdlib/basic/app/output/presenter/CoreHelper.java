package com.soybeany.bdlib.basic.app.output.presenter;

import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.soybeany.bdlib.basic.app.core.ICoreX;
import com.soybeany.bdlib.basic.app.core.component.BaseBusiness;
import com.soybeany.bdlib.basic.app.core.component.BaseToolbarManager;
import com.soybeany.bdlib.basic.app.core.component.ToolbarInfo;
import com.soybeany.bdlib.basic.app.core.interfaces.IProgressDialogKeeper;
import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;
import com.soybeany.bdlib.util.system.PermissionRequester;

/**
 * {@link com.soybeany.bdlib.basic.app.core.ICoreX}的辅助器，提供其的快速应用
 * <br>Created by Soybeany on 2017/12/7.
 */
class CoreHelper implements IProgressDialogKeeper {

    private ICoreX mCoreX; // 被辅助的核心
    private BaseBusiness mBS; // 业务逻辑
    private ToolbarInfo mTI; // 工具栏信息
    private BaseToolbarManager mTM; // 工具栏管理器
    private IProgressDialogHolder mDH; // 进度弹窗持有器(代理模式)

    private PermissionRequester mPR; // 权限请求者

    private boolean mHasDoneBusiness; // 记录是否已调用业务逻辑
    private boolean mNeedToRefresh; // 记录是否需要刷新界面
    private boolean mIsDestroyed; // 记录该页面是否已被销毁


    CoreHelper(ICoreX coreX, ToolbarInfo tI, BaseToolbarManager tM, IProgressDialogHolder dH) {
        mCoreX = coreX;
        mDH = dH;
        mTI = tI;
        mTM = tM;
        mPR = new PermissionRequester(coreX.getActivity(), mCoreX);
    }

    /**
     * 初始化工具栏信息
     */
    void initToolbarInfo(ToolbarInfo prototype, ToolbarInfo.IItemListener listener) {
        // 设置默认项
        mTI.copy(prototype);
        mTI.backItemListener = listener;
        // 设置自定义项
        if (mCoreX.initToolbar(mTI)) {
            mCoreX.updateToolbar();
        }
    }


    // //////////////////////////////////IProgressDialogKeeper实现//////////////////////////////////

    @Override
    public IProgressDialogHolder getProgressDialogHolder() {
        return mDH;
    }

    @Override
    public boolean showProgressDialog(String msg) {
        mDH.hint(msg);
        return mDH.showDialog();
    }

    @Override
    public boolean hideProgressDialog() {
        return mDH.hideDialog();
    }

    @Override
    public void clearProgressDialog() {
        mDH.clearDialog();
    }


    // //////////////////////////////////权限请求//////////////////////////////////

    /**
     * 请求指定的必要权限
     */
    void requestEPermissions() {
        mPR.requestEPermissions(mCoreX, mCoreX.checkEPermissions());
    }

    /**
     * 请求指定的可选权限
     */
    boolean requestOPermissions(PermissionRequester.IPermissionCallback callback, String... permissions) {
        return mPR.requestOPermissions(callback, permissions);
    }

    /**
     * 检查权限结果
     */
    void checkPermissionResults(int requestCode, String[] permissions, int[] grantResults) {
        mPR.checkResults(requestCode, permissions, grantResults);
    }


    // //////////////////////////////////Toolbar相关//////////////////////////////////

    /**
     * 获得工具栏信息
     */
    ToolbarInfo getToolbarInfo() {
        return mTI;
    }

    /**
     * 获得工具栏
     */
    Toolbar getToolbar() {
        return mTM.getToolbar();
    }

    /**
     * 创建选项菜单时的回调
     */
    boolean onCreateOptionsMenu(ToolbarInfo info, Menu menu) {
        return mTM.onCreateOptionsMenu(info, menu);
    }

    /**
     * 选择列表项时的回调
     */
    boolean onOptionsItemSelected(ToolbarInfo info, MenuItem item) {
        return mTM.onOptionsItemSelected(info, item);
    }

    /**
     * 更新工具栏
     */
    void updateToolbar(ToolbarInfo info) {
        mTM.update(info);
    }


    // //////////////////////////////////Refresh//////////////////////////////////

    /**
     * 需要刷新
     */
    void requireToRefresh(boolean isNow) {
        mNeedToRefresh = true;
        if (isNow) {
            refresh();
        }
    }

    /**
     * 调用刷新
     */
    void refresh() {
        if (mNeedToRefresh) {
            mCoreX.onRefresh();
            mNeedToRefresh = false;
        }
    }


    // //////////////////////////////////Business//////////////////////////////////

    /**
     * 是否需要运行业务逻辑，默认为true
     */
    boolean needDoBusiness() {
        return true;
    }

    /**
     * 处理业务逻辑
     */
    void doBusiness(@Nullable BaseBusiness bS) {
        if (!mHasDoneBusiness) {
            if (null != (mBS = bS)) {
                mBS.bind(mDH);
            }
            // 运行业务逻辑或结束当前页面
            if (mCoreX.needDoBusiness()) {
                mCoreX.doBusiness();
            } else {
                mCoreX.getActivity().finish();
            }
            mHasDoneBusiness = true;
        }
    }


    // //////////////////////////////////Destroy//////////////////////////////////

    /**
     * 销毁时的回调
     */
    void onDestroyed() {
        if (null != mBS) {
            mBS.unbind();
        }
        mIsDestroyed = true;
    }

    /**
     * 判断是否已销毁
     */
    boolean isDestroyed() {
        return mIsDestroyed;
    }

}
