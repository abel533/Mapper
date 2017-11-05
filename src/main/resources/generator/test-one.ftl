目标package: ${package}

当前时间：
<#assign dateTime = .now>
${dateTime?date}
${dateTime?time}
${dateTime?string["yyyy-MM-dd HH:mm:ss"]}

所有配置的属性信息:
<#list props?keys as key>
${key} - ${props[key]}
</#list>

实体和表的信息：
表名：${tableClass.tableName}
变量名：${tableClass.variableName}
小写名：${tableClass.lowerCaseName}
类名：${tableClass.shortClassName}
全名：${tableClass.fullClassName}
包名：${tableClass.packageName}

列的信息：
=====================================
<#if tableClass.pkFields??>
主键：
    <#list tableClass.pkFields as field>
    -------------------------------------
    列名：${field.columnName}
    列类型：${field.jdbcType}
    字段名：${field.fieldName}
    注释：${field.remarks}
    类型包名：${field.typePackage}
    类型短名：${field.shortTypeName}
    类型全名：${field.fullTypeName}
    是否主键：${field.identity?c}
    是否可空：${field.nullable?c}
    是否为BLOB列：${field.blobColumn?c}
    是否为String列：${field.stringColumn?c}
    是否为字符串列：${field.jdbcCharacterColumn?c}
    是否为日期列：${field.jdbcDateColumn?c}
    是否为时间列：${field.jdbcTimeColumn?c}
    是否为序列列：${field.sequenceColumn?c}
    列长度：${field.length?c}
    列精度：${field.scale}
    </#list>
</#if>

<#if tableClass.baseFields??>
基础列：
    <#list tableClass.baseFields as field>
    -------------------------------------
    列名：${field.columnName}
    列类型：${field.jdbcType}
    字段名：${field.fieldName}
    注释：${field.remarks}
    类型包名：${field.typePackage}
    类型短名：${field.shortTypeName}
    类型全名：${field.fullTypeName}
    是否主键：${field.identity?c}
    是否可空：${field.nullable?c}
    是否为BLOB列：${field.blobColumn?c}
    是否为String列：${field.stringColumn?c}
    是否为字符串列：${field.jdbcCharacterColumn?c}
    是否为日期列：${field.jdbcDateColumn?c}
    是否为时间列：${field.jdbcTimeColumn?c}
    是否为序列列：${field.sequenceColumn?c}
    列长度：${field.length?c}
    列精度：${field.scale}
    </#list>
</#if>

<#if tableClass.blobFields??>
Blob列：
    <#list tableClass.blobFields as field>
    -------------------------------------
    列名：${field.columnName}
    列类型：${field.jdbcType}
    字段名：${field.fieldName}
    注释：${field.remarks}
    类型包名：${field.typePackage}
    类型短名：${field.shortTypeName}
    类型全名：${field.fullTypeName}
    是否主键：${field.identity?c}
    是否可空：${field.nullable?c}
    是否为BLOB列：${field.blobColumn?c}
    是否为String列：${field.stringColumn?c}
    是否为字符串列：${field.jdbcCharacterColumn?c}
    是否为日期列：${field.jdbcDateColumn?c}
    是否为时间列：${field.jdbcTimeColumn?c}
    是否为序列列：${field.sequenceColumn?c}
    列长度：${field.length?c}
    列精度：${field.scale}
    </#list>
</#if>

=====================================
全部列：
<#if tableClass.allFields??>
列名 - 字段名
    <#list tableClass.allFields as field>
    ${field.columnName} - ${field.fieldName}
    </#list>
</#if>