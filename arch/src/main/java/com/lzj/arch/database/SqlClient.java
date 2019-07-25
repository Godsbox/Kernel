/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

import static com.lzj.arch.rx.ObservableException.ERROR_CODE_NONE;
import static com.lzj.arch.rx.ObservableException.ofResultError;
import static com.lzj.arch.util.CollectionUtils.getFirst;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;

/**
 * 数据库客户端。
 *
 * @author 吴吉林
 */
public class SqlClient {

    /**
     * 数据库帮助者。
     */
    private DatabaseHelper helper;

    public void setHelper(DatabaseHelper helper) {
        this.helper = helper;
    }

    public SQLiteDatabase getReadableDatabase() {
        return helper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return helper.getWritableDatabase();
    }

    /**
     * 新增数据。
     *
     * @param table  表名
     * @param values 数据
     * @return 新增数据 ID
     */
    public Observable<Long> insert(final String table, final ContentValues values) {
        return create(new Call<Long>() {
            @Override
            public Long execute(DatabaseHelper helper) throws Exception {
                long result = helper.getWritableDatabase().insert(table, null, values);
                if (result == -1) {
                    throw ofResultError(ERROR_CODE_NONE, "");
                }
                return Long.valueOf(result);
            }
        });
    }

    /**
     * 删除表中所有数据。
     *
     * @param table 表名
     * @return 删除结果
     */
    public Observable<String> delete(String table) {
        return delete(table, "1", (String[]) null);
    }

    /**
     * 删除数据。
     *
     * @param table 表名
     * @param id    ID
     * @return 删除结果
     */
    public Observable<String> delete(String table, long id) {
        return delete(table, "_id=?", id + "");
    }

    /**
     * 删除数据。
     *
     * @param table       表名
     * @param whereClause where 语句
     * @param whereArgs   where 参数表
     * @return 删除结果
     */
    public Observable<String> delete(final String table, final String whereClause, final String... whereArgs) {
        return create(new Call<String>() {
            @Override
            public String execute(DatabaseHelper helper) throws Exception {
                helper.getWritableDatabase().delete(table, whereClause, whereArgs);
                return "";
            }
        });
    }

    /**
     * 更新数据。
     *
     * @param table  表名
     * @param values 更新内容
     * @param id     数据 ID
     * @return 更新结果
     */
    public Observable<String> update(String table, ContentValues values, long id) {
        return update(table, values, "_id=?", id + "");
    }

    /**
     * 更新数据。
     * @return 更新结果
     */
    public Observable<String> updateByGameId(String table, ContentValues values, int gameId) {
        return updateByGameId(table, values, "game_id=?", gameId);
    }

    /**
     * 更新数据。
     * @return 更新结果
     */
    public Observable<String> updateByGameId(final String table, final ContentValues values, final String whereClause, final int gameId) {
        return create(new Call<String>() {
            @Override
            public String execute(DatabaseHelper helper) {
                long result = helper.getWritableDatabase().update(table, values, whereClause, new String[]{gameId+""});
                if(result < 1){
                    values.put("game_id", gameId);
                    result = helper.getWritableDatabase().insert(table, null, values);
                }
                return "" + result;
            }
        });
    }

    /**
     * 更新数据。
     *
     * @param table       表名
     * @param values      更新内容
     * @param whereClause where 语句
     * @param whereArgs   where 参数表
     * @return 更新结果
     */
    public Observable<String> update(final String table, final ContentValues values, final String whereClause, final String... whereArgs) {
        return create(new Call<String>() {
            @Override
            public String execute(DatabaseHelper helper) throws Exception {
                int result = helper.getWritableDatabase().update(table, values, whereClause, whereArgs);
                return "" + result;
            }
        });
    }

    /**
     * 查询单条数据。根据game_id
     *
     * @param table  表名
     * @param mapper 数据映射器
     * @param id     数据 ID
     * @param <T>    结果类型
     * @return 查询结果
     */
    public <T> Observable<T> select(final String table, final CursorMapper<T> mapper, long id) {
        StringBuilder builder = new StringBuilder("SELECT * FROM ")
                .append(table)
                .append(" WHERE game_id=?");
        return list(builder.toString(), mapper, id + "")
                .map(new ListToFirstFunction<T>());
    }

