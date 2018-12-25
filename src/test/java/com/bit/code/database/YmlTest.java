package com.bit.code.database;

import com.alibaba.fastjson.JSON;
import com.bit.code.config.mapping.TableConfig;
import com.bit.code.util.FileReadUtils;
import org.ho.yaml.Yaml;
import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class YmlTest {
    @Test
    public void test() throws FileNotFoundException {
        File dumpFile = new File("D:\\jd\\CodeGenerate-pay\\Template\\pay-database\\TableConfig\\tableConfig.yml");
        Map tableConfigList = Yaml.loadType(dumpFile, Map.class);
        Assert.assertEquals(tableConfigList.size(), 2);

    }

    @Test
    public void testsnakeyaml() throws FileNotFoundException {
        File file = new File("D:\\jd\\CodeGenerate-pay\\Template\\pay-database\\tableConfig.yml");
        String text = FileReadUtils.readContext(file.getAbsolutePath(), Charset.forName("GBK"));

        org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        List ret = yaml.loadAs(text, List.class);
        String json = JSON.toJSONString(ret);
        System.out.println(json);
        List<TableConfig> tableConfigList = JSON.parseArray(json, TableConfig.class);
        System.out.println(tableConfigList.size());

        file = new File("D:\\jd\\CodeGenerate-pay\\Template\\pay-database\\systemConfig.yml");
        text = FileReadUtils.readContext(file.getAbsolutePath(), Charset.forName("GBK"));
        Object obj = yaml.load(text);
        System.out.println(json);

    }
}
