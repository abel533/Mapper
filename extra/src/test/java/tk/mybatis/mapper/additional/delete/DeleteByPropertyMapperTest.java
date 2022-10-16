package tk.mybatis.mapper.additional.delete;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.additional.BaseTest;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DeleteByPropertyMapperTest extends BaseTest {

    /**
     * 获取 mybatis 配置
     *
     * @return
     */
    protected Reader getConfigFileAsReader() throws IOException {
        URL url = getClass().getResource("mybatis-config.xml");
        return toReader(url);
    }

    /**
     * 获取初始化 sql
     *
     * @return
     */
    protected Reader getSqlFileAsReader() throws IOException {
        URL url = getClass().getResource("CreateDB.sql");
        return toReader(url);
    }

    @Test
    public void deleteByPropertyTest() {
        SqlSession sqlSession = getSqlSession();
        try {
            CourseMapper mapper = sqlSession.getMapper(CourseMapper.class);

            Course beforeDelete = mapper.selectByPrimaryKey(2);
            Assert.assertNotNull(beforeDelete);
            Assert.assertEquals("JavaStarter2", beforeDelete.getName());

            int deletedCount = mapper.deleteByProperty(Course::getName, "JavaStarter2");
            Assert.assertEquals(1, deletedCount);

            Course afterDelete = mapper.selectByPrimaryKey(2);
            Assert.assertNull(afterDelete);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void deleteInByPropertyTest() {
        SqlSession sqlSession = getSqlSession();
        try {
            CourseMapper mapper = sqlSession.getMapper(CourseMapper.class);

            List<Course> beforeDelete = mapper.selectAll();
            Assert.assertEquals(4, beforeDelete.size());

            int deletedCount = mapper.deleteInByProperty(Course::getPrice, Arrays.asList(50, 80, 100));

            Assert.assertEquals(4, deletedCount);

            List<Course> afterDelete = mapper.selectAll();
            Assert.assertEquals(0, afterDelete.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void deleteBetweenByPropertyTest() {
        SqlSession sqlSession = getSqlSession();
        try {
            CourseMapper mapper = sqlSession.getMapper(CourseMapper.class);

            List<Course> beforeDelete = mapper.selectAll();
            Assert.assertEquals(4, beforeDelete.size());

            int deletedCount = mapper.deleteBetweenByProperty(Course::getPrice, 80, 100);

            Assert.assertEquals(2, deletedCount);

            List<Course> afterDelete = mapper.selectAll();
            Assert.assertEquals(2, afterDelete.size());
        } finally {
            sqlSession.close();
        }
    }
}
