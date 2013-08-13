package com.arondor.common.reflection.parser.spring;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedList;

import com.arondor.common.reflection.api.hash.HashHelper;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;

abstract class BeanPropertyParser
{
    private static final Logger LOGGER = Logger.getLogger(BeanPropertyParser.class);

    private final HashHelper hashHelper;

    private final ObjectConfigurationFactory objectConfigurationFactory;

    public BeanPropertyParser(HashHelper hashHelper, ObjectConfigurationFactory objectConfigurationFactory)
    {
        this.hashHelper = hashHelper;
        this.objectConfigurationFactory = objectConfigurationFactory;
    }

    public ElementConfiguration parseProperty(Object value)
    {
        LOGGER.debug("value : " + value);
        if (value instanceof TypedStringValue)
        {
            TypedStringValue stringValue = (TypedStringValue) value;
            if (stringValue.getTargetTypeName() != null)
            {
                ObjectConfiguration enumObjectConfiguration = getEnumObjectConfiguration(stringValue);
                return enumObjectConfiguration;
            }
            else
            {
                PrimitiveConfiguration primitiveConfiguration = objectConfigurationFactory
                        .createPrimitiveConfiguration();
                primitiveConfiguration.setValue(stringValue.getValue());
                return primitiveConfiguration;
            }
        }
        else if (value instanceof RuntimeBeanReference)
        {
            RuntimeBeanReference beanReference = (RuntimeBeanReference) value;
            ReferenceConfiguration referenceConfiguration = objectConfigurationFactory.createReferenceConfiguration();
            referenceConfiguration.setReferenceName(beanReference.getBeanName());
            return referenceConfiguration;
        }
        else if (value instanceof ManagedList<?>)
        {
            return parseValueList((ManagedList<?>) value);
        }
        else if (value instanceof BeanDefinitionHolder)
        {
            BeanDefinitionHolder beanDefinitionHolder = (BeanDefinitionHolder) value;
            return parseBeanDefinition(beanDefinitionHolder.getBeanDefinition());
        }
        else
        {
            throw new RuntimeException("The type of property value is not suppported : " + value + " (class : "
                    + value.getClass().getName() + ")");
        }
    }

    private ObjectConfiguration getEnumObjectConfiguration(TypedStringValue stringValue)
    {
        ObjectConfiguration enumObjectConfiguration = objectConfigurationFactory.createObjectConfiguration();
        enumObjectConfiguration.setClassName(hashHelper.hashClassName(stringValue.getTargetTypeName()));
        enumObjectConfiguration.setConstructorArguments(new ArrayList<ElementConfiguration>());

        PrimitiveConfiguration enumFieldConfiguration = objectConfigurationFactory
                .createPrimitiveConfiguration(stringValue.getValue());
        enumObjectConfiguration.getConstructorArguments().add(enumFieldConfiguration);
        return enumObjectConfiguration;
    }

    private ElementConfiguration parseValueList(ManagedList<?> value)
    {
        try
        {
            return parseFieldList(value);
        }
        catch (ClassCastException e)
        {
            return parseBeanList(value);
        }
    }

    private ElementConfiguration parseFieldList(ManagedList<?> value)
    {
        @SuppressWarnings("unchecked")
        ManagedList<TypedStringValue> stringValueList = (ManagedList<TypedStringValue>) value;

        ListConfiguration listConfiguration = objectConfigurationFactory.createListConfiguration();
        listConfiguration.setListConfiguration(new ArrayList<ElementConfiguration>());

        for (TypedStringValue stringValue : stringValueList)
        {
            ElementConfiguration primitiveConfiguration = objectConfigurationFactory
                    .createPrimitiveConfiguration(stringValue.getValue());
            listConfiguration.getListConfiguration().add(primitiveConfiguration);
        }
        return listConfiguration;
    }

    private ElementConfiguration parseBeanList(ManagedList<?> value)
    {
        @SuppressWarnings("unchecked")
        ManagedList<RuntimeBeanReference> beanReferences = (ManagedList<RuntimeBeanReference>) value;

        ListConfiguration listConfiguration = objectConfigurationFactory.createListConfiguration();
        listConfiguration.setListConfiguration(new ArrayList<ElementConfiguration>());

        for (RuntimeBeanReference beanReference : beanReferences)
        {
            listConfiguration.getListConfiguration().add(parseBeanDefinition(beanReference.getBeanName()));
        }
        return listConfiguration;
    }

    public abstract ObjectConfiguration parseBeanDefinition(String beanDefinitionName);

    public abstract ObjectConfiguration parseBeanDefinition(BeanDefinition beanDefinition);
}
