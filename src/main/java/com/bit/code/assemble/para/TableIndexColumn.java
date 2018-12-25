package com.bit.code.assemble.para;

/**
 * 索引字段
 */
public class TableIndexColumn {
    /**
     * 索引名字
     */
    String indexName;
    /**
     * 字段名字
     */
    String columnName;
    /**
     * 是否为唯一索引
     */
    IndexType indexType;
    /**
     * 在索引中的顺序
     */
    Integer seq;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public IndexType getIndexType() {
        return indexType;
    }

    public void setIndexType(IndexType indexType) {
        this.indexType = indexType;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
