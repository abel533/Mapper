package tk.mybatis.mapper.autoconfigure;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

/**
 * 初始化完成后，清空类信息的缓存
 *
 * @author liuzh
 */
public class MapperCacheDisabler implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(MapperCacheDisabler.class);

    @Override
    public void afterPropertiesSet() {
        disableCaching();
    }

    private void disableCaching() {
        try {
            //因为jar包的类都是 AppClassLoader 加载的，所以此处获取的就是 AppClassLoader
            ClassLoader appClassLoader = getClass().getClassLoader();
            removeStaticCache(ClassUtils.forName("tk.mybatis.mapper.util.MsUtil", appClassLoader), "CLASS_CACHE");
            removeStaticCache(ClassUtils.forName("tk.mybatis.mapper.genid.GenIdUtil", appClassLoader));
            removeStaticCache(ClassUtils.forName("tk.mybatis.mapper.version.VersionUtil", appClassLoader));

            removeEntityHelperCache(ClassUtils.forName("tk.mybatis.mapper.mapperhelper.EntityHelper", appClassLoader));
        } catch (Exception ex) {
        }
    }


    private void removeStaticCache(Class<?> utilClass) {
        removeStaticCache(utilClass, "CACHE");
    }

    private void removeStaticCache(Class<?> utilClass, String fieldName) {
        try {
            Field cacheField = ReflectionUtils.findField(utilClass, fieldName);
            if (cacheField != null) {
                ReflectionUtils.makeAccessible(cacheField);
                Object cache = ReflectionUtils.getField(cacheField, null);
                if (cache instanceof Map) {
                    ((Map) cache).clear();
                } else if (cache instanceof Cache) {
                    ((Cache) cache).clear();
                } else {
                    throw new UnsupportedOperationException("cache field must be a java.util.Map " +
                            "or org.apache.ibatis.cache.Cache instance");
                }
                logger.info("Clear " + utilClass.getCanonicalName() + " " + fieldName + " cache.");
            }
        } catch (Exception ex) {
            logger.warn("Failed to disable " + utilClass.getCanonicalName() + " "
                    + fieldName + " cache. ClassCastExceptions may occur", ex);
        }
    }

    private void removeEntityHelperCache(Class<?> entityHelper) {
        try {
            Field cacheField = ReflectionUtils.findField(entityHelper, "entityTableMap");
            if (cacheField != null) {
                ReflectionUtils.makeAccessible(cacheField);
                Map cache = (Map) ReflectionUtils.getField(cacheField, null);
                //如果使用了 Devtools，这里获取的就是当前的 RestartClassLoader
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                for (Object key : new ArrayList(cache.keySet())) {
                    Class entityClass = (Class) key;
                    //清理老的ClassLoader缓存的数据，避免测试环境溢出
                    if (!entityClass.getClassLoader().equals(classLoader)) {
                        cache.remove(entityClass);
                    }
                }
                logger.info("Clear EntityHelper entityTableMap cache.");
            }
        } catch (Exception ex) {
            logger.warn("Failed to disable Mapper MsUtil cache. ClassCastExceptions may occur", ex);
        }
    }

}