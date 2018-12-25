package com.bit.code.util;

import com.bit.code.assemble.para.Table;
import com.bit.code.config.mapping.CodeTemplateConfig;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;

public class CodeTemplateUtils {

    private static Logger log = LoggerFactory.getLogger(CodeTemplateUtils.class);
    /**
     * 渲染代码模板内容
     * @param table
     * @param codeTemplateContext
     * @return
     */
    public static String render(Table table, CodeTemplateConfig codeTemplateConfig, String codeTemplateContext) {
        try {
            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("table", table);
            velocityContext.put("tem", codeTemplateConfig);
            StringWriter writer = new StringWriter();
            Velocity.evaluate(velocityContext, writer, "", codeTemplateContext);
            return  writer.toString();
        } catch (Exception e) {
            log.info("渲染代码模板异常:"+e.getMessage());
        }
        return null;
    }
}
