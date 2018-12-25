package com.bit.code.config.datasource.local;

import com.bit.code.config.mapping.CodeTemplateConfig;
import com.bit.code.config.mapping.SystemConfig;
import com.bit.code.config.mapping.TableConfig;

import java.util.List;
import java.util.Map;

public abstract class LocalData {
    String systemConfigFile = "\\systemConfig.yml";
    String tableConfigFlie = "\\tableConfig.yml";
    String tempConfig = "\\tempConfig.yml";
    String tempFile = "\\TempFile\\";


    SystemConfig systemConfig;
    List<TableConfig> tableConfigList;
    Map<String, TableConfig> tableConfigMap;
    List<CodeTemplateConfig> localCodeTemplateConfigList;


    /**
     * 获取本地的系统配置
     * @param path
     */
    abstract void loadSystemConfig(String path);

    /**
     * 获取本地的表映射配置
     * @param filePath
     */
    abstract void loadTableConfig(String filePath);

    /**
     *
     * @param filePath
     * @param tempConfig
     * @param tempFile
     */
    abstract void loadTemplateConfig(String filePath, String tempConfig, String tempFile);
}
