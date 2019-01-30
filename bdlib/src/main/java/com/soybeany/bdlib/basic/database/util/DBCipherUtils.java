package com.soybeany.bdlib.basic.database.util;

import android.content.Context;

import com.soybeany.bdlib.util.context.BDContext;
import com.soybeany.bdlib.util.file.FileUtils;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

/**
 * 数据库加密工具类
 * <br>Created by Soybeany on 2017/3/25.
 */
public class DBCipherUtils {

    private static final String TEMP_PRE = "TEMP_"; // 临时数据库前缀

    /**
     * 加密数据库方法
     *
     * @param dbName 数据库名
     * @param key    密钥
     */
    public static boolean encryptDatabase(String dbName, final String key) {
        return copyDatabase(dbName, "", key);
    }

    /**
     * 解密数据库
     *
     * @param dbName 数据库名
     * @param key    密钥
     */
    public static boolean decryptDatabase(String dbName, String key) {
        return copyDatabase(dbName, key, "");
    }

    /**
     * 拷贝数据库
     */
    private static boolean copyDatabase(String oDbName, String oDbKey, String tDbKey) {
        Context context = BDContext.getContext();

        // 原始数据库
        File oDbFile = context.getDatabasePath(oDbName);

        // 若原数据库不存在则返回
        if (!oDbFile.exists()) {
            return false;
        }

        // 临时数据库
        String tDbName = TEMP_PRE + oDbName;
        File tDbFile = context.getDatabasePath(tDbName);

        // 转换数据
        boolean isSuccess = false;
        SQLiteDatabase database = null;
        try {
            database = SQLiteDatabase.openOrCreateDatabase(oDbFile, oDbKey, null);
            database.rawExecSQL(String.format("ATTACH DATABASE '%s' AS tempDb KEY '%s'", tDbFile.getAbsolutePath(), tDbKey));
            database.rawExecSQL("SELECT sqlcipher_export('tempDb')");
            database.rawExecSQL("DETACH DATABASE tempDb");
            isSuccess = true;
        } catch (Exception e) {
            DBLogUtils.i(oDbName + "数据库转换出错，" + e.getMessage());
        } finally {
            if (database != null) {
                database.close();
            }
        }

        // 用临时数据库取代正式数据库
        if (isSuccess) {
            FileUtils.deleteFile(oDbFile);
            if (FileUtils.copyFile(tDbFile, oDbFile)) {
                FileUtils.deleteFile(tDbFile);
            }
        } else {
            FileUtils.deleteFile(tDbFile);
        }

        return isSuccess;
    }

}
