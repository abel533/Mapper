package tk.mybatis.mapper.additional.select;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.additional.BaseTest;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class SelectByPropertyMapperTest extends BaseTest {

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
    public void selectOneByPropertyTest() {
        SqlSession sqlSession = getSqlSession();
        try {
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            Book book = mapper.selectOneByProperty(Book::getName, "JavaStarter1");
            Assert.assertNotNull(book);
            Assert.assertEquals("JavaStarter1", book.getName());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void selectByPropertyTest() {
        SqlSession sqlSession = getSqlSession();
        try {
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            List<Book> books = mapper.selectByProperty(Book::getPrice, 50);
            Assert.assertEquals(2, books.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void selectInByPropertyTest() {
        SqlSession sqlSession = getSqlSession();
        try {
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            List<Book> books = mapper.selectInByProperty(Book::getPrice, Arrays.asList(50, 80));
            Assert.assertEquals(3, books.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void selectBetweenByPropertyTest() {
        SqlSession sqlSession = getSqlSession();
        try {
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            List<Book> books = mapper.selectBetweenByProperty(Book::getPublished, LocalDate.of(2015, 11, 11),
                    LocalDate.of(2019, 11, 11));
            Assert.assertEquals(4, books.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void selectCountByPropertyTest() {
        SqlSession sqlSession = getSqlSession();
        try {
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            int count = mapper.selectCountByProperty(Book::getPrice, 50);
            Assert.assertEquals(2, count);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void existsWithPropertyTest() {
        SqlSession sqlSession = getSqlSession();
        try {
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            boolean exist = mapper.existsWithProperty(Book::getPrice, 50);
            Assert.assertTrue(exist);
        } finally {
            sqlSession.close();
        }
    }

}
