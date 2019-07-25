/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库帮助者。
 *
 * @author 吴吉林
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * 表列表。
     */
    private List<Table> tables = new ArrayList<>();

    /**
     * 创建一个新的数据库帮助者。
     *
     * @param context 上下文
     * @param name 数据库名
     * @param version 版本号
     */
    public DatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Table table : tables) {
            table.create(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Table table : tables) {
            table.onUpgrade(db, oldVersion, newVersion);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Table table : tables) {
            table.onDowngrade(db, oldVersion, newVersion);
        }
    }

    /**
     * 添加表。
     *
     * @param table 表
     */
    public void addTable(Table table) {
        tables.add(table);
    }
}
