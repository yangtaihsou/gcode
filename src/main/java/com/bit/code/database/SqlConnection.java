package com.bit.code.database;

import com.bit.code.config.mapping.SystemConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection  {

    private final static Logger log = LoggerFactory.getLogger(SqlConnection.class);

    public Connection getConnection() {
        return connection;
    }

    private Connection connection = null;
    public static SqlConnection build(){
        return new SqlConnection();
    }
    public  SqlConnection getSQLConnection(String sqlDriverType,String sqlUrl,String sqlUser,String sqlPass) {
        Connection con = null;
        try {
            String driverClassName = "com.mysql.jdbc.Driver";
            if(!StringUtils.isEmpty(sqlDriverType)){
                // driverClassName =
            }
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(sqlUrl, sqlUser,sqlPass);
        } catch (Exception e) {
            log.info("创建数据库连接异常:"+e.getMessage());
        }
        return this;
    }
    public SqlConnection getSQLConnection(SystemConfig systemConfig) {
        return getSQLConnection(systemConfig.getSqlType(),systemConfig.getSqlUrl(),systemConfig.getUserName(),systemConfig.getUserPass());
    }

    public void close(){
        try {
            if(connection!=null) {
                connection.close();
            }
        } catch (SQLException e) {
            log.info("关闭数据库链接异常:"+e.getMessage());
        }
    }
}
