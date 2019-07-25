/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app;

/**
 * 参数配置。<br /><br />
 *
 * 该类实现界面额外参数与URL参数一一对应关系，并通过参数值类型将URL参数设置到界面额外参数表中。
 *
 * @author 吴吉林
 */
public class ParamConfig {

    /**
     * 额外参数名称。
     */
    final String extra;

    /**
     * URL 参数名称。
     */
    final String key;

    /**
     * 参数值类型。
     */
    final Class<?> valueClass;

    public ParamConfig(String extra, String key, Class<?> valueClass) {
        this.extra = extra;
        this.key = key;
        this.valueClass = valueClass;
    }
}
