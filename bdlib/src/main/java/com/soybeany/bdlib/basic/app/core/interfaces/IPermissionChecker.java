package com.soybeany.bdlib.basic.app.core.interfaces;

import com.soybeany.bdlib.util.system.PermissionRequester;

/**
 * 权限检测者接口
 * <br>Created by Soybeany on 2017/3/24.
 */
public interface IPermissionChecker {

    /**
     * 检测返回值中的权限(必要权限)
     */
    String[] checkEPermissions();

    /**
     * 检测指定的权限(可选)
     *
     * @return 是否已获得指定的权限
     */
    boolean checkOPermissions(PermissionRequester.IPermissionCallback callback, String... permissions);

}
