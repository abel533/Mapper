/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.mapper.generator.formatter;


import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import tk.mybatis.mapper.generator.model.TableClass;

import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * 基于 freemarker 的实现
 *
 * @author liuzh
 * @since 3.4.5
 */
public class FreemarkerTemplateFormatter implements TemplateFormatter, ListTemplateFormatter {
    private final Configuration        configuration  = new Configuration(Configuration.VERSION_2_3_23);
    private final StringTemplateLoader templateLoader = new StringTemplateLoader();

    public FreemarkerTemplateFormatter() {
        configuration.setLocale(Locale.CHINA);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateLoader(templateLoader);
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_23));
    }

    /**
     * 根据模板处理
     *
     * @param templateName
     * @param templateSource
     * @param params
     * @return
     */
    public String process(String templateName, String templateSource, Map<String, Object> params) {
        try {
            Template template = new Template(templateName, templateSource, configuration);
            Writer writer = new StringWriter();
            template.process(params, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFormattedContent(TableClass tableClass, Properties properties, String targetPackage, String templateContent) {
        Map<String, Object> params = new HashMap<String, Object>();
        for (Object o : properties.keySet()) {
            params.put(String.valueOf(o), properties.get(o));
        }
        params.put("props", properties);
        params.put("package", targetPackage);
        params.put("tableClass", tableClass);
        return process(properties.getProperty("templatePath"), templateContent, params);
    }

    @Override
    public String getFormattedContent(Set<TableClass> tableClassSet, Properties properties, String targetPackage, String templateContent) {
        Map<String, Object> params = new HashMap<String, Object>();
        for (Object o : properties.keySet()) {
            params.put(String.valueOf(o), properties.get(o));
        }
        params.put("props", properties);
        params.put("package", targetPackage);
        params.put("tableClassSet", tableClassSet);
        return process(properties.getProperty("templatePath"), templateContent, params);
    }
}
