package com.arondor.common.reflection.parser.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.arondor.common.reflection.api.hash.HashHelper;
import com.arondor.common.reflection.api.hash.NoHashHelper;
import com.arondor.common.reflection.api.parser.ObjectConfigurationMapParser;
import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.model.config.FieldConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

/**
 * Implementation of {@link BeanDefinitionParser} which allows to parse XML
 * Spring {@link ApplicationContext}
 * 
 * @see ClassPathXmlApplicationContext
 * @see BeanDefinition
 * @author Christopher Laszczuk
 * 
 */
public class XMLBeanDefinitionParser implements ObjectConfigurationMapParser
{
    private static final Logger LOGGER = Logger.getLogger(XMLBeanDefinitionParser.class);

    private final ApplicationContext applicationContext;

    private BeanPropertyParser beanPropertyParser;

    private final HashHelper hashHelper;

    private final ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

    public XMLBeanDefinitionParser(String xmlPath)
    {
        this(xmlPath, null);
    }

    public XMLBeanDefinitionParser(String xmlPath, String hashMethod)
    {
        applicationContext = new ClassPathXmlApplicationContext(xmlPath);
        this.hashHelper = new NoHashHelper();
        beanPropertyParser = new BeanPropertyParser(hashHelper, objectConfigurationFactory)
        {
            @Override
            public ObjectConfiguration parseBeanDefinition(BeanDefinition beanDefinition)
            {
                return XMLBeanDefinitionParser.this.parseBeanDefinition(beanDefinition);
            }

            @Override
            public ObjectConfiguration parseBeanDefinition(String beanDefinitionName)
            {
                return XMLBeanDefinitionParser.this.parseBeanDefinition(beanDefinitionName);
            }
        };
    }

    public ObjectConfigurationMap parse()
    {
        BeanDefinitionRegistry registry = getBeanDefinitionRegistry();
        ObjectConfigurationMap parsedObjectConfiguration = objectConfigurationFactory.createObjectConfigurationMap();
        for (String beanDefinitionName : registry.getBeanDefinitionNames())
        {
            LOGGER.debug("Bean defintion name : " + beanDefinitionName);
            ObjectConfiguration objectConfiguration = parseBeanDefinition(beanDefinitionName);
            objectConfiguration.setReferenceName(beanDefinitionName);
            parsedObjectConfiguration.put(beanDefinitionName, objectConfiguration);
        }
        return parsedObjectConfiguration;
    }

    private ObjectConfiguration parseBeanDefinition(String beanDefinitionName)
    {
        BeanDefinition beanDefinition = getBeanDefinitionRegistry().getBeanDefinition(beanDefinitionName);
        return parseBeanDefinition(beanDefinition);
    }

    private ObjectConfiguration parseBeanDefinition(BeanDefinition beanDefinition)
    {
        ObjectConfiguration objectConfiguration = objectConfigurationFactory.createObjectConfiguration();
        objectConfiguration.setClassName(hashHelper.hashClassName(beanDefinition.getBeanClassName()));
        objectConfiguration.setSingleton(beanDefinition.isSingleton());

        Map<String, FieldConfiguration> fields = new HashMap<String, FieldConfiguration>();
        objectConfiguration.setFields(fields);

        for (PropertyValue ppt : beanDefinition.getPropertyValues().getPropertyValueList())
        {
            FieldConfiguration fieldConfiguration = beanPropertyParser.parseProperty(ppt.getValue());
            fields.put(hashHelper.hashFieldName(beanDefinition.getBeanClassName(), ppt.getName()), fieldConfiguration);
        }

        List<FieldConfiguration> constructorArguments = new ArrayList<FieldConfiguration>();
        objectConfiguration.setConstructorArguments(constructorArguments);
        for (ValueHolder va : beanDefinition.getConstructorArgumentValues().getGenericArgumentValues())
        {
            FieldConfiguration constructorAgrument = beanPropertyParser.parseProperty(va.getValue());
            constructorArguments.add(constructorAgrument);
        }
        return objectConfiguration;
    }

    private BeanDefinitionRegistry getBeanDefinitionRegistry()
    {
        return (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
    }
}