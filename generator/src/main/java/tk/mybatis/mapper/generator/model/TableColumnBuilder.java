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

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzh
 * @since 3.4.5
 */
public class TableColumnBuilder {

    /**
     * 创建 TableClass
     *
     * @param introspectedTable
     * @return
     */
    public static TableClass build(IntrospectedTable introspectedTable) {
        TableClass tableClass = new TableClass();
        tableClass.setIntrospectedTable(introspectedTable);

        FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();
        tableClass.setTableName(fullyQualifiedTable.getIntrospectedTableName());

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        tableClass.setType(type);
        tableClass.setVariableName(Introspector.decapitalize(type.getShortName()));
        tableClass.setLowerCaseName(type.getShortName().toLowerCase());
        tableClass.setShortClassName(type.getShortName());
        tableClass.setFullClassName(type.getFullyQualifiedName());
        tableClass.setPackageName(type.getPackageName());

        List<ColumnField> pkFields = new ArrayList<ColumnField>();
        List<ColumnField> baseFields = new ArrayList<ColumnField>();
        List<ColumnField> blobFields = new ArrayList<ColumnField>();
        List<ColumnField> allFields = new ArrayList<ColumnField>();
        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            ColumnField field = build(column);
            field.setTableClass(tableClass);
            pkFields.add(field);
            allFields.add(field);
        }
        for (IntrospectedColumn column : introspectedTable.getBaseColumns()) {
            ColumnField field = build(column);
            field.setTableClass(tableClass);
            baseFields.add(field);
            allFields.add(field);
        }
        for (IntrospectedColumn column : introspectedTable.getBLOBColumns()) {
            ColumnField field = build(column);
            field.setTableClass(tableClass);
            blobFields.add(field);
            allFields.add(field);
        }
        tableClass.setPkFields(pkFields);
        tableClass.setBaseFields(baseFields);
        tableClass.setBlobFields(blobFields);
        tableClass.setAllFields(allFields);

        return tableClass;
    }

    /**
     * 创建 ColumnField
     *
     * @param column
     * @return
     */
    public static ColumnField build(IntrospectedColumn column) {
        ColumnField field = new ColumnField();
        field.setColumnName(column.getActualColumnName());
        field.setJdbcType(column.getJdbcTypeName());
        field.setFieldName(column.getJavaProperty());
        field.setRemarks(column.getRemarks());
        FullyQualifiedJavaType type = column.getFullyQualifiedJavaType();
        field.setType(type);
        field.setTypePackage(type.getPackageName());
        field.setShortTypeName(type.getShortName());
        field.setFullTypeName(type.getFullyQualifiedName());
        field.setIdentity(column.isIdentity());
        field.setNullable(column.isNullable());
        field.setSequenceColumn(column.isSequenceColumn());
        field.setBlobColumn(column.isBLOBColumn());
        field.setStringColumn(column.isStringColumn());
        field.setJdbcCharacterColumn(column.isJdbcCharacterColumn());
        field.setJdbcDateColumn(column.isJDBCDateColumn());
        field.setJdbcTimeColumn(column.isJDBCTimeColumn());
        field.setLength(column.getLength());
        field.setScale(column.getScale());
        return field;
    }


}
