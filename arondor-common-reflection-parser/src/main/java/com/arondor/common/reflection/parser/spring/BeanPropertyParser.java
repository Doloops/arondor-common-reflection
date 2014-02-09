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
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;

import com.arondor.common.reflection.api.hash.HashHelper;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
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
                return getEnumObjectConfiguration(stringValue);
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
        else if (value instanceof ManagedMap<?, ?>)
        {
            return parseValueMap((ManagedMap<?, ?>) value);
        }
        else if (value instanceof BeanDefinitionHolder)
        {
            BeanDefinitionHolder beanDefinitionHolder = (BeanDefinitionHolder) value;
            return parseBeanDefinition(beanDefinitionHolder.getBeanDefinition());
        }
        else
        {
            throw new UnsupportedOperationException("The type of property value is not suppported : " + value
                    + " (class : " + value.getClass().getName() + ")");
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
        return parseBeanList(value);
        // try
        // {
        // return parseFieldList(value);
        // }
        // catch (ClassCastException e)
        // {
        //
        // }
    }

    private ElementConfiguration parseValueMap(ManagedMap<?, ?> value)
    {
        MapConfiguration mapConfiguration = objectConfigurationFactory.createMapConfiguration();
        mapConfiguration.setMapConfiguration(new HashMap<ElementConfiguration, ElementConfiguration>());

        for (Entry<?, ?> entry : value.entrySet())
        {
            LOGGER.debug("entry key=" + entry.getKey() + ", value=" + entry.getValue());
            ElementConfiguration keyConfiguration = parseBeanObject(entry.getKey());
            ElementConfiguration valueConfiguration = parseBeanObject(entry.getValue());
            mapConfiguration.getMapConfiguration().put(keyConfiguration, valueConfiguration);
        }

        return mapConfiguration;
    }

    // private ElementConfiguration parseFieldList(ManagedList<?> value)
    // {
    // @SuppressWarnings("unchecked")
    // ManagedList<TypedStringValue> stringValueList =
    // (ManagedList<TypedStringValue>) value;
    //
    // ListConfiguration listConfiguration =
    // objectConfigurationFactory.createListConfiguration();
    // listConfiguration.setListConfiguration(new
    // ArrayList<ElementConfiguration>());
    //
    // for (TypedStringValue stringValue : stringValueList)
    // {
    // ElementConfiguration primitiveConfiguration = objectConfigurationFactory
    // .createPrimitiveConfiguration(stringValue.getValue());
    // listConfiguration.getListConfiguration().add(primitiveConfiguration);
    // }
    // return listConfiguration;
    // }

    private ElementConfiguration parseBeanObject(Object item)
    {
        if (item instanceof TypedStringValue)
        {
            TypedStringValue stringValue = (TypedStringValue) item;
            ElementConfiguration primitiveConfiguration = objectConfigurationFactory
                    .createPrimitiveConfiguration(stringValue.getValue());
            return primitiveConfiguration;
        }
        else if (item instanceof RuntimeBeanReference)
        {
            RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) item;
            return parseBeanDefinition(runtimeBeanReference.getBeanName());
        }
        else if (item instanceof BeanDefinitionHolder)
        {
            BeanDefinitionHolder beanDefinitionHolder = (BeanDefinitionHolder) item;
            return parseBeanDefinition(beanDefinitionHolder.getBeanDefinition());
        }
        else
        {
            throw new IllegalArgumentException("Not supported : item class " + item.getClass().getName());
        }

    }

    private ElementConfiguration parseBeanList(ManagedList<?> managedList)
    {
        ListConfiguration listConfiguration = objectConfigurationFactory.createListConfiguration();
        listConfiguration.setListConfiguration(new ArrayList<ElementConfiguration>());

        for (Object item : managedList)
        {
            ElementConfiguration elementConfiguration = parseBeanObject(item);
            listConfiguration.getListConfiguration().add(elementConfiguration);
        }
        return listConfiguration;
    }

    public abstract ObjectConfiguration parseBeanDefinition(String beanDefinitionName);

    public abstract ObjectConfiguration parseBeanDefinition(BeanDefinition beanDefinition);
}
