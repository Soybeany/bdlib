package com.soybeany.bdlib.basic.network.interfaces;

import java.util.Map;

/**
 * 服务器对请求响应后返回的信息
 * <br>Created by Ben on 2016/8/31.
 */
public interface IResponse {

    /**
     * 获得响应头
     */
    Map<String, String> getHeaders();

    /**
     * 获得内容长度
     */
    long getContentLength();
}
