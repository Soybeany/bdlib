package com.soybeany.bdlib.basic.app.output.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.basic.app.annotation.BackType;
import com.soybeany.bdlib.basic.app.annotation.ResultCodeType;
import com.soybeany.bdlib.basic.app.core.IActivity;
import com.soybeany.bdlib.basic.app.core.ICoreX;
import com.soybeany.bdlib.basic.app.core.IFragment;
import com.soybeany.bdlib.basic.app.core.component.BaseBusiness;
import com.soybeany.bdlib.basic.app.core.component.BaseFragmentCreator;
import com.soybeany.bdlib.basic.app.core.component.BaseFragmentManager;
import com.soybeany.bdlib.basic.app.core.component.BaseToolbarManager;
import com.soybeany.bdlib.basic.app.core.component.BaseViewContainer;
import com.soybeany.bdlib.basic.app.core.component.BaseViewHolder;
import com.soybeany.bdlib.basic.app.core.component.ToolbarInfo;
import com.soybeany.bdlib.basic.app.core.interfaces.IToolbarOwner;
import com.soybeany.bdlib.basic.app.output.entity.std.StdSystemConfig;
import com.soybeany.bdlib.basic.app.util.GlobalInfoTransferUtils;
import com.soybeany.bdlib.util.async.AsyncUtils;
import com.soybeany.bdlib.util.async.impl.option.TimerAsyncOption;
import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;
import com.soybeany.bdlib.util.display.FragmentOperationUtils;
import com.soybeany.bdlib.util.display.ToastUtils;
import com.soybeany.bdlib.util.system.KeyboardUtils;
import com.soybeany.bdlib.util.system.PermissionRequester;
import com.soybeany.bdlib.util.system.SystemSettingUtils;

import static com.soybeany.bdlib.basic.app.annotation.BackType.BACK_ITEM;
import static com.soybeany.bdlib.basic.app.annotation.BackType.BACK_KEY;
import static com.soybeany.bdlib.basic.app.core.component.ToolbarInfo.IItemListener;

/**
 * 基本的活动界面，MVP模式中的P层
 * <br>内置对象：1.mTag; 2.mActivity; 3.mVC; 4.mVH; 5.mBS
 * <br>Created by Soybeany on 2017/3/13.
 */
