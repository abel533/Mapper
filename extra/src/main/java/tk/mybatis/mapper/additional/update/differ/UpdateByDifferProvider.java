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

package tk.mybatis.mapper.additional.update.differ;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.annotation.Version;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.version.VersionException;

import java.util.Set;

/**
 * @author liuzh
 */
public class UpdateByDifferProvider extends MapperTemplate {
    public static final String OLD   = "old";
    public static final String NEWER = "newer";

    public UpdateByDifferProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 差异更新
     *
     * @param ms
     */
    public String updateByDiffer(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append(updateSetColumnsByDiffer(entityClass));
        sql.append(wherePKColumns(entityClass, true));
        return sql.toString();
    }

    /**
     * where主键条件
     *
     * @param entityClass
     * @return
     */
    public String wherePKColumns(Class<?> entityClass, boolean useVersion) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        //获取全部列
        Set<EntityColumn> columnSet = EntityHelper.getPKColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnSet) {
            sql.append(" AND " + column.getColumnEqualsHolder(NEWER));
        }
        if (useVersion) {
            sql.append(whereVersion(entityClass));
        }
        sql.append("</where>");
        return sql.toString();
    }


    /**
     * 乐观锁字段条件
     *
     * @param entityClass
     * @return
     */
    public String whereVersion(Class<?> entityClass) {
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        boolean hasVersion = false;
        String result = "";
        for (EntityColumn column : columnSet) {
            if (column.getEntityField().isAnnotationPresent(Version.class)) {
                if (hasVersion) {
                    throw new VersionException(entityClass.getCanonicalName() + " 中包含多个带有 @Version 注解的字段，一个类中只能存在一个带有 @Version 注解的字段!");
                }
                hasVersion = true;
                result = " AND " + column.getColumnEqualsHolder(NEWER);
            }
        }
        return result;
    }

    /**
     * update set列
     *
     * @param entityClass
     * @return
     */
    public String updateSetColumnsByDiffer(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<set>");
        //获取全部列
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        //对乐观锁的支持
        EntityColumn versionColumn = null;
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnSet) {
            if (column.getEntityField().isAnnotationPresent(Version.class)) {
                if (versionColumn != null) {
                    throw new VersionException(entityClass.getCanonicalName() + " 中包含多个带有 @Version 注解的字段，一个类中只能存在一个带有 @Version 注解的字段!");
                }
                versionColumn = column;
            }
            if (!column.isId() && column.isUpdatable()) {
                if (column == versionColumn) {
                    Version version = versionColumn.getEntityField().getAnnotation(Version.class);
                    String versionClass = version.nextVersion().getCanonicalName();
                    //version = ${@tk.mybatis.mapper.version@nextVersionClass("versionClass", version)}
                    sql.append(column.getColumn())
                            .append(" = ${@tk.mybatis.mapper.version.VersionUtil@nextVersion(")
                            .append("@").append(versionClass).append("@class, ")
                            .append(column.getProperty()).append(")},");
                } else {
                    //if old.xx != newer.xx
                    sql.append(getIfNotEqual(column, column.getColumnEqualsHolder(NEWER) + ","));
                }
            } else if (column.isId() && column.isUpdatable()) {
                //set id = id,
                sql.append(column.getColumn()).append(" = ").append(column.getColumn()).append(",");
            }
        }
        sql.append("</set>");
        return sql.toString();
    }

    /**
     * 判断自动!=null的条件结构
     *
     * @param column
     * @param contents
     * @return
     */
    public String getIfNotEqual(EntityColumn column, String contents) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"").append(OLD).append(".").append(column.getProperty());
        sql.append(" != ").append(NEWER).append(".").append(column.getProperty()).append("\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }

}
