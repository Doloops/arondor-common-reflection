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
package com.arondor.common.reflection.noreflect.instantiator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiator;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;
import com.arondor.common.reflection.noreflect.exception.NoReflectRuntimeException;
import com.arondor.common.reflection.noreflect.model.FieldSetter;
import com.arondor.common.reflection.noreflect.model.ObjectConstructor;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorRegistrar;
import com.arondor.common.reflection.noreflect.runtime.AsynchronousObject;
import com.arondor.common.reflection.noreflect.runtime.SimpleInstantiationContext;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ReflectionInstantiatorNoReflect implements ReflectionInstantiator
{
    private static final Logger LOGGER = Logger.getLogger(ReflectionInstantiatorNoReflect.class.getName());

    public InstantiationContext createDefaultInstantiationContext()
    {
        return new SimpleInstantiationContext();
    }

    public ReflectionInstantiatorNoReflect()
    {
        if (GWT.isClient())
        {
            ReflectionInstantiatorCatalog catalog = GWT.create(ReflectionInstantiatorCatalog.class);
            ReflectionInstantiatorRegistrar registrar = GWT.create(ReflectionInstantiatorRegistrar.class);
            registrar.register(catalog);
            setReflectionInstantiatorCatalog(catalog);
        }
    }

    private ReflectionInstantiatorCatalog reflectionInstantiatorCatalog;

    public final ReflectionInstantiatorCatalog getReflectionInstantiatorCatalog()
    {
        return reflectionInstantiatorCatalog;
    }

    public final void setReflectionInstantiatorCatalog(ReflectionInstantiatorCatalog reflectionInstantiatorCatalog)
    {
        this.reflectionInstantiatorCatalog = reflectionInstantiatorCatalog;
    }

    @SuppressWarnings("unchecked")
    private final <T> T castObject(Object object, Class<T> desiredClass)
    {
        return (T) object;
    }

    public <T> T instanciateObject(ObjectConfiguration objectConfiguration, Class<T> desiredClass,
            InstantiationContext context)
    {
        if (objectConfiguration == null)
        {
            throw new IllegalArgumentException("Null objectConfiguration !");
        }
        ObjectConstructor objectConstructor = reflectionInstantiatorCatalog.getObjectConstructor(objectConfiguration
                .getClassName());
        if (objectConstructor == null)
        {
            throw new IllegalArgumentException("No class name found : " + objectConfiguration.getClassName());
        }
        List<Object> constructorArguments = instanciateObjectConstructorArguments(objectConfiguration, context);
        Object object = objectConstructor.create(constructorArguments);

        instanciateObjectFields(objectConfiguration, context, object);
        return castObject(object, desiredClass);
    }

    private void instanciateObjectFields(ObjectConfiguration objectConfiguration, InstantiationContext context,
            Object object)
    {
        if (objectConfiguration.getFields() != null)
        {
            for (Map.Entry<String, ElementConfiguration> entry : objectConfiguration.getFields().entrySet())
            {
                instanciateObjectField(object, objectConfiguration.getClassName(), entry.getKey(), entry.getValue(),
                        context);
            }
        }
    }

    private List<Object> instanciateObjectConstructorArguments(ObjectConfiguration objectConfiguration,
            InstantiationContext context)
    {
        List<Object> constructorArguments = new ArrayList<Object>();
        if (objectConfiguration.getConstructorArguments() != null
                && !objectConfiguration.getConstructorArguments().isEmpty())
        {
            for (ElementConfiguration field : objectConfiguration.getConstructorArguments())
            {
                constructorArguments.add(instanciateObjectField(field, context));
            }
        }
        return constructorArguments;
    }

    private Object instantiateSharedObjectReference(ReferenceConfiguration referenceConfiguration,
            InstantiationContext context)
    {
        Object resolvedObject = context.getSharedObject(referenceConfiguration.getReferenceName());

        if (resolvedObject != null)
        {
            return resolvedObject;
        }
        ObjectConfiguration resolvedConfiguration = context.getSharedObjectConfiguration(referenceConfiguration
                .getReferenceName());
        if (resolvedConfiguration != null)
        {
            return instanciateObjectField(resolvedConfiguration, context);
        }
        throw new IllegalArgumentException("Could not resolve reference : " + referenceConfiguration.getReferenceName());

    }

    private Object instanciateObjectField(ElementConfiguration fieldConfiguration, InstantiationContext context)
    {
        switch (fieldConfiguration.getFieldConfigurationType())
        {
        case Primitive:
        {
            return ((PrimitiveConfiguration) fieldConfiguration).getValue();
        }
        case Object:
        {
            return instanciateObject((ObjectConfiguration) fieldConfiguration, Object.class, context);
        }
        case List:
        {
            List<ElementConfiguration> list = ((ListConfiguration) fieldConfiguration).getListConfiguration();
            if (list.isEmpty())
            {
                return null;
            }

            List<Object> objectList = new ArrayList<Object>();
            for (ElementConfiguration childFieldConfiguration : list)
            {
                objectList.add(instanciateObjectField(childFieldConfiguration, context));
            }
            return objectList;
        }
        case Reference:
        {
            return instantiateSharedObjectReference((ReferenceConfiguration) fieldConfiguration, context);
        }
        case Map:
        {
            Map<Object, Object> objectMap = new HashMap<Object, Object>();
            Map<ElementConfiguration, ElementConfiguration> mapConfiguration = ((MapConfiguration) fieldConfiguration)
                    .getMapConfiguration();
            for (Entry<ElementConfiguration, ElementConfiguration> entry : mapConfiguration.entrySet())
            {
                Object key = instanciateObjectField(entry.getKey(), context);
                Object value = instanciateObjectField(entry.getValue(), context);
                objectMap.put(key, value);
            }
            return objectMap;
        }
        default:
            throw new NoReflectRuntimeException("Not implemented yet : ElementConfiguration type:"
                    + fieldConfiguration.getFieldConfigurationType());
        }

    }

    private void instanciateObjectField(final Object object, String className, String propertyName,
            ElementConfiguration fieldConfiguration, InstantiationContext context)
    {
        final FieldSetter fieldSetter = reflectionInstantiatorCatalog.getFieldSetter(className, propertyName);
        if (fieldSetter == null)
        {
            throw new NoReflectRuntimeException("No setter found : className:" + className + ", propertyName:"
                    + propertyName);
        }
        Object value = instanciateObjectField(fieldConfiguration, context);
        if (value instanceof AsynchronousObject)
        {
            ((AsynchronousObject) value).addCallback(new AsyncCallback<Object>()
            {

                public void onSuccess(Object result)
                {
                    fieldSetter.set(object, result);
                }

                public void onFailure(Throwable caught)
                {
                    LOGGER.log(Level.SEVERE, "Could not set asynchronous object", caught);
                }
            });
        }
        else
        {
            fieldSetter.set(object, value);
        }
    }

    public Object instanciatePrimite(String value, Class<?> targetClass)
    {
        throw new NoReflectRuntimeException("Not implemented : instanciatePrimite()");
    }

    public <T> T instanciateObject(String beanName, Class<T> desiredClass, InstantiationContext context)
    {
        {
            Object sharedObject = context.getSharedObject(beanName);
            if (sharedObject != null)
            {
                return castObject(sharedObject, desiredClass);
            }
        }
        ObjectConfiguration objectConfiguration = context.getSharedObjectConfiguration(beanName);
        if (objectConfiguration == null)
        {
            throw new NoReflectRuntimeException("No configuration for beanName=" + beanName + ", desiredClass="
                    + desiredClass.getName());
        }
        if (objectConfiguration.isSingleton())
        {
            Object singleton = context.getSharedObject(beanName);
            if (singleton != null)
            {
                return castObject(singleton, desiredClass);
            }
        }
        T result = instanciateObject(objectConfiguration, desiredClass, context);
        if (objectConfiguration.isSingleton())
        {
            context.putSharedObject(beanName, result);
        }
        return result;
    }
}
