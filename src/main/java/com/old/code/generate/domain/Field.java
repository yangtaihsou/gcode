package com.old.code.generate.domain;

import java.util.Map;

public class Field {

    /*
     * 转化为javabean的属性
     */
    private String propertyName;
    /*
 * 转化为javabean的get后面属性。开头大写
 */
    private String propertyNameForGetAndSet;
    /*
     * 数据库字
     */
    private String fieldName;
    private String javaFullType;
    private String javaType;
    private String jdbcType;
    private String getterName;
    private String setterName;
    private String comment;

    private Boolean indexFlag;//是否是索引字段

    private String htmlElmentType;

    private int length;


    private String simpleComment;//简单的注释。如 字段的注释是 "用户类型。1、小学生,2、大学生"，以"。"分拆，"用户类型"是简单注释。
    private Map<String,String> indexSelectValueMap;//当字段是主索引，且这个索引只有这个字段时，将这个字段作为字典表，映射到html就是select组件

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
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

    public void setGetterName(String getterName) {
        this.getterName = getterName;
    }

    public String getSetterName() {
        return setterName;
    }

    public void setSetterName(String setterName) {
        this.setterName = setterName;
    }

    public String getJavaFullType() {
        return javaFullType;
    }

    public void setJavaFullType(String javaFullType) {
        this.javaFullType = javaFullType;
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

    public String getPropertyNameForGetAndSet() {
        return propertyNameForGetAndSet;
    }

    public void setPropertyNameForGetAndSet(String propertyNameForGetAndSet) {
        this.propertyNameForGetAndSet = propertyNameForGetAndSet;
    }
}
