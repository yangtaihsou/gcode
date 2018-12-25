package com.bit.code.config.mapping;

import java.util.List;
import java.util.Map;

public class TableConfig {
    private String tableName;
    private String className;
    private Map<String, TableColumnConfig> columnConfigMap;
    private List<TableColumnConfig> columnConfigs;
    private Map<String, TableIndexConfig> indexConfigMap;
    private List<TableIndexConfig> indexConfigs;
    /**
     * 是否参与代码生成
     */
    Boolean loadFlag;

    public Boolean getLoadFlag() {
        return loadFlag;
    }

    public void setLoadFlag(Boolean loadFlag) {
        this.loadFlag = loadFlag;
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

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, TableColumnConfig> getColumnConfigMap() {
        return columnConfigMap;
    }

    public void setColumnConfigMap(Map<String, TableColumnConfig> columnConfigMap) {
        this.columnConfigMap = columnConfigMap;
    }

    public List<TableColumnConfig> getColumnConfigs() {
        return columnConfigs;
    }

    public void setColumnConfigs(List<TableColumnConfig> columnConfigs) {
        this.columnConfigs = columnConfigs;
    }

    public List<TableIndexConfig> getIndexConfigs() {
        return indexConfigs;
    }

    public void setIndexConfigs(List<TableIndexConfig> indexConfigs) {
        this.indexConfigs = indexConfigs;
    }

    public Map<String, TableIndexConfig> getIndexConfigMap() {
        return indexConfigMap;
    }

    public void setIndexConfigMap(Map<String, TableIndexConfig> indexConfigMap) {
        this.indexConfigMap = indexConfigMap;
    }
}
