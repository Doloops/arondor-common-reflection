package com.arondor.common.management.mbean;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.log4j.Logger;

/**
 * Simple javax.management.DynamicMBean implementation using straightforward
 * automatic reflection
 * 
 * @author Francois Barre
 * 
 */
public abstract class MBeanObject implements javax.management.DynamicMBean
{
    /**
     * Logger stuff
     */
    private static final Logger log = Logger.getLogger(MBeanObject.class);

    /**
     * Default object description
     */
    protected String description = "{Unknown}";

    /**
     * The name used when registering
     */
    protected ObjectName effectiveObjectName = null;

    /**
     * Simple constructor
     * 
     * @param name
     *            the name of this instance
     */
    protected MBeanObject(String name)
    {
        effectiveObjectName = MBeanObjectHelper.getSingleton().registerMBean(this.getClass(), this, name);
    }

    /**
     * Retype constructor
     * 
     * @param clazz
     *            the class to retype this object to
     * @param name
     *            this instance name
     */
    protected MBeanObject(Class<?> clazz, String name)
    {
        delayedRegister(clazz, name);
    }

    protected void delayedRegister(Class<?> clazz, String name)
    {
        if (effectiveObjectName == null)
        {
            effectiveObjectName = MBeanObjectHelper.getSingleton().registerMBean(clazz, this, name);
        }
    }

    /**
     * Detailed Constructor
     * 
     * @param name
     *            this instance name
     * @param description
     *            a description for this object
     */
    protected MBeanObject(String name, String description)
    {
        effectiveObjectName = MBeanObjectHelper.getSingleton().registerMBean(this.getClass(), this, name);
        this.description = description;
    }

    protected MBeanObject(Class<?> clazz, String name, boolean disableMBeanRegistering)
    {
        if (!disableMBeanRegistering)
            effectiveObjectName = MBeanObjectHelper.getSingleton().registerMBean(clazz, this, name);
    }

    protected void unregisterMBean()
    {
        if (effectiveObjectName != null)
        {
            MBeanObjectHelper.getSingleton().unregisterMBean(effectiveObjectName);
            effectiveObjectName = null;
        }
    }

    /**
     * Finalize this object : unregister mbean
     */
    @Override
    public void finalize()
    {
        log.debug("Finalize for class=" + this.getClass().getName() + ", this=" + this);
        unregisterMBean();
    }

    public MBeanInfo getMBeanInfo()
    {
        return MBeanObjectHelper.getSingleton().getMBeanInfo(this, description);
    }

    public Object getAttribute(String arg0) throws AttributeNotFoundException, MBeanException, ReflectionException
    {
        return MBeanObjectHelper.getSingleton().getAttribute(this, arg0);
    }

    public AttributeList getAttributes(String[] arg0)
    {
        return MBeanObjectHelper.getSingleton().getAttributes(this, arg0);
    }

    public Object invoke(String name, Object[] objects, String[] signatureNames) throws MBeanException,
            ReflectionException
    {
        return MBeanObjectHelper.getSingleton().invoke(this, name, objects, signatureNames);
    }

    public AttributeList setAttributes(AttributeList arg0)
    {
        return MBeanObjectHelper.getSingleton().setAttributes(this, arg0);
    }

    public void setAttribute(Attribute arg0) throws AttributeNotFoundException, InvalidAttributeValueException,
            MBeanException, ReflectionException
    {
        MBeanObjectHelper.getSingleton().setAttribute(this, arg0);
    }
}
