package com.bit.code.database;

import com.bit.code.assemble.para.TableIndexColumn;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface DataTableService {
      /**
       * 得到所有表名字
       * @param sqlType
       * @param conn
       * @return
       */
      List<String> getAllTables(String sqlType, Connection conn);

      /**
       * 得到所有表的注释
       * @param sqlType
       * @param conn
       * @return
       */
      Map<String,String> getCommentsOfTable(String sqlType, Connection conn);

      /**
       * 得到某个表的所有字段名
       * @param tableName
       * @param sqlType
       * @param conn
       * @return
       */
      List<String> getColumnNameOfTable(String tableName, String sqlType, Connection conn);

      /**
       * 得到某个表的所有字段和备注
       * @param tableName
       * @param sqlType
       * @param conn
       * @return
       */
      Map<String,String> getColumnCommentsOfTable(String tableName, String sqlType, Connection conn);
      /**
       * 得到某个表的所有字段和原生类型
       * @param tableName
       * @param sqlType
       * @param conn
       * @return
       */
      Map<String,String> getColumnRawTypeOfTable(String tableName, String sqlType, Connection conn);

      /**
       * 得到某个表的所有字段和字段长度(指个数长)
       * @param tableName
       * @param sqlType
       * @param conn
       * @return
       */
      Map<String,Integer> getColumnLengthOfTable(String tableName, String sqlType, Connection conn);

      /**
       * 得到某个表的所有字段和默认值
       * @param tableName
       * @param sqlType
       * @param conn
       * @return
       */
      Map<String,String> getColumnDefaultValueOfTable(String tableName, String sqlType, Connection conn);
      /**
       * 得到某个表的所有字段和备注
       * @param tableName
       * @param sqlType
       * @param conn
       * @return
       */
      Map<String,Boolean> getColumnIsNullAbleOfTable(String tableName, String sqlType, Connection conn);
      /**
       * 得到某个表的所有字段和自动增长属性
       * @param tableName
       * @param sqlType
       * @param conn
       * @return
       */
      Map<String,Boolean> getColumnIsAutoIncreOfTable(String tableName, String sqlType, Connection conn);
      /**
       * 得到某个表的所有索引
       * @param tableName
       * @param sqlType
       * @param conn
       * @return
       */
      List<TableIndexColumn> getTableIndexs(String tableName, String sqlType, Connection conn);
      /**
       * 得到某个表的所有字段和对应的java类型
       * @param tableName
       * @param sqlType
       * @param conn
       * @return
       */
      Map<String,String> getColumnJavaTypeOfTable(String tableName, String sqlType, Connection conn);


}
