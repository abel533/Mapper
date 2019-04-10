package tk.mybatis.mapper.test.logic;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.mapper.TbUserLogicDeleteMapper;
import tk.mybatis.mapper.mapper.TbUserMapper;
import tk.mybatis.mapper.model.TbUser;
import tk.mybatis.mapper.model.TbUserLogicDelete;

import java.util.List;

public class TestLogicDelete {

    @Test
    public void testLogicDeleteByPrimaryKey() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();

        try {
            TbUserMapper mapper = sqlSession.getMapper(TbUserMapper.class);
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            logicDeleteMapper.deleteByPrimaryKey(3);
            Assert.assertFalse(logicDeleteMapper.existsWithPrimaryKey(3));

            Assert.assertTrue(mapper.existsWithPrimaryKey(3));

            // 删除已经被逻辑删除的数据，受影响行数为0
            Assert.assertEquals(0, logicDeleteMapper.deleteByPrimaryKey(9));

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }

    }

    @Test
    // 删除实体，会带上未删除的查询条件，并忽略实体类给逻辑删除字段设置的值
    public void testLogicDelete() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();

        try {
            TbUserMapper mapper = sqlSession.getMapper(TbUserMapper.class);
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            // 有2条username为test的数据，其中1条已经被标记为逻辑删除
            TbUserLogicDelete tbUserLogicDelete = new TbUserLogicDelete();
            tbUserLogicDelete.setUsername("test");
            Assert.assertTrue(logicDeleteMapper.existsWithPrimaryKey(8));

            // 逻辑删除只会删除1条
            Assert.assertEquals(1, logicDeleteMapper.delete(tbUserLogicDelete));
            Assert.assertFalse(logicDeleteMapper.existsWithPrimaryKey(8));

            // 未删除的一共有4条
            Assert.assertEquals(4, logicDeleteMapper.selectAll().size());

            TbUser tbUser = new TbUser();
            tbUser.setUsername("test");
            Assert.assertEquals(2,  mapper.select(tbUser).size());

            // 物理删除2条已经为逻辑删除状态的数据
            Assert.assertEquals(2, mapper.delete(tbUser));

            // 未删除的总数仍为4条
            Assert.assertEquals(4, logicDeleteMapper.selectAll().size());

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testLogicDeleteByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();

        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);
            TbUserMapper mapper = sqlSession.getMapper(TbUserMapper.class);

            Example example = new Example(TbUserLogicDelete.class);
            example.createCriteria().andEqualTo("id", 1);

            logicDeleteMapper.deleteByExample(example);
            Assert.assertFalse(logicDeleteMapper.existsWithPrimaryKey(1));

            Assert.assertTrue(mapper.existsWithPrimaryKey(1));

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    // 根据主键查询，逻辑删除注解查询时会使用未删除的查询条件
    public void testSelectByPrimaryKey() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);
            Assert.assertNull(logicDeleteMapper.selectByPrimaryKey(9));

            TbUserMapper mapper = sqlSession.getMapper(TbUserMapper.class);
            Assert.assertEquals(0, (int) mapper.selectByPrimaryKey(9).getIsValid());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testExistsWithPrimaryKey() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);
            Assert.assertFalse(logicDeleteMapper.existsWithPrimaryKey(9));

            TbUserMapper mapper = sqlSession.getMapper(TbUserMapper.class);
            Assert.assertTrue(mapper.existsWithPrimaryKey(9));
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    // 查询所有，逻辑删除注解查询时会使用未删除的查询条件
    public void testSelectAll() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);
            Assert.assertEquals(5, logicDeleteMapper.selectAll().size());

            TbUserMapper mapper = sqlSession.getMapper(TbUserMapper.class);
            Assert.assertEquals(9, mapper.selectAll().size());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    // 查询数量，会带上未删除的查询条件，并忽略实体类给逻辑删除字段设置的值
    public void selectCount() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            TbUserLogicDelete tbUserLogicDelete = new TbUserLogicDelete();
            // 实际有5条未删除的，4条已删除的，忽略设置的0值，查询出未删除的5条
            tbUserLogicDelete.setIsValid(0);
            Assert.assertEquals(5, logicDeleteMapper.selectCount(tbUserLogicDelete));

            // 没有逻辑删除注解的，根据指定条件查询
            TbUserMapper mapper = sqlSession.getMapper(TbUserMapper.class);
            TbUser tbUser = new TbUser();
            Assert.assertEquals(9, mapper.selectCount(tbUser));
            tbUser.setIsValid(0);
            Assert.assertEquals(4, mapper.selectCount(tbUser));
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    // 根据实体查询，会带上未删除的查询条件，并忽略实体类给逻辑删除字段设置的值
    public void testSelect() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            TbUserLogicDelete tbUserLogicDelete = new TbUserLogicDelete();
            // 实际有5条未删除的，4条已删除的，忽略设置的0值，查询出未删除的5条
            tbUserLogicDelete.setIsValid(0);
            Assert.assertEquals(5, logicDeleteMapper.select(tbUserLogicDelete).size());

            tbUserLogicDelete.setUsername("test");
            Assert.assertEquals(1, logicDeleteMapper.select(tbUserLogicDelete).size());
            Assert.assertEquals(8, (long) logicDeleteMapper.select(tbUserLogicDelete).get(0).getId());

            TbUserMapper mapper = sqlSession.getMapper(TbUserMapper.class);
            TbUser tbUser = new TbUser();
            // 没有逻辑删除的注解，根据指定条件查询
            tbUser.setIsValid(0);
            Assert.assertEquals(4, mapper.select(tbUser).size());

            tbUser.setUsername("test");
            Assert.assertEquals(1, mapper.select(tbUser).size());
            Assert.assertEquals(9, (long) mapper.select(tbUser).get(0).getId());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            TbUserLogicDelete tbUserLogicDelete = new TbUserLogicDelete();
            tbUserLogicDelete.setUsername("test111");
            logicDeleteMapper.insert(tbUserLogicDelete);

            TbUserMapper mapper = sqlSession.getMapper(TbUserMapper.class);
            TbUser tbUser = new TbUser();
            tbUser.setUsername("test222");
            mapper.insert(tbUser);

            Assert.assertEquals(1, mapper.selectCount(tbUser));

            TbUserLogicDelete tbUserLogicDelete1 = new TbUserLogicDelete();
            tbUserLogicDelete1.setUsername("test222");
            Assert.assertEquals(0, logicDeleteMapper.selectCount(tbUserLogicDelete1));

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testInsertSelective() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            TbUserLogicDelete tbUserLogicDelete = new TbUserLogicDelete();
            tbUserLogicDelete.setUsername("test333");
            logicDeleteMapper.insertSelective(tbUserLogicDelete);

            Assert.assertEquals(1, logicDeleteMapper.selectCount(tbUserLogicDelete));

            TbUserLogicDelete tbUserLogicDelete1 = new TbUserLogicDelete();
            tbUserLogicDelete1.setUsername("test333");
            Assert.assertEquals(1, logicDeleteMapper.selectCount(tbUserLogicDelete1));

            TbUserMapper mapper = sqlSession.getMapper(TbUserMapper.class);
            TbUser tbUser = new TbUser();
            tbUser.setUsername("test333");
            mapper.insertSelective(tbUser);

            TbUser tbUser2 = new TbUser();
            tbUser2.setUsername("test333");
            Assert.assertEquals(2, mapper.selectCount(tbUser2));

            Assert.assertEquals(1, logicDeleteMapper.selectCount(tbUserLogicDelete1));

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testUpdate() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            TbUserLogicDelete tbUserLogicDelete = logicDeleteMapper.selectByPrimaryKey(1);

            tbUserLogicDelete.setPassword(null);
            logicDeleteMapper.updateByPrimaryKey(tbUserLogicDelete);

            Assert.assertNull(logicDeleteMapper.select(tbUserLogicDelete).get(0).getPassword());

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testUpdateSelective() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            TbUserLogicDelete tbUserLogicDelete = logicDeleteMapper.selectByPrimaryKey(1);

            tbUserLogicDelete.setPassword(null);
            logicDeleteMapper.updateByPrimaryKeySelective(tbUserLogicDelete);

            Assert.assertEquals("12345678", logicDeleteMapper.select(tbUserLogicDelete).get(0).getPassword());

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            Example example = new Example(TbUserLogicDelete.class);
            example.createCriteria().andEqualTo("id", 9);
            Assert.assertEquals(0, logicDeleteMapper.selectByExample(example).size());

            example.or().andEqualTo("username", "test");
            Assert.assertEquals(1, logicDeleteMapper.selectByExample(example).size());


        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExample2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            // username为test的有两条  一条标记为已删除
            Example example = new Example(TbUserLogicDelete.class);
            example.createCriteria().andEqualTo("username", "test");
            Assert.assertEquals(1, logicDeleteMapper.selectByExample(example).size());

            // password为dddd的已删除  username为test2的未删除
            example.or().andEqualTo("password", "dddd").orEqualTo("username", "test2");

            Assert.assertEquals(2, logicDeleteMapper.selectByExample(example).size());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testUpdateByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            // username为test的有两条  一条标记为已删除
            Example example = new Example(TbUserLogicDelete.class);
            example.createCriteria().andEqualTo("username", "test");

            TbUserLogicDelete tbUserLogicDelete = new TbUserLogicDelete();
            tbUserLogicDelete.setUsername("123");
            logicDeleteMapper.updateByExample(tbUserLogicDelete, example);

            example.clear();
            example.createCriteria().andEqualTo("username", "123");
            List<TbUserLogicDelete> list = logicDeleteMapper.selectByExample(example);
            Assert.assertEquals(1, list.size());

            Assert.assertNull(list.get(0).getPassword());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testUpdateByExampleSelective() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            Example example = new Example(TbUserLogicDelete.class);
            example.createCriteria().andEqualTo("username", "test");

            TbUserLogicDelete tbUserLogicDelete = new TbUserLogicDelete();
            tbUserLogicDelete.setUsername("123");
            logicDeleteMapper.updateByExampleSelective(tbUserLogicDelete, example);

            example.clear();
            example.createCriteria().andEqualTo("username", "123");
            List<TbUserLogicDelete> list = logicDeleteMapper.selectByExample(example);
            Assert.assertEquals(1, list.size());

            Assert.assertEquals("gggg", list.get(0).getPassword());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    // Example中没有条件的非正常情况，where条件应只有逻辑删除注解的未删除条件
    public void testExampleWithNoCriteria() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            TbUserLogicDeleteMapper logicDeleteMapper = sqlSession.getMapper(TbUserLogicDeleteMapper.class);

            Example example = new Example(TbUserLogicDelete.class);

            TbUserLogicDelete tbUserLogicDelete = new TbUserLogicDelete();
            tbUserLogicDelete.setUsername("123");

            Assert.assertEquals(5, logicDeleteMapper.updateByExample(tbUserLogicDelete, example));

            Assert.assertEquals(5, logicDeleteMapper.updateByExampleSelective(tbUserLogicDelete, example));

            List<TbUserLogicDelete> list = logicDeleteMapper.selectByExample(example);
            Assert.assertEquals(5, list.size());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }
}
