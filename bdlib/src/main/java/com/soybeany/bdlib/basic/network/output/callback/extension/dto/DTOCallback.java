package com.soybeany.bdlib.basic.network.output.callback.extension.dto;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.soybeany.bdlib.basic.network.interfaces.IDataTransferObject;
import com.soybeany.bdlib.basic.network.interfaces.IRequest;
import com.soybeany.bdlib.basic.network.output.callback.basic.StringCallback;
import com.soybeany.bdlib.basic.network.output.parser.JsonParser;
import com.soybeany.bdlib.util.text.StdHintUtils;

/**
 * 默认的网络请求（数据传输对象类型）回调
 * <br>Created by Ben on 2016/6/14.
 */
public abstract class DTOCallback<DTO extends IDataTransferObject> extends StringCallback {

    /**
     * statusCode（json转换出错）
     */
    public static final int STATUS_CODE_JSON_ERROR = -2;

    /**
     * statusCode（服务器没返回任何数据）
     */
    public static final int STATUS_CODE_EMPTY_MSG = -3;

    /**
     * statusCode（取消了请求）
     */
    public static final int STATUS_CODE_CANCEL = -4;

    /**
     * statusCode（由ErrorResponse提供的其它错误）
     */
    public static final int STATUS_CODE_OTHER_ERROR = -99;

    /**
     * 服务器没返回任何数据的提示信息
     */
    public static String EMPTY_MSG_HINT = "服务器有响应但没有返回数据" + StdHintUtils.STD_RETRY_SUFFIX;

    private Class<DTO> mClass; // 需要将响应转换为的类型
    private IRequest mRequest; // 请求
    private IDataTransferObject mDTO; // 内部传输用的DTO

    public DTOCallback(Class<DTO> aClass) {
        mClass = aClass;
    }

    @Override
    public void onStart(IRequest request, Object tag) {
        mRequest = request;
    }

    @Override
    public String onPreProcess(boolean hasResponse, @Nullable String json, Object tag) {
        if (!TextUtils.isEmpty(json)) { // 服务器响应且有返回数据的情况
            try {
                mDTO = parseDTO(json, mClass, setupGson()); // 服务器返回的值对象
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                mDTO = new InnerDTO(STATUS_CODE_JSON_ERROR, mRequest.getStdHint() + StdHintUtils.STD_FAIL_SUFFIX + StdHintUtils.STD_RETRY_SUFFIX + "(json格式错误）");
            }
        } else { // 服务器响应但没有返回数据的情况
            mDTO = new InnerDTO(STATUS_CODE_EMPTY_MSG, EMPTY_MSG_HINT);
        }
        return json;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(String response, Object tag) {
        if (mDTO.isSuccess()) {
            onSuccessResponse((DTO) mDTO, tag);
        } else {
            onFailureResponse(mDTO.getStatusCode(), mDTO.getErrorMsg(), tag);
        }
    }

    @Override
    public void onErrorResponse(boolean isCancel, String errorMsg, Object tag) {
        onFailureResponse(isCancel ? STATUS_CODE_CANCEL : STATUS_CODE_OTHER_ERROR, errorMsg, tag);
    }

    @Override
    public boolean isSmooth() {
        return !(mDTO instanceof InnerDTO);
    }

    /**
     * json转换为DTO的实现
     */
    @SuppressWarnings("unchecked")
    @WorkerThread
    protected DTO parseDTO(@NonNull String json, Class<DTO> clazz, Gson gson) throws JsonSyntaxException {
        DTO dto = JsonParser.json2Dto(json, clazz, gson);
        onParseDTO(dto);
        return dto;
    }

    /**
     * 设置自定义gson
     */
    @WorkerThread
    protected Gson setupGson() {
        return JsonParser.DEFAULT_GSON;
    }

    /**
     * 数据传输对象转换时的回调
     */
    @WorkerThread
    protected void onParseDTO(DTO dto) {
        // 留空，子类按需实现
    }

    /**
     * 成功后的回调（服务器有响应）
     */
    @UiThread
    protected abstract void onSuccessResponse(DTO dto, Object tag);

    /**
     * 失败后的回调（服务器有响应）
     */
    @UiThread
    protected abstract void onFailureResponse(int statusCode, String errMsg, Object tag);

    /**
     * 内部用DTO对象(处理错误信息)
     */
    private static class InnerDTO extends StdDTO {
        InnerDTO(int code, String errMsg) {
            statusCode = code;
            errorMsg = errMsg;
        }
    }
}
