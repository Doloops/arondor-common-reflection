package com.arondor.common.reflection.noreflect.instantiator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiator;
import com.arondor.common.reflection.model.config.FieldConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.noreflect.exception.NoReflectRuntimeException;
import com.arondor.common.reflection.noreflect.model.FieldSetter;
import com.arondor.common.reflection.noreflect.model.ObjectConstructor;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorRegistrar;
import com.arondor.common.reflection.noreflect.runtime.SimpleInstantiationContext;
import com.google.gwt.core.shared.GWT;

public class ReflectionInstantiatorNoReflect implements ReflectionInstantiator
{
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
        if (isSharedObjectReference(objectConfiguration))
        {
            return getSharedObjectReference(objectConfiguration, desiredClass, context);
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

    private void instanciateObjectFields(ObjectConfiguration objectConfiguration, InstantiationContext context, Object object)
    {
        if (objectConfiguration.getFields() != null)
        {
            for (Map.Entry<String, FieldConfiguration> entry : objectConfiguration.getFields().entrySet())
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
            for (FieldConfiguration field : objectConfiguration.getConstructorArguments())
            {
                constructorArguments.add(instanciateObjectField(field, context));
            }
        }
        return constructorArguments;
    }

    private <T> T getSharedObjectReference(ObjectConfiguration objectConfiguration, Class<T> desiredClass,
            InstantiationContext context)
    {
        Object resolvedObject = context.getSharedObject(objectConfiguration.getReferenceName());

        if (resolvedObject != null)
        {
            return castObject(resolvedObject, desiredClass);
        }
        ObjectConfiguration resolvedConfiguration = context.getSharedObjectConfiguration(objectConfiguration
                .getReferenceName());
        if (resolvedConfiguration != null)
        {
            return instanciateObject(resolvedConfiguration, desiredClass, context);
        }
        throw new IllegalArgumentException("Could not resolve reference : "
                + objectConfiguration.getReferenceName());
    }

    private boolean isSharedObjectReference(ObjectConfiguration objectConfiguration)
    {
        return objectConfiguration.getClassName() == null && objectConfiguration.getReferenceName() != null;
    }

    private Object instanciateObjectField(FieldConfiguration fieldConfiguration, InstantiationContext context)
    {
        switch (fieldConfiguration.getFieldConfigurationType())
        {
        case Primitive_Single:
        {
            return fieldConfiguration.getValue();
        }
        case Object_Single:
        {
            if (fieldConfiguration.getObjectConfiguration() == null)
            {
                throw new IllegalArgumentException(
                        "Can not instanciate fieldConfiguration, no ObjectConfiguration provided");
            }
            Object value = instanciateObject(fieldConfiguration.getObjectConfiguration(), Object.class, context);
            return value;
        }
        case Object_Multiple:
        {
            if (fieldConfiguration.getObjectConfigurations() == null)
            {
                throw new IllegalArgumentException(
                        "Can not instanciate fieldConfiguration, no ObjectConfigurations provided");
            }
            List<FieldConfiguration> list = fieldConfiguration.getObjectConfigurations();
            if (list.isEmpty())
            {
                return null;
            }

            List<Object> objectList = new ArrayList<Object>();
            for (FieldConfiguration childFieldConfiguration : list)
            {
                if (childFieldConfiguration.getValue() != null)
                {
                    throw new NoReflectRuntimeException("NOT IMPLEMENTED : valued list is supposed to be a map !");
                }
                objectList.add(instanciateObjectField(childFieldConfiguration, context));
            }
            return objectList;
        }
        default:
            throw new NoReflectRuntimeException("Not implemented yet !");
        }

    }

    private void instanciateObjectField(Object object, String className, String propertyName,
            FieldConfiguration fieldConfiguration, InstantiationContext context)
    {
        FieldSetter fieldSetter = reflectionInstantiatorCatalog.getFieldSetter(className, propertyName);
        if (fieldSetter == null)
        {
            throw new NoReflectRuntimeException("No setter found : className:" + className + ", propertyName:"
                    + propertyName);
        }
        Object value = instanciateObjectField(fieldConfiguration, context);
        fieldSetter.set(object, value);
    }

    public Object instanciatePrimite(String value, Class<?> targetClass)
    {
        throw new NoReflectRuntimeException("Not implemented : instanciatePrimite()");
    }

    public <T> T instanciateObject(String beanName, Class<T> desiredClass, InstantiationContext context)
    {
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
