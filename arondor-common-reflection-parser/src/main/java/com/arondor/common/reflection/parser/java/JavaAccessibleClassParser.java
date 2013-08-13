package com.arondor.common.reflection.parser.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.arondor.common.management.mbean.annotation.Description;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.bean.java.AccessibleClassBean;
import com.arondor.common.reflection.bean.java.AccessibleConstructorBean;
import com.arondor.common.reflection.bean.java.AccessibleFieldBean;
import com.arondor.common.reflection.bean.java.AccessibleMethodBean;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleConstructor;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.model.java.AccessibleMethod;

public class JavaAccessibleClassParser implements AccessibleClassParser
{
    /**
     * Logger stuff
     */
    private static final Logger LOG = Logger.getLogger(JavaAccessibleClassParser.class);

    /**
     * Expensive log for this class
     */
    private static final boolean DEBUG = LOG.isDebugEnabled();

    /**
     * Convert a getter method name to an attribute name, in Java naming
     * conventions
     * 
     * @param getterName
     *            the getField() (or setField()) method
     * @return the field name, with first character in lowercase
     */
    public String getterToAttribute(String getterName)
    {
        int offset = -1;
        if (getterName.startsWith("get") || getterName.startsWith("set"))
        {
            offset = 3;
        }
        else if (getterName.startsWith("is"))
        {
            offset = 2;
        }
        else
        {
            throw new IllegalArgumentException("Invalid call to getterToAttribute('" + getterName + "')");
        }
        String rName = getterName.substring(offset, offset + 1);
        String rgName = getterName.substring(offset + 1);
        return rName.toLowerCase() + rgName;
    }

