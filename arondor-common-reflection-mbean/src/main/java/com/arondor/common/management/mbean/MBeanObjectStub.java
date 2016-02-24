package com.arondor.common.management.mbean;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class MBeanObjectStub implements javax.management.DynamicMBean
{
    private Object assignedObject;

    private ObjectName registeredObjectName;

    public MBeanObjectStub(Class<?> clazz, Object assignedObject, String name)
    {
        this.assignedObject = assignedObject;
        registeredObjectName = MBeanObjectHelper.getSingleton().registerMBean(clazz, this, name);
    }

    public Object getAttribute(String arg0) throws AttributeNotFoundException, MBeanException, ReflectionException
    {
        return MBeanObjectHelper.getSingleton().getAttribute(assignedObject, arg0);
    }

    public AttributeList getAttributes(String[] arg0)
    {
        return MBeanObjectHelper.getSingleton().getAttributes(assignedObject, arg0);
    }

    public MBeanInfo getMBeanInfo()
    {
        String description = "MBeanObject Stub";
        return MBeanObjectHelper.getSingleton().getMBeanInfo(assignedObject, description);
    }

    public Object invoke(String name, Object[] objects, String[] signatureNames) throws MBeanException,
            ReflectionException
    {
        return MBeanObjectHelper.getSingleton().invoke(assignedObject, name, objects, signatureNames);
    }

    public void setAttribute(Attribute arg0) throws AttributeNotFoundException, InvalidAttributeValueException,
            MBeanException, ReflectionException
    {
        MBeanObjectHelper.getSingleton().setAttribute(assignedObject, arg0);
    }

    public AttributeList setAttributes(AttributeList arg0)
    {
        return MBeanObjectHelper.getSingleton().setAttributes(assignedObject, arg0);
    }

    public void unregister()
    {
        if (registeredObjectName != null)
        {
            MBeanObjectHelper.getSingleton().unregisterMBean(registeredObjectName);
        }
    }
}
