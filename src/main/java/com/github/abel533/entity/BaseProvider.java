/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 abel533@gmail.com
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

package com.github.abel533.entity;

import com.github.abel533.mapperhelper.MapperTemplate;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.util.List;
import java.util.Map;

/**
 * 基础类
 * <p/>
 * Created by liuzh on 2015/1/16.
 */
public class BaseProvider {

    /**
     * 主键字段不能为空
     *
     * @param property
     * @param value
     */
    protected void notNullKeyProperty(String property, Object value) {
        if (value == null || (value instanceof String && isEmpty((String) value))) {
            throwNullKeyException(property);
        }
    }

    protected void throwNullKeyException(String property) {
        throw new NullPointerException("主键属性" + property + "不能为空!");
    }

    /**
     * 获取实体类型
     *
     * @param params
     * @return
     */
    protected Class<?> getEntityClass(Map<String, Object> params) {
        Class<?> entityClass = null;
        if (params.containsKey("record")) {
            entityClass = getEntity(params).getClass();
        } else if (params.containsKey("entityClass")) {
            entityClass = (Class<?>) params.get("entityClass");
        }
        if (entityClass == null) {
            throw new RuntimeException("无法获取实体类型!");
        }
        return entityClass;
    }

    /**
     * 获取实体类
     *
     * @param params
     * @return
     */
    protected Object getEntity(Map<String, Object> params) {
        Object result;
        if (params.containsKey("record")) {
            result = params.get("record");
        } else if (params.containsKey("key")) {
            result = params.get("key");
        } else {
            throw new RuntimeException("当前方法没有实体或主键参数!");
        }
        if (result == null) {
            throw new NullPointerException("实体或者主键参数不能为空!");
        }
        return result;
    }

    /**
     * Example条件
     */
    protected String applyWhere(Object example, boolean includeExamplePhrase) {
        if (example == null) {
            return "";
        }
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        StringBuilder sb = new StringBuilder();
        MetaObject exampleObject = MapperTemplate.forObject(example);
        //TODO 根据Example的结构，通过判断是否包含某些属性来判断条件是否为合法的example类型
        List<?> oredCriteria = (List<?>) exampleObject.getValue("oredCriteria");
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            MetaObject criteria = MapperTemplate.forObject(oredCriteria.get(i));
            List<?> criterions = (List<?>) criteria.getValue("criteria");
            if (criterions.size() > 0) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }

                sb.append('(');
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    MetaObject criterion = MapperTemplate.forObject(criterions.get(j));
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }

                    if ((Boolean) criterion.getValue("noValue")) {
                        sb.append(criterion.getValue("condition"));
                    } else if ((Boolean) criterion.getValue("singleValue")) {
                        if (criterion.getValue("typeHandler") == null) {
                            sb.append(String.format(parmPhrase1, criterion.getValue("condition"), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getValue("condition"), i, j, criterion.getValue("typeHandler")));
                        }
                    } else if ((Boolean) criterion.getValue("betweenValue")) {
                        if (criterion.getValue("typeHandler") == null) {
                            sb.append(String.format(parmPhrase2, criterion.getValue("condition"), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getValue("condition"), i, j, criterion.getValue("typeHandler"), i, j, criterion.getValue("typeHandler")));
                        }
                    } else if ((Boolean) criterion.getValue("listValue")) {
                        sb.append(criterion.getValue("condition"));
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue("value");
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getValue("typeHandler") == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getValue("typeHandler")));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }

        if (sb.length() > 0) {
            return sb.toString();
        }
        return "";
    }

    protected boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    protected boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }
}
