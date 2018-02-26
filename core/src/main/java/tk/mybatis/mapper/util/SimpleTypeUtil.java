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

package tk.mybatis.mapper.util;

import tk.mybatis.mapper.MapperException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 参考 org.apache.ibatis.type.SimpleTypeRegistry
 */
public class SimpleTypeUtil {
    public static final  String[]      JAVA8_DATE_TIME = {
            "java.time.Instant",
            "java.time.LocalDateTime",
            "java.time.LocalDate",
            "java.time.LocalTime",
            "java.time.OffsetDateTime",
            "java.time.OffsetTime",
            "java.time.ZonedDateTime",
            "java.time.Year",
            "java.time.Month",
            "java.time.YearMonth"
    };
    private static final Set<Class<?>> SIMPLE_TYPE_SET = new HashSet<Class<?>>();

    /**
     * 特别注意：由于基本类型有默认值，因此在实体类中不建议使用基本类型作为数据库字段类型
     */
    static {
        SIMPLE_TYPE_SET.add(byte[].class);
        SIMPLE_TYPE_SET.add(String.class);
        SIMPLE_TYPE_SET.add(Byte.class);
        SIMPLE_TYPE_SET.add(Short.class);
        SIMPLE_TYPE_SET.add(Character.class);
        SIMPLE_TYPE_SET.add(Integer.class);
        SIMPLE_TYPE_SET.add(Long.class);
        SIMPLE_TYPE_SET.add(Float.class);
        SIMPLE_TYPE_SET.add(Double.class);
        SIMPLE_TYPE_SET.add(Boolean.class);
        SIMPLE_TYPE_SET.add(Date.class);
        SIMPLE_TYPE_SET.add(Timestamp.class);
        SIMPLE_TYPE_SET.add(Class.class);
        SIMPLE_TYPE_SET.add(BigInteger.class);
        SIMPLE_TYPE_SET.add(BigDecimal.class);
        //反射方式设置 java8 中的日期类型
        for (String time : JAVA8_DATE_TIME) {
            registerSimpleTypeSilence(time);
        }
    }

    /**
     * 注册新的类型
     *
     * @param clazz
     */
    public static void registerSimpleType(Class<?> clazz){
        SIMPLE_TYPE_SET.add(clazz);
    }

    /**
     * 注册 8 种基本类型
     */
    public static void registerPrimitiveTypes(){
        registerSimpleType(boolean.class);
        registerSimpleType(byte.class);
        registerSimpleType(short.class);
        registerSimpleType(int.class);
        registerSimpleType(long.class);
        registerSimpleType(char.class);
        registerSimpleType(float.class);
        registerSimpleType(double.class);
    }

    /**
     * 注册新的类型
     *
     * @param classes
     */
    public static void registerSimpleType(String classes){
        if(StringUtil.isNotEmpty(classes)){
            String[] cls = classes.split(",");
            for (String c : cls) {
                try {
                    SIMPLE_TYPE_SET.add(Class.forName(c));
                } catch (ClassNotFoundException e) {
                    throw new MapperException("注册类型出错:" + c, e);
                }
            }
        }
    }

    /**
     * 注册新的类型，不存在时不抛出异常
     *
     * @param clazz
     */
    private static void registerSimpleTypeSilence(String clazz) {
        try {
            SIMPLE_TYPE_SET.add(Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            //ignore
        }
    }

    /*
     * Tells us if the class passed in is a known common type
     *
     * @param clazz The class to check
     * @return True if the class is known
     */
    public static boolean isSimpleType(Class<?> clazz) {
        return SIMPLE_TYPE_SET.contains(clazz);
    }

}
