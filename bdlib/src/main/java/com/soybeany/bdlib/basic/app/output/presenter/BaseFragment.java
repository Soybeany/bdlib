package com.soybeany.bdlib.basic.app.output.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.soybeany.bdlib.basic.app.annotation.BackType;
import com.soybeany.bdlib.basic.app.annotation.ResultCodeType;
import com.soybeany.bdlib.basic.app.core.ICoreX;
import com.soybeany.bdlib.basic.app.core.IFragment;
import com.soybeany.bdlib.basic.app.core.component.BaseBusiness;
import com.soybeany.bdlib.basic.app.core.component.BaseViewHolder;
import com.soybeany.bdlib.basic.app.core.component.ToolbarInfo;
import com.soybeany.bdlib.basic.app.util.GlobalInfoTransferUtils;
import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;
import com.soybeany.bdlib.util.display.FragmentOperationUtils;
import com.soybeany.bdlib.util.display.ToastUtils;
import com.soybeany.bdlib.util.display.ViewTransferUtils;
import com.soybeany.bdlib.util.system.PermissionRequester;

import static com.soybeany.bdlib.basic.app.annotation.BackType.BACK_ITEM;
import static com.soybeany.bdlib.basic.app.annotation.ResultCodeType.RESULT_CANCELED;

/**
 * 基本的片段界面，MVP模式中的P层
 * <br>内置对象：1.mTag; 2.mActivity; 3.mVH; 4.mBS
 * <br>Created by Soybeany on 2017/3/13.
 */
