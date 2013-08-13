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
            throw new RuntimeException("Invalid call to getterToAttribute('" + getterName + "')");
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

    public static final Class<?> PRIMITIVES[] = { java.lang.String.class, java.lang.Long.class,
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
    public boolean isExposable(Class<?> clazz, boolean includeNonPrimitive)
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
            if (!isExposable(parameterTypes[i], includeNonPrimitive))
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
    protected void parseExposedMethodAndAttributes(Method[] methods, Map<String, AccessibleField> exposedAttributes,
            List<Method> exposedMethods, boolean includeNonPrimitive)
    {
        for (int mth = 0; mth < methods.length; mth++)
        {
            Method method = methods[mth];
            if (DEBUG)
            {
                LOG.debug("Method : " + method.getName() + ", return=" + method.getReturnType().getName()
                        + " (exposable=" + isExposable(method.getReturnType(), includeNonPrimitive) + ")" + ", args="
                        + method.getParameterTypes().length);
            }
            if (!Modifier.isPublic(method.getModifiers()))
            {
                if (DEBUG)
                {
                    LOG.debug("Method : " + method.getName() + " is not public, skipping.");
                }
                continue;
            }
            if (Modifier.isStatic(method.getModifiers()))
            {
                if (DEBUG)
                {
                    LOG.debug("Method : " + method.getName() + " is static, skipping.");
                }
                continue;
            }
            if (method.getName().equals("wait") || method.getName().equals("notify")
                    || method.getName().equals("notifyAll") || method.getName().equals("finalize")
                    || method.getName().equals("getClass"))
            {
                if (DEBUG)
                {
                    LOG.debug("Skipping java.lang.Object method : " + method.getName());
                }
                continue;
            }
            if ((method.getName().startsWith("get") || method.getName().startsWith("is"))
                    && (isExposable(method.getReturnType(), includeNonPrimitive))
                    && method.getParameterTypes().length == 0)
            {
                String prop = getterToAttribute(method.getName());
                if (DEBUG)
                {
                    LOG.debug("** Exposable GET field : " + prop + " (from " + method.getName() + ")");
                }
                AccessibleFieldBean attributeInfo = (AccessibleFieldBean) exposedAttributes.get(prop);
                if (attributeInfo == null)
                {
                    Class<?> ptyClass = method.getReturnType();
                    attributeInfo = new AccessibleFieldBean(prop, prop, ptyClass, true, false);
                    exposedAttributes.put(prop, attributeInfo);
                    if (DEBUG)
                    {
                        LOG.debug("New exposed attribute (R) : " + attributeInfo.getName());
                    }
                }
                else
                {
                    if (DEBUG)
                    {
                        LOG.debug("Set readable attribute : " + attributeInfo.getName());
                    }
                    attributeInfo.setReadable();
                }
                if (method.getName().startsWith("is"))
                {
                    attributeInfo.setIs(true);
                }
            }
            else if (method.getName().startsWith("set") && method.getParameterTypes().length == 1
                    && (isExposableSignature(method.getParameterTypes(), includeNonPrimitive))
                    && isVoid(method.getReturnType()))
            {
                String prop = getterToAttribute(method.getName());
                if (DEBUG)
                {
                    LOG.debug("** Exposable SET field : " + prop + " (from " + method.getName() + ")");
                    LOG.debug("** ** Param : " + method.getParameterTypes()[0]);
                }
                Class<?> parameterType = method.getParameterTypes()[0];
                AccessibleFieldBean attributeInfo = (AccessibleFieldBean) exposedAttributes.get(prop);
                if (attributeInfo == null)
                {
                    attributeInfo = new AccessibleFieldBean(prop, prop, parameterType, false, true);
                    if (DEBUG)
                    {
                        LOG.debug("New exposed attribute (W) : " + attributeInfo.getName());
                    }
                    exposedAttributes.put(prop, attributeInfo);
                }
                else
                {
                    if (DEBUG)
                    {
                        LOG.debug("Setting writable : " + attributeInfo.getName());
                    }
                    if (!parameterType.getName().equals(attributeInfo.getClassName()))
                    {
                        LOG.warn("Diverging classes at setter for property " + prop + " : was "
                                + attributeInfo.getClassName() + ", now is " + parameterType.getName());
                    }
                    else
                    {
                        attributeInfo.setWritable();
                    }
                }
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
            else if ((isVoid(method.getReturnType()) || (isExposable(method.getReturnType(), includeNonPrimitive)))
                    && (isExposableSignature(method.getParameterTypes(), includeNonPrimitive)))
            {
                if (DEBUG)
                {
                    LOG.debug("Exposing : " + method.getName() + ",modifiers=" + method.getModifiers());
                }
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
        AccessibleClassBean accessClass = new AccessibleClassBean();
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

        accessClass.setAllInterfaces(new ArrayList<String>());
        accessClass.setInterfaces(new ArrayList<String>());
        accessClass.setConstructors(new ArrayList<AccessibleConstructor>());

        accessClass.getInterfaces().add(java.lang.Object.class.getName());
        accessClass.getAllInterfaces().add(java.lang.Object.class.getName());

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

        LOG.debug("Class '" + clazz.getName() + "', interfaces=" + accessClass.getInterfaces() + ", allInterfaces="
                + accessClass.getAllInterfaces());

        Map<String, AccessibleField> exposedAttributes = new HashMap<String, AccessibleField>();
        List<Method> exposedMethods = new ArrayList<Method>();

        Method[] methods = clazz.getMethods();

        this.parseExposedMethodAndAttributes(methods, exposedAttributes, exposedMethods, true);

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

        accessClass.setAccessibleFields(exposedAttributes);

        List<AccessibleMethod> accessibleMethods = new ArrayList<AccessibleMethod>();
        for (Method method : exposedMethods)
        {
            String name = method.getName();
            if (name.equals("equals") || name.equals("toString") || name.equals("hashCode"))
            {
                continue;
            }
            AccessibleMethodBean aMethod = new AccessibleMethodBean();
            aMethod.setName(name);
            accessibleMethods.add(aMethod);
        }

        accessClass.setAccessibleMethods(accessibleMethods);

        return accessClass;
    }
}
