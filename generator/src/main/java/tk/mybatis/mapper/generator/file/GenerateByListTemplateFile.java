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

package tk.mybatis.mapper.generator.file;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import tk.mybatis.mapper.generator.formatter.ListTemplateFormatter;
import tk.mybatis.mapper.generator.model.TableClass;

import java.util.Properties;
import java.util.Set;

/**
 * @author liuzh
 * @since 3.4.5
 */
public class GenerateByListTemplateFile extends GeneratedJavaFile {
    public static final String ENCODING = "UTF-8";

    private String targetPackage;

    private String fileNameTemplate;

    private String templateContent;

    private Properties properties;

    private Set<TableClass> tableClassSet;

    private ListTemplateFormatter templateFormatter;

    public GenerateByListTemplateFile(Set<TableClass> tableClassSet, ListTemplateFormatter templateFormatter, Properties properties, String targetProject, String targetPackage, String fileNameTemplate, String templateContent) {
        super(null, targetProject, ENCODING, null);
        this.targetProject = targetProject;
        this.targetPackage = targetPackage;
        this.fileNameTemplate = fileNameTemplate;
        this.templateContent = templateContent;
        this.properties = properties;
        this.tableClassSet = tableClassSet;
        this.templateFormatter = templateFormatter;
    }

    @Override
    public CompilationUnit getCompilationUnit() {
        return null;
    }

    @Override
    public String getFileName() {
        return templateFormatter.getFormattedContent(tableClassSet, properties, targetPackage, fileNameTemplate);
    }

    @Override
    public String getFormattedContent() {
        return templateFormatter.getFormattedContent(tableClassSet, properties, targetPackage, templateContent);
    }

    @Override
    public String getTargetPackage() {
        return targetPackage;
    }

    @Override
    public boolean isMergeable() {
        return false;
    }

}
