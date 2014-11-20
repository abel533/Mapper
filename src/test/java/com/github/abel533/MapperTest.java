package com.github.abel533;

import com.github.abel533.mapper.CountryMapper;
import com.github.abel533.mapper.Mapper;
import com.github.abel533.model.Country;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by liuzh on 2014/11/19.
 */
public class MapperTest {

    @Test
    public void test(){
        SqlSession sqlSession = MybatisHelper.getSqlSession();
//        Mapper<Country> mapper = sqlSession.getMapper(Mapper.class);
        CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
        Country country = mapper.selectByPrimaryKey(1);
        System.out.println(country);
        country = mapper.selectByPrimaryKey(2);
        System.out.println(country);

    }

    @Test
    public void testMapper(){
        Class clazz = CountryMapper.class;
        for (Method method : clazz.getMethods()) {
            System.out.println(method.getName()+":"+method.getReturnType());
        }

        Type[] types = clazz.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
                if (t.getRawType() == Mapper.class) {
                    System.out.println(t.getActualTypeArguments()[0]);
                }
            }

        }
    }

    //com.github.abel533.mapper.CountryMapper.selectByPrimaryKey

    @Test
    public void testMSID() throws ClassNotFoundException{
        System.out.println(getSelectReturnType("com.github.abel533.mapper.CountryMapper.selectByPrimaryKey"));
    }

    public Class getSelectReturnType(String msid) throws ClassNotFoundException {
        String mapperClassStr = msid.substring(0,msid.lastIndexOf("."));
        Class mapperClass = Class.forName(mapperClassStr);
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
                if (t.getRawType() == Mapper.class) {
                    return (Class) t.getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }
}
