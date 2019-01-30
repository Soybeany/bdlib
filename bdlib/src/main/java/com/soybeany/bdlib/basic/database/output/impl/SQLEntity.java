package com.soybeany.bdlib.basic.database.output.impl;


import com.soybeany.bdlib.basic.database.core.enums.Logic;
import com.soybeany.bdlib.basic.database.interfaces.ISQLEntity;
import com.soybeany.bdlib.basic.database.output.sql.StdSQL;
import com.soybeany.bdlib.basic.database.util.DBStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询语句实体（默认实现）
 * <br>Created by Soybeany on 2017/1/19.
 */
public class SQLEntity implements ISQLEntity {

    private static final String DESC = "DESC"; // 降序排列

    private boolean mIsDistinct; // 是否去重
    private String[] mColumns; // 需要查询的列
    private List<String> mWhereList = new ArrayList<>(); // 条件数组 用来筛选 FROM 子句中指定的操作所产生的行
    private String mGroupBy; // 用来分组WHERE子句的输出
    private String mHaving; // 用来从分组的结果中筛选行
    private List<String> mOrderList = new ArrayList<>(); // 排序数组
    private String mLimit; // 条数限制


    // //////////////////////////////////方法重写区域//////////////////////////////////

    @Override
    public boolean isDistinct() {
        return mIsDistinct;
    }

    @Override
    public String[] getColumns() {
        return mColumns;
    }

    @Override
    public String getWhere() {
        return DBStringUtils.join(mWhereList, " ");
    }

    @Override
    public String getGroupBy() {
        return mGroupBy;
    }

    @Override
    public String getHaving() {
        return mHaving;
    }

    @Override
    public String getOrderBy() {
        return DBStringUtils.join(mOrderList, ", ");
    }

    @Override
    public String getLimit() {
        return mLimit;
    }


    // //////////////////////////////////DISTINCT区域//////////////////////////////////

    /**
     * 设置是否需要去重
     */
    public SQLEntity distinct(boolean isDistinct) {
        mIsDistinct = isDistinct;
        return this;
    }


    // //////////////////////////////////COLUMNS区域//////////////////////////////////

    /**
     * 设置需要查询的数据列
     */
    public SQLEntity columns(String... columns) {
        mColumns = columns;
        return this;
    }


    // //////////////////////////////////WHERE区域//////////////////////////////////

    /**
     * 添加结果筛选
     */
    public SQLEntity addWhere(String expression, Logic logic) {
        if (mWhereList.isEmpty()) {
            mWhereList.add(expression);
        } else {
            mWhereList.add(getConnectLogic(logic) + " " + expression);
        }
        return this;
    }

    /**
     * 添加结果筛选
     */
    public SQLEntity addWhere(String expression) {
        return addWhere(expression, Logic.and);
    }

    /**
     * 添加判等的结果筛选
     */
    public SQLEntity addWhereEqual(String field, Object value) {
        return addWhere(StdSQL.equal(field, value));
    }

    // //////////////////////////////////GROUP BY区域//////////////////////////////////

    /**
     * 设置分组
     */
    public SQLEntity groupBy(String expression) {
        mGroupBy = expression;
        return this;
    }


    // //////////////////////////////////HAVING区域//////////////////////////////////

    /**
     * 设置分组筛选
     */
    public SQLEntity having(String expression) {
        mHaving = expression;
        return this;
    }


    // //////////////////////////////////ORDER BY区域//////////////////////////////////

    /**
     * 添加排序
     */
    public SQLEntity addOrder(String field, boolean isDesc) {
        mOrderList.add(field + (isDesc ? " " + DESC : ""));
        return this;
    }


    // //////////////////////////////////LIMIT区域//////////////////////////////////

    /**
     * 设置限制
     *
     * @param count 返回的条数，-1表示返回到最后
     */
    public SQLEntity limit(long count) {
        return limit(0, count);
    }

    /**
     * 设置限制
     *
     * @param offset 偏移数，表示返回的记录从哪行开始，默认为0，返回第1条数据
     * @param count  返回的条数，-1表示返回到最后
     */
    public SQLEntity limit(long offset, long count) {
        mLimit = DBStringUtils.join(new String[]{offset + "", count + ""}, ISQLEntity.DOT_SEPARATOR);
        return this;
    }

    /**
     * 获得连接逻辑
     */
    private String getConnectLogic(Logic logic) {
        String logicStr;
        switch (logic) {
            case and:
                logicStr = AND;
                break;
            case or:
                logicStr = OR;
                break;
            case not:
                logicStr = NOT;
                break;
            default:
                throw new RuntimeException("使用了不支持的逻辑类型：" + logic);
        }
        return logicStr;
    }

}
