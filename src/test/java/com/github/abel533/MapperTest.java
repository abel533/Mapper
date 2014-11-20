package com.github.abel533;

import com.github.abel533.mapper.CountryMapper;
import com.github.abel533.mapper.Mapper;
import com.github.abel533.model.Country;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by liuzh on 2014/11/19.
 */
public class MapperTest {

    @Test
    public void test(){
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
        //查询
        Country country = mapper.selectByPrimaryKey(1);
        System.out.println(country);
        //删除
        System.out.println(mapper.deleteByPrimaryKey(100));
        //新增
        country.setId(1000);
        country.setCountryname("中国");
        System.out.println(mapper.insert(country));
        //更新
        country.setCountrycode("C");
        System.out.println(mapper.updateByPrimaryKey(country));
        //查询
        country = mapper.selectByPrimaryKey(1000);
        System.out.println(country);
    }

    @Test
    public void testDynamicSelect(){
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
        Country country = new Country();
        country.setId(100);
        //查询
        List<Country> list = mapper.select(country);
        System.out.println(list.get(0));

        country = new Country();
        country.setCountrycode("MY");
        list = mapper.select(country);
        System.out.println(list.get(0));
    }

    @Test
    public void testDynamicSelectCount(){
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
        Country country = new Country();
        country.setId(100);
        //查询
        System.out.println(mapper.selectCount(country));
        System.out.println(mapper.selectCount(new Country()));
    }

    @Test
    public void testDynamicUpdate(){
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
        Country country = new Country();
        country.setId(100);
        country.setCountryname("中国");
        //选择性更新
        System.out.println(mapper.updateByPrimaryKeySelective(country));
        //查询更新结果
        country = mapper.selectByPrimaryKey(100);
        System.out.println(country);
    }

    @Test
    public void testDynamicInsert(){
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
        Country country = new Country();
        country.setId(1000);
        country.setCountryname("lzh");
        //选择性插入
        System.out.println(mapper.insertSelective(country));
        //查询插入结果
        country = mapper.selectByPrimaryKey(1000);
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
