package com.soybeany.bdlib.basic.network.output.request;


import android.support.annotation.WorkerThread;

import com.soybeany.bdlib.basic.network.interfaces.IRequest;
import com.soybeany.bdlib.basic.network.interfaces.IRequestCallback;
import com.soybeany.bdlib.basic.network.output.dialog.RequestDialogWrapper;
import com.soybeany.bdlib.basic.network.output.parser.URLParser;
import com.soybeany.bdlib.basic.network.output.response.BDResponse;
import com.soybeany.bdlib.util.async.AsyncUtils;
import com.soybeany.bdlib.util.async.impl.option.DialogAsyncOption;
import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;
import com.soybeany.bdlib.util.log.LogUtils;
import com.soybeany.bdlib.util.reflect.GenericUtils;
import com.soybeany.bdlib.util.system.TimeUtils;
import com.soybeany.bdlib.util.text.StdHintUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * 基于okhttp的网络请求类
 * <br>Created by Soybeany on 2017/1/25.
 */
public class BDRequest<Request extends BDRequest> implements IRequest {

    /**
     * get类型
     */
    public static final int TYPE_GET = 1;

    /**
     * post类型
     */
    public static final int TYPE_POST = 2;

    /**
     * download类型
     */
    public static final int TYPE_DOWNLOAD = 3;

    /**
     * upload类型
     */
    public static final int TYPE_UPLOAD = 4;

    /**
     * 默认的请求标签
     */
    public static final String DEFAULT_REQUEST_TAG = "default_request_tag";

    /**
     * 默认超时
     */
    public static long DEFAULT_TIME_OUT = 30 * TimeUtils.SECOND;

    private static String LOG_TAG = BDRequest.class.getSimpleName(); // 日志用标识

    static {
        setupOkHttpClient();
    }


    // //////////////////////////////////供子类拓展的成员变量//////////////////////////////////

    /**
     * 网络地址
     */
    protected String mUrl;

    /**
     * 请求参数
     */
    protected Map<String, String> mRequestParam;

    /**
     * 设置目标文件
     */
    protected File mUploadFile;

    /**
     * 上传的文件列表
     */
    protected List<UploadFile> mUploadList;

    /**
     * 构造器类型
     */
    protected int mBuilderType;

    /**
     * 标准提示语
     */
    protected String mStdHint = StdHintUtils.STD_REQUEST_HINT;

    /**
     * 自定义标记
     */
    protected Object mTag;

    /**
     * 请求的回调(P层)
     */
    protected IRequestCallback mCallback;

    /**
     * 进度弹窗
     */
    protected RequestDialogWrapper mDialogHolder;

    /**
     * 是否允许弹窗取消请求
     */
    protected boolean mEnableDialogCancel = true;

    /**
     * 请求的状态码
     */
    protected Integer mStatusCode;


    // //////////////////////////////////私有成员变量//////////////////////////////////

    /**
     * 网络请求
     */
    private RequestCall mRequestCall;

    /**
     * 内部使用的回调
     */
    private Callback mInnerCallback;

    /**
     * 请求标记，用于取消网络请求
     */
    private Object mRequestTag;

    /**
     * 自定义设置的超时
     */
    private long mTimeout = DEFAULT_TIME_OUT;

    /**
     * 标识该请求是否请求中
     */
    private boolean mIsRequesting;

    /**
     * 回调是否已处理
     */
    private boolean mHasHandle;

    /**
     * 每一个请求对应的哈希码
     */
    private String mHashCode = Integer.toHexString(hashCode());


    // //////////////////////////////////构造器//////////////////////////////////

    public BDRequest(int buildType, String url, Map<String, String> requestParam, IRequestCallback callback) {
        this(buildType, url, callback);
        mRequestParam = requestParam;
    }

    public BDRequest(int buildType, String url, File uploadFile, IRequestCallback callback) {
        this(buildType, url, callback);
        mUploadFile = uploadFile;
    }

    public BDRequest(int buildType, String url, Map<String, String> requestParam, List<UploadFile> uploadList, IRequestCallback callback) {
        this(buildType, url, requestParam, callback);
        mUploadList = uploadList;
    }

    private BDRequest(int buildType, String url, IRequestCallback callback) {
        mBuilderType = buildType;
        mUrl = url;
        mCallback = callback;
    }


