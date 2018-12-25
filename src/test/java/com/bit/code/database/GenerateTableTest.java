package com.bit.code.database;

import com.bit.code.util.Contants;
import com.bit.code.assemble.service.VariableParaServiceImpl;
import com.bit.code.assemble.para.Table;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class GenerateTableTest {
    Connection connection;
    @Before
    public void initConnection(){
        //paymentplatform
        connection = SqlConnection.build().getSQLConnection(Contants.MYSQL,"jdbc:mysql://127.0.0.1:3306/paymentplatform","root","").getConnection();
    }

    @Test
    public void testGetAllTables(){
        Assert.assertNotNull(connection);
        VariableParaServiceImpl generateTableAndField = new VariableParaServiceImpl();
        List<Table> tables = generateTableAndField.wrapTablePara(null,null);
        Assert.assertNotNull(tables);
        Assert.assertEquals(tables.size(),38);
    }

}
