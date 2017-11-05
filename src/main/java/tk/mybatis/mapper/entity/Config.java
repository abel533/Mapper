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

package tk.mybatis.mapper.entity;

import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.code.IdentityDialect;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.util.SimpleTypeUtil;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Properties;

/**
 * 通用Mapper属性配置
 *
 * @author liuzh
 */
public class Config {
    private String  UUID;
    private String  IDENTITY;
    private boolean BEFORE;
    private String  seqFormat;
    private String  catalog;
    private String  schema;
    //校验调用Example方法时，Example(entityClass)和Mapper<EntityClass>是否一致
    private boolean checkExampleEntityClass;
    //使用简单类型
    private boolean useSimpleType;
    /**
     * 是否支持方法上的注解，默认false
     */
    private boolean enableMethodAnnotation;
    /**
     * 对于一般的getAllIfColumnNode，是否判断!=''，默认不判断
     */
    private boolean notEmpty = false;

    /**
     * 字段转换风格，默认驼峰转下划线
     */
    private Style style;

    public String getCatalog() {
        return catalog;
    }

    /**
     * 设置全局的catalog,默认为空，如果设置了值，操作表时的sql会是catalog.tablename
     *
     * @param catalog
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * 获取主键自增回写SQL
     *
     * @return
     */
    public String getIDENTITY() {
        if (StringUtil.isNotEmpty(this.IDENTITY)) {
            return this.IDENTITY;
        }
        //针对mysql的默认值
        return IdentityDialect.MYSQL.getIdentityRetrievalStatement();
    }

    /**
     * 主键自增回写方法,默认值MYSQL,详细说明请看文档
     *
     * @param IDENTITY
     */
    public void setIDENTITY(String IDENTITY) {
        IdentityDialect identityDialect = IdentityDialect.getDatabaseDialect(IDENTITY);
        if (identityDialect != null) {
            this.IDENTITY = identityDialect.getIdentityRetrievalStatement();
        } else {
            this.IDENTITY = IDENTITY;
        }
    }

    /**
     * 获取表前缀，带catalog或schema
     *
     * @return
     */
    public String getPrefix() {
        if (StringUtil.isNotEmpty(this.catalog)) {
            return this.catalog;
        }
        if (StringUtil.isNotEmpty(this.schema)) {
            return this.schema;
        }
        return "";
    }

    public String getSchema() {
        return schema;
    }

    /**
     * 设置全局的schema,默认为空，如果设置了值，操作表时的sql会是schema.tablename
     * <br>如果同时设置了catalog,优先使用catalog.tablename
     *
     * @param schema
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * 获取序列格式化模板
     *
     * @return
     */
    public String getSeqFormat() {
        if (StringUtil.isNotEmpty(this.seqFormat)) {
            return this.seqFormat;
        }
        return "{0}.nextval";
    }

    /**
     * 序列的获取规则,使用{num}格式化参数，默认值为{0}.nextval，针对Oracle
     * <br>可选参数一共3个，对应0,1,2,3分别为SequenceName，ColumnName, PropertyName，TableName
     *
     * @param seqFormat
     */
    public void setSeqFormat(String seqFormat) {
        this.seqFormat = seqFormat;
    }

