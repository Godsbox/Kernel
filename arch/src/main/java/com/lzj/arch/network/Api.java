/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.network;

import android.support.v4.util.Pair;

import java.util.List;
import java.util.Map;

/**
 * API 接口。
 *
 * @author 吴吉林
 */
public interface Api {

    /**
     * 获取缓存配置。
     *
     * @return 缓存配置
     */
    CacheConfig getCacheConfig();

    /**
     * 获取 API 接口的 URL 地址。
     *
     * @return URL 地址
     */
    String getUrl();

    /**
     * 获取请求方法。
     *
     * @return 请求方法
     */
    String getMethod();

    /**
     * 获取请求参数。
     *
     * @return 请求参数
     */
    Map<String, String> getParams();

    /**
     * 获取请求头。
     *
     * @return 请求头
     */
    Map<String, String> getHeaders();

    /**
     * 获取上传文件。
     *
     * @return 上传文件
     */
    List<Pair<String, String>> getFiles();
}
