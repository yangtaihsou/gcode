package com.bit.code.util;

import com.bit.code.assemble.para.Table;
import com.bit.code.config.mapping.CodeTemplateConfig;
import com.bit.code.config.mapping.SystemConfig;

import java.io.File;
import java.util.Optional;

public class BuilderUtils {
    public static String buildSrcFileFullPath(Table table, CodeTemplateConfig codeTemplateConfig, SystemConfig systemConfig){
        return buildSrcFileDirPath(codeTemplateConfig,systemConfig)+buildSrcFileName(table,codeTemplateConfig);
    }

    public static String buildSrcFileDirPath(CodeTemplateConfig codeTemplateConfig , SystemConfig systemConfig){
        String packagePath = "";
        //指定了标志，如果不参与，设置为空字符串。
        if(!codeTemplateConfig.getPackageIsSrc()){
            packagePath = "";
        }
        //java文件默认，包路径参与源文件路径计算。
        if("java".equals(codeTemplateConfig.getFileType())||codeTemplateConfig.getPackageIsSrc()){
            packagePath = codeTemplateConfig.getPackagePath()==null?packagePath:codeTemplateConfig.getPackagePath();
            packagePath = packagePath.replace(".",File.separator);
        }
        StringBuffer srcFileFullPath = new StringBuffer()
                .append(File.separator)
                .append(codeTemplateConfig.getSrcTargetChildPath())
                .append(File.separator)
                .append(packagePath)
                .append(packagePath.isEmpty()?"":File.separator);
        return srcFileFullPath.toString();
    }

    public static String buildSrcFileName(Table table, CodeTemplateConfig codeTemplateConfig ){
        String fileName = table.getClassName()+ Optional.ofNullable(codeTemplateConfig.getFileExtendName()).orElse("");
        if(codeTemplateConfig.getAliasFileName()!=null&&!codeTemplateConfig.getAliasFileName().isEmpty()){
            fileName = codeTemplateConfig.getAliasFileName();
        }
        StringBuffer srcFileFullPath = new StringBuffer()
                .append(fileName)
                .append(".")
                .append(codeTemplateConfig.getFileType());
        return srcFileFullPath.toString();
    }
}
