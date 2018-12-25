package com.bit.code.assemble.para;

import com.google.common.base.CaseFormat;

import java.util.Map;

public class Field {
    /**
     * vo属性名
     */
    private String propertyName;

    /**
     * 属性名首字母大写
     */
    private String propertyUpName;

    /**
     * 数据库字段对应的属性java全路径
     */
    private String javaFullType;
    /**
     * 数据库字段对应的属性java类型
     */
    private String javaType;
    /**
     * vo的get
     */
    private String getterName;
    /**
     * vo的set
     */
    private String setterName;

    /**
     * 数据库字段名字
     */
    private String columnName;
    /**
     * 数据库字段的类型
     */
    private String jdbcType;

    /**
     * 字段备注
     */
    private String comment;
    /**
     * 长度
     */
    private Integer length;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 是否可以为空
     * true为空。false不为空
     */
    private Boolean emptyFlag;
    /**
     * 是否自增长
     * true自增长。
     */
    private Boolean autoIncreFlag;
    /**
     * 是否是索引字段
     */
    private Boolean indexFlag;
    private String htmlElmentType;

    private String simpleComment;
    private Map<String,String> indexSelectValueMap;



    public void setPropertyName(String propertyName) {
        if(propertyName.contains("_")) {
            propertyName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, propertyName);
        }
        if(propertyName.contains("-")) {
            propertyName = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, propertyName);
        }
        this.propertyName = propertyName;
        setGetterName(this.propertyName);
        setSetterName(this.propertyName);
        setPropertyUpName(this.propertyName);
    }
    public void setPropertyUpName(String propertyUpName) {
        this.propertyUpName = propertyUpName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }
    public void setGetterName(String propertyName) {
        this.getterName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }
    public void setSetterName(String propertyName) {
        this.setterName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    public void setJavaType(String javaType) {

        this.javaType = javaType.substring(javaType.lastIndexOf(".") + 1);
        if(("Timestamp").equals(this.javaType)){
            this.javaType = "Date";
        }
    }

    public void setJavaFullType(String javaFullType) {
        if(("java.sql.Timestamp").equals(javaFullType)) {
            javaFullType = "java.util.Date";
        }
        this.javaFullType = javaFullType;
    }

    public String getPropertyUpName() {
        return propertyUpName;
    }



    public Boolean getAutoIncreFlag() {
        return autoIncreFlag;
    }

    public void setAutoIncreFlag(Boolean autoIncreFlag) {
        this.autoIncreFlag = autoIncreFlag;
    }

    public Boolean getEmptyFlag() {
        return emptyFlag;
    }

    public void setEmptyFlag(Boolean emptyFlag) {
        this.emptyFlag = emptyFlag;
    }
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getJavaType() {
        return javaType;
    }


    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getGetterName() {
        return getterName;
    }


    public String getSetterName() {
        return setterName;
    }



    public String getJavaFullType() {
        return javaFullType;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean isIndexFlag() {
        return indexFlag;
    }

    public void setIndexFlag(Boolean indexFlag) {
        this.indexFlag = indexFlag;
    }

    public String getHtmlElmentType() {
        return htmlElmentType;
    }

    public void setHtmlElmentType(String htmlElmentType) {
        this.htmlElmentType = htmlElmentType;
    }

    public String getSimpleComment() {
        return simpleComment;
    }

    public void setSimpleComment(String simpleComment) {
        this.simpleComment = simpleComment;
    }

    public Map<String, String> getIndexSelectValueMap() {
        return indexSelectValueMap;
    }

    public void setIndexSelectValueMap(Map<String, String> indexSelectValueMap) {
        this.indexSelectValueMap = indexSelectValueMap;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getIndexFlag() {
        return indexFlag;
    }
}
