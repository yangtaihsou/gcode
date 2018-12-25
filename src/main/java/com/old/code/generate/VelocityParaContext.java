package com.old.code.generate;

import com.bit.code.util.Contants;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.old.code.generate.domain.Field;

import java.sql.*;
import java.util.*;

import static com.old.code.generate.util.Utils.readProperties;

/**
 * User: yangkuan@old.com
 * Date: 15-1-13
 * Time: 下午4:34
 */
public class VelocityParaContext {

    Map<String, Object> commonContext = new HashMap<String, Object>();


    static  Map<String, Field> fieldsMap = new HashMap<String, Field>();//key是表字段名字

    static Map<String,String> paraMap = new HashMap();

    static  Map<String, Field> indexfieldsMap = new HashMap<String, Field>();//key是表字段名字

    static Map<String,String> classNameMap = new HashMap<String,String>();//key是表名,value表示类名
    static Map<String,String> instNameMap = new HashMap<String,String>();//key是表名,value表示类的变量名

    static Map<String,String> camelFieldNameMap = new HashMap<String,String>();//key是表的字段名,value表示类的属性（用驼峰表示）


    static {
        readProperties("DB2ClassMapping/classNameMapping.properties", classNameMap);
        readProperties("DB2ClassMapping/classVarNameMapping.properties", instNameMap);
        readProperties("DB2ClassMapping/ClassFieldNameMapping.properties", camelFieldNameMap);
    }

