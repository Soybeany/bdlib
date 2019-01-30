package com.soybeany.bdlib.basic.database.core;

import android.content.ContentValues;
import android.database.Cursor;

import com.soybeany.bdlib.basic.database.annotation.Table;
import com.soybeany.bdlib.basic.database.interfaces.IConfig;
import com.soybeany.bdlib.basic.database.interfaces.IEntity;
import com.soybeany.bdlib.basic.database.interfaces.IManipulation;
import com.soybeany.bdlib.basic.database.interfaces.IMigration;
import com.soybeany.bdlib.basic.database.interfaces.ITransaction;
import com.soybeany.bdlib.basic.database.util.DBCipherUtils;
import com.soybeany.bdlib.basic.database.util.DBLogUtils;
import com.soybeany.bdlib.basic.database.util.DBUpgradeUtils;
import com.soybeany.bdlib.util.context.BDContext;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * 数据库操作辅助类，接收一个数据库配置，对应一个数据库
 * <br>Created by Soybeany on 2017/1/11.
 */
public class DBHelper extends SQLiteOpenHelper implements IManipulation {

    /**
     * 数据库的密码
     */
    public static String KEY = "bd_secret_key";

    /**
     * 是否需要加密
     */
    public static boolean NEED_ENCRYPT = false;

    private IConfig mDBConfig; // 数据库配置

