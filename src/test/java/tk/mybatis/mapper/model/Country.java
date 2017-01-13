/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
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

package tk.mybatis.mapper.model;

import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.entity.IDynamicTableName;
import tk.mybatis.mapper.typehandler.StringType2Handler;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * Description: Country
 * Author: liuzh
 * Update: liuzh(2014-06-06 13:38)
 */
public class Country extends Entity<Integer, String> implements Serializable, IDynamicTableName {
    private static final long serialVersionUID = -1626761012846137805L;

    @Column
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = StringType2Handler.class)
    private String countryname;
    private String countrycode;

    List<Country> list;

    @Transient
    private String dynamicTableName123;

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public List<Country> getList() {
        return list;
    }

    public void setList(List<Country> list) {
        this.list = list;
    }

    @Override
    @Transient
    public String getDynamicTableName() {
        return dynamicTableName123;
    }

    public void setDynamicTableName123(String dynamicTableName) {
        this.dynamicTableName123 = dynamicTableName;
    }
}
