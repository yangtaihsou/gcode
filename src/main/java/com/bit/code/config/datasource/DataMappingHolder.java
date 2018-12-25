package com.bit.code.config.datasource;

import com.bit.code.config.mapping.SystemConfig;
import com.bit.code.config.mapping.CodeTemplateConfig;
import com.bit.code.config.mapping.TableConfig;

import java.util.List;

public interface DataMappingHolder {

    void initMapping(String system);
    /**
     * 得到表对应的类名
     * @param tableName
     * @return
     */
    String getTableClassName(String tableName);

    /**
     * 得到表的某个字段对应的属性名
     * @param tableName
     * @param columnName
     * @return
     */
    String getPropertityNameOfColumn(String tableName,String columnName);

    /**
     * 得到表的某个索引对应的别名
     * @param tableName
     * @param indexName
     * @return
     */
    String getAliasNameOfIndex(String tableName,String indexName);

    /**
     * 系统的配置
     * @return
     */
    SystemConfig getSystemConfig();

    /**
     * 得到代码模板配置
     * @return
     */
    List<CodeTemplateConfig> getCodeTemplateConfigList();


    /**
     * 得到表对应的配置
     * @param tableName
     * @return
     */
    TableConfig getTableConfig(String tableName);
}
