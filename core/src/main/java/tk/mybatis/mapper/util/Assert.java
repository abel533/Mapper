package tk.mybatis.mapper.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author liuzh
 */
public abstract class Assert {

    public static void isTrue(boolean expression, String errorMsg) throws IllegalArgumentException {
        if (!expression) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static void isNull(Object object, String errorMsg) throws IllegalArgumentException {
        if (object != null) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static <T> T notNull(T object, String errorMsg) throws NullPointerException {
        if (object == null) {
            throw new NullPointerException(errorMsg);
        }
        return object;
    }

    public static String notEmpty(String text, String errorMsg) throws IllegalArgumentException {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException(errorMsg);
        }
        return text;
    }

    public static Object[] notEmpty(Object[] array, String errorMsg) throws IllegalArgumentException {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(errorMsg);
        }
        return array;
    }

    public static <T> Collection<T> notEmpty(Collection<T> collection, String errorMsg) throws IllegalArgumentException {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }
        return collection;
    }

    public static <K, V> Map<K, V> notEmpty(Map<K, V> map, String errorMsg) throws IllegalArgumentException {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }
        return map;
    }

}
