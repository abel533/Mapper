package tk.mybatis.mapper.additional.aggregation;

import java.util.Arrays;
import java.util.Map;

import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.StringUtil;

/**
 * 聚合查询条件
 * 
 * @author liuchan
 *
 */
public class AggregateCondition {

  protected Class<?> entityClass;

  protected EntityTable table;
  // 属性和列对应
  protected Map<String, EntityColumn> propertyMap;

  // 聚合属性
  private String aggregateProperty;
  // 聚合列
  private String aggregateColumn;
  // groupBy 查询列
  private String groupByColumns;
  // groupBy 条件
  private String groupByClause;
  // 聚合函数
  private AggregateType aggregateType;

  /**
   * 默认查询count计数，不分组
   * 
   * @param entityClass
   * @param aggregateProperty
   *          聚合查询属性，不能为空；为保证返回结果key与传入值相同 方法不会去除前后空格
   */
  public AggregateCondition(Class<?> entityClass, String aggregateProperty) {
    this(entityClass, aggregateProperty, AggregateType.COUNT, null);
  }

  /**
   * 默认查询count计数
   * 
   * @param entityClass
   * @param aggregateProperty 聚合查询属性，不能为空；为保证返回结果key与传入值相同 方法不会去除前后空格
   * @param groupByProperties 为保证返回结果key与传入值相同 方法不会去除每一项前后空格
   */
  public AggregateCondition(Class<?> entityClass, String aggregateProperty, String[] groupByProperties) {
    this(entityClass, aggregateProperty, AggregateType.COUNT, groupByProperties);
  }
  
  /**
   * 按指定聚合方法查询，不分组
   * @param entityClass
   * @param aggregateProperty
   * @param aggregateType
   */
  public AggregateCondition(Class<?> entityClass, String aggregateProperty,AggregateType aggregateType) {
    this(entityClass, aggregateProperty, aggregateType, null);
  }
  
  /**
   * 
   * @param entityClass
   * @param aggregateProperty  不能为空，为保证返回结果key与传入值相同 方法不会去除前后空格
   * @param aggregateType
   * @param groupByProperties 为保证返回结果key与传入值相同 方法不会去除每一项前后空格
   */
  public AggregateCondition(Class<?> entityClass, String aggregateProperty, AggregateType aggregateType,
      String[] groupByProperties) {
    this.entityClass = entityClass;
    this.table = EntityHelper.getEntityTable(entityClass);
    this.propertyMap = table.getPropertyMap();

    // 需要放在propertyMap初始化完成后执行
    setAggregateType(aggregateType);
    setAggregateProperty(aggregateProperty);
    groupBy(groupByProperties);
  }

  /**
   * 设置分组条件， 会覆盖之前的值
   * 
   * @param groupByProperties
   *          为空不分组
   * @return
   */
  public AggregateCondition groupBy(String[] groupByProperties) {
    if (groupByProperties == null || groupByProperties.length == 0) {
      this.groupByColumns = null;
      this.groupByClause = null;
    } else {
      StringBuilder groupByColumnsBuilder = new StringBuilder();
      StringBuilder groupByClauseBuilder = new StringBuilder();
      for (String property : groupByProperties) {
        if (StringUtil.isEmpty(property) || StringUtil.isEmpty(property.trim())) {
          throw new MapperException("groupByProperties不能包含空属性！");
        }
        property = property.trim();
        if (!propertyMap.containsKey(property)) {
          throw new MapperException("当前实体类不包含名为" + property + "的属性!");
        }
        String column = propertyMap.get(property).getColumn();
        if (column == null) {
          continue;
        }

        if (groupByClauseBuilder.length() > 0) {
          groupByColumnsBuilder.append(",");
          groupByClauseBuilder.append(",");
        }
        groupByColumnsBuilder.append(column).append(" AS '").append(property).append("'");
        groupByClauseBuilder.append(column);
      }
      if (groupByClauseBuilder.length() == 0) {
        throw new MapperException("传入属性groupByProperties:" + Arrays.toString(groupByProperties) + " 不存在有效列！");
      }
      this.groupByColumns = groupByColumnsBuilder.toString();
      this.groupByClause = groupByClauseBuilder.toString();
    }

    return this;
  }

  public String getAggregateProperty() {
    return aggregateProperty;
  }

  /**
   * 修改聚合查询属性
   * 
   * @param aggregateProperty
   *          不能为空，为保证返回结果key与传入值相同 方法不会去除前后空格
   * @return
   */
  public AggregateCondition setAggregateProperty(String aggregateProperty) {
    if (StringUtil.isEmpty(aggregateProperty)) {
      throw new MapperException("aggregateProperty不能为空");
    }
    if (!propertyMap.containsKey(aggregateProperty)) {
      throw new MapperException("当前实体类不包含名为" + aggregateProperty + "的属性!");
    }

    if (this.aggregateType != null && !aggregateProperty.equals(this.aggregateProperty)) {
      // 暂时不考虑column是否会为null
      String column = propertyMap.get(aggregateProperty).getColumn();
      this.aggregateColumn = aggregateType.name() + "( " + column + ") AS '" + aggregateProperty + "'";
    }
    this.aggregateProperty = aggregateProperty;

    return this;
  }

  public AggregateType getAggregateType() {
    return aggregateType;
  }

  /**
   * 修改聚合函数
   * 
   * @param aggregateType
   *          不能为空
   * @return
   */
  public AggregateCondition setAggregateType(AggregateType aggregateType) {
    if (aggregateType == null) {
      throw new MapperException("aggregateType不能为空");
    }
    if (this.aggregateProperty != null && !aggregateType.equals(this.aggregateType)) {
      // 暂时不考虑column是否会为null
      String column = propertyMap.get(this.aggregateProperty).getColumn();
      this.aggregateColumn = aggregateType.name() + "( " + column + ") AS '" + this.aggregateProperty + "'";
    }
    this.aggregateType = aggregateType;

    return this;
  }

  public String getAggregateColumn() {
    return aggregateColumn;
  }

  public String getGroupByColumns() {
    return groupByColumns;
  }

  public String getGroupByClause() {
    return groupByClause;
  }
  
  
  

}
