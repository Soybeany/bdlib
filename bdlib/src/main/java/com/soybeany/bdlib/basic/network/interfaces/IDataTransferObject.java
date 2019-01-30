package com.soybeany.bdlib.basic.network.interfaces;

/**
 * 标准的数据传输对象需遵循的协议,用于兼容其它类型（非statusCode）的返回字段
 * <br>Created by Ben on 2016/4/29.
 */
public interface IDataTransferObject {

    /**
     * 服务器返回“值对象”时的回调
     *
     * @return 用于判断服务器是否正常返回了需要的信息
     */
    boolean isSuccess();

    /**
     * 获取状态码
     */
    int getStatusCode();

    /**
     * 获取错误信息
     */
    String getErrorMsg();

}
