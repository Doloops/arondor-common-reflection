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

import com.arondor.common.reflection.api.instantiator.InstantiationCallback;
import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiator;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiatorAsync;
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

public class ReflectionInstantiatorNoReflect implements ReflectionInstantiator, ReflectionInstantiatorAsync
{
    private static final Logger LOGGER = Logger.getLogger(ReflectionInstantiatorNoReflect.class.getName());

    private static class AsyncPackages
    {
        private final List<String> packageNames = new ArrayList<String>();

        private final Map<String, Boolean> packagesMap = new HashMap<String, Boolean>();

        public void addPackage(String packageName)
        {
            if (!packagesMap.containsKey(packageName))
            {
                packageNames.add(packageName);
                packagesMap.put(packageName, true);
            }
        }

        public List<String> getPackages()
        {
            return packageNames;
        }
    }

    @Override
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

    @Override
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
            LOGGER.log(Level.SEVERE, "No class name found : " + objectConfiguration.getClassName());
            throw new IllegalArgumentException("No class name found : " + objectConfiguration.getClassName());
        }
        List<Object> constructorArguments = instanciateObjectConstructorArguments(objectConfiguration, context);
        Object object = objectConstructor.create(constructorArguments);

        instanciateObjectFields(objectConfiguration, context, object);

        for (ObjectInstanciationHook hook : objectInstanciationHook)
        {
            hook.onObjectInstanciated(objectConfiguration, object);
        }
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
        try
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

                    @Override
                    public void onSuccess(Object result)
                    {
                        fieldSetter.set(object, result);
                    }

                    @Override
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
        catch (IllegalArgumentException e)
        {
            LOGGER.log(Level.SEVERE, "At " + className + ":" + propertyName);
            throw (e);
        }
    }

    public Object instanciatePrimite(String value, Class<?> targetClass)
    {
        throw new NoReflectRuntimeException("Not implemented : instanciatePrimite()");
    }

    @Override
    public <T> T instanciateObject(String beanName, Class<T> desiredClass, InstantiationContext context)
    {
        T result = instanciateObjectFromShared(beanName, desiredClass, context);
        if (result != null)
        {
            return result;
        }
        ObjectConfiguration objectConfiguration = context.getSharedObjectConfiguration(beanName);
        if (objectConfiguration == null)
        {
            throw new NoReflectRuntimeException("No configuration for beanName=" + beanName + ", desiredClass="
                    + desiredClass.getName());
        }
        if (objectConfiguration.isSingleton())
            instanciateObjectFromShared(beanName, desiredClass, context);
        result = instanciateObject(objectConfiguration, desiredClass, context);
        mayPutSharedObject(beanName, objectConfiguration, context, result);
        return result;
    }

    private <T> void mayPutSharedObject(String beanName, ObjectConfiguration objectConfiguration,
            InstantiationContext context, T result)
    {
        if (objectConfiguration.isSingleton())
        {
            context.putSharedObject(beanName, result);
        }
    }

    private <T> T instanciateObjectFromShared(String beanName, Class<T> desiredClass, InstantiationContext context)
    {
        Object sharedObject = context.getSharedObject(beanName);
        if (sharedObject != null)
        {
            return (T) castObject(sharedObject, desiredClass);
        }
        return null;
    }

    private void putAsyncClass(AsyncPackages asyncPackages, String clazz)
    {
        String packageName = reflectionInstantiatorCatalog.getPackageForClass(clazz);
        if (packageName != null)
        {
            asyncPackages.addPackage(packageName);
        }
    }

    private void walkElementConfigurationForClass(final InstantiationContext context, AsyncPackages asyncClasses,
            ElementConfiguration fieldConfiguration)
    {
        switch (fieldConfiguration.getFieldConfigurationType())
        {
        case Primitive:
        {
            break;
        }
        case Object:
        {
            ObjectConfiguration objectConfiguration = (ObjectConfiguration) fieldConfiguration;
            putAsyncClass(asyncClasses, objectConfiguration.getClassName());
            if (objectConfiguration.getConstructorArguments() != null)
            {
                for (ElementConfiguration arg : objectConfiguration.getConstructorArguments())
                {
                    walkElementConfigurationForClass(context, asyncClasses, arg);
                }
            }
            if (objectConfiguration.getFields() != null)
            {
                for (ElementConfiguration field : objectConfiguration.getFields().values())
                {
                    walkElementConfigurationForClass(context, asyncClasses, field);
                }
            }
            break;
        }
        case List:
        {
            List<ElementConfiguration> list = ((ListConfiguration) fieldConfiguration).getListConfiguration();
            for (ElementConfiguration child : list)
            {
                walkElementConfigurationForClass(context, asyncClasses, child);
            }
            break;
        }
        case Reference:
        {
            ReferenceConfiguration ref = (ReferenceConfiguration) fieldConfiguration;
            if (context.getSharedObject(ref.getReferenceName()) != null)
            {
                break;
            }
            ObjectConfiguration sharedConf = context.getSharedObjectConfiguration(ref.getReferenceName());
            if (sharedConf != null)
            {
                walkElementConfigurationForClass(context, asyncClasses, sharedConf);
            }
            break;
        }
        case Map:
        {
            Map<ElementConfiguration, ElementConfiguration> mapConfiguration = ((MapConfiguration) fieldConfiguration)
                    .getMapConfiguration();
            for (Entry<ElementConfiguration, ElementConfiguration> entry : mapConfiguration.entrySet())
            {
                walkElementConfigurationForClass(context, asyncClasses, entry.getKey());
                walkElementConfigurationForClass(context, asyncClasses, entry.getValue());
            }
            break;
        }
        default:
            throw new NoReflectRuntimeException("Not implemented yet : ElementConfiguration type:"
                    + fieldConfiguration.getFieldConfigurationType());
        }

    }

    @Override
    public <T> void instanciateObject(final ObjectConfiguration objectConfiguration, final Class<T> desiredClass,
            final InstantiationContext context, final InstantiationCallback<T> callback)
    {
        String beanName = objectConfiguration.getObjectName();

        AsyncPackages asyncPackages = new AsyncPackages();

        long startWalk = System.currentTimeMillis();

        walkElementConfigurationForClass(context, asyncPackages, objectConfiguration);

        final long endWalk = System.currentTimeMillis();
        if (LOGGER.isLoggable(Level.INFO))
        {
            LOGGER.info("walkElementConfigurationForClass() took " + (endWalk - startWalk) + "ms");
        }

        if (asyncPackages.getPackages().isEmpty())
        {
            T result = instanciateObject(objectConfiguration, desiredClass, context);
            callback.onSuccess(result);
            return;
        }

        List<String> asyncPackagesList = asyncPackages.getPackages();
        LOGGER.info("Instantiating async bean " + beanName + ", asyncPackages are :" + asyncPackagesList);

        callAsyncRecursive(asyncPackagesList, 0, new AsyncCallback<Void>()
        {
            @Override
            public void onFailure(Throwable caught)
            {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(Void __void)
            {
                long endAsyncRecursive = System.currentTimeMillis();
                LOGGER.info("End of asyncRecursive=" + (endAsyncRecursive - endWalk) + "ms");
                T result = instanciateObject(objectConfiguration, desiredClass, context);
                long endInstanciateObject = System.currentTimeMillis();
                LOGGER.info("instanciateObject(name=" + objectConfiguration.getObjectName() + ", class="
                        + objectConfiguration.getClassName() + ")" + " took:"
                        + (endInstanciateObject - endAsyncRecursive) + "ms");
                callback.onSuccess(result);

            }
        });
    }

    @Override
    public <T> void instanciateObject(final String beanName, final Class<T> desiredClass,
            final InstantiationContext context, final InstantiationCallback<T> callback)
    {
        T result = instanciateObjectFromShared(beanName, desiredClass, context);
        if (result != null)
        {
            callback.onSuccess(result);
            return;
        }
        final ObjectConfiguration objectConfiguration = context.getSharedObjectConfiguration(beanName);
        instanciateObject(objectConfiguration, desiredClass, context, new InstantiationCallback<T>()
        {
            @Override
            public void onFailure(Throwable caught)
            {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(T result)
            {
                mayPutSharedObject(beanName, objectConfiguration, context, result);
                callback.onSuccess(result);
            }
        });
    }

    private void callAsyncRecursive(final List<String> asyncPackages, final int index,
            final AsyncCallback<Void> callback)
    {
        if (index == asyncPackages.size())
        {
            callback.onSuccess(null);
            return;
        }
        final String packageName = asyncPackages.get(index);
        final long startGetObjectConstructor = System.currentTimeMillis();
        LOGGER.info("Now instantiate async :" + packageName + ", index=" + index + "/" + asyncPackages.size());

        reflectionInstantiatorCatalog.getPackageInstantiator(packageName).instantiatePackage(
                new InstantiationCallback<Void>()
                {
                    @Override
                    public void onFailure(Throwable caught)
                    {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(Void result)
                    {
                        long endGetObjectConstructor = System.currentTimeMillis();
                        LOGGER.info("Instantiated async :" + packageName + ", index=" + index + "/"
                                + asyncPackages.size() + ", time="
                                + (endGetObjectConstructor - startGetObjectConstructor) + "ms");
                        callAsyncRecursive(asyncPackages, index + 1, callback);
                    }
                });
    }

    private final List<ObjectInstanciationHook> objectInstanciationHook = new ArrayList<ReflectionInstantiator.ObjectInstanciationHook>();

    @Override
    public HookHandler addObjectInstanciationHook(final ObjectInstanciationHook hook)
    {
        if (hook == null)
        {
            LOGGER.severe("Invalid null hook provided !");
            return new HookHandler()
            {
                @Override
                public void remove()
                {
                }
            };
        }
        objectInstanciationHook.add(hook);
        return new HookHandler()
        {
            @Override
            public void remove()
            {
                objectInstanciationHook.remove(hook);
            }
        };
    }
}
