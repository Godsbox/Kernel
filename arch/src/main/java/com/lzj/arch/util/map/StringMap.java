/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.util.map;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 字符串映射表。<br /><br />该映射表使用字符串类型的键。
 *
 * @author 吴吉林
 */
public interface StringMap extends ReadOnlyMap<String> {

    ArrayList<Parcelable> getParcelableList(String key);

    <T> T getParcelable(String key);
}
