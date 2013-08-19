package com.arondor.common.reflection.parser.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.api.parser.AccessibleClassProvider;
import com.arondor.common.reflection.model.java.AccessibleClass;

/**
 * Abstract stuff for java-based AccessibleClass provider
 * 
 * @author francois
 * 
 */
public abstract class AbstractJavaAccessibleClassProvider implements AccessibleClassProvider
{
    private static final Logger LOG = Logger.getLogger(JavaAccessibleClassParser.class);

    private AccessibleClassParser accessibleClassParser = new JavaAccessibleClassParser();

    private boolean allowClassWithNoEmptyConstructor = true;

    private boolean allowInterfaces = true;

    protected boolean isValidClass(Class<?> clazz)
    {
        String className = clazz.getName();
        Class<?> emptyConstructorSignature[] = new Class<?>[0];

        try
        {
            LOG.debug("Class '" + className + "', modifiers : " + clazz.getModifiers());
            if (clazz.isAnonymousClass())
            {
                LOG.debug("Skipping anonymous class " + clazz.getName());
                return false;
            }
            int modifiers = clazz.getModifiers();
            if (Modifier.isAbstract(modifiers))
            {
                LOG.debug("Abstract class : " + clazz.getName());
                return false;
            }
            if (!isAllowInterfaces() && Modifier.isInterface(modifiers))
            {
                LOG.debug("Interface class : " + clazz.getName());
                return false;
            }
            if (!isAllowClassWithNoEmptyConstructor())
            {
                Constructor<?> emptyConstructor = null;
                try
                {
                    emptyConstructor = clazz.getConstructor(emptyConstructorSignature);
                }
                catch (SecurityException e)
                {
                }
                catch (NoSuchMethodException e)
                {
                }
                if (emptyConstructor == null)
                {
                    LOG.debug("No empty constructor : " + clazz.getName());
                    return false;
                }
            }
            return true;
        }
        catch (NoClassDefFoundError e)
        {
            LOG.warn("Could not fetch class '" + className + "'");
        }
        catch (UnsupportedOperationException e)
        {
            LOG.debug("Could not fetch class '" + className + "'");
        }
        catch (UnsatisfiedLinkError e)
        {
            LOG.debug("Could not fetch class '" + className + "'");
        }
        catch (ExceptionInInitializerError e)
        {
            LOG.debug("Could not fetch class '" + className + "'");
        }
        catch (RuntimeException e)
        {
            LOG.debug("Could not fetch class '" + className + "'");
        }
        return false;
    }

    protected void addClass(AccessibleClassCatalog catalog, String className)
    {
        try
        {
            Class<?> clazz = Class.forName(className);
            addClass(catalog, clazz);
        }
        catch (ClassNotFoundException e)
        {
            LOG.error("Could not get class for name : " + className);
        }
    }

    protected void addClass(AccessibleClassCatalog catalog, Class<?> clazz)
    {
        AccessibleClass accessibleClass = accessibleClassParser.parseAccessibleClass(clazz);
        if (accessibleClass == null)
        {
            LOG.error("Could not parse class :" + clazz.getName());
            return;
        }
        catalog.addAccessibleClass(accessibleClass);
    }

    public boolean isAllowClassWithNoEmptyConstructor()
    {
        return allowClassWithNoEmptyConstructor;
    }

    public void setAllowClassWithNoEmptyConstructor(boolean allowClassWithNoEmptyConstructor)
    {
        this.allowClassWithNoEmptyConstructor = allowClassWithNoEmptyConstructor;
    }

    public boolean isAllowInterfaces()
    {
        return allowInterfaces;
    }

    public void setAllowInterfaces(boolean allowInterfaces)
    {
        this.allowInterfaces = allowInterfaces;
    }

}
