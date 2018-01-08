package tk.mybatis.mapper.test.Logical;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.LogicalDeleteMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import tk.mybatis.mapper.model.LogicalDeleteEntity;

import static org.junit.Assert.*;

/**
 * @author CarlJia.
 * @date 2018-01-08.
 */
public class LogicalDeleteTest {
    /**
     * 新增
     */
    @Test
    public void deleteTest() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            LogicalDeleteMapper mapper = sqlSession.getMapper(LogicalDeleteMapper.class);
            // 逻辑删除实体
            LogicalDeleteEntity entity = new LogicalDeleteEntity();
            entity.setCode("test");
            entity.setDelFlag("0");

            assertEquals(1, mapper.insert(entity));

            assertNotNull(entity.getId());
            assertEquals("0", entity.getDelFlag());


            entity.setDelFlag("1");
            mapper.deleteByPrimaryKey(entity);

            // 逻辑删除，没有物理删除
            LogicalDeleteEntity selectByPrimaryKey = mapper.selectByPrimaryKey(entity.getId());
            assertEquals("1",selectByPrimaryKey.getDelFlag());

            // 物理删除测试
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            Country entity1 = new Country();
            entity1.setId(10086);
            entity1.setCountrycode("CN");
            entity1.setCountryname("天朝");
            assertEquals(1, countryMapper.insert(entity1));
            assertNotNull( countryMapper.selectByPrimaryKey(entity1));

            countryMapper.deleteByPrimaryKey(entity1);

            assertNull( countryMapper.selectByPrimaryKey(entity1));

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }
}