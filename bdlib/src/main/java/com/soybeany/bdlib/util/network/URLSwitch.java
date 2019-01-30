package com.soybeany.bdlib.util.network;


import com.soybeany.bdlib.util.log.LogUtils;

/**
 * 开发中的调试地址与能自动切换地址的工具类
 * <br>Created by Ben on 2016/6/7.
 */
public class URLSwitch {

    /**
     * 服务器地址(Soybeany)
     */
//    public static final String SOYBENAY_URL = "http://192.168.0.103:8080/NanHang"; // (Ben家)
    public static final String SOYBENAY_URL = "http://192.168.137.225:8080/NanHang"; // (Ben公司)

    /**
     * 全部使用本地数据的标识
     */
    public static final int TYPE_ALL_LOCAL = -1;

    /**
     * 独立决定使用本地或服务器数据的标识
     */
    public static final int TYPE_INDEPENDENT = 0;

    /**
     * 全部使用服务器数据的标识
     */
    public static final int TYPE_ALL_SERVER = 1;

    private static final String TAG = URLSwitch.class.getSimpleName(); // 标识

    private static final String SERVER = "服务器"; // 服务器字符串
    private static final String LOCAL = "本地"; // 本地字符串
    private static final String WARNING = "由于%sURL没有设置，将使用%sURL“%s”进行代替"; // 警告提示

    /**
     * 当前正在使用的标识,使用{@link #TYPE_ALL_LOCAL}、{@link #TYPE_ALL_SERVER} 或 {@link #TYPE_INDEPENDENT}
     */
    public static int TYPE_IN_USE = TYPE_INDEPENDENT;

    /**
     * 获得本地或服务器URL，通过{@link #TYPE_IN_USE}设置，但一般项目中也会有相关的变量进行设置，建议使用项目中的变量
     *
     * @param localUrl  本地URL
     * @param serverUrl 服务器URL
     */
    public static String get(String localUrl, String serverUrl) {
        return get(localUrl, serverUrl, false);
    }

    /**
     * 获得本地或服务器URL，通过{@link #TYPE_IN_USE}设置，但一般项目中也会有相关的变量进行设置，建议使用项目中的变量
     *
     * @param localUrl    本地URL
     * @param serverUrl   服务器URL
     * @param preferLocal 是否倾向使用本地数据
     */
    public static String get(String localUrl, String serverUrl, boolean preferLocal) {
        switch (TYPE_IN_USE) {
            case TYPE_ALL_LOCAL:
                preferLocal = true;
                break;
            case TYPE_ALL_SERVER:
                preferLocal = false;
                break;
        }
        if (preferLocal) {
            if (localUrl != null) {
                return localUrl;
            } else {
                LogUtils.i(TAG, String.format(WARNING, LOCAL, SERVER, serverUrl));
                return serverUrl;
            }
        } else {
            if (serverUrl != null) {
                return serverUrl;
            } else {
                LogUtils.i(TAG, String.format(WARNING, SERVER, LOCAL, localUrl));
                return localUrl;
            }
        }
    }
}
