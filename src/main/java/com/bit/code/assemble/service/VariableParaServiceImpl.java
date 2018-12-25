package com.bit.code.assemble.service;

import com.bit.code.assemble.para.*;
import com.bit.code.config.datasource.DataMappingHolder;
import com.bit.code.config.mapping.SystemConfig;
import com.bit.code.database.DataTableService;
import com.bit.code.database.DataTableServiceImpl;
import com.bit.code.database.SqlConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class VariableParaServiceImpl {
    private final Logger log = LoggerFactory.getLogger(SqlConnection.class);

    public static VariableParaServiceImpl build() {
        return new VariableParaServiceImpl();
    }

    DataTableService dataTableService = new DataTableServiceImpl();

    public List<Table> wrapTablePara(DataMappingHolder dataMappingHolder) {
        SystemConfig systemConfig = dataMappingHolder.getSystemConfig();
        return this.wrapTablePara(systemConfig, dataMappingHolder);
    }

    public List<Table> wrapTablePara(SystemConfig systemConfig) {
        return this.wrapTablePara(systemConfig,null);
    }

    public List<Table> wrapTablePara(SystemConfig systemConfig, DataMappingHolder dataMappingHolder) {
        String sqlType = systemConfig.getSqlType();
        SqlConnection sqlConnection = SqlConnection.build().getSQLConnection(systemConfig);
        Connection connection = sqlConnection.getConnection();
        List<String> tables = dataTableService.getAllTables(sqlType, connection);
        Map<String, String> tableCommentMap = dataTableService.getCommentsOfTable(sqlType, connection);
        List<Table> tableList = new ArrayList<>();
        for (String tableName : tables) {
            Table table = new Table();
            table.setTableName(tableName);
            table.setTableComment(tableCommentMap.get(tableName));
            table.setClassName(tableName);
            if (dataMappingHolder != null) {
                if(dataMappingHolder.getTableConfig(tableName)==null){
                    continue;
                }
                String className = dataMappingHolder.getTableClassName(tableName);
                table.setClassName(className == null ? tableName : className);
            }
            table.setFieldList(this.buildColumns(tableName, sqlType, connection, dataMappingHolder));
            table.setFieldMap(table.getFieldList().stream().collect(toMap(x -> x.getColumnName(), x -> x)));

            List<TableIndexColumn> tableIndexColumnList = dataTableService.getTableIndexs(tableName, sqlType, connection);
            List<TableIndex> tableIndexList = this.buildTableIndexList(tableIndexColumnList,table.getFieldMap());
            table.setIndexList(tableIndexList);
            table.setUniqIndexFieldGroup(this.buildUniqIndexGroup(tableIndexList));
            table.setUniqIndexForOneFieldList(this.buildUniqIndexForOneFieldList(table.getUniqIndexFieldGroup()));
            table.setCommonIndexFieldGroup(this.buildCommonIndexGroup(tableIndexList));
            table.setPrimaryKeyFieldList(this.buildPrimaryKeyFieldList(tableIndexList));
            table.setPrimaryField(table.getPrimaryKeyFieldList().size() == 1 ? table.getPrimaryKeyFieldList().get(0) : null);
            tableList.add(table);
        }
        sqlConnection.close();
        return tableList;
    }

    public List<Field> buildColumns(String tableName, String sqlType, Connection connection, DataMappingHolder dataMappingHolder) {
        List<String> columnNameList = dataTableService.getColumnNameOfTable(tableName, sqlType, connection);
        Map<String, String> columnJavaTypeMap = dataTableService.getColumnJavaTypeOfTable(tableName, sqlType, connection);
        Map<String, String> columnRawTypeMap = dataTableService.getColumnRawTypeOfTable(tableName, sqlType, connection);
        Map<String, String> columnCommentsMap = dataTableService.getColumnCommentsOfTable(tableName, sqlType, connection);
        Map<String, String> columnDefaultValueMap = dataTableService.getColumnDefaultValueOfTable(tableName, sqlType, connection);
        Map<String, Integer> columnLengthMap = dataTableService.getColumnLengthOfTable(tableName, sqlType, connection);
        Map<String, Boolean> columnIsNullMap = dataTableService.getColumnIsNullAbleOfTable(tableName, sqlType, connection);
        Map<String, Boolean> columnIsAutoIncreMap = dataTableService.getColumnIsAutoIncreOfTable(tableName, sqlType, connection);
        List<Field> fieldList = new ArrayList<>(columnNameList.size());
        for (String columnName : columnNameList) {
            Field field = new Field();
            field.setColumnName(columnName);
            field.setJavaFullType(columnJavaTypeMap.get(columnName));
            field.setJavaType(columnJavaTypeMap.get(columnName));
            field.setJdbcType(columnRawTypeMap.get(columnName));
            field.setComment(columnCommentsMap.get(columnName));
            field.setDefaultValue(columnDefaultValueMap.get(columnName));
            field.setLength(columnLengthMap.get(columnName));
            field.setEmptyFlag(columnIsNullMap.get(columnName));
            field.setAutoIncreFlag(columnIsAutoIncreMap.get(columnName));
            field.setPropertyName(columnName);
            if (dataMappingHolder != null) {
                String propertyName = dataMappingHolder.getPropertityNameOfColumn(tableName, columnName);
                field.setPropertyName(propertyName == null ? columnName : propertyName);
            }
            fieldList.add(field);
        }
        return fieldList;
    }

    public List<TableIndex> buildTableIndexList(List<TableIndexColumn> tableIndexColumnList,Map<String, Field> fieldMap){
        List<TableIndex> tableIndexList = new ArrayList<>();
        Map<String, List<TableIndexColumn>> indexGroup = tableIndexColumnList.stream().collect(groupingBy(TableIndexColumn::getIndexName));
        for(String indexKey:indexGroup.keySet()){
            List<TableIndexColumn> groupList = indexGroup.get(indexKey);
            TableIndex tableIndex = new TableIndex();
            tableIndex.setIndexName(indexKey);
            tableIndex.setIndexType(groupList.get(0).getIndexType());
            tableIndex.setFieldList(groupList.stream().map(x->fieldMap.get(x.getColumnName())).collect(toList()));
            tableIndex.setFieldMap(tableIndex.getFieldList().stream().collect(toMap(x -> x.getColumnName(), x -> x)));
            tableIndexList.add(tableIndex);
        }
        return tableIndexList;
    }

    private List<Field> buildPrimaryKeyFieldList(List<TableIndex> tableIndexList) {
        return tableIndexList.stream().
                filter(y->y.getIndexType().equals(IndexType.PRIMARYKEY)).findFirst().get().getFieldList();
    }

    private Map<String, List<Field>> buildUniqIndexGroup(List<TableIndex> tableIndexList) {
     return tableIndexList.stream().
                filter(y -> y.getIndexType().equals(IndexType.UNIQKEY)).
               collect(toMap(x -> x.getIndexName(), x -> x.getFieldList()));
    }

    private Map<String, List<Field>> buildCommonIndexGroup(List<TableIndex> tableIndexList) {
        return tableIndexList.stream().
                filter(y -> y.getIndexType().equals(IndexType.COMMONKEY)).
                collect(toMap(x -> x.getIndexName(), x -> x.getFieldList()));
    }

    private List<Field> buildUniqIndexForOneFieldList(Map<String, List<Field>> filedGroup) {
        return filedGroup.entrySet().stream().
                filter(x -> x.getValue().size() == 1).
                map(x -> x.getValue().get(0)).
                collect(toList());
    }
}
