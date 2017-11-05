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

package tk.mybatis.mapper.generator.model;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuzh
 * @since 3.4.5
 */
public class TableClass implements Serializable {
    private static final long serialVersionUID = -746251813735169289L;

    private IntrospectedTable introspectedTable;

    private String                 tableName;
    private String                 variableName;
    private String                 lowerCaseName;
    private String                 shortClassName;
    private String                 fullClassName;
    private String                 packageName;
    private FullyQualifiedJavaType type;

    private List<ColumnField> pkFields;
    private List<ColumnField> baseFields;
    private List<ColumnField> blobFields;
    private List<ColumnField> allFields;

    public List<ColumnField> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<ColumnField> allFields) {
        this.allFields = allFields;
    }

    public List<ColumnField> getBaseFields() {
        return baseFields;
    }

    public void setBaseFields(List<ColumnField> baseFields) {
        this.baseFields = baseFields;
    }

    public List<ColumnField> getBlobFields() {
        return blobFields;
    }

    public void setBlobFields(List<ColumnField> blobFields) {
        this.blobFields = blobFields;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }

    public void setIntrospectedTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    public String getLowerCaseName() {
        return lowerCaseName;
    }

    public void setLowerCaseName(String lowerCaseName) {
        this.lowerCaseName = lowerCaseName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<ColumnField> getPkFields() {
        return pkFields;
    }

    public void setPkFields(List<ColumnField> pkFields) {
        this.pkFields = pkFields;
    }

    public String getShortClassName() {
        return shortClassName;
    }

    public void setShortClassName(String shortClassName) {
        this.shortClassName = shortClassName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public FullyQualifiedJavaType getType() {
        return type;
    }

    public void setType(FullyQualifiedJavaType type) {
        this.type = type;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
}
