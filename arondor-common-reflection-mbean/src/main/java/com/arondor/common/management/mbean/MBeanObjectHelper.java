package com.arondor.common.management.mbean;

import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.model.java.AccessibleMethod;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;

/**
 * Simple class helper for MBeanObject, containing utility methods for
 * MBeanObject manipulation
 * 
 * @author Francois Barre
 * 
 */
public class MBeanObjectHelper
{
    /**
     * Logger stuff
     */
    private static final Logger LOG = Logger.getLogger(MBeanObjectHelper.class);

    /**
     * Expensive log for this class
     */
    private static final boolean verbose = LOG.isDebugEnabled();

    private static final class WeakDynamicMBean implements javax.management.DynamicMBean
    {
        private final WeakReference<javax.management.DynamicMBean> weakRef;

        private final ObjectName objectName;

        protected WeakDynamicMBean(javax.management.DynamicMBean target, ObjectName objectName)
        {
            this.weakRef = new WeakReference<javax.management.DynamicMBean>(target);
            this.objectName = objectName;
        }

        private javax.management.DynamicMBean get()
        {
            DynamicMBean mbean = weakRef.get();
            if (mbean == null)
            {
                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
                LOG.warn("Could not resolve weak reference for " + objectName.getCanonicalName());
                try
                {
                    mbs.unregisterMBean(objectName);
                }
                catch (MBeanRegistrationException e)
                {
                    LOG.error("Could not unregister " + objectName.getCanonicalName(), e);
                }
                catch (InstanceNotFoundException e)
                {
                    LOG.error("Could not unregister " + objectName.getCanonicalName(), e);
                }
                throw new IllegalStateException("The bean has already been freed !");
            }
            return mbean;
        }

        public Object getAttribute(String attribute)
                throws AttributeNotFoundException, MBeanException, ReflectionException
        {
            return get().getAttribute(attribute);
        }

        public void setAttribute(Attribute attribute)
                throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
        {
            get().setAttribute(attribute);
        }

        public AttributeList getAttributes(String[] attributes)
        {
            return get().getAttributes(attributes);
        }

        public AttributeList setAttributes(AttributeList attributes)
        {
            return get().setAttributes(attributes);
        }

        public Object invoke(String actionName, Object[] params, String[] signature)
                throws MBeanException, ReflectionException
        {
            return get().invoke(actionName, params, signature);
        }

        public MBeanInfo getMBeanInfo()
        {
            return get().getMBeanInfo();
        }
    }

    private int maximumDuplicateObjects = 1024;

    private final AccessibleClassParser accessibleClassParser = new JavaAccessibleClassParser();

    private final Map<String, MBeanInfo> mbeanInfoCache = new HashMap<String, MBeanInfo>();

    private static final MBeanObjectHelper SINGLETON = new MBeanObjectHelper();

    public static final MBeanObjectHelper getSingleton()
    {
        return SINGLETON;
    }

    /**
     * Generate MBean Information
     * 
     * @param assignedObject
     *            the exposed mbean object
     * @param description
     *            the object description
     * @return MBeanInfo the MBean information describing this object
     */
    public MBeanInfo getMBeanInfo(Object assignedObject, String description)
    {
        String className = assignedObject.getClass().getName();
        if (mbeanInfoCache.containsKey(className))
        {
            return mbeanInfoCache.get(className);
        }

        if (verbose)
        {
            LOG.debug("Building MBeanInfo : " + className);
        }

        AccessibleClass accessibleClass = accessibleClassParser.parseAccessibleClass(assignedObject.getClass());

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[accessibleClass.getAccessibleFields().size()];
        int idx = 0;
        for (AccessibleField fieldInfo : accessibleClass.getAccessibleFields().values())
        {
            attributes[idx++] = toMBeanAttributeInfo(fieldInfo);
        }

        MBeanConstructorInfo[] constructors = null;
        MBeanOperationInfo[] operations = new MBeanOperationInfo[accessibleClass.getAccessibleMethods().size()];
        idx = 0;
        for (AccessibleMethod accessibleMethod : accessibleClass.getAccessibleMethods())
        {
            operations[idx++] = new MBeanOperationInfo(accessibleMethod.getName(),
                    toMethod(assignedObject.getClass(), accessibleMethod));
        }
        MBeanNotificationInfo[] notifications = null;
        MBeanInfo mbeanInfo = new MBeanInfo(className, description, attributes, constructors, operations,
                notifications);

        mbeanInfoCache.put(className, mbeanInfo);
        return mbeanInfo;
    }

    private Method toMethod(Class<?> clazz, AccessibleMethod accessibleMethod)
    {
        for (Method mth : clazz.getMethods())
        {
            if (mth.getName().equals(accessibleMethod.getName()))
            {
                return mth;
            }
        }
        return null;
    }

    private MBeanAttributeInfo toMBeanAttributeInfo(AccessibleField accessibleField)
    {
        return new MBeanAttributeInfo(accessibleField.getName(), accessibleField.getClassName(),
                accessibleField.getDescription(), accessibleField.getReadable(), accessibleField.getWritable(),
                accessibleField.isIs());
    }