    /*
	 * 生成velocity模版里的参数
	 */
    public  Map<String, Object> generateTable2VelocityParameter(String sqlserverType, Connection conn, String tableName) throws SQLException {
        commonContext.clear();


        Set<String> imports = new HashSet<String>();
        List<Field> fields = new ArrayList<Field>();
        commonContext.put(Constant.CLASS_NAME, classNameMap.get(tableName)==null?tableName:classNameMap.get(tableName));
        commonContext.put(Constant.TABLE_NAME, tableName);
        commonContext.put(Constant.INST_NAME, instNameMap.get(tableName)==null?tableName:instNameMap.get(tableName));
        // commonContext.put(PK_ID, pkId);
        String pkField = null;
        List<String> commentList = null;
        String sql = null;
        if (sqlserverType.equals(Contants.MSSQL)) {
            pkField = getMSSQLPkField(conn, tableName);
            sql = "select top 1 * from [" + tableName + "]";
        } else if (sqlserverType.equals(Contants.MYSQL)) {
            pkField = getMYSQLPkField(conn, tableName);

            commentList = getMysqlCommentList(conn, tableName);
            sql = "select * from " + tableName + " limit 1";
        }
        if(pkField!=null) {
            commonContext.put(Constant.PK_ID, pkField);
            commonContext.put(Constant.PK_NAME, pkField.substring(0, 1).toLowerCase() + pkField.substring(1));
        }
        String pkType = null;
        String pkJavaFullType = null;
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
       // demoResultSet(conn,tableName);
        ResultSetMetaData rsmd = rs.getMetaData();
        // 表的字段数量
        int columnCounts = rsmd.getColumnCount();

        System.out.println("表名="+tableName);
        for (int i = 1; i <= columnCounts; i++) {

            String name = rsmd.getColumnName(i);
            String columnClassName = rsmd.getColumnClassName(i);
            String fdname = name;
            String javaType = columnClassName.substring(columnClassName.lastIndexOf(".") + 1);
            boolean isPk = (pkField != null && pkField.equalsIgnoreCase(fdname)) || (pkField == null && i == 1);
            if (isPk) {
                pkType = javaType;
                pkJavaFullType = columnClassName;
            }

            Field field = new Field();

            field.setFieldName(name);
            //System.out.println(name + "=" + name);
            paraMap.put(name,name);
            String fieldName = camelFieldName(name);
            field.setPropertyName(fieldName);
            field.setPropertyNameForGetAndSet(fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            field.setSetterName("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            field.setGetterName("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            field.setJavaType(javaType);
            if(javaType.equals("Timestamp")){
                field.setJavaType("Date");
            }
            field.setJdbcType(rsmd.getColumnTypeName(i));
            String javaFullType = rsmd.getColumnClassName(i);
            field.setJavaFullType(javaFullType);
            if(javaFullType.equals("java.sql.Timestamp")) {
                field.setJavaFullType("java.util.Date");
            }
            field.setComment(commentList.get(i - 1));
            field.setSimpleComment(field.getComment());

            try {
                String[] simpleCommnetStrs = field.getSimpleComment().split("\\.");
                if (simpleCommnetStrs.length > 1) {
                    Map<String,String> dictionayMap = new HashMap<String, String>();
                    field.setSimpleComment(simpleCommnetStrs[0]);
                    String[] indexDictionaryFields = simpleCommnetStrs[1].split("\\,");
                    for(String indexDictionary:indexDictionaryFields){
                        String[] dictionary = indexDictionary.split("\\:");
                        dictionayMap.put(dictionary[0],dictionary[1]);
                    }
                    field.setIndexSelectValueMap(dictionayMap);
                }
            }catch (Exception e){
                System.out.println("字段的注释不符合格式要求" + e.getMessage());
            }

            if(field.getJdbcType().equals("DATETIME")||field.getJdbcType().equals("DATE")){
                field.setHtmlElmentType("date");
            }else if(field.getJdbcType().contains("TEXT")){
                field.setHtmlElmentType("textarea");
            }else{
                field.setHtmlElmentType("input");

            }



            fields.add(field);
            fieldsMap.put(name,field);
            if (field.getJavaFullType().indexOf("java.lang") == -1) {
                imports.add(field.getJavaFullType());
            }
        }

        getMYSQLIndexField(conn, tableName);
        getMYSQLTableComment(conn, tableName);
        commonContext.put(Constant.FIELDS, fields);
        commonContext.put(Constant.PK_TYPE, pkType);
        commonContext.put(Constant.IMPORTS, imports);
        commonContext.put(Constant.indexFields, setFieldIndexFlag(tableName, fields));
        commonContext.put(Constant.AUTHOR, "yangkuan@old.com");
        commonContext.put("result", "result");
        commonContext.put(Constant.pk_JavaFullType, pkJavaFullType);
        return commonContext;


    }


    public static void demoResultSet(Connection con,String tableName) throws SQLException {
        ResultSet colrs = con.getMetaData().getColumns(null, "%", tableName, "%");

        while(colrs.next()){
            System.out.println("TABLE_CAT" + "===" + colrs.getString("TABLE_CAT"));
            System.out.println("TABLE_SCHEM" + "===" + colrs.getString("TABLE_SCHEM"));
            System.out.println("TABLE_NAME" + "===" + colrs.getString("TABLE_NAME"));
            System.out.println("COLUMN_NAME" + "===" + colrs.getString("COLUMN_NAME"));
            System.out.println("DATA_TYPE" + "===" + colrs.getString("DATA_TYPE"));
            System.out.println("TYPE_NAME" + "===" + colrs.getString("TYPE_NAME"));
            System.out.println("COLUMN_SIZE" + "===" + colrs.getString("COLUMN_SIZE"));
            System.out.println("BUFFER_LENGTH" + "===" + colrs.getString("BUFFER_LENGTH"));
            System.out.println("DECIMAL_DIGITS" + "===" + colrs.getString("DECIMAL_DIGITS"));
            System.out.println("NUM_PREC_RADIX" + "===" + colrs.getString("NUM_PREC_RADIX"));
            System.out.println("NULLABLE" + "===" + colrs.getString("NULLABLE"));
            System.out.println("REMARKS" + "===" + colrs.getString("REMARKS"));
            System.out.println("COLUMN_DEF" + "===" + colrs.getString("COLUMN_DEF"));
            System.out.println("SQL_DATA_TYPE" + "===" + colrs.getString("SQL_DATA_TYPE"));
            System.out.println("SQL_DATETIME_SUB" + "===" + colrs.getString("SQL_DATETIME_SUB"));
            System.out.println("CHAR_OCTET_LENGTH" + "===" + colrs.getString("CHAR_OCTET_LENGTH"));
            System.out.println("ORDINAL_POSITION" + "===" + colrs.getString("ORDINAL_POSITION"));
            System.out.println("IS_NULLABLE" + "===" + colrs.getString("IS_NULLABLE"));
            System.out.println("SCOPE_CATALOG" + "===" + colrs.getString("SCOPE_CATALOG"));
            System.out.println("SCOPE_SCHEMA" + "===" + colrs.getString("SCOPE_SCHEMA"));
            System.out.println("SCOPE_TABLE" + "===" + colrs.getString("SCOPE_TABLE"));
            System.out.println("SOURCE_DATA_TYPE" + "===" + colrs.getString("SOURCE_DATA_TYPE"));
            System.out.println("IS_AUTOINCREMENT" + "===" + colrs.getString("IS_AUTOINCREMENT"));
        }
    }
    public static void demoResultSetMetaData(ResultSetMetaData data) throws SQLException {
        for (int i = 1; i <= data.getColumnCount(); i++) {
            // 获得所有列的数目及实际列数
            int columnCount = data.getColumnCount();
            // 获得指定列的列名
            String columnName = data.getColumnName(i);
            // 获得指定列的列值
            // String columnValue = rs.getString(i);
            // 获得指定列的数据类型
            int columnType = data.getColumnType(i);
            // 获得指定列的数据类型名
            String columnTypeName = data.getColumnTypeName(i);
            // 所在的Catalog名字
            String catalogName = data.getCatalogName(i);
            // 对应数据类型的类
            String columnClassName = data.getColumnClassName(i);
            // 在数据库中类型的最大字符个数
            int columnDisplaySize = data.getColumnDisplaySize(i);
            // 默认的列的标题
            String columnLabel = data.getColumnLabel(i);
            // 获得列的模式
            String schemaName = data.getSchemaName(i);
            // 某列类型的精确度(类型的长度)
            int precision = data.getPrecision(i);
            // 小数点后的位数
            int scale = data.getScale(i);
            // 获取某列对应的表名
            String tableName = data.getTableName(i);
            // 是否自动递增
            boolean isAutoInctement = data.isAutoIncrement(i);
            // 在数据库中是否为货币型
            boolean isCurrency = data.isCurrency(i);
            // 是否为空
            int isNullable = data.isNullable(i);
            // 是否为只读
            boolean isReadOnly = data.isReadOnly(i);
            // 能否出现在where中
            boolean isSearchable = data.isSearchable(i);
            System.out.println(columnCount);
            System.out.println("获得列" + i + "的字段名称:" + columnName);
            //System.out.println("获得列" + i + "的字段值:" + columnValue);
            System.out.println("获得列" + i + "的类型,返回SqlType中的编号:" + columnType);
            System.out.println("获得列" + i + "的数据类型名:" + columnTypeName);
            System.out.println("获得列" + i + "所在的Catalog名字:" + catalogName);
            System.out.println("获得列" + i + "对应数据类型的类:" + columnClassName);
            System.out.println("获得列" + i + "在数据库中类型的最大字符个数:" + columnDisplaySize);
            System.out.println("获得列" + i + "的默认的列的标题:" + columnLabel);
            System.out.println("获得列" + i + "的模式:" + schemaName);
            System.out.println("获得列" + i + "类型的精确度(类型的长度):" + precision);
            System.out.println("获得列" + i + "小数点后的位数:" + scale);
            System.out.println("获得列" + i + "对应的表名:" + tableName);
            System.out.println("获得列" + i + "是否自动递增:" + isAutoInctement);
            System.out.println("获得列" + i + "在数据库中是否为货币型:" + isCurrency);
            System.out.println("获得列" + i + "是否为空:" + isNullable);
            System.out.println("获得列" + i + "是否为只读:" + isReadOnly);
            System.out.println("获得列" + i + "能否出现在where中:" + isSearchable);
        }
    }


    /**
     * 给字段添加是否是索引标记
     * 对普通索引字段进行扫描，且普通索引只包含了一列，那么这列是打上索引标记
     * @param tableName
     * @param fields
     */
    private  Map<String, Field> setFieldIndexFlag( String tableName,List<Field> fields ) {
        Map<String, Field> indexfieldsMap = new HashMap<String, Field>();
       // System.out.println(tableName+"给字段添加是否是索引标记start");
        Multimap<String, Field> uniqueIndexMultiMap = (Multimap<String, Field>) commonContext.get(Constant.Unique_index);
        Multimap<String, Field> commonIndexMultiMap = (Multimap<String, Field>) commonContext.get(Constant.Common_index);
        for(Field field:fields){
            Set<String> uniqueKeys = uniqueIndexMultiMap.keySet();
            Set<String> commonKeys = commonIndexMultiMap.keySet();

            for (String commonKey:commonKeys){
                List<Field> commonFields = (List<Field>) commonIndexMultiMap.get(commonKey);
                if(commonFields!=null&&commonFields.size()==1){
                    Field commonField = commonFields.get(0);
                    if(commonField.getFieldName().equals(field.getFieldName())){
                        field.setIndexFlag(true);
                        field.setHtmlElmentType("select");
                        System.out.println(commonField.getFieldName());
                        indexfieldsMap.put(field.getPropertyName(),field);
                    }
                }
            }
        }
       // System.out.println(tableName+"给字段添加是否是索引标记end");
        return indexfieldsMap;
    }


    private static String camelFieldName(String fieldName){
        return camelFieldNameMap.get(fieldName)==null?fieldName:camelFieldNameMap.get(fieldName);
    }

    /*
     * 得到MSSQL表的主键id
     */
    public static String getMSSQLPkField(Connection conn, String tableName) throws SQLException {
        PreparedStatement pstmt;
        String pkField = null;
        pstmt = conn
                .prepareStatement("SELECT syscolumns.name From sysobjects inner join syscolumns on sysobjects.id = syscolumns.id left outer join (select  o.name sTableName, c.Name sColName From  sysobjects o  inner join sysindexes i on o.id = i.id and (i.status & 0X800) = 0X800 inner join syscolumns c1 on c1.colid <= i.keycnt and c1.id = o.id inner join syscolumns c on o.id = c.id and c.name = index_col (o.name, i.indid, c1.colid)) pkElements on pkElements.sTableName = sysobjects.name and pkElements.sColName = syscolumns.name inner join sysobjects syscons on sysobjects.id=syscons.parent_obj and syscons.xtype='PK' where (syscolumns.Status & 128)=128 and sysobjects.name=?");
        pstmt.setString(1, tableName);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            pkField = rs.getString(1);
        }
        pstmt.close();
        return pkField;
    }

    /*
     * 得到MYSQL表的主键id
     */
    public static String getMYSQLPkField(Connection conn, String tableName) throws SQLException {
        PreparedStatement pstmt;
        String pkField = null;
        pstmt = conn.prepareStatement("show index from " + tableName + "  where Key_name='PRIMARY'");
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            pkField = rs.getString(5);
            if (pkField.indexOf(",") > 0) {
                pkField = pkField.substring(0, pkField.indexOf(","));

            }
            //System.out.println("MYSQL表" + tableName + "主键id=" + pkField);

        }
        pstmt.close();
        return pkField;
    }

