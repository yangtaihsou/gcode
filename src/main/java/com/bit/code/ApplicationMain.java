package com.bit.code;

import com.bit.code.assemble.service.VariableParaServiceImpl;
import com.bit.code.config.mapping.CodeTemplateConfig;
import com.bit.code.config.datasource.DataMappingHolder;
import com.bit.code.config.datasource.local.LocalDataConfigHolder;
import com.bit.code.assemble.para.Table;
import com.bit.code.util.BuilderUtils;
import com.bit.code.util.CodeTemplateUtils;
import com.bit.code.util.FileReadUtils;
import com.bit.code.util.FileWriteUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationMain {
    /**
     * 配置初始目录
     */
    static String path = "D:\\jd\\gcode\\Template\\pay-database";;
    public static void main(String[] args) throws IOException {
        DataMappingHolder holder = getDataMappingHolder();
        List<Table> tables = VariableParaServiceImpl.build().wrapTablePara(holder);
        List<CodeTemplateConfig> tempList = holder.getCodeTemplateConfigList();
        if(tempList.isEmpty()||tables.isEmpty()) {
            return;
        }
        //构建项目的源码目录
        FileWriteUtils fileWriteUtils = new FileWriteUtils()
                .mkTargetSrcDir(holder.getSystemConfig().getOutputPath()+ File.separator+holder.getSystemConfig().getSysName());
        //构建项目的压缩包
        fileWriteUtils.buildZipOs(holder.getSystemConfig().getSysName());
        for(CodeTemplateConfig codeTemplateConfig :tempList){
            for(Table table:tables){
                    //生成源码文本
                   String codeText = CodeTemplateUtils.render(table,codeTemplateConfig, codeTemplateConfig.getCodeTemplateText());
                   //得到源码文件根目录里的全路径
                   String srcFileFullPath = BuilderUtils.buildSrcFileFullPath(table,codeTemplateConfig,holder.getSystemConfig());
                   fileWriteUtils.zip(srcFileFullPath,codeText);
                   fileWriteUtils.writeSrcFile(
                           BuilderUtils.buildSrcFileDirPath(codeTemplateConfig,holder.getSystemConfig()),
                           BuilderUtils.buildSrcFileName(table,codeTemplateConfig),
                           codeText);
                }
        }
        fileWriteUtils.closeZipOs();
    }
    public static DataMappingHolder getDataMappingHolder(){
        List<File> fileList = FileReadUtils.listFile(path);
        String[] configDirectory = {"systemConfig.yml","tableConfig.yml","tempConfig.yml","TempFile"};
        List<String> configDirectoryList = Arrays.stream(configDirectory).collect(Collectors.toList());
        if(fileList.stream()
                .filter(x->configDirectoryList.contains(x.getName()))
                .count()!=4){
            System.out.println("配置文件不对");
            return null;
        }
        LocalDataConfigHolder localData = new LocalDataConfigHolder();
        localData.initMapping(path);
        return localData;
    }
}
