package com.soybeany.bdlib.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.soybeany.bdlib.util.context.BDContext;


/**
 * 手机网络工具类，获取手机网络状态
 * <br>Created by Soybeany on 2017/1/24.
 */
public class NetworkUtils {

    /**
     * 检测网络是否可用
     */
    public static boolean isAvailable() {
        return 0 != getType();
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络  1：WIFI网络  2：移动网络  3：其它网络
     */
    public static int getType() {
        int netType;
        ConnectivityManager cm = (ConnectivityManager) BDContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            return 0;
        }
        switch (networkInfo.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                netType = 1;
                break;
            case ConnectivityManager.TYPE_MOBILE:
                netType = 2;
                break;
            default:
                netType = 3;
                break;
        }
        return netType;
    }

}
