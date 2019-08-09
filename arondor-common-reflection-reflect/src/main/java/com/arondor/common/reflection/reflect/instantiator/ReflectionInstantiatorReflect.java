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
package com.arondor.common.reflection.reflect.instantiator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiator;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;
import com.arondor.common.reflection.reflect.runtime.SimpleInstantiationContext;
import com.arondor.common.reflection.util.StrongReference;

public class ReflectionInstantiatorReflect implements ReflectionInstantiator
{
    private AccessibleClassParser accessibleClassParser;

    public synchronized AccessibleClassParser getAccessibleClassParser()
    {
        if (accessibleClassParser == null)
        {
            accessibleClassParser = new JavaAccessibleClassParser();
        }
        return accessibleClassParser;
    }

    public synchronized void setAccessibleClassParser(AccessibleClassParser accessibleClassParser)
    {
        this.accessibleClassParser = accessibleClassParser;
    }

    private AccessibleClassCatalog accessibleClassCatalog;

    public synchronized AccessibleClassCatalog getAccessibleClassCatalog()
    {
        if (accessibleClassCatalog == null)
        {
            accessibleClassCatalog = new SimpleAccessibleClassCatalog();
        }
        return accessibleClassCatalog;
    }

    public synchronized void setAccessibleClassCatalog(AccessibleClassCatalog accessibleClassCatalog)
    {
        this.accessibleClassCatalog = accessibleClassCatalog;
    }

    @Override
    public InstantiationContext createDefaultInstantiationContext()
    {
        return new SimpleInstantiationContext();
    }

    public ReflectionInstantiatorReflect()
    {
        initLocalClassCache();
    }

    private final Map<String, Class<?>> localClassCache = new HashMap<String, Class<?>>();

    private void initLocalClassCache()
    {
        localClassCache.put("int", int.class);
        localClassCache.put("long", long.class);
        localClassCache.put("float", float.class);
        localClassCache.put("double", double.class);
        localClassCache.put("boolean", boolean.class);
    }

    private synchronized Class<?> resolveClass(String className) throws ClassNotFoundException
    {
        Class<?> clazz = localClassCache.get(className);
        if (clazz == null)
        {
            clazz = this.getClass().getClassLoader().loadClass(className);
            localClassCache.put(className, clazz);
        }
        return clazz;
    }

    private synchronized AccessibleClass resolveAccessibleClass(String className) throws ClassNotFoundException
    {
        AccessibleClass accessibleClass = getAccessibleClassCatalog().getAccessibleClass(className);
        if (accessibleClass == null)
        {
            Class<?> clazz = resolveClass(className);
            accessibleClass = getAccessibleClassParser().parseAccessibleClass(clazz);
            getAccessibleClassCatalog().addAccessibleClass(accessibleClass);
        }
        return accessibleClass;
    }

    private Method getSetterMethod(String className, String setterName, String setterClassName)
            throws ClassNotFoundException, NoSuchMethodException, SecurityException
    {
        Class<?> clazz = resolveClass(className);
        Class<?> setterClass = resolveClass(setterClassName);
        return clazz.getMethod(setterName, setterClass);
    }

    private <T> Constructor<T> getClassConstructor(String className, List<ElementConfiguration> constructorArguments)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException,
            SecurityException
    {
        Class<T> clazz = (Class<T>) resolveClass(className);
        if (clazz.equals(String.class))
        {
            /**
             * Special case for Primitive Class
             */
            return clazz.getConstructor(String.class);
        }
        for (Constructor<T> constructor : (Constructor<T>[]) clazz.getConstructors())
        {
            if (constructor.getParameterTypes().length == constructorArguments.size())
            {
                return constructor;
            }
        }
        throw new NoSuchMethodException("Could not find a constructor with " + constructorArguments.size()
                + " arguments for class " + className);
    }