    /**
     * 查询单条数据。
     *
     * @param params 查询参数
     * @param mapper 数据映射器
     * @param <T>    结果类型
     * @return 查询结果
     */
    public <T> Observable<T> select(QueryParams params, CursorMapper<T> mapper) {
        return list(params, mapper)
                .map(new ListToFirstFunction<T>());
    }

    /**
     * 查询列表数据。
     *
     * @param params 查询参数
     * @param mapper 数据映射器
     * @param <T>    列表元素类型
     * @return 查询结果
     */
    public <T> Observable<List<T>> list(final QueryParams params, final CursorMapper<T> mapper) {
        return create(new Call<List<T>>() {
            @Override
            public List<T> execute(DatabaseHelper helper) throws Exception {
                Cursor cursor = helper.getReadableDatabase()
                        .query(params.distinct, params.table, params.columns,
                                params.selection, params.selectionArgs, params.groupBy,
                                params.having, params.orderBy, params.limit);
                return newList(cursor, mapper);
            }
        });
    }

    /**
     * 查询列表数据。
     *
     * @param sql           SQL 语句
     * @param mapper        数据映射器
     * @param selectionArgs 查询参数表
     * @param <T>           列表元素类型
     * @return 查询结果
     */
    public <T> Observable<List<T>> list(final String sql, final CursorMapper<T> mapper, final String... selectionArgs) {
        return create(new Call<List<T>>() {
            @Override
            public List<T> execute(DatabaseHelper helper){
                Cursor cursor = helper.getReadableDatabase().rawQuery(sql, selectionArgs);
                return newList(cursor, mapper);
            }
        });
    }

    public static <T> List<T> newList(Cursor cursor, CursorMapper<T> mapper) {
        List<T> list = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            list.add(mapper.map(cursor));
        }
        cursor.close();
        return list;
    }

    public <T> Observable<T> transaction(final Call<T> call) {
        return create(call, true);
    }

    public <T> Observable<T> create(final Call<T> call) {
        return create(call, false);
    }

    private <T> Observable<T> create(Call<T> call, boolean transactionEnabled) {
        return Observable.create(new OnSubscribe<>(call, transactionEnabled))
                .subscribeOn(io())
                .observeOn(mainThread());
    }


    /**
     * 检查某表列是否存在
     *
     * @param db
     * @param tableName  表名
     * @param columnName 列名
     * @return
     */
    public static boolean checkColumnExist1(SQLiteDatabase db, String tableName
            , String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            //查询一行
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0"
                    , null);
            result = cursor != null && cursor.getColumnIndex(columnName) != -1;
        } catch (Exception e) {
            //异常
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return result;
    }


    private class OnSubscribe<T> implements ObservableOnSubscribe<T> {

        /**
         * 数据库操作。
         */
        private Call<T> call;

        /**
         * 是否启用事务。
         */
        private boolean transactionEnabled;

        private OnSubscribe(Call<T> call, boolean transactionEnabled) {
            this.call = call;
            this.transactionEnabled = transactionEnabled;
        }

        @Override
        public void subscribe(ObservableEmitter<T> emitter) throws Exception {
            SQLiteDatabase db = null;
            if (transactionEnabled) {
                db = helper.getWritableDatabase();
                db.beginTransaction();
            }
            try {
                emitter.onNext(call.execute(helper));
                if (db != null) {
                    db.setTransactionSuccessful();
                }
            } catch (Exception ex) {
                emitter.onError(ofResultError(ERROR_CODE_NONE, ex.getMessage()));
            } finally {
                if (db != null) {
                    db.endTransaction();
                }
            }
        }
    }

    /**
     * 将列表转化成成一个的函数，即取列表的第一个元素作为转换结果。
     *
     * @param <T> 元素类型
     */
    private static class ListToFirstFunction<T> implements Function<List<T>, T> {
        @Override
        public T apply(@NonNull List<T> list) throws Exception {
            return getFirst(list);
        }
    }
}
