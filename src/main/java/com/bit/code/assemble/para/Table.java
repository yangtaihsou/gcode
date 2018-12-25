package com.bit.code.assemble.para;

import java.util.List;
import java.util.Map;

public class Table {
    /**
     * 表名
     */
    String tableName;
    /**
     * 表的程序名字
     */
    String className;
    /**
     * 表的程序变量名
     */
    String instName;
    /**
     * 表的注释
     */
    String tableComment;

    /**
     * 表的字段属性列表集合
     */
    List<Field> fieldList;
    /**
     * 表的字段属性map集合
     */
    Map<String,Field> fieldMap;

    /**
     * 表的唯一建索引集合。包含唯一索引（不含主键）。每个索引名对应至少一个字段。
     * map的key为索引名字。
     */
    Map<String,List<Field>>  uniqIndexFieldGroup;

    /**
     * 表的普通索引集合。包含普通索引。每个索引名对应至少一个字段。
     * map的key为索引名字。
     */
    Map<String,List<Field>>  commonIndexFieldGroup;


    /**
     * 表的主键字段。当没有主键或者主键对应多个字段，则为空。
     */
    Field primaryField;


    /**
     * 表的主键对应字段集合
     */
    List<Field> primaryKeyFieldList;

    /**
     * 表的唯一索引。默认处理只对应一个字段的索引
     */
    List<Field> uniqIndexForOneFieldList;

    /**
     * 表的索引集合。主键、唯一索引、普通索引都在里面
     */
    List<TableIndex> indexList;


    public void setClassName(String className) {
        this.className = className;
        this.instName = className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    public List<TableIndex> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<TableIndex> indexList) {
        this.indexList = indexList;
    }

    public List<Field> getUniqIndexForOneFieldList() {
        return uniqIndexForOneFieldList;
    }

    public void setUniqIndexForOneFieldList(List<Field> uniqIndexForOneFieldList) {
        this.uniqIndexForOneFieldList = uniqIndexForOneFieldList;
    }

    public List<Field> getPrimaryKeyFieldList() {
        return primaryKeyFieldList;
    }

    public void setPrimaryKeyFieldList(List<Field> primaryKeyFieldList) {
        this.primaryKeyFieldList = primaryKeyFieldList;
    }
    public Map<String, List<Field>> getUniqIndexFieldGroup() {
        return uniqIndexFieldGroup;
    }

    public void setUniqIndexFieldGroup(Map<String, List<Field>> uniqIndexFieldGroup) {
        this.uniqIndexFieldGroup = uniqIndexFieldGroup;
    }

    public Map<String, List<Field>> getCommonIndexFieldGroup() {
        return commonIndexFieldGroup;
    }

    public void setCommonIndexFieldGroup(Map<String, List<Field>> commonIndexFieldGroup) {
        this.commonIndexFieldGroup = commonIndexFieldGroup;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }



    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }


    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, Field> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public Field getPrimaryField() {
        return primaryField;
    }

    public void setPrimaryField(Field primaryField) {
        this.primaryField = primaryField;
    }


}