    /*
 * 得到MYSQL表的index
 */
    public   String getMYSQLIndexField(Connection conn, String tableName) throws SQLException {
        PreparedStatement pstmt;
        String pkField = null;
        pstmt = conn.prepareStatement("show index from " + tableName );
        ResultSet rs = pstmt.executeQuery();

        Multimap<String, Field> uniqueIndexMultiMap = ArrayListMultimap.create();
        Multimap<String, Field> commonIndexMultiMap = ArrayListMultimap.create();

        while (rs.next()) {
            String nonUnique = rs.getString(2);
            String keyName = rs.getString(3);
            String columnName = rs.getString(5);
            if(keyName.equals("PRIMARY")){//主键
                commonContext.put(Constant.PK_ID, columnName);
            }else{//普通索引和唯一索引
                if(nonUnique.equals("0")){//唯一索引
                    uniqueIndexMultiMap.put(keyName,fieldsMap.get(columnName));
                }
                if(nonUnique.equals("1")){//普通索引
                    commonIndexMultiMap.put(keyName,fieldsMap.get(columnName));
                }
            }
        }
        commonContext.put(Constant.Unique_index,uniqueIndexMultiMap);
        commonContext.put(Constant.Common_index,commonIndexMultiMap);
        pstmt.close();
        return pkField;
    }

