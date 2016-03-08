package tk.mybatis.mapper.generator;

import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

/**
 * 新增一种 targetRuntime: tk.mybatis.mapper.generator.MyBatis3UpdatedMsIdIImpl;自动生成代码时候,当希望同时使用 MBG core的 具体Example, tk.mybatis的 tk.mybatis.mapper.common.ExampleMapper 使用,请使用这个 targetRuntime,以此避免 修改statementId 重复冲突  .
 * <p/>
 * 在 generatorConfig.xml 配置.
 * targetRuntime=tk.mybatis.mapper.generator.MyBatis3UpdatedMsIdIImpl
 * tk.mybatis.mapper.generator.MapperPlugin 配置 mappers属性中包含 tk.mybatis.mapper.common.ExampleMapper .
 * <p/>
 * 相对targetRuntime=MyBatis3, 修改statementId 如下,会影响生成的XxxMapper.java中的方法以及XxxMapper.xml种的id.
 * countByExample => countByConcreteExample
 * deleteByExample => deleteByConcreteExample
 * selectByExample => selectByConcreteExample
 * updateByExample => updateByConcreteExample
 * updateByExampleSelective => updateByConcreteExampleSelective
 * <p/>
 * 当 targetRuntime=Mybatis3 时,自动生成的 XxxExample 系列的 StatementId 为:
 * countByExample
 * deleteByExample
 * selectByExample
 * updateByExample
 * updateByExampleSelective
 * <p/>
 * 当希望既有 通用Example,又有具体Example时,可以采用一下MBG配置:
 * targetRuntime=Mybatis3 ,并且使用 tk.mybatis plugin (配置mappers属性中包含tk.mybatis.mapper.common.ExampleMapper).
 * 但此时会出现一个问题, 具体 Example 和 通用 Example 的 StatementId 重复了!
 * <p/>
 * 解决办法: 新增一种 targetRuntime(tk.mybatis.mapper.generator.MyBatis3UpdatedMsIdIImpl) 代替 Mybatis3,
 * 唯一不同在于改变生成的 StatementId , 避免和 tk.mybatis 冲突.
 * <p/>
 */
public class MyBatis3UpdatedMsIdIImpl extends IntrospectedTableMyBatis3Impl {

    protected void calculateXmlAttributes() {
        super.calculateXmlAttributes();

        setCountByExampleStatementId("countByConcreteExample");
        setDeleteByExampleStatementId("deleteByConcreteExample");
        setSelectByExampleStatementId("selectByConcreteExample");
        setUpdateByExampleStatementId("updateByConcreteExample");
        setUpdateByExampleSelectiveStatementId("updateByConcreteExampleSelective");
    }

}
