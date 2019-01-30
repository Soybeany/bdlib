package com.soybeany.bdlib.util.display;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * 警告弹窗工具类，包含信息提示与询问弹窗
 * <br>Created by Soybeany on 16/3/2.
 */
public class AlertDialogUtils {

    /**
     * 温馨提示
     */
    public static final String STD_TITLE = "温馨提示";

    /**
     * 确定
     */
    public static final String STD_POSITIVE = "确定";

    /**
     * 取消
     */
    public static final String STD_NEGATIVE = "取消";


    private AlertDialogUtils() {

    }


    // //////////////////////////////////提示弹窗//////////////////////////////////

    /**
     * 显示提示内容的弹窗
     */
    public static AlertDialog showInfoDialog(Context context, String title, String message, DialogInterface.OnDismissListener listener) {
        return showInfoDialog(context, title, message, null, listener);
    }

    /**
     * 显示提示内容的弹窗
     */
    public static AlertDialog showInfoDialog(Context context, String title, String message, View view, DialogInterface.OnDismissListener listener) {
        AlertDialog dialog = InitInfoDialog(context, title, message, view, listener);
        dialog.show();
        return dialog;
    }

    /**
     * 初始化提示内容的弹窗
     */
    public static AlertDialog InitInfoDialog(Context context, String title, String message, View view, DialogInterface.OnDismissListener listener) {
        return initAlertDialog(context, title, message, view, false, null, null, listener);
    }


    // //////////////////////////////////询问弹窗//////////////////////////////////

    /**
     * 显示询问内容的弹窗
     */
    public static AlertDialog showQueryDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        return showQueryDialog(context, title, message, null, listener, null);
    }

    /**
     * 显示询问内容的弹窗
     */
    public static AlertDialog showQueryDialog(Context context, String title, String message, View view, DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener cancelListener) {
        AlertDialog dialog = InitQueryDialog(context, title, message, view, listener, cancelListener);
        dialog.show();
        return dialog;
    }

    /**
     * 初始化询问内容的弹窗
     */
    public static AlertDialog InitQueryDialog(Context context, String title, String message, View view, DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener cancelListener) {
        return initAlertDialog(context, title, message, view, true, listener, cancelListener, null);
    }


    // //////////////////////////////////弹窗操作//////////////////////////////////

    /**
     * 显示弹窗
     */
    public static void showDialog(AlertDialog dialog) {
        dialog.show();
    }

    /**
     * 隐藏弹窗
     */
    public static void dismissDialog(AlertDialog dialog) {
        dialog.dismiss();
    }


    // //////////////////////////////////内部类//////////////////////////////////

    /**
     * 初始化弹窗
     */
    private static AlertDialog initAlertDialog(Context context, String title, String message, View view, boolean needNegativeBtn, DialogInterface.OnClickListener listener, final DialogInterface.OnCancelListener cancelListener, DialogInterface.OnDismissListener dismissListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(view);
        builder.setMessage(message);
        if (needNegativeBtn) {
            builder.setNegativeButton(STD_NEGATIVE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null != cancelListener) {
                        cancelListener.onCancel(dialog); // 点击按钮取消
                    }
                }
            });
        }
        builder.setPositiveButton(STD_POSITIVE, listener);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setOnCancelListener(cancelListener); // 点击屏幕取消
        alertDialog.setOnDismissListener(dismissListener);

        return alertDialog;
    }

}
