package com.bit.code.config.mapping;

public class TableColumnConfig {
    /**
     * 数据库字段名字
     */
    private String columnName;
    /**
     * 数据库字段映射的程序名
     */
    private String propertyName;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
