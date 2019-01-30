package com.soybeany.bdlib.basic.app.core.interfaces;

import android.os.Bundle;

import com.soybeany.bdlib.basic.app.annotation.ResultCodeType;

/**
 * 片段界面结果的监听者
 * <br>Created by Soybeany on 2017/3/14.
 */
public interface IFragmentResultListener {

    /**
     * 请求码，用于带回调的push
     */
    String BD_REQUEST_CODE = "bd_requestCode";

    /**
     * 覆盖状态,其值为true时，其根视图会拦截手势
     */
    String BD_COVER_STATE = "bd_coverState";

    /**
     * fragment的结果回调
     *
     * @param bundle 可通过fragment的getArguments()进行设置
     */
    void onFragmentResult(int requestCode, @ResultCodeType int resultCode, Bundle bundle);

}