public abstract class BaseActivity<
        Activity extends BaseActivity,
        ViewHolder extends BaseViewHolder,
        Business extends BaseBusiness,
        SystemConfig extends StdSystemConfig
        > extends AppCompatActivity implements
        ICoreX<Intent, ViewHolder, Business>, IActivity, BaseFragmentManager.IOptionCallback {


    // //////////////////////////////////成员变量//////////////////////////////////

    /**
     * 类标识，主要用于打印调试信息
     */
    protected String mTag;

    /**
     * 所在的活动页面
     */
    protected Activity mActivity;

    /**
     * 视图容器
     */
    protected BaseViewContainer<ToolbarInfo> mVC;

    /**
     * 视图持有器
     */
    protected ViewHolder mVH;

    /**
     * 业务逻辑
     */
    protected Business mBS;

    private CoreHelper mCoreHelper; // 核心辅助类
    private BaseFragmentManager mFragmentManager; // 片段界面管理器

    private boolean mIsAttachFragment; // 是否粘附片段页面


    // //////////////////////////////////官方重写//////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 初始化系统配置
        SystemConfig config = setupSystemConfig();
        initConfig(config);
        applyConfig(config);
        super.onCreate(savedInstanceState);
        // 初始化
        init();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 若是重新创建的界面，则清空片段栈
        if (null != savedInstanceState) {
            while (FragmentOperationUtils.popFragment(mActivity)) ;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mCoreHelper.onCreateOptionsMenu(getCurToolbarInfo(), menu) || super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mCoreHelper.onOptionsItemSelected(getCurToolbarInfo(), item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking() && !event.isCanceled()) {
            Fragment fragment = mFragmentManager.getTopFragment();

            // 如果不为空表明有栈中有fragment，需要按其配置进行设置，否则按照activity的配置进行设置
            if (null != fragment) {
                // 如果不为空，且实现了IFragment，则回调询问函数，否则不作处理
                if (fragment instanceof IFragment) {
                    ((IFragment) fragment).popThisFragment(BACK_KEY);
                } else {
                    return super.onKeyUp(keyCode, event);
                }
            } else {
                finishThisActivity(BACK_KEY);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        // 检测权限
        mCoreHelper.requestEPermissions();
        // 调用业务
        mCoreHelper.doBusiness(mBS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 刷新回调
        mCoreHelper.refresh();
    }

    @Override
    protected void onDestroy() {
        mCoreHelper.onDestroyed();
        super.onDestroy();
    }

    @Override
    public boolean isDestroyed() {
        return mCoreHelper.isDestroyed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mCoreHelper.checkPermissionResults(requestCode, permissions, grantResults);
    }


    // //////////////////////////////////IIdentifier重写//////////////////////////////////

    @Override
    public Activity getActivity() {
        return mActivity;
    }

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
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    @Override
    public void onPermissionPass() {
        // 子类按需实现
    }

    @Override
    public void onPermissionDeny() {
        finish();
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
    public void onInitData(Intent intent) {
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
        mCoreHelper.updateToolbar(getCurToolbarInfo());
    }


    // //////////////////////////////////IActivity重写//////////////////////////////////

    @Override
    public void finishThisActivity(@BackType int backType) {
        if (shouldDestroy(backType)) {
            finish();
        }
    }

    @Override
    public void pushFragment(ToolbarInfo info, BaseFragmentCreator creator) {
        pushFragment(info, creator, null);
    }

    @Override
    public void pushFragment(final ToolbarInfo info, final BaseFragmentCreator creator, final Integer requestCode) {
        AsyncUtils.timer(100, true, new TimerAsyncOption() {
            @Override
            protected void onTimeUp() {
                mFragmentManager.pushFragment(creator.getNewFragment(), info, requestCode);
            }
        }); // 适当减缓页面的切换速度，使某些页面动画正常播放完
    }

    @Override
    public boolean popFragment() {
        return mFragmentManager.popFragment();
    }

    @Override
    public void attachFragment(ToolbarInfo info, Fragment fragment) {
        mFragmentManager.attachFragment(info, fragment);
        mVC.getContainerV().removeView(mVC.getContentV());
        mIsAttachFragment = true;
    }

    @Override
    public void onFragmentResult(int requestCode, @ResultCodeType int resultCode, Bundle bundle) {
        // 子类按需实现
    }


    // //////////////////////////////////IOptionCallback重写//////////////////////////////////

    @Override
    public void onPush(boolean isEmpty) {
        // 设置activity内容的隐藏
        if (isEmpty) {
            mVC.getContentV().setVisibility(View.GONE);
        }
    }

    @Override
    public void onPop(boolean isEmpty) {
        // 设置activity内容的显示
        if (isEmpty && !mIsAttachFragment) {
            mVC.getContentV().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterPushOrPop(boolean needToUpdate) {
        // 设置工具栏
        if (needToUpdate && !mIsAttachFragment) {
            updateToolbar();
        }

        // 回收键盘
        KeyboardUtils.closeKeyboard(this);
    }


    // //////////////////////////////////子类方法//////////////////////////////////

    /**
     * 初始化系统配置
     */
    protected void initConfig(SystemConfig config) {
        // 默认不需要实现
    }

    /**
     * 应用所设置的系统配置(若使用了拓展的SystemConfig，可重写此方法拓展设置新增的配置)
     */
    protected void applyConfig(SystemConfig config) {
        // 配置状态栏
        if (!config.needStatusBar) {
            SystemSettingUtils.hideStatusBar(this);
        }

        // 配置操作菜单悬浮
        if (config.needOptionMenuOverlay) {
            SystemSettingUtils.setOptionMenuOverlay(this);
        }
    }

    /**
     * 从intent中读取工具栏信息
     */
    protected ToolbarInfo loadToolbarInfoFromIntent() {
        return GlobalInfoTransferUtils.loadToolbarInfo(getIntent());
    }


    // //////////////////////////////////共享变量//////////////////////////////////

    /**
     * 获得新的工具栏信息
     */
    ToolbarInfo getNewToolbarInfo() {
        return mVC.getNewToolbarInfo();
    }

    /**
     * 获得工具栏管理器
     */
    BaseToolbarManager getToolbarManager() {
        return mVC.getToolbarManager();
    }


    // //////////////////////////////////内部实现//////////////////////////////////

    /**
     * 初始化
     */
    @SuppressWarnings("unchecked")
    private void init() {
        // 读取传输信息
        onInitData(getIntent());
        // 初始化内置对象
        mTag = getClass().getSimpleName(); // 设置标识值
        mActivity = (Activity) this; // 设置此页面
        mVC = setupViewContainer(); // 设置视图容器
        mVH = setupViewHolder(); // 设置视图持有器
        mBS = setupBusiness(); // 设置业务逻辑
        // 初始化其它对象
        mCoreHelper = new CoreHelper(this, getNewToolbarInfo(), getToolbarManager(), setupDialogHolder()); // 设置核心辅助器
        mFragmentManager = setupFragmentManager(); // 设置片段界面管理器
        // 初始化其它信息
        mCoreHelper.initToolbarInfo(loadToolbarInfoFromIntent(), new IItemListener() {
            @Override
            public void itemOnClick(int index, ToolbarInfo.MenuItemInfo info) {
                finishThisActivity(BACK_ITEM);
            }
        }); // 初始工具栏信息
        setupRootView(); // 设置根视图
    }

    /**
     * 设置根视图
     */
    private void setupRootView() {
        int id = setupLayoutResId();
        if (0 == id) {
            return;
        }
        // 渲染视图
        ViewGroup contentV = mVC.getContentV();
        View rootView = getLayoutInflater().inflate(id, contentV, false);
        contentV.addView(rootView);
        // 回调视图持有器
        if (null != mVH) {
            mVH.setContext(mActivity);
            mVH.bindViews(rootView);
            mVH.initViews();
        }
    }

    /**
     * 获取当前的工具栏信息(fragment上的或者自身)
     */
    private ToolbarInfo getCurToolbarInfo() {
        ToolbarInfo info = null;
        Fragment fragment;

        if (null != (fragment = mFragmentManager.getTopFragment()) && fragment instanceof IToolbarOwner) {
            info = ((IToolbarOwner) fragment).getToolbarInfo();
        }

        return null != info ? info : getToolbarInfo();
    }


    // //////////////////////////////////子类配置//////////////////////////////////

    /**
     * 设置具体使用的系统配置
     */
    protected abstract SystemConfig setupSystemConfig();

}
