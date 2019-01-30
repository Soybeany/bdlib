package com.soybeany.bdlib.util.display;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.soybeany.bdlib.util.system.KeyboardUtils;


/**
 * 负责实现Fragment具体操作的工具类
 * <br>Created by Ben on 2016/4/8.
 */
public class FragmentOperationUtils {

    private FragmentOperationUtils() {

    }


    // //////////////////////////////////单页面界面//////////////////////////////////

    /**
     * 推入fragment
     *
     * @param newFragment 建议每次调用都new一个新的fragment，避免fragment状态值残留（如listView的状态信息）
     */
    public static void pushFragment(FragmentActivity activity, Fragment oldFragment, Fragment newFragment, int containerViewID, boolean needAddToBackStack) {
        handleFragment(activity, oldFragment, newFragment, containerViewID, needAddToBackStack);
    }

    /**
     * 弹出fragment
     */
    public static boolean popFragment(FragmentActivity activity) {
        return activity.getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * 在管理器中替换fragment
     */
    public static void replaceFragment(FragmentActivity activity, final int containerViewID, final Fragment fragment) {
        exeTransaction(activity, new IProcessor() {
            @Override
            public void onProcess(FragmentTransaction transaction) {
                transaction.replace(containerViewID, fragment);
            }
        });
    }


    // //////////////////////////////////多页面界面//////////////////////////////////

    /**
     * 在管理器中移除fragments
     */
    public static void removeFragments(FragmentActivity activity, final Fragment... fragments) {
        exeTransaction(activity, new IProcessor() {
            @Override
            public void onProcess(FragmentTransaction transaction) {
                for (Fragment fragment : fragments) {
                    transaction.remove(fragment);
                }
            }
        });
    }

    /**
     * 在管理器中添加fragments（在调用switchFragment前必须调用）
     */
    public static void addFragments(FragmentActivity activity, final int containerViewID, final Fragment... fragments) {
        exeTransaction(activity, new IProcessor() {
            @Override
            public void onProcess(FragmentTransaction transaction) {
                for (Fragment fragment : fragments) {
                    transaction.add(containerViewID, fragment);
                }
            }
        });
    }

    /**
     * 隐藏fragment
     */
    public static void hideFragments(FragmentActivity activity, final Fragment... fragments) {
        exeTransaction(activity, new IProcessor() {
            @Override
            public void onProcess(FragmentTransaction transaction) {
                for (Fragment fragment : fragments) {
                    transaction.hide(fragment);
                }
            }
        });
    }

    /**
     * 切换fragment
     */
    public static void switchFragment(FragmentActivity activity, Fragment oldFragment, Fragment newFragment) {
        handleFragment(activity, oldFragment, newFragment, null, false);
        KeyboardUtils.closeKeyboard(activity);
    }

    /**
     * 处理fragment
     *
     * @return 是否进行了处理
     */
    private static boolean handleFragment(FragmentActivity activity, final Fragment oldFragment, final Fragment newFragment, final Integer containerViewID, final boolean needAddToBackStack) {
        if (null == activity || null == newFragment) {
            return false;
        }
        exeTransaction(activity, new IProcessor() {
            @Override
            public void onProcess(FragmentTransaction transaction) {
                // 隐藏旧fragment
                if (null != oldFragment) {
                    transaction.hide(oldFragment);
                }
                // 按需对新fragment进行处理
                if (null != containerViewID) {
                    transaction.add(containerViewID, newFragment);
                } else {
                    transaction.show(newFragment);
                }
                // 按需将此事务添加到返回栈
                if (needAddToBackStack) {
                    transaction.addToBackStack(null);
                }
            }
        });
        return true;
    }

    /**
     * 执行事务
     */
    private static void exeTransaction(FragmentActivity activity, IProcessor processor) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        processor.onProcess(transaction);
        transaction.commit();
    }

    /**
     * 处理者
     */
    private interface IProcessor {
        /**
         * 处理事务时的回调
         */
        void onProcess(FragmentTransaction transaction);
    }

}
