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

package tk.mybatis.mapper.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.*;

/**
 * 通用Mapper生成器插件
 *
 * @author liuzh
 */
public class MapperPlugin extends FalseMethodPlugin {

    private Set<String> mappers = new HashSet<String>();
    private boolean caseSensitive = false;
    private boolean useMapperCommentGenerator = true;
    //开始的分隔符，例如mysql为`，sqlserver为[
    private String beginningDelimiter = "";
    //结束的分隔符，例如mysql为`，sqlserver为]
    private String endingDelimiter = "";
    //数据库模式
    private String schema;
    //注释生成器
    private CommentGeneratorConfiguration commentCfg;
    //强制生成注解
    private boolean forceAnnotation;

    //是否需要生成Data注解
    private boolean needsData = false;
    //是否需要生成Getter注解
    private boolean needsGetter = false;
    //是否需要生成Setter注解
    private boolean needsSetter = false;
    //是否需要生成ToString注解
    private boolean needsToString = false;
    //是否需要生成Accessors(chain = true)注解
    private boolean needsAccessors = false;
    private boolean needsBuilder = false;
    private boolean needsSuperBuilder = false;
    private boolean needsNoArgsConstructor = false;
    private boolean needsAllArgsConstructor = false;

    //是否需要生成EqualsAndHashCode注解
    private boolean needsEqualsAndHashCode = false;
    //是否需要生成EqualsAndHashCode注解，并且“callSuper = true”
    private boolean needsEqualsAndHashCodeAndCallSuper = false;
    //是否生成字段名常量
    private boolean generateColumnConsts = false;
    //是否生成默认的属性的静态方法
    private boolean generateDefaultInstanceMethod = false;
    //是否生成swagger注解,包括 @ApiModel和@ApiModelProperty
    private boolean needsSwagger = false;
    //是否逻辑删除
    private boolean logicDelete = false;


    public String getDelimiterName(String name) {
        StringBuilder nameBuilder = new StringBuilder();
        if (StringUtility.stringHasValue(schema)) {
            nameBuilder.append(schema);
            nameBuilder.append(".");
        }
        nameBuilder.append(beginningDelimiter);
        nameBuilder.append(name);
        nameBuilder.append(endingDelimiter);
        return nameBuilder.toString();
    }

    /**
     * 生成的Mapper接口
     *
     * @param interfaze
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        //获取实体类
        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        //import接口
        for (String mapper : mappers) {
            interfaze.addImportedType(new FullyQualifiedJavaType(mapper));
            interfaze.addSuperInterface(new FullyQualifiedJavaType(mapper + "<" + entityType.getShortName() + ">"));
        }
        //import实体类
        interfaze.addImportedType(entityType);
        return true;
    }

    /**
     * 处理实体类的包和@Table注解
     *
     * @param topLevelClass
     * @param introspectedTable
     */
    private void processEntityClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //引入JPA注解
        topLevelClass.addImportedType("jakarta.persistence.*");
        //lombok扩展开始
        //如果需要Data，引入包，代码增加注解
        if (this.needsData) {
            topLevelClass.addImportedType("lombok.Data");
            topLevelClass.addAnnotation("@Data");
        }
        //如果需要Getter，引入包，代码增加注解
        if (this.needsGetter) {
            topLevelClass.addImportedType("lombok.Getter");
            topLevelClass.addAnnotation("@Getter");
        }
        //如果需要Setter，引入包，代码增加注解
        if (this.needsSetter) {
            topLevelClass.addImportedType("lombok.Setter");
            topLevelClass.addAnnotation("@Setter");
        }
        //如果需要ToString，引入包，代码增加注解
        if (this.needsToString) {
            topLevelClass.addImportedType("lombok.ToString");
            topLevelClass.addAnnotation("@ToString");
        }
        // 如果需要EqualsAndHashCode，并且“callSuper = true”，引入包，代码增加注解
        if (this.needsEqualsAndHashCodeAndCallSuper) {
            topLevelClass.addImportedType("lombok.EqualsAndHashCode");
            topLevelClass.addAnnotation("@EqualsAndHashCode(callSuper = true)");
        } else {
            // 如果需要EqualsAndHashCode，引入包，代码增加注解
            if (this.needsEqualsAndHashCode) {
                topLevelClass.addImportedType("lombok.EqualsAndHashCode");
                topLevelClass.addAnnotation("@EqualsAndHashCode");
            }
        }
        // 如果需要Accessors，引入包，代码增加注解
        if (this.needsAccessors) {
            topLevelClass.addImportedType("lombok.experimental.Accessors");
            topLevelClass.addAnnotation("@Accessors(chain = true)");
        }
        if (this.needsSuperBuilder) {
            topLevelClass.addImportedType("lombok.experimental.SuperBuilder");
            topLevelClass.addAnnotation("@SuperBuilder");
        }
        if (this.needsBuilder) {
            topLevelClass.addImportedType("lombok.Builder");
            topLevelClass.addAnnotation("@Builder");
        }
        if (this.needsNoArgsConstructor) {
            topLevelClass.addImportedType("lombok.NoArgsConstructor");
            topLevelClass.addAnnotation("@NoArgsConstructor");
        }
        if (this.needsAllArgsConstructor) {
            topLevelClass.addImportedType("lombok.AllArgsConstructor");
            topLevelClass.addAnnotation("@AllArgsConstructor");
        }
        // lombok扩展结束
        // region swagger扩展
        if (this.needsSwagger) {
            //导包
            topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
            topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
            //增加注解(去除注释中的转换符)
            String remarks = introspectedTable.getRemarks();
            if (remarks == null) {
                remarks = "";
            }
            topLevelClass.addAnnotation("@ApiModel(\"" + remarks.replaceAll("\r", "").replaceAll("\n", "") + "\")");
        }
        // endregion swagger扩展
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();

