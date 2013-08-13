package com.arondor.common.reflection.parser.spring;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedList;

import com.arondor.common.reflection.api.hash.HashHelper;
import com.arondor.common.reflection.model.config.FieldConfiguration;
import com.arondor.common.reflection.model.config.FieldConfiguration.FieldConfigurationType;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;

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

    public FieldConfiguration parseProperty(Object value)
    {
        FieldConfiguration field = objectConfigurationFactory.createFieldConfiguration();
        LOGGER.debug("value : " + value);
        if (value instanceof TypedStringValue)
        {
            TypedStringValue stringValue = (TypedStringValue) value;
            if (stringValue.getTargetTypeName() != null)
            {
                field.setFieldConfigurationType(FieldConfigurationType.Object_Single);
                ObjectConfiguration enumObjectConfiguration = getEnumObjectConfiguration(stringValue);
                field.setObjectConfiguration(enumObjectConfiguration);
            }

            else
            {
                field.setFieldConfigurationType(FieldConfigurationType.Primitive_Single);
                field.setValue(stringValue.getValue());
            }
        }
        else if (value instanceof RuntimeBeanReference)
        {
            field.setFieldConfigurationType(FieldConfigurationType.Object_Single);
            RuntimeBeanReference beanReference = (RuntimeBeanReference) value;
            field.setObjectConfiguration(objectConfigurationFactory.createObjectConfiguration());
            field.getObjectConfiguration().setReferenceName(beanReference.getBeanName());
        }
        else if (value instanceof ManagedList<?>)
        {
            parseValueList(field, (ManagedList<?>) value);
        }
        else if (value instanceof BeanDefinitionHolder)
        {
            field.setFieldConfigurationType(FieldConfigurationType.Object_Single);
            BeanDefinitionHolder beanDefinitionHolder = (BeanDefinitionHolder) value;
            field.setObjectConfiguration(parseBeanDefinition(beanDefinitionHolder.getBeanDefinition()));
        }
        else
        {
            throw new RuntimeException("The type of property value is not suppported : " + value + " (class : "
                    + value.getClass().getName() + ")");
        }
        return field;
    }

    private ObjectConfiguration getEnumObjectConfiguration(TypedStringValue stringValue)
    {
        ObjectConfiguration enumObjectConfiguration = objectConfigurationFactory.createObjectConfiguration();
        enumObjectConfiguration.setClassName(hashHelper.hashClassName(stringValue.getTargetTypeName()));
        enumObjectConfiguration.setConstructorArguments(new ArrayList<FieldConfiguration>());

        FieldConfiguration enumFieldConfiguration = objectConfigurationFactory.createFieldConfiguration();
        enumFieldConfiguration.setFieldConfigurationType(FieldConfigurationType.Primitive_Single);
        enumFieldConfiguration.setValue(stringValue.getValue());
        enumObjectConfiguration.getConstructorArguments().add(enumFieldConfiguration);
        return enumObjectConfiguration;
    }

    private void parseValueList(FieldConfiguration field, ManagedList<?> value)
    {
        try
        {
            parseFieldList(value, field);
        }
        catch (ClassCastException e)
        {
            parseBeanList(value, field);
        }
    }

    private void parseFieldList(ManagedList<?> value, FieldConfiguration field)
    {
        @SuppressWarnings("unchecked")
        ManagedList<TypedStringValue> stringValueList = (ManagedList<TypedStringValue>) value;
        field.setFieldConfigurationType(FieldConfigurationType.Primitive_Multiple);
        field.setValues(new ArrayList<String>());
        for (TypedStringValue stringValue : stringValueList)
        {
            field.getValues().add(stringValue.getValue());
        }
    }

    private void parseBeanList(ManagedList<?> value, FieldConfiguration field)
    {
        @SuppressWarnings("unchecked")
        ManagedList<RuntimeBeanReference> beanReferences = (ManagedList<RuntimeBeanReference>) value;
        field.setFieldConfigurationType(FieldConfigurationType.Object_Multiple);
        List<FieldConfiguration> objectConfigurations = new ArrayList<FieldConfiguration>();
        field.setObjectConfigurations(objectConfigurations);
        for (RuntimeBeanReference beanReference : beanReferences)
        {
            FieldConfiguration subField = objectConfigurationFactory.createFieldConfiguration();
            subField.setFieldConfigurationType(FieldConfigurationType.Object_Single);
            subField.setObjectConfiguration(parseBeanDefinition(beanReference.getBeanName()));
            field.getObjectConfigurations().add(subField);
        }
    }

    public abstract ObjectConfiguration parseBeanDefinition(String beanDefinitionName);

    public abstract ObjectConfiguration parseBeanDefinition(BeanDefinition beanDefinition);
}
