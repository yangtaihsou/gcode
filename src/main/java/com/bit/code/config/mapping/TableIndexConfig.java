package com.bit.code.config.mapping;

public class TableIndexConfig {
    /**
     * 数据库的索引名
     */
    private String indexName;
    /**
     * 数据库的索引名别名
     */
    private String aliasName;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
