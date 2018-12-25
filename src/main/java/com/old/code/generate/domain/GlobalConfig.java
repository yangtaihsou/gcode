package com.old.code.generate.domain;

/**
 * User: yangkuan@old.com
 * Date: 15-3-20
 * Time: 下午4:01
 */
public class GlobalConfig {
    /**生成文件的存储根目录**/
    private String outputPath;
    /**数据库类型，如mysql**/
    private String sqlType;
    /**数据库驱动**/
    private String slqDriver;
    /**数据库链接**/
    private String sqlUrl;
    /**数据库用户**/
    private String sqlUser;
    /**数据库密码**/
    private String sqlPass;

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getSlqDriver() {
        return slqDriver;
    }

    public void setSlqDriver(String slqDriver) {
        this.slqDriver = slqDriver;
    }

    public String getSqlUrl() {
        return sqlUrl;
    }

    public void setSqlUrl(String sqlUrl) {
        this.sqlUrl = sqlUrl;
    }

    public String getSqlUser() {
        return sqlUser;
    }

    public void setSqlUser(String sqlUser) {
        this.sqlUser = sqlUser;
    }

    public String getSqlPass() {
        return sqlPass;
    }

    public void setSqlPass(String sqlPass) {
        this.sqlPass = sqlPass;
    }
}
