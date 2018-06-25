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

package tk.mybatis.mapper.model;

import tk.mybatis.mapper.entity.IDynamicTableName;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @Description:  验证数值空值强制更新
 * @author qrqhuang
 * @date 2018-06-25
 */
public class CountryInt extends Entity<Integer, String> implements Serializable, IDynamicTableName {
    private static final long serialVersionUID = -1626761012846137805L;
    List<CountryInt> list;
    private String countryname;
    private Integer countrycode;
    @Transient
    private String dynamicTableName123;

    public Integer getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(Integer countrycode) {
        this.countrycode = countrycode;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    @Override
    @Transient
    public String getDynamicTableName() {
        return dynamicTableName123;
    }

    public List<CountryInt> getList() {
        return list;
    }

    public void setList(List<CountryInt> list) {
        this.list = list;
    }

    public void setDynamicTableName123(String dynamicTableName) {
        this.dynamicTableName123 = dynamicTableName;
    }
}
