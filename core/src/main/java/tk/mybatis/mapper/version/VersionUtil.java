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

package tk.mybatis.mapper.version;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuzh
 * @since 3.5.0
 */
public class VersionUtil {

    private static final Map<Class<? extends NextVersion>, NextVersion> CACHE = new ConcurrentHashMap<Class<? extends NextVersion>, NextVersion>();

    private static final ReentrantLock LOCK = new ReentrantLock();

    /**
     * 获取下一个版本
     *
     * @param nextVersionClass
     * @param current
     * @return
     * @throws VersionException
     */
    public static Object nextVersion(Class<? extends NextVersion> nextVersionClass, Object current) throws VersionException {
        try {
            NextVersion nextVersion;
            if (CACHE.containsKey(nextVersionClass)) {
                nextVersion = CACHE.get(nextVersionClass);
            } else {
                LOCK.lock();
                try {
                    if (!CACHE.containsKey(nextVersionClass)) {
                        CACHE.put(nextVersionClass, nextVersionClass.newInstance());
                    }
                    nextVersion = CACHE.get(nextVersionClass);
                } finally {
                    LOCK.unlock();
                }
            }
            return nextVersion.nextVersion(current);
        } catch (Exception e) {
            throw new VersionException("获取下一个版本号失败!", e);
        }
    }

}