public abstract class BaseFragment<
        Activity extends BaseActivity,
        ViewHolder extends BaseViewHolder,
        Business extends BaseBusiness
        > extends Fragment implements
        ICoreX<Bundle, ViewHolder, Business>, IFragment {


    // //////////////////////////////////成员变量//////////////////////////////////

    /**
     * 类标识，主要用于打印调试信息
     */
    protected String mTag;

    /**
     * 所依附的活动页面
     */
    protected Activity mActivity;

    /**
     * 视图持有器
     */
    protected ViewHolder mVH;

    /**
     * 业务逻辑
     */
    protected Business mBS;

    private CoreHelper mCoreHelper; // 核心辅助类
    private View mRootView; // 根视图

    private int mResultCode = RESULT_CANCELED; // 用于记录结果码
    private boolean mIsRecreated; // 记录是否为重建的页面

    public BaseFragment() {
        setArguments(new Bundle());
    }


    // //////////////////////////////////官方重写//////////////////////////////////

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 获取所依附的页面
        mActivity = (Activity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 若是重新创建，则自我移除
        if (null != savedInstanceState) {
            mIsRecreated = true;
            FragmentOperationUtils.removeFragments(mActivity, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup realRootView = null;
        if (!mIsRecreated) {
            int resId = setupLayoutResId();
            if (0 != resId) {
                mRootView = inflater.inflate(resId, container, false);
                // 转移根视图
                ViewTransferUtils.transfer(mRootView, realRootView = new FrameLayout(mActivity), 0);
            }
        }
        return realRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mIsRecreated) {
            return;
        }
        init();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mIsRecreated) {
            return;
        }
        if (!hidden) { // 若显示
            // 更新工具栏
            updateToolbar();
            // 刷新回调
            mCoreHelper.refresh();
        }
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        if (mIsRecreated) {
            return;
        }
        // 检测权限
        mCoreHelper.requestEPermissions();
        // 调用业务
        mCoreHelper.doBusiness(mBS);
    }

    @Override
    public void onDestroy() {
        if (!mIsRecreated) {
            mCoreHelper.onDestroyed();
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mCoreHelper.checkPermissionResults(requestCode, permissions, grantResults);
    }


    // //////////////////////////////////IIdentifier重写//////////////////////////////////

    @Override
    public String getClassTag() {
        return mTag;
    }


    // //////////////////////////////////IRefreshable重写//////////////////////////////////

    @Override
    public void requireToRefresh(boolean isNow) {
        mCoreHelper.requireToRefresh(isNow);
    }

    @Override
    public void onRefresh() {
        // 默认不需要实现
    }


    // //////////////////////////////////IDestroyListener重写//////////////////////////////////

    @Override
    public boolean shouldDestroy(@BackType int backType) {
        return true;
    }

    @Override
    public boolean isDestroyed() {
        return mCoreHelper.isDestroyed();
    }


    // //////////////////////////////////IToastHolder重写//////////////////////////////////

    @Override
    public void showToast(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void cancelToast() {
        ToastUtils.cancel();
    }


    // //////////////////////////////////IPermissionChecker重写//////////////////////////////////

    @Override
    public String[] checkEPermissions() {
        return null;
    }

    @Override
    public boolean checkOPermissions(PermissionRequester.IPermissionCallback callback, String... permissions) {
        return mCoreHelper.requestOPermissions(callback, permissions);
    }

    @Override
    public void onRequestPermissions(android.app.Activity activity, String[] permissions, int requestCode) {
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void onPermissionPass() {
        // 子类按需实现
    }

    @Override
    public void onPermissionDeny() {
        mActivity.popFragment();
    }


    // //////////////////////////////////IProgressDialogKeeper重写//////////////////////////////////

    @Override
    public IProgressDialogHolder getProgressDialogHolder() {
        return mCoreHelper.getProgressDialogHolder();
    }

    @Override
    public boolean showProgressDialog(String msg) {
        return mCoreHelper.showProgressDialog(msg);
    }

    @Override
    public boolean hideProgressDialog() {
        return mCoreHelper.hideProgressDialog();
    }

    @Override
    public void clearProgressDialog() {
        mCoreHelper.clearProgressDialog();
    }


    // //////////////////////////////////ITemplateX重写//////////////////////////////////

    @Override
    public void onInitData(Bundle bundle) {
        // 子类按需实现
    }

    @Override
    public Business setupBusiness() {
        return null;
    }

    @Override
    public boolean needDoBusiness() {
        return mCoreHelper.needDoBusiness();
    }

    @Override
    public void doBusiness() {
        // 默认不需要实现
    }


    // //////////////////////////////////IToolbarOwner重写//////////////////////////////////

    @Override
    public ToolbarInfo getToolbarInfo() {
        return mCoreHelper.getToolbarInfo();
    }

    @Override
    public Toolbar getToolbar() {
        return mCoreHelper.getToolbar();
    }

    @Override
    public boolean initToolbar(ToolbarInfo info) {
        return true;
    }

    @Override
    public void updateToolbar() {
        mCoreHelper.updateToolbar(getToolbarInfo());
    }


    // //////////////////////////////////IFragment重写//////////////////////////////////

    @Override
    public Bundle getDataBundle() {
        return getArguments();
    }

    @Override
    public int getResultCode() {
        return mResultCode;
    }

    @Override
    public void setResultCode(@ResultCodeType int resultCode) {
        mResultCode = resultCode;
    }

    @Override
    public void popThisFragment(@BackType int backType) {
        if (shouldDestroy(backType) && !mActivity.popFragment()) {
            mActivity.finishThisActivity(backType);
        }
    }

    @Override
    public void onFragmentResult(int requestCode, @ResultCodeType int resultCode, Bundle bundle) {
        // 子类按需实现
    }


    // //////////////////////////////////子类方法//////////////////////////////////

    /**
     * 从bundle中读取工具栏信息
     */
    protected ToolbarInfo loadToolbarInfoFromBundle() {
        return GlobalInfoTransferUtils.loadToolbarInfo(this);
    }


    // //////////////////////////////////内部实现//////////////////////////////////

    /**
     * 初始化
     */
    @SuppressWarnings("unchecked")
    private void init() {
        // 读取传输信息
        onInitData(getDataBundle());
        // 初始化内置对象
        mTag = getClass().getSimpleName(); // 设置标识值
        mVH = setupViewHolder(); // 设置视图持有器
        mBS = setupBusiness(); // 设置业务逻辑
        // 初始化其它对象
        mCoreHelper = new CoreHelper(this, mActivity.getNewToolbarInfo(), mActivity.getToolbarManager(),
                mActivity.getProgressDialogHolder()); // 设置核心辅助器
        // 初始化其它信息
        mCoreHelper.initToolbarInfo(loadToolbarInfoFromBundle(), new ToolbarInfo.IItemListener() {
            @Override
            public void itemOnClick(int index, ToolbarInfo.MenuItemInfo info) {
                popThisFragment(BACK_ITEM);
            }
        }); // 初始工具栏信息
        setupVH(); // 设置视图持有器
    }

    /**
     * 设置视图持有器
     */
    private void setupVH() {
        if (null != mVH) {
            mVH.setContext(mActivity);
            mVH.bindViews(mRootView);
            mVH.initViews();
        }
    }

}
