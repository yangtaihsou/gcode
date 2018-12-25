package com.bit.code.config.mapping;

/**
 * 生成代码的模板
 */
public class CodeTemplateConfig {
    private String packagePath;
    private String fileExtendName;
    private String fileType;
    private String srcTargetChildPath;
    /**
     * 模板文件名字
     */
    private String codeTemplateName;
    /**
     * true表示参与代码生成
     * false不参与代码生成
     */
    private Boolean loadFlag = Boolean.TRUE;
    /**
     * false表示库的每个表都会执行生成
     * true表示只会运行一次
     */
    private Boolean simpleFlag = Boolean.FALSE;
    /**
     * 模板文本内容
     */
    private String codeTemplateText;

    /**
     * 文件别名。如果不为空，源文件直接使用此属性命名。适用于单独生成的源文件
     */
    private String aliasFileName;
    /**
     * true:包路径参与文件路径生成
     */
    private Boolean packageIsSrc;

    public String getAliasFileName() {
        return aliasFileName;
    }

    public void setAliasFileName(String aliasFileName) {
        this.aliasFileName = aliasFileName;
    }

    public Boolean getPackageIsSrc() {
        return packageIsSrc;
    }

    public void setPackageIsSrc(Boolean packageIsSrc) {
        this.packageIsSrc = packageIsSrc;
    }

    public String getCodeTemplateName() {
        return codeTemplateName;
    }

    public void setCodeTemplateName(String codeTemplateName) {
        this.codeTemplateName = codeTemplateName;
    }

    public String getCodeTemplateText() {
        return codeTemplateText;
    }

    public void setCodeTemplateText(String codeTemplateText) {
        this.codeTemplateText = codeTemplateText;
    }

    public String getSrcTargetChildPath() {
        return srcTargetChildPath;
    }

    public void setSrcTargetChildPath(String srcTargetChildPath) {
        this.srcTargetChildPath = srcTargetChildPath;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }


    public String getFileExtendName() {
        return fileExtendName;
    }

    public void setFileExtendName(String fileExtendName) {
        this.fileExtendName = fileExtendName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Boolean getLoadFlag() {
        return loadFlag;
    }

    public void setLoadFlag(Boolean loadFlag) {
        this.loadFlag = loadFlag;
    }

    public Boolean getSimpleFlag() {
        return simpleFlag;
    }

    public void setSimpleFlag(Boolean simpleFlag) {
        this.simpleFlag = simpleFlag;
    }
}
