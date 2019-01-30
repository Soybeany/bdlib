package com.soybeany.bdlib.util.system;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;

import com.soybeany.bdlib.util.display.AlertDialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限请求者
 * <br>Created by Soybeany on 2017/3/23.
 */
public class PermissionRequester {

    // //////////////////////////////////常用权限//////////////////////////////////

    /**
     * 读取手机状态
     */
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;

    /**
     * 读取外部存储
     */
    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    /**
     * 写入外部存储
     */
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    // //////////////////////////////////内部变量//////////////////////////////////

    private static final String MSG = "当前应用缺少必要权限，是否前往设置?"; // 权限提示，请点击"设置"-"权限"-打开所需权限

    private static final int DEFAULT_REQUEST_CODE = 10000; // 必要权限的请求码
    private static int AUTO_REQUEST_CODE; // 自动增长的请求码

    private Activity mActivity; // 活动页面
    private AlertDialog mDialog; // 弹窗
    private IPermissionDealer mDealer; // 权限处理者

    private final SparseArray<IPermissionCallback> mCallbackMap = new SparseArray<>(); // 回调映射
    private final List<DenyInfo> mDenyList = new ArrayList<>(); // 拒绝信息的列表

    private boolean mIsInitial = true; // 是否为初始状态(还未回调过的状态)

    /**
     * 获得自增长请求码
     */
    private static int getAutoRequestCode() {
        return AUTO_REQUEST_CODE++ % DEFAULT_REQUEST_CODE;
    }

    public PermissionRequester(Activity activity, IPermissionDealer dealer) {
        mActivity = activity;
        mDealer = dealer;
        initDialog();
    }

    /**
     * 请求指定的必要权限(重复调用将覆盖callback)
     * <br>
     * 常在界面的{@link Activity#onStart()}中调用
     */
    public void requestEPermissions(IPermissionCallback callback, String... permissions) {
        requestPermissions(DEFAULT_REQUEST_CODE, callback, permissions);
    }

    /**
     * 请求指定的可选权限(可在需要的地方动态调用)
     */
    public boolean requestOPermissions(IPermissionCallback callback, String... permissions) {
        return requestPermissions(getAutoRequestCode(), callback, permissions);
    }

    /**
     * 检测请求结果
     * 在界面的{@link Activity#onRequestPermissionsResult(int, String[], int[])}中调用
     */
    public void checkResults(int requestCode, String[] permissions, int[] grantResults) {
        // 检测是否此类发出的请求
        IPermissionCallback callback = mCallbackMap.get(requestCode);
        if (null == callback) {
            return;
        }
        mCallbackMap.remove(requestCode);

        // 检测是否全部权限均被允许
        boolean isAllGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false;
                break;
            }
        }

        // 权限全部被允许时，调用权限通过的回调，否则显示设置弹窗
        if (isAllGranted) {
            callback.onPermissionPass();
        } else {
            mDenyList.add(new DenyInfo(requestCode, callback, permissions));
            if (!mDialog.isShowing()) {
                AlertDialogUtils.showDialog(mDialog);
            }
        }
    }

    /**
     * 请求指定权限
     *
     * @return 是否已获得权限
     */
    private boolean requestPermissions(int requestCode, IPermissionCallback callback, String... permissions) {
        List<String> requestList = getRequestList(permissions);
        if (!requestList.isEmpty()) {
            mCallbackMap.put(requestCode, callback);
            mDealer.onRequestPermissions(mActivity, requestList.toArray(new String[requestList.size()]), requestCode);
            return false;
        } else if (DEFAULT_REQUEST_CODE == requestCode && mIsInitial) {
            callback.onPermissionPass();
            mIsInitial = false;
        }
        return true;
    }

    /**
     * 获取需要申请权限的列表
     */
    @NonNull
    private List<String> getRequestList(@Nullable String[] permissions) {
        List<String> deniedList = new ArrayList<>();
        if (null != permissions) {
            for (String permission : permissions) {
                // 判断是否app已经获取到某一个权限 或者 是否显示申请权限对话框，如果同意了或者不再询问则返回false
                if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                    deniedList.add(permission);
                }
            }
        }
        return deniedList;
    }

    /**
     * 初始化弹窗
     */
    private void initDialog() {
        mDialog = AlertDialogUtils.InitQueryDialog(mActivity, AlertDialogUtils.STD_TITLE, MSG, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        }, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                for (DenyInfo info : mDenyList) {
                    if (getRequestList(info.permissions).isEmpty()) {
                        info.callback.onPermissionPass();
                    } else {
                        info.callback.onPermissionDeny();
                    }
                }
                mDenyList.clear();
            }
        });
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
        mActivity.startActivity(intent);
    }

    /**
     * 权限处理者
     */
    public interface IPermissionDealer {
        /**
         * 需要申请权限时的回调
         */
        void onRequestPermissions(Activity activity, String[] permissions, int requestCode);
    }

    /**
     * 权限请求的回调接口
     */
    public interface IPermissionCallback {
        /**
         * 权限请求全部通过时的回调
         */
        void onPermissionPass();

        /**
         * 权限请求被拒绝时的回调
         */
        void onPermissionDeny();
    }

    /**
     * 权限拒绝的信息
     */
    private static class DenyInfo {
        int requestCode;
        IPermissionCallback callback;
        String[] permissions;

        DenyInfo(int requestCode, IPermissionCallback callback, String[] permissions) {
            this.requestCode = requestCode;
            this.callback = callback;
            this.permissions = permissions;
        }
    }

}
