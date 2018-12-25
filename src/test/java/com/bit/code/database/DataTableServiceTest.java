package com.bit.code.database;

import com.bit.code.util.Contants;
import com.bit.code.assemble.para.TableIndexColumn;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class DataTableServiceTest {
    Connection connection;
    DataTableService dataTableService;
    @Before
    public void initConnection(){
        dataTableService = new DataTableServiceImpl();
        connection = SqlConnection.build().getSQLConnection(Contants.MYSQL,"jdbc:mysql://127.0.0.1:3306/test","root","").getConnection();
    }

    @Test
    public void testGetAllTables(){
        Assert.assertNotNull(connection);
        List<String> tables =  dataTableService.getAllTables(Contants.MYSQL,connection);
        Assert.assertNotNull(tables);
        Assert.assertEquals(tables.size(),38);
    }



    @Test
    public void testGetTableComments(){
        Assert.assertNotNull(connection);
        Map<String,String> map =  dataTableService.getCommentsOfTable(Contants.MYSQL,connection);
        Assert.assertNotNull(map);
        Assert.assertEquals(map.size(),8);
    }

    @Test
    public void testGetTableColumnJavaType(){
        Assert.assertNotNull(connection);
        Map<String,String> map =  dataTableService.getColumnJavaTypeOfTable("tb_merchant_account",Contants.MYSQL,connection);
        Assert.assertNotNull(map);
        Assert.assertEquals(map.size(),8);
    }

    @Test
    public void testGetColumnRawTypeOfTable(){
        Assert.assertNotNull(connection);
        Map<String,String> map =  dataTableService.getColumnRawTypeOfTable("tb_merchant_account",Contants.MYSQL,connection);
        Assert.assertNotNull(map);
        Assert.assertEquals(map.size(),8);
    }

    @Test
    public void testGetTableColumnComments(){
        Assert.assertNotNull(connection);
        Map<String,String> map =  dataTableService.getColumnCommentsOfTable("tb_merchant_account",Contants.MYSQL,connection);
        Assert.assertNotNull(map);
        Assert.assertEquals(map.size(),8);
    }
    @Test
    public void testGetTableIndexs(){
        Assert.assertNotNull(connection);
         List<TableIndexColumn> tableIndexColumnList =  dataTableService.getTableIndexs("sys_user",Contants.MYSQL,connection);
        Map<String,List<TableIndexColumn>> indexGroup = tableIndexColumnList.stream().collect(groupingBy(TableIndexColumn::getIndexName));
        Assert.assertNotNull(indexGroup);
        Assert.assertEquals(indexGroup.size(),8);
    }
    @Test
    public void testgetColumnLengthOfTable(){
        Assert.assertNotNull(connection);
        Map<String,Integer> map =  dataTableService.getColumnLengthOfTable("sys_user",Contants.MYSQL,connection);
        Assert.assertNotNull(map);
        Assert.assertEquals(map.size(),8);
    }
}