    /*
* 得到MYSQL表的注释
*/
    public   void getMYSQLTableComment(Connection conn, String tableName) throws SQLException {
        PreparedStatement pstmt;
        //pstmt = conn.prepareStatement(" select table_comment from information_schema.tables where table_name=' " + tableName +"'");//命令行里可用
        pstmt = conn.prepareStatement("show create table "+tableName);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String allInfo = rs.getString(2);
            int startIndex = allInfo.indexOf("COMMENT='");
            if(startIndex<0)
                return;
            String comment = allInfo.substring(startIndex+9);
            comment = comment.substring(0,comment.length()-1);
            commonContext.put(Constant.TABLE_COMMENT,comment);
        }
        pstmt.close();
    }

    /*
     * 得到mysql表列注释集合
     */
    public static List<String> getMysqlCommentList(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData dmd = conn.getMetaData();
        ResultSet rs = dmd.getColumns(null, null, tableName, null);
        List<String> commentList = new ArrayList<String>();
        int ix = 1;
        while (rs.next()) {
            //System.out.println(rs.getString("REMARKS"));
            commentList.add(rs.getString("REMARKS"));
            ix += 1;
        }
        return commentList;
    }

    private static class Constant{
        static   String CLASS_NAME = "className";
        static  String TABLE_NAME = "tableName";
        static  String INST_NAME = "instName";
        static  String TABLE_COMMENT = "tableComment";
        static String IMPORTS = "imports";
        static String AUTHOR = "author";
        static String PK_ID = "pkid";
        // javabean 主键java类型
        static String PK_TYPE = "pkType";
        // javabean 主键的java全路径类�?
        static String pk_JavaFullType = "pkJavaFullType";
        static  String PK_NAME = "pkname";
        static  String FIELDS = "fields";

        static  String Unique_index = "uniqueIndex";
        static  String Common_index = "commonIndex";

        //存放普通索引且索引只包含一列的filed
        static  String indexFields = "indexFields";
    }
}
