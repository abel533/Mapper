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

package tk.mybatis.mapper.helper;

import org.junit.Test;
import tk.mybatis.mapper.entity.EntityField;
import tk.mybatis.mapper.mapperhelper.FieldHelper;
import tk.mybatis.mapper.model.Country;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

/**
 * @author liuzh_3nofxnp
 * @since 2015-12-28 21:30
 */
public class FieldTest {

    /**
     * 通过方法获取属性
     *
     * @param entityClass
     * @return
     */
    private static List<EntityField> _getProperties(Class<?> entityClass) {
        Map<String, Class<?>> genericMap = _getGenericTypeMap(entityClass);
        List<EntityField> entityFields = new ArrayList<EntityField>();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(entityClass);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor desc : descriptors) {
            if (desc != null && !"class".equals(desc.getName())) {
                EntityField entityField = new EntityField(null, desc);
                if (desc.getReadMethod() != null
                        && desc.getReadMethod().getGenericReturnType() != null
                        && desc.getReadMethod().getGenericReturnType() instanceof TypeVariable) {
                    entityField.setJavaType(genericMap.get(((TypeVariable) desc.getReadMethod().getGenericReturnType()).getName()));
                } else if (desc.getWriteMethod() != null
                        && desc.getWriteMethod().getGenericParameterTypes() != null
                        && desc.getWriteMethod().getGenericParameterTypes().length == 1
                        && desc.getWriteMethod().getGenericParameterTypes()[0] instanceof TypeVariable) {
                    entityField.setJavaType(genericMap.get(((TypeVariable) desc.getWriteMethod().getGenericParameterTypes()[0]).getName()));
                }
                entityFields.add(entityField);
            }
        }
        return entityFields;
    }

    /**
     * @param entityClass
     */
    private static Map<String, Class<?>> _getGenericTypeMap(Class<?> entityClass) {
        Map<String, Class<?>> genericMap = new HashMap<String, Class<?>>();
        if (entityClass == Object.class) {
            return genericMap;
        }
        //获取父类和泛型信息
        Class<?> superClass = entityClass.getSuperclass();
        //判断superClass
        if (superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(Entity.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)))) {
            if (entityClass.getGenericSuperclass() instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) entityClass.getGenericSuperclass()).getActualTypeArguments();
                TypeVariable[] typeVariables = superClass.getTypeParameters();
                if (typeVariables.length > 0) {
                    for (int i = 0; i < typeVariables.length; i++) {
                        if (types[i] instanceof Class) {
                            genericMap.put(typeVariables[i].getName(), (Class<?>) types[i]);
                        }
                    }
                }
            }
            genericMap.putAll(_getGenericTypeMap(superClass));
        }
        return genericMap;
    }

    private static void processAllColumns(Class<?> entityClass, List<EntityField> fieldList, Map<String, Class<?>> genericMap) {
        if (fieldList == null) {
            fieldList = new ArrayList<EntityField>();
        }
        if (entityClass == Object.class) {
            return;
        }
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            EntityField entityField = new EntityField(field, null);
            if (field.getGenericType() != null && field.getGenericType() instanceof TypeVariable) {
                if (genericMap == null || !genericMap.containsKey(((TypeVariable) field.getGenericType()).getName())) {
                    throw new RuntimeException(entityClass + "字段" + field.getName() + "的泛型类型无法获取!");
                } else {
                    entityField.setJavaType(genericMap.get(((TypeVariable) field.getGenericType()).getName()));
                }
            } else {
                entityField.setJavaType(field.getType());
            }
            fieldList.add(entityField);
        }

        Class<?> superClass = entityClass.getSuperclass();
        Map<String, Class<?>> _genericMap = null;
        if (entityClass.getGenericSuperclass() instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) entityClass.getGenericSuperclass()).getActualTypeArguments();
            TypeVariable[] typeVariables = superClass.getTypeParameters();
            if (typeVariables.length > 0) {
                _genericMap = new HashMap<String, Class<?>>();
                for (int i = 0; i < typeVariables.length; i++) {
                    _genericMap.put(typeVariables[i].getName(), (Class<?>) types[i]);
                }
            }
        }
        processAllColumns(superClass, fieldList, _genericMap);
    }

    //    @Test
    public void test1() throws IntrospectionException {
        List<EntityField> fields = null;// = new ArrayList<EntityField>();
        processAllColumns(Country.class, fields, null);
        for (EntityField field : fields) {
            System.out.println(field.getName() + "  -  @Id:" + field.isAnnotationPresent(Id.class) + "  -  javaType:" + field.getJavaType());
        }
        System.out.println("======================================");

        fields = FieldHelper.getAll(Country.class);
        for (EntityField field : fields) {
            System.out.println(field.getName() + "  -  @Id:" + field.isAnnotationPresent(Id.class) + "  -  javaType:" + field.getJavaType());
        }
        System.out.println("======================================");
    }

    @Test
    public void test2() {
        List<EntityField> fields = _getProperties(Country.class);
        for (EntityField field : fields) {
            System.out.println(field.getName() + "  -  @Id:" + field.isAnnotationPresent(Id.class) + "  -  javaType:" + field.getJavaType());
        }
        System.out.println("======================================");
    }
}
