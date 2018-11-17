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

package tk.mybatis.mapper.additional.dialact.oracle;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.KeySql;

import java.util.List;

/**
 * <p>Oracle批量插入
 * <p>支持@{@link KeySql#genId()}，不支持@{@link KeySql#sql()}
 * <p>因INSERT ALL语法不支持序列，可手工获取序列并设置至Entity或绑定触发器
 * @author qrqhuangcy
 * @date 2018-11-16
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface InsertListMapper<T> {

    /**
     * <p>生成如下批量SQL:
     * <p>INSERT ALL
     * <p>INTO demo_country ( country_id,country_name,country_code ) VALUES ( ?,?,? )
     * <p>INTO demo_country ( country_id,country_name,country_code ) VALUES ( ?,?,? )
     * <p>INTO demo_country ( country_id,country_name,country_code ) VALUES ( ?,?,? )
     * <p>SELECT 1 FROM DUAL
     *
     * @param recordList
     * @return
     */
    @InsertProvider(type = OracleProvider.class, method = "dynamicSQL")
    int insertList(List<? extends T> recordList);

}