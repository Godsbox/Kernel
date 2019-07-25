/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.database;

/**
 * 查询参数。
 *
 * @author 吴吉林
 */
public class QueryParams {

    boolean distinct;
    String table;
    String[] columns;
    String selection;
    String[] selectionArgs;
    String groupBy;
    String having;
    String orderBy;
    String limit;

    public QueryParams(String table) {
        this.table = table;
    }

    public QueryParams distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public QueryParams columns(String... columns) {
        this.columns = columns;
        return this;
    }

    public QueryParams selection(String selection) {
        this.selection = selection;
        return this;
    }

    public QueryParams selectionArgs(String... selectionArgs) {
        this.selectionArgs = selectionArgs;
        return this;
    }

    public QueryParams groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public QueryParams having(String having) {
        this.having = having;
        return this;
    }

    public QueryParams orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public QueryParams limit(String limit) {
        this.limit = limit;
        return this;
    }
}
