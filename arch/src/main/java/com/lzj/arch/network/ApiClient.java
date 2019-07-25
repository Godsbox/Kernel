/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.network;

import android.support.v4.util.Pair;
import android.webkit.MimeTypeMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jakewharton.disklrucache.DiskLruCache;
import com.lzj.arch.R;
import com.lzj.arch.rx.ObservableException;
import com.lzj.arch.util.EmptyUtil;
import com.lzj.arch.util.ParameterizedTypeImpl;
import com.lzj.arch.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

import static com.lzj.arch.rx.ObservableException.ERROR_CODE_JSON;
import static com.lzj.arch.rx.ObservableException.ERROR_CODE_NETWORK;
import static com.lzj.arch.rx.ObservableException.ofHttpError;
import static com.lzj.arch.util.CollectionUtils.isEmpty;
import static com.lzj.arch.util.ContextUtils.getAppContext;
import static com.lzj.arch.util.CryptoUtils.getMd5Digest;
import static com.lzj.arch.util.FilenameUtils.getExtension;
import static com.lzj.arch.util.FilenameUtils.getName;
import static com.lzj.arch.util.IoUtils.readFromFile;
import static com.lzj.arch.util.IoUtils.writeToFile;
import static com.lzj.arch.util.ResourceUtils.getString;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;
import static okhttp3.MultipartBody.FORM;

/**
 * API 客户端。
 *
 * @author 吴吉林
 */
public final class ApiClient {

    /**
     * 单例。
     */
    private static final ApiClient INSTANCE = new ApiClient();

    /**
     * Gson。
     */
    public static final Gson GSON = new Gson();

    /**
     * 硬盘缓存大小。
     */
    public static final int DISK_CACHE_MAX_SIZE = 15 * 1024 * 1024;

    /**
     * HTTP 客户端。
     */
    private OkHttpClient client;

    /**
     * 硬盘缓存。
     */
    private DiskLruCache diskCache;

    /**
     * 私有构造方法。
     */
    private ApiClient() {
        File dir = new File(getAppContext().getCacheDir().getPath(), "api");
        try {
            diskCache = DiskLruCache.open(dir, 1, 1, DISK_CACHE_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单例。
     *
     * @return 单例
     */
    public static ApiClient getInstance() {
        return INSTANCE;
    }

    /**
     * 设置 OkHttp 客户端。
     *
     * @param client OkHttp 客户端
     */
    public void setOkHttpClient(OkHttpClient client) {
        this.client = client;
    }

    /**
     * 调用 API 接口。<br /><br />
     *
     * 如果返回的 data 字段是 null，则 <code>clazz</code> 必须是 <code>String.class</code>。
     *
     * @param api API 接口
     * @return 调用结果
     */
    public static <T> Observable<T> callApi(final Api api, Class<T> clazz) {
        return callApi(api, (Type) clazz);
    }

    /**
     * 调用 API 接口。<br /><br />
     *
     * 如果返回的 data 字段是 null，则 <code>clazz</code> 必须是 <code>String.class</code>。
     *
     * @param api API 接口
     * @param raw 结果类型的原始类型（泛型擦除后的类型），如：<code>List&lt;String&gt;</code>的 <code>List.class</code>。
     * @param arg 结果类型的泛型类型，如：<code>List&lt;String&gt;</code>的 <code>String.class</code>。
     * @param <T> 结果类型
     * @return 调用结果
     */
    public static <T> Observable<T> callApi(final Api api, Class<?> raw, Type arg) {
        return callApi(api, new ParameterizedTypeImpl(raw, arg));
    }

    /**
     * 调用 API 接口。<br /><br />
     *
     * 如果返回的 data 字段是 null，则 <code>clazz</code> 必须是 <code>String.class</code>。
     *
     * @param api API 接口
     * @param type 结果类型
     * @return 调用结果
     */
    @SuppressWarnings("unchecked")
    private static <T> Observable<T> callApi(final Api api, final Type type) {
        return Observable.create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> e) {
                        JsonObject result;
                        try{
                            result = callApiSync(api);
                            e.onNext((T) GSON.fromJson(result, type));
                        }catch (ObservableException error){
                            e.onError(error);
                        }
                    }
                })
                .subscribeOn(io())
                .observeOn(mainThread());
    }

    /**
     * 同步调用 API 接口。<br /><br />
     *
     * 注意：该方法会阻塞当前线程。
     *
     * @param api API 接口
     * @return 调用结果
     */
    public static JsonObject callApiSync(Api api) throws ObservableException{
        CacheConfig cacheConfig = api.getCacheConfig();
        if (cacheConfig.isCacheEnabled() && !cacheConfig.isIgnoreCache()) {
            JsonObject object = readDiskCache(cacheConfig.getKey());
            if (object != null) {
                return object;
            }
        }

        Request request = buildRequest(api);
        Timber.d("request url:%s", request.url());
        Call call = getInstance().client.newCall(request);
        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                throwFailureException(response.code());
            }
            String result = new String(response.body().bytes());
            response.close();

