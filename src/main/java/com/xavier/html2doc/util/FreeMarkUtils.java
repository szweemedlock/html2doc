package com.xavier.html2doc.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class FreeMarkUtils {

    public static Configuration getConfiguration(){
        // 创建配置实例
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        // 设置编码
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(FreeMarkUtils.class, "/templates");
        return configuration;
    }

    /**
     * 获取模板字符串输入流
     * @param dataMap   参数
     * @param templateName  模板名称
     * @return stream
     */
    public static ByteArrayInputStream getFreemarkerContentInputStream(Map<String, Object> dataMap, String templateName) {
        ByteArrayInputStream in = null;
        try {
            // 获取模板
            Template template = getConfiguration().getTemplate(templateName);
            StringWriter stringWriter = new StringWriter();
            // 生成文件
            template.process(dataMap, stringWriter);
            // 这里一定要设置utf-8编码 否则导出的word中中文会是乱码
            in = new ByteArrayInputStream(stringWriter.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("模板生成错误！");
        }
        return in;
    }
}