    private Object doInstantiateObject(ObjectConfiguration objectConfiguration, InstantiationContext context)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException,
            InvocationTargetException
    {
        String className = objectConfiguration.getClassName();
        if (StrongReference.CLASSNAME.contentEquals(className))
        {
            if (objectConfiguration.getConstructorArguments().size() == 1
                    && objectConfiguration.getConstructorArguments().get(0) != null
                    && objectConfiguration.getConstructorArguments().get(0) instanceof ReferenceConfiguration)
            {
                ReferenceConfiguration referenceConfiguration = (ReferenceConfiguration) objectConfiguration
                        .getConstructorArguments().get(0);
                return instantiateElementConfiguration(referenceConfiguration, null, context);
            }
            else
            {
                throw new IllegalArgumentException(
                        "Invalid StrongReference objectConfiguration !" + objectConfiguration);
            }
        }
        Class<?> clazz = resolveClass(className);
        return doInstantiateObject(objectConfiguration, context, clazz);
    }

    @Override
    public <T> T instanciateObject(ObjectConfiguration objectConfiguration, Class<T> desiredClass,
            InstantiationContext context)
    {
        String className = objectConfiguration.getClassName();
        try
        {
            Object instantiatedObject = doInstantiateObject(objectConfiguration, context);
            if (!(desiredClass.isAssignableFrom(instantiatedObject.getClass())))
            {
                throw new InstantiationException("Could not assign " + instantiatedObject.getClass().getName() + " to "
                        + desiredClass.getName());
            }
            for (ObjectInstanciationHook hook : objectInstanciationHook)
            {
                hook.onObjectInstanciated(objectConfiguration, instantiatedObject);
            }
            return (T) instantiatedObject;
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Could resolve class " + className, e);
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException("Could resolve class " + className, e);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException("Could resolve class " + className, e);
        }
        catch (SecurityException e)
        {
            throw new RuntimeException("Could resolve class " + className, e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("Could resolve class " + className, e);
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException("Could resolve class " + className, e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException("Could resolve class " + className, e);
        }

    }

    private <T> T doInstantiateObject(ObjectConfiguration objectConfiguration, InstantiationContext context,
            Class<?> clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException
    {
        T object = null;
        if (objectConfiguration.getConstructorArguments() == null
                || objectConfiguration.getConstructorArguments().isEmpty())
        {
            object = (T) clazz.newInstance();
        }
        else if (objectConfiguration.getConstructorArguments().size() == 1 && Enum.class.isAssignableFrom(clazz))
        {
            String enumValue = (String) instantiateElementConfiguration(
                    objectConfiguration.getConstructorArguments().get(0), String.class.getName(), context);
            return (T) Enum.valueOf((Class<Enum>) clazz, enumValue);
        }
        else
        {
            Constructor<T> constructor = getClassConstructor(objectConfiguration.getClassName(),
                    objectConfiguration.getConstructorArguments());
            Object arguments[] = new Object[constructor.getParameterTypes().length];
            for (int argumentIndex = 0; argumentIndex < constructor.getParameterTypes().length; argumentIndex++)
            {
                String argumentClassName = constructor.getParameterTypes()[argumentIndex].getName();
                ElementConfiguration argumentConfiguration = objectConfiguration.getConstructorArguments()
                        .get(argumentIndex);
                arguments[argumentIndex] = instantiateElementConfiguration(argumentConfiguration, argumentClassName,
                        context);
            }
            object = constructor.newInstance(arguments);
        }
        setFields(object, objectConfiguration, context);
        if (objectConfiguration.isSingleton())
        {
            context.putSharedObject(objectConfiguration.getObjectName(), object);
        }
        return object;
    }

    private Object instantiateElementConfiguration(ElementConfiguration elementConfiguration, String elementClassName,
            InstantiationContext context) throws ClassNotFoundException
    {
        switch (elementConfiguration.getFieldConfigurationType())
        {
        case Primitive:
            Object convertedFieldValue = convertPrimitive(((PrimitiveConfiguration) elementConfiguration).getValue(),
                    elementClassName);
            return convertedFieldValue;
        case Object:
            ObjectConfiguration objectConfiguration = (ObjectConfiguration) elementConfiguration;
            Class<?> clazz = resolveClass(objectConfiguration.getClassName());
            Object resultObject = instanciateObject(objectConfiguration, clazz, context);
            return resultObject;
        case Reference:
            ReferenceConfiguration referenceConfiguration = (ReferenceConfiguration) elementConfiguration;
            return instanciateObject(referenceConfiguration.getReferenceName(), Object.class, context);
        case List:
            ListConfiguration listConfiguration = (ListConfiguration) elementConfiguration;
            List<Object> list = new ArrayList<Object>();
            for (ElementConfiguration listElement : listConfiguration.getListConfiguration())
            {
                list.add(instantiateElementConfiguration(listElement, String.class.getName(), context));
            }
            return list;
        case Map:
            MapConfiguration mapConfiguration = (MapConfiguration) elementConfiguration;
            Map<Object, Object> map = new HashMap<Object, Object>();
            for (Map.Entry<ElementConfiguration, ElementConfiguration> entry : mapConfiguration.getMapConfiguration()
                    .entrySet())
            {
                Object key = instantiateElementConfiguration(entry.getKey(), String.class.getName(), context);
                Object value = instantiateElementConfiguration(entry.getValue(), String.class.getName(), context);
                map.put(key, value);
            }
            return map;
        default:
            throw new RuntimeException("NOT IMPLEMENTED YET :" + elementConfiguration.getFieldConfigurationType());
        }

    }

    private <T> void setFields(T object, ObjectConfiguration objectConfiguration, InstantiationContext context)
            throws ClassNotFoundException, InstantiationException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        if (objectConfiguration.getFields() == null || objectConfiguration.getFields().isEmpty())
        {
            return;
        }
        for (Map.Entry<String, ElementConfiguration> fieldEntry : objectConfiguration.getFields().entrySet())
        {
            String fieldName = fieldEntry.getKey();
            String setterName = getAccessibleClassParser().attributeToSetter(fieldName);
            AccessibleClass accessibleClass = resolveAccessibleClass(objectConfiguration.getClassName());
            AccessibleField accessibleField = accessibleClass.getAccessibleFields().get(fieldName);
            if (accessibleField == null)
            {
                throw new InstantiationException("Invalid field name : " + fieldName);
            }
            String fieldClassName = accessibleField.getClassName();
            Method setterMethod = getSetterMethod(objectConfiguration.getClassName(), setterName, fieldClassName);
            ElementConfiguration fieldConfiguration = fieldEntry.getValue();

            T fieldObject = (T) instantiateElementConfiguration(fieldConfiguration, fieldClassName, context);
            setterMethod.invoke(object, fieldObject);
        }
    }

    private final FastPrimitiveConverter fastPrimitiveConverter = new FastPrimitiveConverter();

    private Object convertPrimitive(String value, String fieldClassName)
    {
        return fastPrimitiveConverter.convert(value, fieldClassName);
    }

    @Override
    public <T> T instanciateObject(String beanName, Class<T> desiredClass, InstantiationContext context)
    {
        {
            Object sharedObject = context.getSharedObject(beanName);
            if (sharedObject != null)
            {
                return (T) sharedObject;
            }
        }
        ObjectConfiguration sharedObjectConfiguration = context.getSharedObjectConfiguration(beanName);
        if (sharedObjectConfiguration == null)
        {
            throw new IllegalArgumentException("Invalid bean name :" + beanName);
        }
        T object = instanciateObject(sharedObjectConfiguration, desiredClass, context);
        if (sharedObjectConfiguration.isSingleton())
        {
            context.putSharedObject(beanName, object);
        }
        return object;
    }

    private final List<ObjectInstanciationHook> objectInstanciationHook = new ArrayList<ReflectionInstantiator.ObjectInstanciationHook>();

    @Override
    public HookHandler addObjectInstanciationHook(final ObjectInstanciationHook hook)
    {
        if (hook == null)
        {
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