            // 缓存API数据
            if (cacheConfig.isCacheEnabled()) {
                writeDiskCache(cacheConfig.getKey(), result);
            }

            return toJsonObject(result, false);
        } catch (IOException e) {
            String message = getString(R.string.http_code_no_network);
            if (e instanceof SocketTimeoutException) {
                message = getString(R.string.http_code_timeout);
            }
            throw ofHttpError(ERROR_CODE_NETWORK, message);
        }
    }

    /**
     * 写入给定 URL 地址的硬盘缓存。
     *
     * @param key 键
     * @param json JSON 数据
     */
    public static void writeDiskCache(String key, String json) {
        key = getMd5Digest(key);
        try {
            DiskLruCache.Editor editor = getDiskCache().edit(key);
            if (editor == null) {
                return;
            }
            OutputStream os = editor.newOutputStream(0);
            if (writeToFile(json, os)) {
                editor.commit();
                getDiskCache().flush();
                return;
            }
            editor.abort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取给定 URL 地址的硬盘缓存。
     *
     * @param key 键
     * @return URL 地址的硬盘缓存，若无缓存则返回null。
     */
    public static JsonObject readDiskCache(String key) {
        key = getMd5Digest(key);
        try {
            DiskLruCache.Snapshot snapshot = getDiskCache().get(key);
            // 没有硬盘缓存
            if (snapshot == null) {
                return null;
            }
            InputStream is = snapshot.getInputStream(0);
            String json = readFromFile(is);
            return StringUtils.isEmpty(json) ? null : toJsonObject(json, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSON字符串转换成JSON对象。
     *
     * @param json JSON字符串
     * @param fromCache true：来自缓存；false：来自网络。
     * @return JSON对象
     */
    private static JsonObject toJsonObject(String json, boolean fromCache) {
        JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);
        jsonObject.addProperty("from_cache", fromCache);
        Timber.d("json result from_cache:%s\n%s", fromCache, jsonObject);
        return jsonObject;
    }

    /**
     * 获取硬盘缓存。
     *
     * @return 硬盘缓存
     */
    private static DiskLruCache getDiskCache() {
        return getInstance().diskCache;
    }

    /**
     * 抛失败异常。
     *
     * @param code 响应码
     * @throws ObservableException 异常
     */
    private static void throwFailureException(int code) {
        String message = getString(R.string.http_code_unknown, code);
        switch (code) {
            case 404:
                message = getString(R.string.http_code_not_found);
                break;
            case 500:
            case 501:
            case 502:
            case 503:
            case ERROR_CODE_JSON:
                message = getString(R.string.http_code_server_error, code);
                break;
        }
        throw ofHttpError(code, message);
    }

    /**
     * 构造请求。
     *
     * @param api API 接口
     * @return HTTP 请求
     */
    private static Request buildRequest(Api api) {
        Request.Builder builder = new Request.Builder().url(api.getUrl());
        for (Map.Entry<String, String> header : api.getHeaders().entrySet()) {
            builder.header(header.getKey(), header.getValue());
        }
        if ("GET".equals(api.getMethod())) {
            addQueryParameters(builder, api);
            return builder.get().build();
        }
        RequestBody body = isEmpty(api.getFiles()) ? buildFormBody(api) : buildUploadBody(api);
        return builder.post(body).build();
    }

    /**
     * 添加查询参数。
     *
     * @param builder 请求构造器
     * @param api     API 接口
     */
    private static void addQueryParameters(Request.Builder builder, Api api) {
        if (isEmpty(api.getParams())) {
            return;
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse(api.getUrl()).newBuilder();
        for (Map.Entry<String, String> entry : api.getParams().entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        builder.url(urlBuilder.build());
    }

    /**
     * 构造上传文件请求体。
     *
     * @param api API 接口
     * @return 请求体
     */
    private static RequestBody buildUploadBody(Api api) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(FORM);
        for (Map.Entry<String, String> entry : api.getParams().entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        for (Pair<String, String> file : api.getFiles()) {
            String filepath = file.second;
            String mimeType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(getExtension(filepath));
            if(EmptyUtil.isEmpty(mimeType)){
                mimeType = "text/plain";
            }
            RequestBody body = RequestBody.create(MediaType.parse(mimeType), new File(filepath));
            builder.addFormDataPart(file.first, getName(filepath), body);
        }
        return builder.build();
    }

    /**
     * 构造表单请求体。
     *
     * @param api API 接口
     * @return 请求体
     */
    private static RequestBody buildFormBody(Api api) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : api.getParams().entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }
}
