package com.soybeany.bdlib.basic.app.output.entity.std;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.basic.app.core.IActivity;
import com.soybeany.bdlib.basic.app.core.IFragment;
import com.soybeany.bdlib.basic.app.core.component.BaseFragmentManager;
import com.soybeany.bdlib.basic.app.core.component.ToolbarInfo;
import com.soybeany.bdlib.basic.app.core.interfaces.IIdentifier;
import com.soybeany.bdlib.basic.app.util.GlobalInfoTransferUtils;
import com.soybeany.bdlib.util.display.FragmentOperationUtils;

import java.util.Stack;

/**
 * Fragment的管理类，主要进行Fragment间的切换，并在内部维护着一个Fragment信息栈
 * <br>Created by Ben on 2016/5/17.
 */
public final class StdFragmentManager extends BaseFragmentManager {

    private FragmentActivity mActivity; // 活动页面
    private int mContainerViewID; // 切换容器的id
    private IOptionCallback mCallback; // 完成推入或弹出后的回调
    private Fragment mAttachedFragment; // 贴在活动页面上的片段
    private Stack<Fragment> mFragmentInfoStack = new Stack<>(); // 用于记录由该activity推入的fragment信息

    public StdFragmentManager(IIdentifier identifier, int containerViewID, IOptionCallback callback) {
        mActivity = identifier.getActivity();
        mContainerViewID = containerViewID;
        mCallback = callback;
    }

    @Override
    public Fragment getTopFragment() {
        if (mFragmentInfoStack.isEmpty()) {
            return mAttachedFragment;
        }
        return mFragmentInfoStack.peek();
    }

    @Override
    public void pushFragment(Fragment fragment, ToolbarInfo info, Integer requestCode) {
        // 将工具栏信息写入数据集
        GlobalInfoTransferUtils.saveToolbarInfo(fragment, info);

        // 将请求码写入数据集
        GlobalInfoTransferUtils.saveRequestCode(fragment, requestCode);

        // 推入fragment
        FragmentOperationUtils.pushFragment(mActivity, getTopFragment(), fragment, mContainerViewID, true);

        // 回调压入方法
        mCallback.onPush(mFragmentInfoStack.isEmpty());
        mCallback.afterPushOrPop(false);

        // 压入管理栈
        mFragmentInfoStack.add(fragment);
    }

    @SuppressWarnings("WrongConstant")
    @Override
    public boolean popFragment() {
        // 弹出管理栈
        Fragment poppedFragment = null;
        if (!mFragmentInfoStack.isEmpty()) {
            poppedFragment = mFragmentInfoStack.pop();
        }

        // 回调弹出方法
        boolean isEmpty = mFragmentInfoStack.isEmpty();
        mCallback.onPop(isEmpty);
        mCallback.afterPushOrPop(isEmpty);

        // 弹出fragment
        boolean popSomething = FragmentOperationUtils.popFragment(mActivity);

        // 判断是否需要回调前一界面的回调方法
        if (null == poppedFragment || !(poppedFragment instanceof IFragment)) { // 若弹出的Fragment为null，或者弹出的不是IFragment
            return popSomething;
        }

        // 获得回调值
        IFragment popped = (IFragment) poppedFragment;
        Integer requestCode = GlobalInfoTransferUtils.loadRequestCode(poppedFragment);
        if (null == requestCode) {
            return popSomething;
        }
        int resultCode = popped.getResultCode();
        Bundle bundle = popped.getDataBundle();

        // 尝试回调前一界面的回调方法
        Fragment lastFragment = getTopFragment();
        if (lastFragment instanceof IFragment) { // 若前一界面为IFragment
            ((IFragment) lastFragment).onFragmentResult(requestCode, resultCode, bundle);
        } else if (mActivity instanceof IActivity) {
            ((IActivity) mActivity).onFragmentResult(requestCode, resultCode, bundle);
        }

        return popSomething;
    }

    @Override
    public void attachFragment(ToolbarInfo info, Fragment fragment) {
        GlobalInfoTransferUtils.saveToolbarInfo(fragment, info);
        FragmentOperationUtils.pushFragment(mActivity, null, mAttachedFragment = fragment, mContainerViewID, false);
    }

}
