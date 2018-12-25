package com.bit.code.database;

import com.bit.code.assemble.para.IndexType;
import com.bit.code.util.Contants;
import com.bit.code.assemble.para.TableIndexColumn;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;


public class DataTableServiceImpl implements DataTableService {
    private final Logger log = LoggerFactory.getLogger(SqlConnection.class);
    @Override
    public List<String> getAllTables(String sqlType, Connection conn) {
        List<String> alltables = new ArrayList();
        try {
            String showTablesSql = "SHOW TABLES";
            if (!StringUtils.isEmpty(sqlType)) {
                if (sqlType.equals(Contants.MSSQL)) {
                    showTablesSql = "select [name] from sysobjects where xtype='U'";
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(showTablesSql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                alltables.add(rs.getString(1));
            }
        } catch (Exception e) {
            log.error("获取数据库的表列表异常:" + e.getMessage());
        }
        return alltables;
    }

    @Override
    public Map<String,String> getCommentsOfTable(String sqlType, Connection conn){
        Map<String,String> commentMap = new LinkedHashMap<String, String>();
        try {
            String showSql = "SELECT TABLE_NAME , TABLE_COMMENT  FROM information_schema.tables WHERE table_schema = (SELECT DATABASE())";
            if (!StringUtils.isEmpty(sqlType)) {
                if (sqlType.equals(Contants.MSSQL)) {
                    showSql = "";
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(showSql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString(1);
                String columnComment = rs.getString(2);
                commentMap.put(columnName,columnComment);
            }
        } catch (Exception e) {
            log.error("获取数据库的表注释异常:" + e.getMessage());
        }
        return commentMap;
    }

    @Override
    public List<String> getColumnNameOfTable(String tableName, String sqlType, Connection conn){
        List<String> columnList = new ArrayList<>();
        try {
            String showTablesSql = "SELECT COLUMN_NAME FROM information_schema.columns WHERE   table_schema = (SELECT DATABASE()) AND  table_name = '"+tableName+"' ";
            if (!StringUtils.isEmpty(sqlType)) {
                if (sqlType.equals(Contants.MSSQL)) {
                    showTablesSql = "";
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(showTablesSql);
            ResultSet rs = pstmt.executeQuery();

            //System.out.println("- tableName: "+tableName);
            //System.out.println("  className: "+tableName);
           //System.out.println("  columnConfigs: ");

            while (rs.next()) {
                String columnName = rs.getString(1);
               // System.out.println("   - columnName: "+columnName);
                //System.out.println("     propertyName: "+columnName);
                columnList.add(columnName);
            }
        } catch (Exception e) {
            log.error("获取数据库的表"+tableName+"的字段注释异常:" + e.getMessage());
        }
        return columnList;
    }

    @Override
    public Map<String,String> getColumnCommentsOfTable(String tableName, String sqlType, Connection conn){
        Map<String,String> commentMap = new LinkedHashMap<String, String>();
        try {
            String showTablesSql = "SELECT COLUMN_NAME,COLUMN_COMMENT FROM information_schema.columns WHERE   table_schema = (SELECT DATABASE()) AND  table_name = '"+tableName+"' ";
            if (!StringUtils.isEmpty(sqlType)) {
                if (sqlType.equals(Contants.MSSQL)) {
                    showTablesSql = "";
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(showTablesSql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString(1);
                String columnComment = rs.getString(2);
                commentMap.put(columnName,columnComment);
            }
        } catch (Exception e) {
            log.error("获取数据库的表"+tableName+"的字段注释异常:" + e.getMessage());
        }
        return commentMap;
    }

    @Override
    public Map<String,String> getColumnRawTypeOfTable(String tableName, String sqlType, Connection conn){
        Map<String,String> commentMap = new LinkedHashMap<String, String>();
        try {
            String sql = "SELECT COLUMN_NAME,DATA_TYPE FROM information_schema.columns WHERE    table_schema = (SELECT DATABASE()) AND table_name = '"+tableName+"' ";
            if (!StringUtils.isEmpty(sqlType)) {
                if (sqlType.equals(Contants.MSSQL)) {
                    sql = "";
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString(1);
                String column2 = rs.getString(2);
                commentMap.put(columnName,column2);
            }
        } catch (Exception e) {
            log.error("获取数据库的表"+tableName+"的字段类型异常:" + e.getMessage());
        }
        return commentMap;
    }

    @Override
    public Map<String,Integer> getColumnLengthOfTable(String tableName, String sqlType, Connection conn){
        Map<String,Integer> map = new LinkedHashMap();
        try {
            String sql = "SELECT COLUMN_NAME,CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE    table_schema = (SELECT DATABASE()) AND table_name = '"+tableName+"' ";
            if (!StringUtils.isEmpty(sqlType)) {
                if (sqlType.equals(Contants.MSSQL)) {
                    sql = "";
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString(1);
                String column2 = rs.getString(2);
                if(column2==null){
                    map.put(columnName,null);
                }else {
                    map.put(columnName,Integer.valueOf(column2));
                }
            }
        } catch (Exception e) {
            log.error("获取数据库的表"+tableName+"的字段长度异常:" + e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String,String> getColumnDefaultValueOfTable(String tableName, String sqlType, Connection conn){
        Map<String,String> map = new LinkedHashMap<String, String>();
        try {
            String sql = "SELECT COLUMN_NAME,COLUMN_DEFAULT FROM information_schema.columns WHERE    table_schema = (SELECT DATABASE()) AND table_name = '"+tableName+"' ";
            if (!StringUtils.isEmpty(sqlType)) {
                if (sqlType.equals(Contants.MSSQL)) {
                    sql = "";
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString(1);
                String column2 = rs.getString(2);
                map.put(columnName,column2);
            }
        } catch (Exception e) {
            log.error("获取数据库的表"+tableName+"的字段默认值异常:" + e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String,Boolean> getColumnIsNullAbleOfTable(String tableName, String sqlType, Connection conn){
        Map<String,Boolean> map = new LinkedHashMap();
        try {
            String sql = "SELECT COLUMN_NAME,IS_NULLABLE FROM information_schema.columns WHERE    table_schema = (SELECT DATABASE()) AND table_name = '"+tableName+"' ";
            if (!StringUtils.isEmpty(sqlType)) {
                if (sqlType.equals(Contants.MSSQL)) {
                    sql = "";
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString(1);
                String column2 = rs.getString(2);
                map.put(columnName,this.buildEmptyFlag(sqlType,column2));
            }
        } catch (Exception e) {
            log.error("获取数据库的表"+tableName+"的字段是否为空异常:" + e.getMessage());
        }
        return map;
    }
    @Override
    public Map<String,Boolean> getColumnIsAutoIncreOfTable(String tableName, String sqlType, Connection conn){
        Map<String,Boolean> map = new LinkedHashMap();
        try {
            String sql = "SELECT COLUMN_NAME, EXTRA FROM information_schema.columns WHERE    table_schema = (SELECT DATABASE()) AND table_name = '"+tableName+"' ";
            if (!StringUtils.isEmpty(sqlType)) {
                if (sqlType.equals(Contants.MSSQL)) {
                    sql = "";
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString(1);
                String column2 = rs.getString(2);
                map.put(columnName,this.buildAutoIncreFlag(sqlType,column2));
            }
        } catch (Exception e) {
            log.error("获取数据库的表"+tableName+"的字段是否为空异常:" + e.getMessage());
        }
        return map;
    }
    public Boolean buildAutoIncreFlag(String sqlType,String autoIncreFlag) {
        if((Contants.MYSQL).equals(sqlType)){
            if(autoIncreFlag!=null&&"auto_increment".equals(autoIncreFlag)){
                return Boolean.TRUE;
            }
        }else{

        }
        return Boolean.FALSE;
    }
    public Boolean buildEmptyFlag(String sqlType,String emptyFlag) {
        if((Contants.MYSQL).equals(sqlType)){
            if("NO".equals(emptyFlag)){
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
    @Override
    public List<TableIndexColumn> getTableIndexs(String tableName, String sqlType, Connection conn){
        List<TableIndexColumn> tableIndexColumnList = new ArrayList();
        try {
            String showTablesSql = "SHOW INDEX FROM "+tableName;
            if (!StringUtils.isEmpty(sqlType)) {
                if (sqlType.equals(Contants.MSSQL)) {
                    showTablesSql = "";
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(showTablesSql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String uniqueFlag = rs.getString(2);
                String keyName = rs.getString(3);
                String seq = rs.getString(4);
                String columnName = rs.getString(5);
                TableIndexColumn tableIndexColumn = new TableIndexColumn();
                tableIndexColumn.setColumnName(columnName);

                tableIndexColumn.setIndexType(uniqueFlag.equals("1")?IndexType.COMMONKEY :IndexType.UNIQKEY);
                //如果索引是主键，mysql的的key是PRIMARY
                tableIndexColumn.setIndexName(keyName);
                if(keyName.equals("PRIMARY")){
                    tableIndexColumn.setIndexType(IndexType.PRIMARYKEY);
                }

                tableIndexColumn.setSeq(Integer.parseInt(seq));
                tableIndexColumnList.add(tableIndexColumn);
            }
        } catch (Exception e) {
            log.error("获取数据库的表"+tableName+"的索引异常:" + e.getMessage());
        }
        return tableIndexColumnList;
    }

    @Override
    public Map<String,String> getColumnJavaTypeOfTable(String tableName, String sqlType, Connection conn){
        Map<String,String> map = new LinkedHashMap<String, String>();
        try {
            String sql ="select * from " + tableName + " limit 1";
            if (!StringUtils.isEmpty(sqlType)) {
                if (sqlType.equals(Contants.MSSQL)) {
                    sql = "";
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            // demoResultSet(conn,tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            // 表的字段数量
            int columnCounts = rsmd.getColumnCount();

           // System.out.println("表名="+tableName);
            for (int i = 1; i <= columnCounts; i++) {
                String javaFullType = rsmd.getColumnClassName(i);

                //数据库字段属性类型
                String jdbcType = rsmd.getColumnTypeName(i);
                String columnName = rsmd.getColumnName(i);
                map.put(columnName,javaFullType);
            }
        } catch (Exception e) {
            log.error("获取数据库的表"+tableName+"的字段java类型异常:" + e.getMessage());
        }
        return map;
    }


}
