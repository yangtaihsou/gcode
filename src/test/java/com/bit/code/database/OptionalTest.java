package com.bit.code.database;

import com.bit.code.config.mapping.SystemConfig;
import org.junit.Test;

import java.util.Optional;

public class OptionalTest {
    @Test
    public void test(){
        Optional<String> op1 = Optional.of("Hello");
        System.out.println(op1.isPresent()); // 输出 true
        System.out.println(op1.get()); // 输出 Hello
        Optional<String> op2 = Optional.ofNullable(null);
        System.out.println(op2.orElse("test"));

    }
    @Test
    public void voTest(){
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setSysName("pay");
        Optional<SystemConfig> configOption = Optional.ofNullable(null);
        String name = configOption.map(x->x.getSysName()).map(x->x.toUpperCase())
                .orElse("111");
        System.out.println(name);

    }
}
