package com.bit.code.config.datasource.local;

import com.alibaba.fastjson.JSON;
import com.bit.code.config.mapping.SystemConfig;
import com.bit.code.config.mapping.TableConfig;
import com.bit.code.config.mapping.CodeTemplateConfig;
import com.bit.code.config.datasource.DataMappingHolder;
import com.bit.code.util.FileReadUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LocalDataConfigHolder extends LocalData implements DataMappingHolder {


    Charset gbk_charset = Charset.forName("GBK");
    Charset utf_charset = Charset.forName("UTF-8");
    Yaml yaml = new Yaml();


    @Override
    public String getTableClassName(String tableName) {
        return tableConfigMap.get(tableName) == null ? null : tableConfigMap.get(tableName).getClassName();
    }


    @Override
    public String getPropertityNameOfColumn(String tableName, String columnName) {
        TableConfig tableConfig = tableConfigMap.get(tableName);
        if (tableConfig == null) {
            return null;
        }
        return tableConfig.getColumnConfigMap() == null ? null : tableConfig.getColumnConfigMap().get(columnName).getPropertyName();
    }

    @Override
    public String getAliasNameOfIndex(String tableName, String indexName) {
        TableConfig tableConfig = tableConfigMap.get(tableName);
        if (tableConfig == null) {
            return null;
        }
        return tableConfig.getIndexConfigMap() == null ? null : tableConfig.getIndexConfigMap().get(indexName).getAliasName();
    }

    @Override
    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    @Override
    public List<CodeTemplateConfig> getCodeTemplateConfigList() {
        return localCodeTemplateConfigList;
    }

    @Override
    public TableConfig getTableConfig(String tableName) {
        return tableConfigMap.get(tableName);
    }

    @Override
    public void initMapping(String systemPath) {
        this.loadSystemConfig(systemPath + systemConfigFile);
        this.loadTableConfig(systemPath + tableConfigFlie);
        this.loadTemplateConfig(systemPath, tempConfig, tempFile);
    }


    @Override
    public void loadSystemConfig(String filePath) {
        Object configList = yaml.load(FileReadUtils.readContext(filePath, gbk_charset));
        String configJsonText = JSON.toJSONString(configList);
        systemConfig = JSON.parseObject(configJsonText, SystemConfig.class);
    }

    @Override
    public void loadTableConfig(String filePath) {
        List configList = yaml.loadAs(FileReadUtils.readContext(filePath, gbk_charset), List.class);
        String configJsonText = JSON.toJSONString(configList);
        List<TableConfig> tableConfigList = JSON.parseArray(configJsonText, TableConfig.class);
        for (TableConfig tableConfig : tableConfigList) {
            tableConfig.setColumnConfigMap(tableConfig.getColumnConfigs().stream().collect(Collectors.toMap(x -> x.getColumnName(), x -> x)));
            if(tableConfig.getIndexConfigs()!=null) {
                tableConfig.setIndexConfigMap(tableConfig.getIndexConfigs().stream().collect(Collectors.toMap(x -> x.getIndexName(), x -> x)));
            }
        }
        tableConfigMap = tableConfigList.stream().distinct().filter(x -> x.getLoadFlag()).collect(Collectors.toMap(x -> x.getTableName(), x -> x));
    }

    @Override
    public void loadTemplateConfig(String filePath, String tempConfig, String tempFile) {
        List configList = yaml.loadAs(FileReadUtils.readContext(filePath + tempConfig, gbk_charset), List.class);
        String configJsonText = JSON.toJSONString(configList);
        List<CodeTemplateConfig> templateConfigList = JSON.parseArray(configJsonText, CodeTemplateConfig.class);
        localCodeTemplateConfigList = templateConfigList.stream().filter(x -> x.getLoadFlag()).collect(Collectors.toList());
        for (CodeTemplateConfig codeTemplateConfig : localCodeTemplateConfigList) {
            codeTemplateConfig.setCodeTemplateText(FileReadUtils.readContext(filePath + tempFile + codeTemplateConfig.getCodeTemplateName(), utf_charset));
        }
    }

    @Deprecated
    public void loadTableConfigOld(String filePath) {
        String fileTypeReg = ".*(.pro|.properties)$";
        List<File> files = FileReadUtils.listFile(filePath, fileTypeReg);
        /**
         * 表字段名和java属性名映射
         * key是表的字段名,value表示类的属性（用驼峰表示）
         */
        Map<String, String> columnConfig = FileReadUtils.readProperties(files.stream().filter(x -> x.getName().equals("columnConfig.properties")).findFirst().get().getAbsolutePath(), utf_charset);
        /**
         * 表的类名映射
         * key是表名,value表示类名
         */
        Map<String, String> tableListConfig = FileReadUtils.readProperties(files.stream().filter(x -> x.getName().equals("tableConfig.properties")).findFirst().get().getAbsolutePath(), utf_charset);
        tableConfigMap = new HashMap();
        tableConfigList = tableListConfig.entrySet().stream().map(x -> {
            TableConfig tableConfig = new TableConfig();
            tableConfig.setTableName(x.getKey());
            tableConfig.setClassName(x.getValue());
            //本地把所有库的所有字段映射塞进去
            //tableConfig.setColumnConfigMap(columnConfig);
            tableConfigMap.put(x.getKey(), tableConfig);
            return tableConfig;
        }).collect(Collectors.toList());

    }


    @Deprecated
    public void loadTemplateConfigOld(String filePath, String tempConfig, String tempFile) {
        localCodeTemplateConfigList = new ArrayList();
        String fileTypeReg = ".*(.properties)$";
        List<File> files = FileReadUtils.listFile(filePath + tempConfig, fileTypeReg);
        for (File file : files) {
            CodeTemplateConfig fileConfig = new CodeTemplateConfig();
            Map<String, String> props = FileReadUtils.readProperties(file.getAbsolutePath(), utf_charset);
            if (props.get("loadFlag") != null && !props.get("loadFlag").isEmpty()) {
                fileConfig.setLoadFlag(Boolean.valueOf(props.get("loadFlag")));
            }
            if (!fileConfig.getLoadFlag()) {
                //不参与代码生成
                continue;
            }
            fileConfig.setPackagePath(props.get("packagePath"));
            fileConfig.setFileExtendName(props.get("fileExtendName"));
            fileConfig.setFileType(props.get("fileType"));
            fileConfig.setSrcTargetChildPath(props.get("srcTargetChildPath"));
            if (props.get("simpleFlag") != null && !props.get("simpleFlag").isEmpty()) {
                fileConfig.setSimpleFlag(Boolean.valueOf(props.get("simpleFlag")));
            }
            localCodeTemplateConfigList.add(fileConfig);
            String codeTemplateName = props.get("codeTemplateName");
            fileConfig.setCodeTemplateText(FileReadUtils.readContext(filePath + tempFile + codeTemplateName, utf_charset));
        }
    }
}
