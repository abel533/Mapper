package tk.mybatis.mapper.additional.aggregation;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * @author liuchan
 */
public class AggregationProvider extends MapperTemplate {

	public AggregationProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	/**
	 * 根据Example查询总数
	 *
	 * @param ms
	 * @return
	 */
	public String selectAggregationByExample(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		if (isCheckExampleEntityClass()) {
			sql.append(SqlHelper.exampleCheck(entityClass));
		}
		sql.append(aggregationSelectClause());
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		sql.append(SqlHelper.updateByExampleWhereClause());
		sql.append(aggregationGroupBy());
		sql.append(SqlHelper.exampleForUpdate());
		return sql.toString();
	}

	public static String aggregationSelectClause() {
		return "SELECT " +
						"<if test=\"aggregateCondition != null\"> ${aggregateCondition.aggregateColumn} "
						  + "<if test=\"aggregateCondition.groupByColumns != null\"> ,${aggregateCondition.groupByColumns} </if>"
						+ "</if>";
	}

	public static String aggregationGroupBy() {
		return "<if test=\"aggregateCondition != null and aggregateCondition.groupByClause != null\"> group by ${aggregateCondition.groupByClause} </if>";
	}

}
