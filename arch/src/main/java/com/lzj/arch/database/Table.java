/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import timber.log.Timber;

/**
 * 代表数据库表基类。
 *
 * @author 吴吉林
 */
public abstract class Table {

    /**
     * 字段：主键 ID
     */
    public static final String COLUMN_ID = "_id";

    /**
     * 表名。
     */
    private String name;

    /**
     * 标识是否启用主键功能。
     */
    private boolean primaryKeyEnabled = true;

    /**
     * 设置表名。
     *
     * @param name 表名
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * 设置是否启用主键功能。
     *
     * @param enabled true：启用；false：关闭。
     */
    protected void setPrimaryKeyEnabled(boolean enabled) {
        this.primaryKeyEnabled = enabled;
    }

    /**
     * 创建表。
     *
     * @param db 数据库
     */
    protected void create(SQLiteDatabase db) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("表名不能为空，请检查是否有调用setName方法");
        }
        StringBuilder sql = new StringBuilder("CREATE TABLE ").append(name)
                .append("(");
        if (primaryKeyEnabled) {
            sql.append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        }
        onCreate(sql);
        sql.append(");");
        try {
            db.execSQL(sql.toString());
        } catch (SQLException e) {
            Timber.e("table create error:%s", e.getMessage());
        }
    }

    /**
     * 处理创建表 SQL 语句的创建。<br /><br />
     *
     * 子类实现该方法按需追加字段。<br /><br />
     *
     * 注意：父类已经为子类统一在 SQL 语句前面准备好：<br />
     *
     * <code>create table $tableName ( _id integer primary key autoincrement,</code><br />
     *
     * 也为子类统一在末尾准备好：<code>);</code><br />
     *
     * 子类请不要重复输入，避免错误。
     *
     * @param sql SQL 语句
     */
    protected abstract void onCreate(StringBuilder sql);

    /**
     * 处理表的升级。
     *
     * @param db 数据库
     * @param oldVersion 旧版本号
     * @param newVersion 新版本号
     */
    protected void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 空实现
    }

    /**
     * 处理表的降级
     *
     * @param db 数据库
     * @param oldVersion 旧版本号
     * @param newVersion 新版本号
     */
    protected void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 空实现
    }
}

