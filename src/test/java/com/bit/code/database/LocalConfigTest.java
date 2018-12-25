package com.bit.code.database;

import com.bit.code.config.mapping.CodeTemplateConfig;
import com.bit.code.config.datasource.local.LocalDataConfigHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class LocalConfigTest {
    LocalDataConfigHolder localData = new LocalDataConfigHolder();
    @Before
    public void init(){

        String path="D:\\jd\\CodeGenerate-pay\\Template\\pay-database";
        localData.initMapping(path);
    }
    @Test
    public void testGlocalTemplateConfigList(){
        List<CodeTemplateConfig> fileConfigList = localData.getCodeTemplateConfigList();
        Assert.assertEquals(fileConfigList.size(),6);
    }
    @Deprecated
    @Test
    public void testcolumnConfig(){
        String text  = localData.getPropertityNameOfColumn("","");
        Assert.assertEquals(text.length(),6);
    }
}
