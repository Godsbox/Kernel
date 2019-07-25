/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.rx;

/**
 * 该类代表可观察者遭遇到的异常。<br /><br />
 *
 * 该类额外定义了错误码字段，方便使用者处理该异常。
 *
 * @author 吴吉林
 */
public class ObservableException extends RuntimeException {

    /**
     * 错误码：网络问题。
     */
    public static final int ERROR_CODE_NETWORK = -1;

    /**
     * 错误码：JSON 数据问题。
     */
    public static final int ERROR_CODE_JSON = -3;

    /**
     * 错误码：无错误码。
     */
    public static final int ERROR_CODE_NONE = -999;

    /**
     * 错误码：取消。
     */
    public static final int ERROR_CODE_CANCEL = -998;

    /**
     * 错误码：副级空页面。
     */
    public static final int ERROR_CODE_SECONDARY_EMPTY = -997;

    /**
     * 错误码。
     */
    private int errorCode = ERROR_CODE_NONE;

    /**
     * 标识是否是 HTTP 层错误。
     */
    private boolean httpError;

    public ObservableException(String message) {
        super(message);
    }

    /**
     * 创建一个业务层错误。
     *
     * @param errorCode 错误码
     * @param message 错误消息
     * @return 业务层错误
     */
    public static ObservableException ofResultError(int errorCode, String message) {
        ObservableException e = new ObservableException(message);
        e.httpError = false;
        e.errorCode = errorCode;
        return e;
    }

    /**
     * 创建一个 HTTP 层错误。
     *
     * @param errorCode 错误码
     * @param message 错误消息
     * @return HTTP 层错误
     */
    public static ObservableException ofHttpError(int errorCode, String message) {
        ObservableException e = new ObservableException(message);
        e.httpError = true;
        e.errorCode = errorCode;
        return e;
    }

    /**
     * 判断是否是 HTTP 层错误。
     *
     * @return true：HTTP 层错误；false：业务层错误。
     */
    public boolean isHttpError() {
        return httpError;
    }

    /**
     * 获取错误码。
     *
     * @return 错误码
     */
    public int getErrorCode() {
        return errorCode;
    }
}
