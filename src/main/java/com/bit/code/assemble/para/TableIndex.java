package com.bit.code.assemble.para;

import java.util.List;
import java.util.Map;

/**
 * 索引字段
 */
public class TableIndex {
    /**
     * 索引名字
     */
    String indexName;
    /**
     * 是否为唯一索引
     */
    IndexType indexType;

    /**
     * 表的字段属性列表集合
     */
    List<Field> fieldList;
    /**
     * 表的字段属性map集合
     */
    Map<String,Field> fieldMap;
    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public IndexType getIndexType() {
        return indexType;
    }

    public void setIndexType(IndexType indexType) {
        this.indexType = indexType;
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
}
