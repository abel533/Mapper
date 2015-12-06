package tk.mybatis.mapper.mapperhelper;

import tk.mybatis.mapper.entity.EntityField;

import javax.persistence.Entity;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 类字段工具类
 *
 * @author liuzh
 * @since 2015-12-06 18:38
 */
public class FieldHelper {

    /**
     * 获取全部的Field
     *
     * @param entityClass
     * @return
     */
    public static List<EntityField> getFields(Class<?> entityClass) {
        List<EntityField> fields = _getFields(entityClass, null, null);
        List<EntityField> properties = _getProperties(entityClass);
        Set<EntityField> usedSet = new HashSet<EntityField>();
        for (EntityField field : fields) {
            for (EntityField property : properties) {
                if (!usedSet.contains(property) && field.getName().equals(property.getName())) {
                    //泛型的情况下通过属性可以得到实际的类型
                    field.setJavaType(property.getJavaType());
                    break;
                }
            }
        }
        return fields;
    }

    /**
     * 获取全部的属性，包含字段和方法
     *
     * @param entityClass
     * @return
     * @throws IntrospectionException
     */
    public static List<EntityField> getAll(Class<?> entityClass) {
        List<EntityField> fields = _getFields(entityClass, null, null);
        List<EntityField> properties = _getProperties(entityClass);
        //拼到一起，名字相同的合并
        List<EntityField> all = new ArrayList<EntityField>();
        Set<EntityField> usedSet = new HashSet<EntityField>();
        for (EntityField field : fields) {
            for (EntityField property : properties) {
                if (!usedSet.contains(property) && field.getName().equals(property.getName())) {
                    field.copyFromPropertyDescriptor(property);
                    usedSet.add(property);
                    break;
                }
            }
            all.add(field);
        }
        for (EntityField property : properties) {
            if (!usedSet.contains(property)) {
                all.add(property);
            }
        }
        return all;
    }

    /**
     * 获取全部的Field，仅仅通过Field获取
     *
     * @param entityClass
     * @param fieldList
     * @param level
     * @return
     */
    private static List<EntityField> _getFields(Class<?> entityClass, List<EntityField> fieldList, Integer level) {
        if (fieldList == null) {
            fieldList = new ArrayList<EntityField>();
        }
        if (level == null) {
            level = 0;
        }
        if (entityClass.equals(Object.class)) {
            return fieldList;
        }
        Field[] fields = entityClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            //排除静态字段，解决bug#2
            if (!Modifier.isStatic(field.getModifiers())) {
                if (level != 0) {
                    fieldList.add(i, new EntityField(field, null));
                } else {
                    fieldList.add(new EntityField(field, null));
                }
            }
        }
        Class<?> superClass = entityClass.getSuperclass();
        if (superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(Entity.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)))) {
            return _getFields(entityClass.getSuperclass(), fieldList, ++level);
        }
        return fieldList;
    }

    /**
     * 通过方法获取属性
     *
     * @param entityClass
     * @return
     */
    private static List<EntityField> _getProperties(Class<?> entityClass) {
        List<EntityField> entityFields = new ArrayList<EntityField>();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(entityClass);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor desc : descriptors) {
            if (!desc.getName().equals("class")) {
                entityFields.add(new EntityField(null, desc));
            }
        }
        return entityFields;
    }
}
