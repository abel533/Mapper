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

package tk.mybatis.mapper.helper;

import org.junit.Test;
import tk.mybatis.mapper.entity.EntityField;
import tk.mybatis.mapper.mapperhelper.FieldHelper;
import tk.mybatis.mapper.model.Country;

import javax.persistence.Id;
import java.beans.IntrospectionException;
import java.util.List;

/**
 * @author liuzh_3nofxnp
 * @since 2015-12-06 18:47
 */
public class FieldHelperTest {

    @Test
    public void test1() throws IntrospectionException {
        List<EntityField> fields = FieldHelper.getFields(Country.class);
        for (EntityField field : fields) {
            System.out.println(field.getName() + "  -  @Id:" + field.isAnnotationPresent(Id.class) + "  -  javaType:" + field.getJavaType());
        }
        System.out.println("======================================");

        fields = FieldHelper.getAll(Country.class);
        for (EntityField field : fields) {
            System.out.println(field.getName() + "  -  @Id:" + field.isAnnotationPresent(Id.class) + "  -  javaType:" + field.getJavaType());
        }
        System.out.println("======================================");
    }

    @Test
    public void test2() throws IntrospectionException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                FieldHelper.getFields(Country.class);
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                FieldHelper.getFields(Country.class);
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                FieldHelper.getFields(Country.class);
            }
        });
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                FieldHelper.getFields(Country.class);
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
