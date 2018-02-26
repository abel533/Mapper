package tk.mybatis.mapper.issues._216_datetime;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.base.BaseTest;

import java.io.IOException;
import java.io.Reader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateTimeTest extends BaseTest {

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        return toReader(DateTimeTest.class.getResource("mybatis-config-timestamp.xml"));
    }

    @Override
    protected Reader getSqlFileAsReader() throws IOException {
        return toReader(DateTimeTest.class.getResource("CreateDB.sql"));
    }

    private String toDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private String toTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }

    private String toDatetime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    @Test
    public void testSelect() {
        SqlSession sqlSession = getSqlSession();
        try {
            TimeModelMapper mapper = sqlSession.getMapper(TimeModelMapper.class);
            List<TimeModel> list = mapper.selectAll();
            Assert.assertEquals(2, list.size());

            Assert.assertEquals("2018-01-01", toDate(list.get(0).getTestDate()));
            Assert.assertEquals("12:11:00", toTime(list.get(0).getTestTime()));
            Assert.assertEquals("2018-01-01 12:00:00", toDatetime(list.get(0).getTestDatetime()));

            Assert.assertEquals("2018-11-11", toDate(list.get(1).getTestDate()));
            Assert.assertEquals("01:59:11", toTime(list.get(1).getTestTime()));
            Assert.assertEquals("2018-02-12 17:58:12", toDatetime(list.get(1).getTestDatetime()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsert() {
        SqlSession sqlSession = getSqlSession();
        try {
            TimeModelMapper mapper = sqlSession.getMapper(TimeModelMapper.class);
            TimeModel timeModel = new TimeModel();
            timeModel.setId(3);
            Date now = new Date();
            timeModel.setTestDate(now);
            timeModel.setTestTime(now);
            timeModel.setTestDatetime(now);
            Assert.assertEquals(1, mapper.insert(timeModel));

            timeModel = mapper.selectByPrimaryKey(3);

            //保存后数据库中不存在时间部分
            Assert.assertEquals(toDate(now), toDate(timeModel.getTestDate()));
            Assert.assertEquals(toDate(now) + " 00:00:00", toDatetime(timeModel.getTestDate()));

            //日期和时间都有
            Assert.assertEquals(toTime(now), toTime(timeModel.getTestTime()));
            Assert.assertEquals(toDatetime(now), toDatetime(timeModel.getTestTime()));

            Assert.assertEquals(toDatetime(now), toDatetime(timeModel.getTestDatetime()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelect2() {
        SqlSession sqlSession = getSqlSession();
        try {
            TimeModel2Mapper mapper = sqlSession.getMapper(TimeModel2Mapper.class);
            List<TimeModel2> list = mapper.selectAll();
            Assert.assertEquals(2, list.size());

            Assert.assertEquals("2018-01-01", toDate(list.get(0).getTestDate()));
            Assert.assertEquals("12:11:00", toTime(list.get(0).getTestTime()));
            Assert.assertEquals("2018-01-01 12:00:00", toDatetime(list.get(0).getTestDatetime()));

            Assert.assertEquals("2018-11-11", toDate(list.get(1).getTestDate()));
            Assert.assertEquals("01:59:11", toTime(list.get(1).getTestTime()));
            Assert.assertEquals("2018-02-12 17:58:12", toDatetime(list.get(1).getTestDatetime()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsert2() {
        SqlSession sqlSession = getSqlSession();
        try {
            TimeModel2Mapper mapper = sqlSession.getMapper(TimeModel2Mapper.class);
            TimeModel2 timeModel = new TimeModel2();
            timeModel.setId(3);
            Date now = new Date();
            Timestamp now2 = new Timestamp(now.getTime());
            timeModel.setTestDate(now);
            timeModel.setTestTime(now);
            timeModel.setTestDatetime(now2);
            Assert.assertEquals(1, mapper.insert(timeModel));

            timeModel = mapper.selectByPrimaryKey(3);

            //保存后数据库中不存在时间部分
            Assert.assertEquals(toDate(now), toDate(timeModel.getTestDate()));
            Assert.assertEquals(toDate(now) + " 00:00:00", toDatetime(timeModel.getTestDate()));

            //日期和时间都有
            Assert.assertEquals(toTime(now), toTime(timeModel.getTestTime()));
            Assert.assertEquals(toDatetime(now), toDatetime(timeModel.getTestTime()));

            Assert.assertEquals(toDatetime(now), toDatetime(timeModel.getTestDatetime()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelect3() {
        SqlSession sqlSession = getSqlSession();
        try {
            TimeModel3Mapper mapper = sqlSession.getMapper(TimeModel3Mapper.class);
            List<TimeModel3> list = mapper.selectAll();
            Assert.assertEquals(2, list.size());

            Assert.assertEquals("2018-01-01", toDate(list.get(0).getTestDate()));
            Assert.assertEquals("12:11:00", toTime(list.get(0).getTestTime()));
            Assert.assertEquals("2018-01-01 12:00:00", toDatetime(list.get(0).getTestDatetime()));

            Assert.assertEquals("2018-11-11", toDate(list.get(1).getTestDate()));
            Assert.assertEquals("01:59:11", toTime(list.get(1).getTestTime()));
            Assert.assertEquals("2018-02-12 17:58:12", toDatetime(list.get(1).getTestDatetime()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsert3() {
        SqlSession sqlSession = getSqlSession();
        try {
            TimeModel3Mapper mapper = sqlSession.getMapper(TimeModel3Mapper.class);
            TimeModel3 timeModel = new TimeModel3();
            timeModel.setId(3);
            Date now = new Date();
            timeModel.setTestDate(now);
            timeModel.setTestTime(now);
            timeModel.setTestDatetime(now);
            /*
                insert 日志能明显看到制定 jdbcType 后的区别

                DEBUG [main] - ==>  Preparing: INSERT INTO test_timestamp ( id,test_date,test_time,test_datetime ) VALUES( ?,?,?,? )
                DEBUG [main] - ==> Parameters: 3(Integer), 2018-02-25(Date), 11:50:18(Time), 2018-02-25 11:50:18.263(Timestamp)
             */
            Assert.assertEquals(1, mapper.insert(timeModel));

            timeModel = mapper.selectByPrimaryKey(3);

            //保存后数据库中不存在时间部分
            Assert.assertEquals(toDate(now), toDate(timeModel.getTestDate()));
            Assert.assertEquals(toDate(now) + " 00:00:00", toDatetime(timeModel.getTestDate()));

            //时间
            Assert.assertEquals(toTime(now), toTime(timeModel.getTestTime()));
            //由于插入数据库时指定的 jdbcType=TIME，所以下面是没有日期部分的
            Assert.assertEquals("1970-01-01 " + toTime(now), toDatetime(timeModel.getTestTime()));

            Assert.assertEquals(toDatetime(now), toDatetime(timeModel.getTestDatetime()));
        } finally {
            sqlSession.close();
        }
    }

}
