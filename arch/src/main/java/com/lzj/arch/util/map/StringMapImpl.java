/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.util.map;

import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 字符串映射表实现。
 *
 * @author 吴吉林
 */
public class StringMapImpl implements StringMap {

    private Bundle bundle;

    public StringMapImpl(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public boolean contains(String key) {
        return bundle.containsKey(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return bundle.getBoolean(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return bundle.getBoolean(key, defaultValue);
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return bundle.getString(key, defaultValue);
    }

    @Override
    public int getInt(String key) {
        return bundle.getInt(key);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return bundle.getInt(key, defaultValue);
    }

    @Override
    public long getLong(String key) {
        return bundle.getLong(key);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return bundle.getLong(key, defaultValue);
    }

    @Override
    public Serializable getSerializable(String key) {
        return bundle.getSerializable(key);
    }

    @Override
    public ArrayList<Parcelable> getParcelableList(String key) {
        return bundle.getParcelableArrayList(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getParcelable(String key) {
        return (T) bundle.getParcelable(key);
    }
}
