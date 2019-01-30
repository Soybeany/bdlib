package com.soybeany.bdlib.basic.network.output.callback.extension.dto;


import com.soybeany.bdlib.basic.network.interfaces.IDataTransferObject;

import java.io.Serializable;

/**
 * 默认的数据传输对象实现，适用于新的项目结构
 * <br>Created by Ben on 2016/4/3.
 */
public class StdDTO implements IDataTransferObject, Serializable {

    /**
     * 状态码(200成功)
     */
    public int statusCode = -1;

    /**
     * 错误信息(当状态码不为200时，封装具体的错误信息)
     */
    public String errorMsg;

    @Override
    public boolean isSuccess() {
        return (200 == statusCode);
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getErrorMsg() {
        if (null == errorMsg) {
            errorMsg = "服务器返回值有误，请稍后再试";
        }
        return errorMsg;
    }
}
