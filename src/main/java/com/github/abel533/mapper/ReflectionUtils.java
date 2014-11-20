package com.github.abel533.mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class ReflectionUtils {


    private static final Map<Class<?>, Method[]> METHODS_CACHEMAP = new HashMap<Class<?>, Method[]>();

    public static List<String> findMethodAllName(Class<?> cls) {
        List<String> list = new ArrayList<String>();
        Method[] methods = cls.getDeclaredMethods();
        String name = cls.getName();
        for (Method method : methods) {
            list.add(name + "." + method.getName());
        }
        return list;
    }

    public static Map<String, String> findColumnsV(Object object) {
        Map<String, String> map = new HashMap<String, String>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                continue;
            }
            Object obj = ReflectionUtils.invokeGetterMethod(object, field.getName());
            if (null != obj) {
                map.put(field.getAnnotation(Column.class).name(), field.getName());
            }
        }
        return map;
    }

    public static Class<?> findType(Object object, String fieldName) {
        try {
            return object.getClass().getDeclaredField(fieldName).getType();
        } catch (SecurityException e) {
            throw new RuntimeException(object.getClass().getName() + " undefine " + fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(object.getClass().getName() + " undefine " + fieldName);
        }

    }

    /**
     * findColumns:获取不包括主键的所有属性的名称及其列名
     *
     * @param object
     * @return
     * @since JDK 1.6
     */
    public static Map<String, String> findColumns(Object object) {
        Map<String, String> map = new HashMap<String, String>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)) {
                map.put(field.getAnnotation(Column.class).name(), field.getName());
            }
        }
        return map;
    }

    public static String findTable(Object obj) {
        if (null == obj) {
            throw new RuntimeException("mybatis参数为空");
        }
        Table table = obj.getClass().getAnnotation(Table.class);
        if (table != null)
            return table.name();
        else
            throw new RuntimeException(obj.getClass().getName() + " undefine @Table, need Tablename(@Table)");
    }

    public static String findPrimary(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class))
                return field.getAnnotation(Column.class).name();
        }
        throw new RuntimeException(obj.getClass().getName() + "undefine @Id");
    }

    public static String findPrimaryProperty(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class))
                return field.getName();
        }
        throw new RuntimeException(obj.getClass().getName() + "undefine @Id");
    }

    public static String findSequenceName(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(SequenceGenerator.class))
                return field.getAnnotation(SequenceGenerator.class).sequenceName();
        }
        throw new RuntimeException(obj.getClass().getName() + "undefine @SequenceGenerator");
    }

    /**
     * copyProperties:反射 取值、设值,合并两个对象(Field same only )
     *
     * @param fromobj
     * @param toobj
     * @param fieldspec
     * @since JDK 1.6
     */
    public static <T> void copyProperties(T fromobj, T toobj, String... fieldspec) {
        for (String filename : fieldspec) {
            Object val = ReflectionUtils.invokeGetterMethod(fromobj, filename);
            ReflectionUtils.invokeSetterMethod(toobj, filename, val);
        }
    }

    /**
     * invokeGetterMethod:调用Getter方法
     *
     * @param obj
     * @param propertyName
     * @return
     * @since JDK 1.6
     */
    public static Object invokeGetterMethod(Object obj, String propertyName) {
        String getterMethodName = "get" + Introspector.decapitalize(propertyName);
        return invokeMethod(obj, getterMethodName, null, null);
    }

    /**
     * invokeSetterMethod:调用Setter方法,不指定参数的类型
     *
     * @param obj
     * @param propertyName
     * @param value
     * @since JDK 1.6
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
        invokeSetterMethod(obj, propertyName, value, null);
    }

    /**
     * invokeSetterMethod:调用Setter方法,指定参数的类型
     *
     * @param obj
     * @param propertyName
     * @param value
     * @param propertyType
     * @since JDK 1.6
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {
        value = handleValueType(obj, propertyName, value);
        propertyType = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = "set" + Introspector.decapitalize(propertyName);
        invokeMethod(obj, setterMethodName, new Class<?>[]{propertyType}, new Object[]{value});

    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    private static Object handleValueType(Object obj, String propertyName, Object value) {
        String getterMethodName = "get" + Introspector.decapitalize(propertyName);
        Class<?> argsType = value.getClass();
        Class<?> returnType = obtainAccessibleMethod(obj, getterMethodName).getReturnType();
        if (argsType == returnType) {
            return value;
        }

        if (returnType == Boolean.class) {
            String temp = value.toString();
            value = (!isBlank(temp) && Long.valueOf(temp) > 0) ? true : false;
        } else if (returnType == Long.class) {
            value = Long.valueOf(value.toString());
        } else if (returnType == Date.class) {

            // try {
            //
            // value = SimpleDateFormatFactory.getInstance(DateUtil.FULL_TIME_FORMAT).parse(value.toString());
            //
            // } catch (ParseException e) {
            //
            // logger.error("类型转型Timpestap-->Date时，发生错误! " + e.getMessage() + "("+value.toString()+")");
            //
            // }

        } else if (returnType == Short.class) {
            value = Short.valueOf(value.toString());
        } else if (returnType == BigDecimal.class) {
            value = BigDecimal.valueOf(Long.valueOf(value.toString()));
        } else if (returnType == BigInteger.class) {
            value = BigInteger.valueOf(Long.valueOf(value.toString()));
        } else if (returnType == String.class) {
            value = String.valueOf(value);
        } else if (returnType == Integer.class) {
            value = Integer.valueOf(value.toString());
        }
        return value;

    }

    /**
     * invokeMethod:直接调用对象方法，忽视private/protected修饰符
     *
     * @param obj
     * @param methodName
     * @param parameterTypes
     * @param args
     * @return
     * @since JDK 1.6
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
                                      final Object[] args) {
        Method method = obtainAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            return null;
        }

        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * obtainAccessibleMethod:循环向上转型，获取对象的DeclaredMethod,并强制设置为可访问 如向上转型到Object仍无法找到，返回null
     * 用于方法需要被多次调用的情况，先使用本函数先取得Method,然后调用Method.invoke(Object obj,Object... args)
     *
     * @param obj
     * @param methodName
     * @param parameterTypes
     * @return
     * @since JDK 1.6
     */
    public static Method obtainAccessibleMethod(final Object obj, final String methodName,
                                                final Class<?>... parameterTypes) {
        Class<?> superClass = obj.getClass();
        Class<Object> objClass = Object.class;
        for (; superClass != objClass; superClass = superClass.getSuperclass()) {
            Method method = null;
            try {
                method = superClass.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义，向上转型

            } catch (SecurityException e) {
                // Method不在当前类定义，向上转型
            }
        }

        return null;

    }

    /**
     * obtainMethod:不能确定方法是否包含参数时，通过方法名匹配获得方法
     *
     * @param obj
     * @param methodName
     * @return
     * @since JDK 1.6
     */
    public static Method obtainMethod(final Object obj, final String methodName) {
        Class<?> clazz = obj.getClass();
        Method[] methods = METHODS_CACHEMAP.get(clazz);
        if (methods == null) { // 尚未缓存
            methods = clazz.getDeclaredMethods();
            METHODS_CACHEMAP.put(clazz, methods);
        }
        for (Method method : methods) {
            if (method.getName().equals(methodName))
                return method;
        }
        return null;
    }

    /**
     * obtainFieldValue:直接读取对象属性值 忽视private/protected修饰符，不经过getter函数
     *
     * @param obj
     * @param fieldName
     * @return
     * @since JDK 1.6
     */
    public static Object obtainFieldValue(final Object obj, final String fieldName) {
        Field field = obtainAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Devkit: could not find field [" + fieldName + "] on target [" + obj
                    + "]");
        }
        Object retval = null;
        try {
            retval = field.get(obj);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return retval;

    }

    /**
     * setFieldValue:直接设置对象属性值 忽视private/protected修饰符，不经过setter函数
     *
     * @param obj
     * @param fieldName
     * @param value
     * @since JDK 1.6
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = obtainAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Devkit: could not find field [" + fieldName + "] on target [" + obj
                    + "]");
        }
        try {
            field.set(obj, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * obtainAccessibleField:循环向上转型，获取对象的DeclaredField,并强制设为可访问 如向上转型Object仍无法找到，返回null
     *
     * @param obj
     * @param fieldName
     * @return
     * @since JDK 1.6
     */
    public static Field obtainAccessibleField(final Object obj, final String fieldName) {
        Class<?> superClass = obj.getClass();
        Class<Object> objClass = Object.class;
        for (; superClass != objClass; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义，向上转型
            } catch (SecurityException e) {
                // Field不在当前类定义，向上转型
            }

        }
        return null;

    }

}
