package com.soybeany.bdlib.basic.network.output.response;


import com.soybeany.bdlib.basic.network.interfaces.IResponse;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Response;

/**
 * 网络请求的响应，转换自OkHttp
 * <br>Created by Ben on 2016/8/31.
 */
public class BDResponse implements IResponse {

    private Response mResponse; // 对应的响应

    public BDResponse(Response response) {
        mResponse = response;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headerMap = new HashMap<>();
        Headers headers = mResponse.headers();
        for (String name : headers.names()) {
            headerMap.put(name, headers.get(name));
        }
        return headerMap;
    }

    @Override
    public long getContentLength() {
        return mResponse.body().contentLength();
    }
}
