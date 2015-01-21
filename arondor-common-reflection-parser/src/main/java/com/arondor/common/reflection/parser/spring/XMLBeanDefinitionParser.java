/*
 *  Copyright 2013, Arondor
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.arondor.common.reflection.parser.spring;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.arondor.common.reflection.api.hash.HashHelper;
import com.arondor.common.reflection.api.hash.NoHashHelper;
import com.arondor.common.reflection.api.parser.ObjectConfigurationMapParser;
import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
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
        applicationContext = new ClassPathXmlApplicationContext(xmlPath)
        {
            protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory)
            {
                LOGGER.info("Skipping default bean instantiation");
            }
        };
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
            objectConfiguration.setObjectName(beanDefinitionName);
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
        LOGGER.debug("Resource description : " + beanDefinition.getResourceDescription());
        ObjectConfiguration objectConfiguration = objectConfigurationFactory.createObjectConfiguration();
        objectConfiguration.setClassName(hashHelper.hashClassName(beanDefinition.getBeanClassName()));
        objectConfiguration.setSingleton(beanDefinition.isSingleton());

        Map<String, ElementConfiguration> fields = new LinkedHashMap<String, ElementConfiguration>();
        objectConfiguration.setFields(fields);

        for (PropertyValue ppt : beanDefinition.getPropertyValues().getPropertyValueList())
        {
            try
            {
                ElementConfiguration fieldConfiguration = beanPropertyParser.parseProperty(ppt.getValue());
                fields.put(hashHelper.hashFieldName(beanDefinition.getBeanClassName(), ppt.getName()),
                        fieldConfiguration);
            }
            catch (Exception e)
            {
                LOGGER.error("The value " + ppt.getValue() + " of property " + ppt + " cannot be parsed", e);
            }
        }

        List<ElementConfiguration> constructorArguments = new ArrayList<ElementConfiguration>();
        objectConfiguration.setConstructorArguments(constructorArguments);
        for (ValueHolder va : beanDefinition.getConstructorArgumentValues().getGenericArgumentValues())
        {
            ElementConfiguration constructorAgrument = beanPropertyParser.parseProperty(va.getValue());
            constructorArguments.add(constructorAgrument);
        }
        return objectConfiguration;
    }

    private BeanDefinitionRegistry getBeanDefinitionRegistry()
    {
        return (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
    }
}