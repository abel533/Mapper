package com.quancheng.mybatis.mapper.generator;

import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3SimpleImpl;

import java.text.MessageFormat;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 可以通过MBG1.3.4+版本提供的table元素的mapperName属性设置统一的名称，使用{0}作为实体类名的占位符。
 * <p>
 * 用法：
 * <pre>
 * &lt;context id="Mysql" targetRuntime="QCMyBatis3SimpleImpl" defaultModelType="flat"&gt;
 * &lt;/context&gt;
 * </pre>
 * </p>
 *
 * @author liuzh
 * @since 2016-09-04 09:57
 */
public class QCMyBatis3SimpleImpl extends IntrospectedTableMyBatis3SimpleImpl {

    @Override
    protected String calculateMyBatis3XmlMapperFileName() {
        StringBuilder sb = new StringBuilder();
        if (stringHasValue(tableConfiguration.getMapperName())) {
            String mapperName = tableConfiguration.getMapperName();
            int ind = mapperName.lastIndexOf('.');
            if (ind != -1) {
                mapperName = mapperName.substring(ind + 1);
            }
            //支持mapperName = "{0}Dao" 等用法
            sb.append(MessageFormat.format(mapperName, fullyQualifiedTable.getDomainObjectName()));
            sb.append(".xml"); //$NON-NLS-1$
        } else {
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper.xml"); //$NON-NLS-1$
        }
        return sb.toString();
    }

    @Override
    protected void calculateJavaClientAttributes() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(calculateJavaClientImplementationPackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("DAOImpl"); //$NON-NLS-1$
        setDAOImplementationType(sb.toString());

        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("DAO"); //$NON-NLS-1$
        setDAOInterfaceType(sb.toString());

        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        if (stringHasValue(tableConfiguration.getMapperName())) {
            //支持mapperName = "{0}Dao" 等用法
            sb.append(MessageFormat.format(tableConfiguration.getMapperName(), fullyQualifiedTable.getDomainObjectName()));
        } else {
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper"); //$NON-NLS-1$
        }
        setMyBatis3JavaMapperType(sb.toString());

        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        if (stringHasValue(tableConfiguration.getSqlProviderName())) {
            //支持mapperName = "{0}SqlProvider" 等用法
            sb.append(MessageFormat.format(tableConfiguration.getSqlProviderName(), fullyQualifiedTable.getDomainObjectName()));
        } else {
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("SqlProvider"); //$NON-NLS-1$
        }
        setMyBatis3SqlProviderType(sb.toString());
    }

    final static String DOMAIN_OBJECT_NAME_POSTFIX = "DO";

        /**
     * Calculate model attributes.
     */
    protected void calculateModelAttributes() {
        super.calculateModelAttributes();

        String domainObjectName = fullyQualifiedTable.getDomainObjectName() + DOMAIN_OBJECT_NAME_POSTFIX;

        String pakkage = calculateJavaModelPackage();

        StringBuilder sb = new StringBuilder();
        sb.append(pakkage);
        sb.append('.');
        sb.append(domainObjectName);
        sb.append("Key"); //$NON-NLS-1$
        setPrimaryKeyType(sb.toString());

        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(domainObjectName);
        setBaseRecordType(sb.toString());

        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(domainObjectName);
        sb.append("WithBLOBs"); //$NON-NLS-1$
        setRecordWithBLOBsType(sb.toString());

        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(domainObjectName);
        sb.append("Example"); //$NON-NLS-1$
        setExampleType(sb.toString());
    }

}