    /**
     * Register an object
     * 
     * @param clazz
     *            the class to type object to
     * @param o
     *            the name of this object
     * @param name
     * @return
     */
    public synchronized ObjectName registerMBean(Class<?> clazz, Object o, String name)
    {
        name = name.replace(':', '_');
        name = name.replace(' ', '_');
        name = name.replace('=', '_');
        try
        {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            for (int iSuffix = 0; iSuffix < maximumDuplicateObjects; iSuffix++)
            {
                String suffix = (iSuffix == 0) ? "" : ("#" + iSuffix);
                ObjectName objectName = new ObjectName(clazz.getName() + ":type=" + name + suffix);
                if (mbs.isRegistered(objectName))
                {
                    LOG.debug("ObjectName '" + objectName + "' already registered.");
                    continue;
                }
                try
                {
                    if (o instanceof DynamicMBean)
                    {
                        DynamicMBean dynamicMBean = (DynamicMBean) o;
                        WeakDynamicMBean weakRef = new WeakDynamicMBean(dynamicMBean, objectName);
                        mbs.registerMBean(weakRef, objectName);
                    }
                    else
                    {
                        mbs.registerMBean(o, objectName);
                    }
                }
                catch (javax.management.InstanceAlreadyExistsException iaee)
                {
                    LOG.warn("Instance already exists '" + objectName + "', exception=" + iaee.getMessage());
                    continue;
                }
                LOG.info("Registered : '" + objectName + "'");
                return objectName;
            }
        }
        catch (Exception e)
        {
            LOG.error(
                    "Could not register MBean class='" + clazz.getName() + "', name='" + name + "' : " + e.getMessage(),
                    e);
        }
        return null;
    }

    /**
     * Unregister MBean
     * 
     * @param objectName
     *            the MBean name to unregister
     */
    public synchronized void unregisterMBean(ObjectName objectName)
    {
        try
        {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            mbs.unregisterMBean(objectName);
        }
        catch (Exception e)
        {
            LOG.error("Could not unregister MBean : " + objectName.toString() + " : " + e.getMessage(), e);
        }
    }

    /**
     * Get attribute
     * 
     * @param assignedObject
     * @param arg0
     * @return
     * @throws AttributeNotFoundException
     * @throws MBeanException
     * @throws ReflectionException
     */
    public Object getAttribute(Object assignedObject, String arg0)
            throws AttributeNotFoundException, MBeanException, ReflectionException
    {
        String methodName = accessibleClassParser.attributeToGetter(arg0);
        try
        {
            Method getterMethod = assignedObject.getClass().getMethod(methodName, new Class<?>[0]);
            return getterMethod.invoke(assignedObject, new Object[0]);
        }
        catch (Exception e)
        {
            String isMethodName = accessibleClassParser.booleanAttributeToGetter(arg0);
            try
            {
                Method getterMethod = assignedObject.getClass().getMethod(isMethodName, new Class<?>[0]);
                return getterMethod.invoke(assignedObject, new Object[0]);
            }
            catch (Exception e2)
            {
                throw new ReflectionException(e2, "Getting attribute : '" + arg0 + "' (method name='" + methodName
                        + "', '" + isMethodName + "')");
            }
        }
    }

    public AttributeList getAttributes(Object assignedObject, String[] arg0)
    {
        AttributeList list = new AttributeList();
        for (int i = 0; i < arg0.length; i++)
        {
            try
            {
                list.add(new Attribute(arg0[i], getAttribute(assignedObject, arg0[i])));
            }
            catch (Exception e)
            {
                LOG.error("Could not add : " + arg0[i], e);
            }
        }
        return list;
    }

    /**
     * Invoke a method
     * 
     * @param assignedObject
     * @param name
     * @param objects
     * @param signatureNames
     * @return
     * @throws MBeanException
     * @throws ReflectionException
     */
    public Object invoke(Object assignedObject, String name, Object[] objects, String[] signatureNames)
            throws MBeanException, ReflectionException
    {
        LOG.debug("invoke(arg0=" + name + ")");

        Class<?> signatures[] = new Class<?>[signatureNames.length];
        for (int i = 0; i < signatureNames.length; i++)
        {
            try
            {
                signatures[i] = Class.forName(signatureNames[i]);
            }
            catch (ClassNotFoundException e)
            {
                throw new ReflectionException(e, "Could not get signature : " + signatureNames[i]);
            }
        }
        Method m;
        try
        {
            m = assignedObject.getClass().getMethod(name, signatures);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(new Exception("Could not get method '" + name + "'"));
        }
        catch (NoSuchMethodException e)
        {
            throw new ReflectionException(new Exception("Could not get method '" + name + "'"));
        }
        if (m == null)
        {
            throw new ReflectionException(new Exception("Could not get method '" + name + "'"));
        }
        try
        {
            return m.invoke(assignedObject, objects);
        }
        catch (Exception e)
        {
            LOG.error("Could not invoke method '" + name + "', " + e.getMessage(), e);
            throw new MBeanException(e, "Could not invoke method '" + name + "'");
        }
    }

