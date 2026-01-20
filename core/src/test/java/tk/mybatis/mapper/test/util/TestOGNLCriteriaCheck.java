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

package tk.mybatis.mapper.test.util;

import org.junit.Test;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.OGNL;

import jakarta.persistence.Table;

/**
 * Test OGNL.exampleHasAtLeastOneCriteriaCheck method
 * @author liuzh
 */
public class TestOGNLCriteriaCheck {

    // Simple test entity class with table annotation
    @Table(name = "test_entity")
    static class TestEntity {
        private Integer id;
        private String name;
        
        public Integer getId() {
            return id;
        }
        
        public void setId(Integer id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * Test with null example - should throw MapperException
     */
    @Test(expected = MapperException.class)
    public void testNullExample() {
        OGNL.exampleHasAtLeastOneCriteriaCheck(null);
    }

    /**
     * Test with empty criteria list - should throw MapperException
     */
    @Test(expected = MapperException.class)
    public void testEmptyCriteriaList() {
        Example example = new Example(TestEntity.class);
        OGNL.exampleHasAtLeastOneCriteriaCheck(example);
    }

    /**
     * Test with criteria that has no conditions (invalid) - should throw MapperException
     */
    @Test(expected = MapperException.class)
    public void testInvalidCriteria() {
        Example example = new Example(TestEntity.class);
        example.createCriteria(); // Create criteria but don't add any conditions
        OGNL.exampleHasAtLeastOneCriteriaCheck(example);
    }

    /**
     * Test with criteria that has null value (invalid) - should throw MapperException  
     */
    @Test(expected = MapperException.class)
    public void testCriteriaWithNullValue() {
        Example example = new Example(TestEntity.class);
        // When notNull=false (default), null values are ignored and no criteria is added
        example.createCriteria().andEqualTo("name", null);
        OGNL.exampleHasAtLeastOneCriteriaCheck(example);
    }

    /**
     * Test with multiple criteria where all are invalid - should throw MapperException
     */
    @Test(expected = MapperException.class)
    public void testMultipleInvalidCriteria() {
        Example example = new Example(TestEntity.class);
        example.createCriteria().andEqualTo("name", null);
        example.or().andEqualTo("name", null);
        OGNL.exampleHasAtLeastOneCriteriaCheck(example);
    }
}