        //region 文档注释
        String remarks = introspectedTable.getRemarks();
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * 表名：" + tableName);
        if (remarks != null) {
            remarks = remarks.trim();
        }
        if (remarks != null && remarks.trim().length() > 0) {
            String[] lines = remarks.split("\\r?\\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (i == 0) {
                    topLevelClass.addJavaDocLine(" * 表注释：" + line);
                } else {
                    topLevelClass.addJavaDocLine(" *         " + line);
                }
            }
        }
        topLevelClass.addJavaDocLine("*/");
        //endregion
        //如果包含空格，或者需要分隔符，需要完善
        if (StringUtility.stringContainsSpace(tableName)) {
            tableName = context.getBeginningDelimiter()
                    + tableName
                    + context.getEndingDelimiter();
        }
        //是否忽略大小写，对于区分大小写的数据库，会有用
        if (caseSensitive && !topLevelClass.getType().getShortName().equals(tableName)) {
            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
        } else if (!topLevelClass.getType().getShortName().equalsIgnoreCase(tableName)) {
            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
        } else if (StringUtility.stringHasValue(schema)
                || StringUtility.stringHasValue(beginningDelimiter)
                || StringUtility.stringHasValue(endingDelimiter)) {
            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
        } else if (forceAnnotation) {
            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
        }
        if (generateColumnConsts) {
            for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
                String fieldName = introspectedColumn.getActualColumnName().toUpperCase(); //$NON-NLS-1$
                FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(String.class.getName()); //$NON-NLS-1$
                Field field = new Field(fieldName, fieldType);
                field.setVisibility(JavaVisibility.PUBLIC);
                field.setStatic(true);
                field.setFinal(true);
                field.setInitializationString("\"" + introspectedColumn.getJavaProperty() + "\"");
                context.getCommentGenerator().addFieldComment(field, introspectedTable, introspectedColumn);
                topLevelClass.addField(field);
                //增加字段名常量,用于pageHelper
                String  columnFieldName = "DB_" + introspectedColumn.getActualColumnName().toUpperCase(); //$NON-NLS-1$
                FullyQualifiedJavaType columnFieldType = new FullyQualifiedJavaType(String.class.getName()); //$NON-NLS-1$
                Field columnField = new Field(columnFieldName, columnFieldType);
                columnField.setVisibility(JavaVisibility.PUBLIC);
                columnField.setStatic(true);
                columnField.setFinal(true);
                columnField.setInitializationString("\"" + introspectedColumn.getActualColumnName() + "\"");
                topLevelClass.addField(columnField);
            }
        }

        if(this.logicDelete)
        {
            topLevelClass.addImportedType("tk.mybatis.mapper.annotation.LogicDelete");
        }


        if (generateDefaultInstanceMethod) {
            //注意基本类型和包装的index要一致,方便后面使用
            List<String> baseClassName = Arrays.asList("byte", "short", "char", "int", "long", "float", "double", "boolean");
            List<String> wrapperClassName = Arrays.asList("Byte", "Short", "Character", "Integer", "Long", "Float", "Double", "Boolean");
            List<String> otherClassName = Arrays.asList("String", "BigDecimal", "BigInteger");
            Method defaultMethod = new Method("defaultInstance");
            //增加方法注释
            defaultMethod.addJavaDocLine("/**");
            defaultMethod.addJavaDocLine(" * 带默认值的实例");
            defaultMethod.addJavaDocLine("*/");
            defaultMethod.setStatic(true);
            defaultMethod.setVisibility(JavaVisibility.PUBLIC);
            defaultMethod.setReturnType(topLevelClass.getType());
            defaultMethod.addBodyLine(String.format("%s instance = new %s();", topLevelClass.getType().getShortName(), topLevelClass.getType().getShortName()));
            for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
                String shortName = introspectedColumn.getFullyQualifiedJavaType().getShortName();
                if (!baseClassName.contains(shortName) && !wrapperClassName.contains(shortName) && !otherClassName.contains(shortName)) {
                    continue;
                }
                if (introspectedColumn.getDefaultValue() != null) {
                    String defaultValue = introspectedColumn.getDefaultValue();
                    //处理备注中带有类型描述情况，如 postgresql中存在 ''::character varying
                    if (defaultValue.matches("'\\.*'::\\w+(\\s\\w+)?")) {
                        //
                        defaultValue = defaultValue.substring(0, defaultValue.lastIndexOf("::"));
                    }
                    //去除前后'',如 '123456' -> 123456
                    if (defaultValue.startsWith("'") && defaultValue.endsWith("'")) {
                        if (defaultValue.length() == 2) {
                            defaultValue = "";
                        } else {
                            defaultValue = defaultValue.substring(1, defaultValue.length() - 1);
                        }
                    }
                    //暂不支持时间类型默认值识别,不同数据库表达式不同
                    if ("Boolean".equals(shortName) || "boolean".equals(shortName)) {
                        if ("0".equals(defaultValue)) {
                            defaultValue = "false";
                        } else if ("1".equals(defaultValue)) {
                            defaultValue = "true";
                        }
                    }

                    if ("String".equals(shortName)) {
                        //字符串,不通过new String 创建
                        // 其实通过new String 没有任何问题,不过强迫症,idea会提示,所以改了
                        defaultMethod.addBodyLine(String.format("instance.%s = \"%s\";", introspectedColumn.getJavaProperty(), defaultValue));
                    } else {
                        String javaProperty = introspectedColumn.getJavaProperty();
                        if (baseClassName.contains(shortName)) {
                            //基本类型,转成包装类的new 创建
                            javaProperty = wrapperClassName.get(baseClassName.indexOf(shortName));
                        }
                        //通过 new 方法转换
                        defaultMethod.addBodyLine(String.format("instance.%s = new %s(\"%s\");", javaProperty, shortName, defaultValue));
                    }
                }

            }
            defaultMethod.addBodyLine("return instance;");
            topLevelClass.addMethod(defaultMethod);
        }
    }

    /**
     * 如果需要生成Getter注解，就不需要生成get相关代码了
     */
    @Override
    public boolean modelGetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {

        return !(this.needsData || this.needsGetter);
    }

    /**
     * 如果需要生成Setter注解，就不需要生成set相关代码了
     */
    @Override
    public boolean modelSetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return !(this.needsData || this.needsSetter);
    }

    /**
     * 生成基础实体类
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * 生成实体类注解KEY对象
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * 生成带BLOB字段的对象
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return false;
    }


    @Override
    public void setContext(Context context) {
        super.setContext(context);
        //设置默认的注释生成器
        useMapperCommentGenerator = !"FALSE".equalsIgnoreCase(context.getProperty("useMapperCommentGenerator"));
        if (useMapperCommentGenerator) {
            commentCfg = new CommentGeneratorConfiguration();
            commentCfg.setConfigurationType(MapperCommentGenerator.class.getName());
            context.setCommentGeneratorConfiguration(commentCfg);
        }

        JDBCConnectionConfiguration jdbcConnectionConfiguration = null;
        try {
            java.lang.reflect.Field jdbcConnectionConfigurationField = Context.class.getDeclaredField("jdbcConnectionConfiguration");
            jdbcConnectionConfigurationField.setAccessible(true);
            jdbcConnectionConfiguration = (JDBCConnectionConfiguration) jdbcConnectionConfigurationField.get(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //支持oracle获取注释#114
        jdbcConnectionConfiguration.addProperty("remarksReporting", "true");
        //支持mysql获取注释
        jdbcConnectionConfiguration.addProperty("useInformationSchema", "true");
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String mappers = getProperty("mappers");
        if (StringUtility.stringHasValue(mappers)) {
            for (String mapper : mappers.split(",")) {
                this.mappers.add(mapper);
            }
        } else {
            throw new RuntimeException("Mapper插件缺少必要的mappers属性!");
        }
        this.caseSensitive = Boolean.parseBoolean(this.properties.getProperty("caseSensitive"));
        this.forceAnnotation = getPropertyAsBoolean("forceAnnotation");
        this.beginningDelimiter = getProperty("beginningDelimiter", "");
        this.endingDelimiter = getProperty("endingDelimiter", "");
        this.schema = getProperty("schema");
        //lombok扩展
        String lombok = getProperty("lombok");
        if (lombok != null && !"".equals(lombok)) {
            this.needsData = lombok.contains("Data");
            //@Data 优先级高于 @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
            this.needsGetter = !this.needsData && lombok.contains("Getter");
            this.needsSetter = !this.needsData && lombok.contains("Setter");
            this.needsToString = !this.needsData && lombok.contains("ToString");
            this.needsEqualsAndHashCode = !this.needsData && lombok.contains("EqualsAndHashCode");
            // 配置lombok扩展EqualsAndHashCode注解是否添加“callSuper = true”
            String lombokEqualsAndHashCodeCallSuper = getProperty("lombokEqualsAndHashCodeCallSuper", "false");
            this.needsEqualsAndHashCodeAndCallSuper = this.needsEqualsAndHashCode && "TRUE".equalsIgnoreCase(lombokEqualsAndHashCodeCallSuper);
            this.needsAccessors = lombok.contains("Accessors");
            this.needsSuperBuilder = lombok.contains("SuperBuilder");
            this.needsBuilder = !this.needsSuperBuilder && lombok.contains("Builder");
            this.needsNoArgsConstructor = lombok.contains("NoArgsConstructor");
            this.needsAllArgsConstructor = lombok.contains("AllArgsConstructor");
        }
        //swagger扩展
        String swagger = getProperty("swagger", "false");
        if ("TRUE".equalsIgnoreCase(swagger)) {
            this.needsSwagger = true;
        }
        if (useMapperCommentGenerator) {
            commentCfg.addProperty("beginningDelimiter", this.beginningDelimiter);
            commentCfg.addProperty("endingDelimiter", this.endingDelimiter);
            String forceAnnotation = getProperty("forceAnnotation");
            if (StringUtility.stringHasValue(forceAnnotation)) {
                commentCfg.addProperty("forceAnnotation", forceAnnotation);
            }
            commentCfg.addProperty("needsSwagger", this.needsSwagger + "");
        }
        this.generateColumnConsts = getPropertyAsBoolean("generateColumnConsts");
        this.generateDefaultInstanceMethod = getPropertyAsBoolean("generateDefaultInstanceMethod");

        this.logicDelete = Boolean.parseBoolean(this.properties.getProperty("logicDelete"));
    }

    protected String getProperty(String key) {
        return this.properties.getProperty(key);
    }

    protected String getProperty(String key, String defaultValue) {
        return this.properties.getProperty(key, defaultValue);
    }

    protected Boolean getPropertyAsBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

}