    protected Method getSetterMethodNoException(Object assignedObject, String methodName, Class<?> clazz)
    {
        Class<?> paramTypes[] = { clazz };
        try
        {
            Method setterMethod = assignedObject.getClass().getMethod(methodName, paramTypes);
            return setterMethod;
        }
        catch (Throwable t)
        {
            return null;
        }
    }

    private Map<Class<?>, Class<?>> classMapAlias = new HashMap<Class<?>, Class<?>>();

    private void putClassMapAlias(Class<?> c1, Class<?> c2)
    {
        classMapAlias.put(c1, c2);
        classMapAlias.put(c2, c1);
    }

    {

        putClassMapAlias(java.lang.Integer.class, int.class);
        putClassMapAlias(java.lang.Long.class, long.class);
        putClassMapAlias(java.lang.Boolean.class, boolean.class);
        putClassMapAlias(java.lang.Float.class, float.class);
        putClassMapAlias(java.lang.Double.class, double.class);
    }

    public String getPrimitiveClassMapAlias(String name)
    {
        if (name.equals("int"))
            return java.lang.Integer.class.getName();
        if (name.equals("long"))
            return java.lang.Long.class.getName();
        if (name.equals("boolean"))
            return java.lang.Boolean.class.getName();
        if (name.equals("float"))
            return java.lang.Float.class.getName();
        if (name.equals("double"))
            return java.lang.Double.class.getName();

        return null;
    }

    public String getClassMapAlias(String name)
    {
        String primitive = getPrimitiveClassMapAlias(name);
        if (primitive != null)
            return primitive;
        try
        {
            Class<?> source = Class.forName(name);
            if (source == null)
                return null;
        }
        catch (ClassNotFoundException e)
        {
            return null;
        }
        Class<?> target = classMapAlias.get(name);
        if (target == null)
            return null;
        return target.getName();
    }

    protected Method getSetterMethod(Object assignedObject, String methodName, Class<?> clazz)
            throws ReflectionException
    {
        for (Class<?> superclass = clazz; superclass != null; superclass = superclass.getSuperclass())
        {
            Method setterMethod = getSetterMethodNoException(assignedObject, methodName, superclass);
            if (setterMethod != null)
                return setterMethod;
            Class<?> aliasClazz = classMapAlias.get(superclass);
            if (aliasClazz != null)
            {
                setterMethod = getSetterMethodNoException(assignedObject, methodName, aliasClazz);
                if (setterMethod != null)
                {
                    if (verbose)
                    {
                        LOG.debug("methodName=" + methodName + ", using alias=" + aliasClazz.getName());
                    }
                    return setterMethod;
                }
            }
            if (superclass.getInterfaces() != null)
            {
                for (Class<?> itfClazz : superclass.getInterfaces())
                {
                    setterMethod = getSetterMethodNoException(assignedObject, methodName, itfClazz);
                    if (setterMethod != null)
                    {
                        if (verbose)
                        {
                            LOG.debug("methodName=" + methodName + ", using itf=" + itfClazz.getName());
                        }
                        return setterMethod;
                    }
                }
            }
        }
        throw new ReflectionException(
                new Exception("Could not find method '" + methodName + "(" + clazz.getName() + ")'"),
                "Reflection Exception on mbean=" + assignedObject);
    }

    /**
     * Set attribute
     * 
     * @param assignedObject
     * @param arg0
     * @throws AttributeNotFoundException
     * @throws InvalidAttributeValueException
     * @throws MBeanException
     * @throws ReflectionException
     */
    public void setAttribute(Object assignedObject, Attribute arg0)
            throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
    {
        String name = arg0.getName();
        String methodName = accessibleClassParser.attributeToSetter(name);
        try
        {
            Method setterMethod = getSetterMethod(assignedObject, methodName, arg0.getValue().getClass());
            if (verbose)
            {
                LOG.debug("mbean=" + assignedObject + ", name=" + name + ", clazz=" + arg0.getValue().getClass()
                        + ", setterMethod=" + setterMethod);
            }
            Object params[] = { arg0.getValue() };
            setterMethod.invoke(assignedObject, params);
        }
        catch (Exception e)
        {
            LOG.error("Exception while setting attribute : '" + arg0 + "'", e);
            throw new ReflectionException(e, "Setting attribute : '" + arg0 + "'");
        }
    }

    /**
     * Set attribute list
     * 
     * @param
     * @param arg0
     * @return
     */
    public AttributeList setAttributes(Object assignedObject, AttributeList arg0)
    {
        LOG.debug("Setting " + arg0.size() + " attributes.");
        AttributeList updatedList = new AttributeList();
        for (Object attr : arg0)
        {
            try
            {
                setAttribute(assignedObject, (Attribute) attr);
                updatedList.add((Attribute) attr);
            }
            catch (Exception e)
            {
                LOG.error("Could not set : " + ((Attribute) attr).getName(), e);
            }
        }
        return updatedList;
    }
}