    // //////////////////////////////////对外静态方法//////////////////////////////////

    /**
     * 根据tag值取消全部请求
     */
    public static void cancelAll(Object tag) {
        OkHttpUtils.getInstance().cancelTag(null != tag ? tag : DEFAULT_REQUEST_TAG);
    }


    // //////////////////////////////////私有静态方法//////////////////////////////////

    /**
     * 设置OkHttp客户端的一些默认参数
     */
    private static void setupOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }


    // //////////////////////////////////对外成员方法//////////////////////////////////

    @Override
    @SuppressWarnings("unchecked")
    public Request stdHint(String stdHint) {
        mStdHint = stdHint;
        return (Request) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Request timeout(long timeout) {
        mTimeout = timeout;
        return (Request) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Request requestTag(Object tag) {
        mRequestTag = tag;
        return (Request) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Request tag(Object tag) {
        mTag = tag;
        return (Request) this;
    }

    @Override
    public String getStdHint() {
        return mStdHint;
    }

    @Override
    public void startRequest() {
        AsyncUtils.run(new DialogAsyncOption<Object>() {
            @Override
            public void onStart() {
                BDRequest.this.showDialog(mStdHint); // 全局
                BDRequest.this.showProcessDialog();
            }

            @Override
            public Object run() {
                mCallback.onPreStart(mTag);
                mInnerCallback = setupInnerCallback(getType());
                return null;
            }

            @Override
            public void onFinish(boolean isCanceled, Object tag) {
                BDRequest.this.dismissProcessDialog();
                BDRequest.this.onStart();
                BDRequest.this.buildRequest();
            }
        });
    }

    @Override
    public boolean cancelRequest() {
        // 若回调未被处理
        if (null != mRequestCall && !mHasHandle) {
            mRequestCall.cancel();
            onError(true, null);
            return true;
        }
        return false;
    }

    /**
     * 设置弹窗
     */
    @SuppressWarnings("unchecked")
    public Request dialogHolder(IProgressDialogHolder dialogHolder) {
        mDialogHolder = new RequestDialogWrapper(dialogHolder, this);
        return (Request) this;
    }

    /**
     * 请求可取消性
     *
     * @param enable 此请求是否允许取消
     */
    @SuppressWarnings("unchecked")
    public Request cancelable(boolean enable) {
        mEnableDialogCancel = enable;
        return (Request) this;
    }

    /**
     * 当前请求是否请求中(onStart ~ onFinish)
     */
    public boolean isRequesting() {
        return mIsRequesting;
    }


    // //////////////////////////////////供子类拓展的生命周期回调方法//////////////////////////////////

    /**
     * 开始
     */
    protected void onStart() {
        mCallback.onStart(this, mTag);
        mIsRequesting = true;
        mHasHandle = false;
    }

    /**
     * 显示弹窗
     */
    protected void showDialog(String hint) {
        if (null != mDialogHolder) {
            mDialogHolder.hint(hint);
            mDialogHolder.showDialog();
        }
    }

    /**
     * 接收到数据的回调
     */
    protected boolean validateResponse(Response response) {
        mStatusCode = response.code();
        mCallback.onReceive(new BDResponse(response), mTag);
        return true;
    }

    /**
     * 进度的回调
     */
    protected void inProgress(float progress, long total) {
        mCallback.inProgress(progress, total, mTag);
    }

    /**
     * 服务器响应正常时的回调
     */
    @SuppressWarnings("unchecked")
    protected void onResponse(final Object response, final String type) {
        if (!setHandled()) {
            return;
        }
        AsyncUtils.run(new DialogAsyncOption<Object>() {

            @Override
            public void onStart() {
                BDRequest.this.showProcessDialog();
            }

            @Override
            public Object run() {
                return mCallback.onPreProcess(true, response, mTag);
            }

            @Override
            public void onFinish(boolean isCanceled, Object response) {
                LogUtils.i(LOG_TAG, "Response(" + mHashCode + "-" + type + "):" + response);
                mCallback.onResponse(response, mTag);
                BDRequest.this.onFinal(true, false, response);
            }
        });
    }

    /**
     * 解析OkHttp返回的错误信息
     */
    protected String getErrorMessage(String stdHint, Exception exception, Integer statusCode) {
        String errMsg = stdHint + StdHintUtils.STD_FAIL_SUFFIX + StdHintUtils.STD_RETRY_SUFFIX;
        if (exception instanceof ConnectException || exception instanceof UnknownHostException) {
            return StdHintUtils.STD_NO_CONNECTION_BASE;
        } else if (exception instanceof ServerErrorException) {
            return errMsg + "（" + statusCode + "）";
        } else if (exception instanceof SocketTimeoutException) {
            return errMsg + "（" + StdHintUtils.STD_NO_RESPONSE_BASE + "）";
        }
        return errMsg + "（" + exception.getMessage() + "）";
    }

    /**
     * 服务器响应错误时的回调
     */
    protected void onError(Call call, Exception e) {
        if (!call.isCanceled()) {
            onError(false, e);
        }
    }

    /**
     * 服务器响应错误时的回调
     */
    protected void onError(final boolean isCancel, final Exception e) {
        if (!setHandled()) {
            return;
        }
        AsyncUtils.run(new DialogAsyncOption<Object>() {

            @Override
            public void onStart() {
                if (!isCancel) {
                    BDRequest.this.showProcessDialog();
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public Object run() {
                return mCallback.onPreProcess(false, null, mTag);
            }

            @Override
            public void onFinish(boolean isCanceled, Object tag) {
                String errMsg = StdHintUtils.STD_CANCEL_PREFIX + mStdHint;
                if (!isCancel) {
                    errMsg = getErrorMessage(mStdHint, null != mStatusCode ? new ServerErrorException() : e, mStatusCode);
                }
                LogUtils.i(LOG_TAG, "ResponseErr(" + mHashCode + "):" + errMsg);
                mCallback.onErrorResponse(isCancel, errMsg, mTag);
                BDRequest.this.onFinal(false, isCancel, null);
            }
        });
    }

    /**
     * 结束
     */
    protected void onFinal(final boolean hasResponse, final boolean isCancel, final Object response) {
        AsyncUtils.run(new DialogAsyncOption<Object>() {

            @SuppressWarnings("unchecked")
            @Override
            public IRequest run() {
                mCallback.onPostProcess(hasResponse && mCallback.isSmooth(), response, mTag);
                return null;
            }

            @Override
            public void onFinish(boolean isCanceled, Object tag) {
                if (!isCancel) {
                    BDRequest.this.dismissProcessDialog(); // 局部
                }
                mIsRequesting = false;
                mCallback.onFinish(mTag);
                BDRequest.this.dismissDialog(); // 全局
                mCallback.onFinal(mTag);
                releaseRefers(); // 释放引用
            }
        });
    }

    /**
     * 关闭弹窗
     */
    protected void dismissDialog() {
        if (null != mDialogHolder) {
            mDialogHolder.hideDialog();
        }
    }

    /**
     * 显示处理弹窗
     */
    protected void showProcessDialog() {
        setupCancelable(false);
        showDialog(mStdHint);
    }

    /**
     * 隐藏处理弹窗
     */
    protected void dismissProcessDialog() {
        setupCancelable(mEnableDialogCancel);
        dismissDialog();
    }

    /**
     * 设置网络请求建造器
     */
    protected OkHttpRequestBuilder setupBuilder() {
        OkHttpRequestBuilder builder;
        switch (mBuilderType) {
            case TYPE_GET:
            case TYPE_DOWNLOAD:
                builder = OkHttpUtils.get().url(URLParser.mergeUrl(mUrl, mRequestParam));
                break;
            case TYPE_POST:
                builder = OkHttpUtils.post().url(mUrl).params(mRequestParam);
                if (null != mUploadList) {
                    for (UploadFile uploadFile : mUploadList) {
                        ((PostFormBuilder) builder).addFile(uploadFile.key, uploadFile.filename, uploadFile.file);
                    }
                }
                break;
            case TYPE_UPLOAD:
                builder = OkHttpUtils.postFile().url(mUrl).file(mUploadFile);
                break;
            default:
                throw new RuntimeException("使用了未定义的请求标识");
        }
        return builder;
    }

    /**
     * 生成并设置请求
     */
    protected void buildRequest() {
        // 生成请求
        mRequestCall = setupBuilder().tag(null != mRequestTag ? mRequestTag : DEFAULT_REQUEST_TAG).build();

        // 设置超时
        mRequestCall.connTimeOut(mTimeout);
        mRequestCall.readTimeOut(mTimeout);
        mRequestCall.writeTimeOut(mTimeout);

        // 执行请求
        mRequestCall.execute(mInnerCallback);
        LogUtils.i(LOG_TAG, "Request(" + mHashCode + "):" + " hint-(" + mStdHint + ")" + " url-(" + mUrl + ")" + " params-("
                + mRequestParam + ")" + " uploadFile-(" + mUploadFile + ")" + " uploadList-(" + mUploadList + ")");
    }

    /**
     * 为指定弹窗设置可取消性
     */
    protected void setupCancelable(boolean cancelable) {
        if (null != mDialogHolder) {
            mDialogHolder.cancelable(cancelable);
        }
    }

    /**
     * 设置内部使用的真实网络请求回调
     */
    @WorkerThread
    protected Callback setupInnerCallback(Type type) {
        Callback callback;
        if (GenericUtils.isType(type, File.class)) {
            callback = new InnerFileCallback();
        } else if (GenericUtils.isType(type, String.class)) {
            callback = new InnerStringCallback();
        } else {
            throw new RuntimeException("暂不支持“" + mCallback.getClass().toString() + "”使用“" + type + "”类型作为泛型");
        }
        return callback;
    }

    /**
     * 释放关键的引用，用于任务结束但线程未结束时调用，避免内存泄露
     */
    protected void releaseRefers() {
        mTag = null;
        mCallback = null;
        mDialogHolder = null;
        mRequestTag = null;
    }


    // //////////////////////////////////私有成员方法//////////////////////////////////

    /**
     * 获得回调的类型
     */
    private Type getType() {
        Type interfaceType = GenericUtils.getSpecifiedInterfaceRec(mCallback, IRequestCallback.class);
        Type[] types = GenericUtils.getGeneric(interfaceType);
        if (0 == types.length) {
            throw new RuntimeException("请为“" + mCallback.getClass().toString() + "”的“" + interfaceType + "”附上泛型");
        }
        return types[0];
    }

    /**
     * 将回调设置为已处理
     *
     * @return 是否设置成功(若已被处理则处理失败)
     */
    private boolean setHandled() {
        if (mHasHandle) {
            return false;
        }
        mHasHandle = true;
        return true;
    }


    // //////////////////////////////////对外静态类//////////////////////////////////

    /**
     * 待上传文件的信息
     */
    public static class UploadFile extends PostFormBuilder.FileInput {

        public static Builder start(String filename, File file) {
            return new Builder(new UploadFile(null, filename, file));
        }

        public UploadFile(String name, String filename, File file) {
            super(name, filename, file);
        }

        public static class Builder {
            private UploadFile mUploadFile;

            private Builder(UploadFile uploadFile) {
                mUploadFile = uploadFile;
            }

            public UploadFile build(String name) {
                mUploadFile.key = name;
                return mUploadFile;
            }
        }
    }


    // //////////////////////////////////私有内部类//////////////////////////////////

    /**
     * 一般的字符串回调
     */
    private class InnerStringCallback extends StringCallback {

        @Override
        public boolean validateReponse(Response response, int id) {
            return super.validateReponse(response, id) & validateResponse(response);
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            BDRequest.this.onError(call, e);
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            super.inProgress(progress, total, id);
            BDRequest.this.inProgress(progress, total);
        }

        @Override
        public void onResponse(String s, int id) {
            BDRequest.this.onResponse(s, "文本");
        }
    }

    /**
     * 一般的文件回调
     */
    private class InnerFileCallback extends FileCallBack {

        InnerFileCallback() {
            super(mCallback.getDestFileDir(), mCallback.getDestFileName());
        }

        @Override
        public boolean validateReponse(Response response, int id) {
            return super.validateReponse(response, id) & validateResponse(response);
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            BDRequest.this.onError(call, e);
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            super.inProgress(progress, total, id);
            BDRequest.this.inProgress(progress, total);
        }

        @Override
        public void onResponse(File file, int id) {
            BDRequest.this.onResponse(file, "文件");
        }
    }

    /**
     * 服务器响应的错误
     */
    private static class ServerErrorException extends Exception {
        // 不需要内容
    }
}