    public Style getStyle() {
        return this.style == null ? Style.camelhump : this.style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    /**
     * 获取UUID生成规则
     *
     * @return
     */
    public String getUUID() {
        if (StringUtil.isNotEmpty(this.UUID)) {
            return this.UUID;
        }
        return "@java.util.UUID@randomUUID().toString().replace(\"-\", \"\")";
    }

    /**
     * 设置UUID生成策略
     * <br>配置UUID生成策略需要使用OGNL表达式
     * <br>默认值32位长度:@java.util.UUID@randomUUID().toString().replace("-", "")
     *
     * @param UUID
     */
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    /**
     * 获取SelectKey的Order
     *
     * @return
     */
    public boolean isBEFORE() {
        return BEFORE;
    }

    public void setBEFORE(boolean BEFORE) {
        this.BEFORE = BEFORE;
    }

    public boolean isCheckExampleEntityClass() {
        return checkExampleEntityClass;
    }

    public void setCheckExampleEntityClass(boolean checkExampleEntityClass) {
        this.checkExampleEntityClass = checkExampleEntityClass;
    }

    public boolean isEnableMethodAnnotation() {
        return enableMethodAnnotation;
    }

    public void setEnableMethodAnnotation(boolean enableMethodAnnotation) {
        this.enableMethodAnnotation = enableMethodAnnotation;
    }

    public boolean isNotEmpty() {
        return notEmpty;
    }

    public void setNotEmpty(boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public boolean isUseSimpleType() {
        return useSimpleType;
    }

    public void setUseSimpleType(boolean useSimpleType) {
        this.useSimpleType = useSimpleType;
    }

    /**
     * 主键自增回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)
     *
     * @param order
     */
    public void setOrder(String order) {
        this.BEFORE = "BEFORE".equalsIgnoreCase(order);
    }

    /**
     * 配置属性
     *
     * @param properties
     */
    public void setProperties(Properties properties) {
        if (properties == null) {
            //默认驼峰
            this.style = Style.camelhump;
            return;
        }
        String UUID = properties.getProperty("UUID");
        if (StringUtil.isNotEmpty(UUID)) {
            setUUID(UUID);
        }
        String IDENTITY = properties.getProperty("IDENTITY");
        if (StringUtil.isNotEmpty(IDENTITY)) {
            setIDENTITY(IDENTITY);
        }
        String seqFormat = properties.getProperty("seqFormat");
        if (StringUtil.isNotEmpty(seqFormat)) {
            setSeqFormat(seqFormat);
        }
        String catalog = properties.getProperty("catalog");
        if (StringUtil.isNotEmpty(catalog)) {
            setCatalog(catalog);
        }
        String schema = properties.getProperty("schema");
        if (StringUtil.isNotEmpty(schema)) {
            setSchema(schema);
        }
        String ORDER = properties.getProperty("ORDER");
        if (StringUtil.isNotEmpty(ORDER)) {
            setOrder(ORDER);
        }
        String notEmpty = properties.getProperty("notEmpty");
        if (StringUtil.isNotEmpty(notEmpty)) {
            this.notEmpty = notEmpty.equalsIgnoreCase("TRUE");
        }
        String enableMethodAnnotation = properties.getProperty("enableMethodAnnotation");
        if (StringUtil.isNotEmpty(enableMethodAnnotation)) {
            this.enableMethodAnnotation = enableMethodAnnotation.equalsIgnoreCase("TRUE");
        }
        String checkExampleStr = properties.getProperty("checkExampleEntityClass");
        if (StringUtil.isNotEmpty(checkExampleStr)) {
            this.checkExampleEntityClass = checkExampleStr.equalsIgnoreCase("TRUE");
        }
        String useSimpleTypeStr = properties.getProperty("useSimpleType");
        if (StringUtil.isNotEmpty(useSimpleTypeStr)) {
            this.useSimpleType = useSimpleTypeStr.equalsIgnoreCase("TRUE");
        }
        //注册新的基本类型，以逗号隔开，使用全限定类名
        String simpleTypes = properties.getProperty("simpleTypes");
        if (StringUtil.isNotEmpty(simpleTypes)) {
            SimpleTypeUtil.registerSimpleType(simpleTypes);
        }
        String styleStr = properties.getProperty("style");
        if (StringUtil.isNotEmpty(styleStr)) {
            try {
                this.style = Style.valueOf(styleStr);
            } catch (IllegalArgumentException e) {
                throw new MapperException(styleStr + "不是合法的Style值!");
            }
        } else {
            //默认驼峰
            this.style = Style.camelhump;
        }
    }
}