    DBHelper(IConfig config) {
        super(BDContext.getContext(), config.getDBName(), null, config.getDBVersion());
        mDBConfig = config;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        int version = mDBConfig.getDBVersion();
        onUpgrade(db, version, version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createTable(db);
        upgradeTables(db, oldVersion, newVersion);
    }


    // //////////////////////////////////“增删改查”操作方法//////////////////////////////////

    @Override
    public long insert(String tableName, ContentValues values) {
        long id = getDatabase().insert(tableName, null, values);
        if (-1 == id) {
            DBLogUtils.e("增加记录失败（" + mDBConfig.getDBName() + " - " + tableName + "）");
        } else {
            DBLogUtils.i("增加了id=" + id + "的记录（" + mDBConfig.getDBName() + " - " + tableName + "）");
        }
        return id;
    }

    @Override
    public int delete(String tableName, String where) {
        int row = getDatabase().delete(tableName, where, null);
        if (0 == row) {
            DBLogUtils.e("删除记录失败（" + mDBConfig.getDBName() + " - " + tableName + "）");
        } else {
            DBLogUtils.i("删除了" + row + "条记录（" + mDBConfig.getDBName() + " - " + tableName + "） - where:" + where);
        }
        return row;
    }

    @Override
    public int delete(String tableName, long id) {
        return delete(tableName, BaseSQL.queryId(id));
    }

    @Override
    public int update(String tableName, String where, ContentValues values) {
        int row = getDatabase().update(tableName, values, where, null);
        if (0 == row) {
            DBLogUtils.e("修改记录失败（" + mDBConfig.getDBName() + " - " + tableName + "）");
        } else {
            DBLogUtils.i("修改了" + row + "条记录（" + mDBConfig.getDBName() + " - " + tableName + "） - where:" + where);
        }
        return row;
    }

    @Override
    public int update(String tableName, long id, ContentValues values) {
        return update(tableName, BaseSQL.queryId(id), values);
    }

    @Override
    public <T extends IEntity> List<T> query(boolean distinct, Class<T> tableClass, String[] columns, String where, String groupBy, String having, String orderBy, String limit) {
        List<T> entities = null;
        Cursor cursor = null;
        String tableName = DBReflectionUtils.getTableName(tableClass);
        try {
            if (null != columns && 0 < columns.length) {
                HashSet<String> set = new HashSet<>();
                Collections.addAll(set, columns);
                set.add(Table.DEFAULT_ID_NAME);
                set.toArray(columns);
            }
            cursor = getDatabase().query(distinct, tableName, columns, where, null, groupBy, having, orderBy, limit);
            entities = DBReflectionUtils.processCursor(this, mDBConfig, tableClass, cursor);
            DBLogUtils.i("查询到" + entities.size() + "条记录（" + mDBConfig.getDBName() + " - " + tableName + "） - " + "where:" + where + " groupBy:" + groupBy + " having:" + having + " orderBy:" + orderBy + " limit:" + limit);
        } catch (Exception e) {
            entities = new ArrayList<>();
//            if (e.getMessage().contains("no such table")) {
//                Toast.makeText(BDAContext.getContext(), "数据库“" + mDBConfig.getDBName() + "”中没有找到表“" + tableName + "”", Toast.LENGTH_SHORT).show();
//            }
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return entities;
    }

    @Override
    public <T extends IEntity> T queryId(Class<T> tableClass, long id) {
        T result = null;
        List<T> resultList = query(false, tableClass, null, BaseSQL.queryId(id), null, null, null, "1");
        if (!resultList.isEmpty()) {
            result = resultList.get(0);
        }
        return result;
    }


    // //////////////////////////////////公开方法//////////////////////////////////

    /**
     * 使用事务操作
     */
    @Override
    public void useTransaction(ITransaction transaction) {
        doTransaction(getDatabase(), transaction);
    }

    /**
     * 获得数据库名
     */
    public String getName() {
        return mDBConfig.getDBName();
    }


    // //////////////////////////////////内部方法//////////////////////////////////

    /**
     * 创建数据库表
     */
    private void createTable(final SQLiteDatabase db) {
        doTransaction(db, new ITransaction() {
            @Override
            public void doBusiness() {
                for (Class<? extends IEntity> table : DBManageUtils.getTables(mDBConfig)) {
                    String sql = BaseSQL.createTable(table);
                    db.execSQL(sql);
                    DBLogUtils.i("创建数据库（" + mDBConfig.getDBName() + "） - " + sql);
                }
            }
        });
    }

    /**
     * 升级数据库表
     */
    private void upgradeTables(final SQLiteDatabase db, int oldVersion, int newVersion) {
        DBUpgradeUtils.upgrade(oldVersion, newVersion, new DBUpgradeUtils.IUpgradeHandler() {
            @Override
            public IMigration[] getMigrations() {
                return mDBConfig.getMigrations();
            }

            @Override
            public Cursor onRawQuery(String sql) {
                return db.rawQuery(sql, null);
            }

            @Override
            public void onExecute(final List<String> sqlList) {
                doTransaction(db, new ITransaction() {
                    @Override
                    public void doBusiness() {
                        for (String sql : sqlList) {
                            db.execSQL(sql);
                            DBLogUtils.i("升级数据库（" + mDBConfig.getDBName() + "） - " + sql);
                        }
                    }
                });
            }
        });
    }

    /**
     * 获得数据库
     */
    private SQLiteDatabase getDatabase() {
        String key = NEED_ENCRYPT ? KEY : null;
        SQLiteDatabase database = null;
        try {
            // 尝试直接获得数据库
            database = getWritableDatabase(key);
        } catch (SQLiteException e) {
            // 尝试转换数据库
            boolean isSuccess;
            if (NEED_ENCRYPT) {
                isSuccess = DBCipherUtils.encryptDatabase(mDBConfig.getDBName(), KEY);
            } else {
                isSuccess = DBCipherUtils.decryptDatabase(mDBConfig.getDBName(), KEY);
            }
            // 若转换成功，再次获得数据库
            if (isSuccess) {
                database = getWritableDatabase(key);
            }
        }
        return database;
    }

    /**
     * 开始事务操作
     */
    private void doTransaction(SQLiteDatabase db, ITransaction transaction) {
        // 开始事务
        db.beginTransaction();
        try {
            transaction.doBusiness();
            // 事务成功
            db.setTransactionSuccessful();
        } finally {
            // 结束事务
            db.endTransaction();
        }
    }
}