    /**
     * Generate an attribute getter name
     * 
     * @param name
     *            the field name
     * @return the getter method name, getField()
     */
    public String attributeToGetter(String name)
    {
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * Generate an attribute getter name
     * 
     * @param name
     *            the field name
     * @return the getter method name, getField()
     */
    public String booleanAttributeToGetter(String name)
    {
        return "is" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * Generate an attribute setter name
     * 
     * @param name
     *            the field name
     * @return the setter method name, setField()
     */
    public String attributeToSetter(String name)
    {
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private static final Class<?> PRIMITIVES[] = { java.lang.String.class, java.lang.Long.class,
            java.lang.Integer.class, java.lang.Float.class, java.lang.Boolean.class, java.lang.Double.class,
            java.lang.Character.class, boolean.class, int.class, long.class, double.class, float.class, char.class };

    public boolean isPrimitiveType(String clazzName)
    {
        for (Class<?> primitive : PRIMITIVES)
        {
            if (primitive.getName().equals(clazzName))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isPrimitiveType(Class<?> clazz)
    {
        for (Class<?> primitive : PRIMITIVES)
        {
            if (primitive.equals(clazz))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a class is part of classes considered to be directly exposable
     * to JConsole
     * 
     * @param clazz
     *            the class to check
     * @return true if this class is considered exposable, false otherwise
     */
    public boolean isExposableType(Class<?> clazz, boolean includeNonPrimitive)
    {
        if (isPrimitiveType(clazz))
        {
            return true;
        }
        if (clazz.getPackage() != null)
        {
            if (clazz.getPackage().getName().startsWith("javax.management"))
            {
                return false;
            }
        }
        return includeNonPrimitive;
    }

    /**
     * Check if an array of classes is exposable to JConsole
     * 
     * @param parameterTypes
     *            array of classes to check
     * @return true if all classes are exposable, false if at least one is not
     *         exposable
     */
    public boolean isExposableSignature(Class<?>[] parameterTypes, boolean includeNonPrimitive)
    {
        if (parameterTypes == null || parameterTypes.length == 0)
        {
            return true;
        }
        for (int i = 0; i < parameterTypes.length; i++)
        {
            if (!isExposableType(parameterTypes[i], includeNonPrimitive))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a class is a 'void' type
     * 
     * @param clazz
     *            the class to check
     * @return true if the class is void, true otherwise
     */
    public boolean isVoid(Class<?> clazz)
    {
        return (clazz.isPrimitive() && clazz.getName().equals("void"));
    }

    /**
     * Parse method signatures and put it in exposed attributes or exposed
     * methods
     * 
     * @param methods
     *            the array of methods to parse
     * @param exposedAttributes
     *            map of exposed attributes
     * @param exposedMethods
     *            list of exposed methods
     */
    private void parseExposedMethodAndAttributes(Method[] methods, Map<String, AccessibleField> exposedAttributes,
            List<Method> exposedMethods, boolean includeNonPrimitive)
    {
        for (int mth = 0; mth < methods.length; mth++)
        {
            Method method = methods[mth];
            if (DEBUG)
            {
                LOG.debug("Method : " + method.getName() + ", return=" + method.getReturnType().getName()
                        + " (exposable=" + isExposableType(method.getReturnType(), includeNonPrimitive) + ")"
                        + ", args=" + method.getParameterTypes().length);
            }
            if (isIgnoredMethod(method))
            {
                continue;
            }
            parsedExposedMethod(exposedAttributes, exposedMethods, includeNonPrimitive, method);
        }
    }

    private void parsedExposedMethod(Map<String, AccessibleField> exposedAttributes, List<Method> exposedMethods,
            boolean includeNonPrimitive, Method method)
    {
        if (isGetterMethod(includeNonPrimitive, method))
        {
            String prop = getterToAttribute(method.getName());
            Class<?> ptyClass = method.getReturnType();
            AccessibleFieldBean attributeInfo = getBeanFromMethod(exposedAttributes, ptyClass, prop);
            attributeInfo.setReadable();
            if (method.getName().startsWith("is"))
            {
                attributeInfo.setIs(true);
            }
        }
        else if (isSetterMethod(includeNonPrimitive, method))
        {
            String prop = getterToAttribute(method.getName());
            Class<?> parameterType = method.getParameterTypes()[0];
            AccessibleFieldBean attributeInfo = getBeanFromMethod(exposedAttributes, parameterType, prop);
            attributeInfo.setWritable();
            handleObjectMultipleMethod(method, parameterType, attributeInfo);
        }
        else if ((isVoid(method.getReturnType()) || (isExposableType(method.getReturnType(), includeNonPrimitive)))
                && (isExposableSignature(method.getParameterTypes(), includeNonPrimitive)))
        {
            exposedMethods.add(method);
        }
        else
        {
            if (DEBUG)
            {
                LOG.debug("Skipping method :" + method.getName() + ", modifiers="
                        + Modifier.toString(method.getModifiers()));
            }
        }
    }

    private void handleObjectMultipleMethod(Method method, Class<?> parameterType, AccessibleFieldBean attributeInfo)
    {
        if (java.util.Map.class.isAssignableFrom(parameterType))
        {
            Type genericParameterType = method.getGenericParameterTypes()[0];
            if (genericParameterType instanceof ParameterizedType)
            {
                ParameterizedType parameterizedType = (ParameterizedType) genericParameterType;

                List<String> genericParameterClassList = new ArrayList<String>();
                for (Type argument : parameterizedType.getActualTypeArguments())
                {
                    String argumentClassName = ((Class<?>) argument).getName();
                    if (DEBUG)
                    {
                        LOG.debug("* Type argument : " + argumentClassName);
                    }
                    genericParameterClassList.add(argumentClassName);
                }
                attributeInfo.setGenericParameterClassList(genericParameterClassList);
            }
        }
    }

    private static final String[] IGNORED_METHODS = { "wait", "notifyAll", "notify", "finalize", "getClass", "equals",
            "toString", "hashCode" };

    private boolean isIgnoredMethod(Method method)
    {
        if (!Modifier.isPublic(method.getModifiers()) || Modifier.isStatic(method.getModifiers()))
        {
            return true;
        }
        for (String mth : IGNORED_METHODS)
        {
            if (method.getName().equals(mth))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether this mehod is a setter method
     * 
     * @param includeNonPrimitive
     * @param method
     * @return
     */
    private boolean isSetterMethod(boolean includeNonPrimitive, Method method)
    {
        return method.getName().startsWith("set") && method.getParameterTypes().length == 1
                && (isExposableSignature(method.getParameterTypes(), includeNonPrimitive))
                && isVoid(method.getReturnType());
    }

    /**
     * Check whether this mehod is a getter method
     * 
     * @param includeNonPrimitive
     * @param method
     * @return
     */
    private boolean isGetterMethod(boolean includeNonPrimitive, Method method)
    {
        return (method.getName().startsWith("get") || method.getName().startsWith("is"))
                && (isExposableType(method.getReturnType(), includeNonPrimitive))
                && method.getParameterTypes().length == 0;
    }

    private AccessibleFieldBean getBeanFromMethod(Map<String, AccessibleField> exposedAttributes,
            Class<?> propertyClass, String propertyName)
    {
        AccessibleFieldBean attributeInfo = (AccessibleFieldBean) exposedAttributes.get(propertyName);
        if (attributeInfo == null)
        {
            attributeInfo = new AccessibleFieldBean(propertyName, propertyName, propertyClass, true, false);
            exposedAttributes.put(propertyName, attributeInfo);
            if (DEBUG)
            {
                LOG.debug("New exposed attribute (R) : " + attributeInfo.getName());
            }
        }
        else
        {
            if (!propertyClass.getName().equals(attributeInfo.getClassName()))
            {
                LOG.warn("Diverging classes at setter for property " + propertyName + " : was "
                        + attributeInfo.getClassName() + ", now is " + propertyClass.getName());
            }
        }
        return attributeInfo;
    }

    private String getClassDescription(Class<?> clazz)
    {
        Description descAnnotation = clazz.getAnnotation(Description.class);
        if (descAnnotation != null)
        {
            return descAnnotation.value();
        }
        return null;
    }

    private String getFieldDescription(Field field)
    {
        Description descAnnotation = field.getAnnotation(Description.class);
        if (descAnnotation != null)
        {
            return descAnnotation.value();
        }
        return null;
    }

    public AccessibleClass parseAccessibleClass(Class<?> clazz)
    {
        if (DEBUG)
        {
            LOG.debug("Parsing accessible class : " + clazz.getName());
        }
        AccessibleClassBean accessClass = createBaseAccessibleClass(clazz);

        setAccessibleClassInheritance(clazz, accessClass);

        setAccessibleClassConstructors(clazz, accessClass);

        Map<String, AccessibleField> exposedAttributes = new HashMap<String, AccessibleField>();
        List<Method> exposedMethods = new ArrayList<Method>();

        parseExposedMethodAndAttributes(clazz.getMethods(), exposedAttributes, exposedMethods, true);

        setAccessibleFieldsDescriptions(accessClass, clazz, exposedAttributes);

        setAccessibleMethods(accessClass, exposedMethods);
        return accessClass;
    }

    private void setAccessibleMethods(AccessibleClassBean accessClass, List<Method> exposedMethods)
    {
        List<AccessibleMethod> accessibleMethods = new ArrayList<AccessibleMethod>();
        for (Method method : exposedMethods)
        {
            if (isIgnoredMethod(method))
            {
                continue;
            }
            AccessibleMethodBean accessibleMethod = new AccessibleMethodBean();
            accessibleMethod.setName(method.getName());
            accessibleMethods.add(accessibleMethod);
        }

        accessClass.setAccessibleMethods(accessibleMethods);
    }

    private void setAccessibleFieldsDescriptions(AccessibleClassBean accessibleClass, Class<?> clazz,
            Map<String, AccessibleField> exposedAttributes)
    {
        for (AccessibleField accessibleField : exposedAttributes.values())
        {
            accessibleField.setClassName(accessibleField.getClassName().replace('$', '.'));

            for (Class<?> superclass = clazz; superclass != null; superclass = superclass.getSuperclass())
            {
                try
                {
                    Field field = superclass.getDeclaredField(accessibleField.getName());
                    ((AccessibleFieldBean) accessibleField).setDescription(getFieldDescription(field));
                    break;
                }
                catch (SecurityException e)
                {
                    LOG.debug("Could not fetch field '" + accessibleField.getName() + "'");
                }
                catch (NoSuchFieldException e)
                {
                    LOG.debug("Could not fetch field '" + accessibleField.getName() + "' from class "
                            + superclass.getName());
                }
            }
        }
        accessibleClass.setAccessibleFields(exposedAttributes);
    }

    private void setAccessibleClassConstructors(Class<?> clazz, AccessibleClassBean accessClass)
    {
        for (Constructor<?> constructor : clazz.getConstructors())
        {
            AccessibleConstructorBean mConstructor = new AccessibleConstructorBean();
            mConstructor.setArgumentTypes(new ArrayList<String>());
            for (Class<?> arg : constructor.getParameterTypes())
            {
                mConstructor.getArgumentTypes().add(arg.getName().replace('$', '.'));
            }
            accessClass.getConstructors().add(mConstructor);
        }
    }

    private void setAccessibleClassInheritance(Class<?> clazz, AccessibleClassBean accessClass)
    {
        for (Class<?> itf : clazz.getInterfaces())
        {
            accessClass.getInterfaces().add(itf.getName());
        }

        for (Class<?> superClass = clazz; superClass != null; superClass = superClass.getSuperclass())
        {
            for (Class<?> itf : superClass.getInterfaces())
            {
                accessClass.getAllInterfaces().add(itf.getName());
            }
        }
    }

    private AccessibleClassBean createBaseAccessibleClass(Class<?> clazz)
    {
        AccessibleClassBean accessClass = new AccessibleClassBean();
        accessClass.setAllInterfaces(new ArrayList<String>());
        accessClass.setInterfaces(new ArrayList<String>());
        accessClass.setConstructors(new ArrayList<AccessibleConstructor>());

        accessClass.getInterfaces().add(java.lang.Object.class.getName());
        accessClass.getAllInterfaces().add(java.lang.Object.class.getName());
        accessClass.setName(clazz.getName().replace('$', '.'));
        accessClass.setDescription(getClassDescription(clazz));

        if (clazz.getSuperclass() == null)
        {
            LOG.warn("No superclass for class : '" + clazz.getName() + "'");
        }
        else
        {
            accessClass.setSuperclass(clazz.getSuperclass().getName());
        }
        return accessClass;
    }
}
