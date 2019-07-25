/*
 * Copyright (c) 2017 3000.com All Rights Reserved.
 */
package com.lzj.arch.file;

import android.content.SharedPreferences;

import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;
import static com.lzj.arch.util.ContextUtils.getAppContext;

/**
 * 键值对缓存的首选项文件实现类。请使用SPUtils代替，简化开发
 */
@Deprecated
public class SharedPreferencesImpl implements KeyValueCache {

    private String name;

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;

    public SharedPreferencesImpl(String name) {
        this.name = name;
        preferences = getAppContext().getSharedPreferences(name, MODE_PRIVATE);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean contains(String key) {
        return preferences.contains(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    @Override
    public String getString(String key) {
        return preferences.getString(key, "");
    }

    @Override
    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    @Override
    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    @Override
    public long getLong(String key) {
        return preferences.getLong(key, 0L);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    @Override
    public Serializable getSerializable(String key) {
        return null;
    }

    @Override
    public KeyValueCache remove(String key) {
        ensureEditor();
        editor.remove(key);
        return this;
    }

    @Override
    public KeyValueCache putString(String key, String value) {
        ensureEditor();
        editor.putString(key, value);
        return this;
    }

    @Override
    public KeyValueCache putBoolean(String key, boolean value) {
        ensureEditor();
        editor.putBoolean(key, value);
        return this;
    }

    @Override
    public KeyValueCache putInt(String key, int value) {
        ensureEditor();
        editor.putInt(key, value);
        return this;
    }

    @Override
    public KeyValueCache putLong(String key, long value) {
        ensureEditor();
        editor.putLong(key, value);
        return this;
    }

    private void ensureEditor() {
        if (editor == null) {
            editor = preferences.edit();
        }
    }

    @Override
    public void apply() {
        if (editor != null) {
            editor.apply();
            editor = null;
        }
    }
}
