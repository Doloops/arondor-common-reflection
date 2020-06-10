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
package com.arondor.common.reflection.parser.java;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.api.parser.AccessibleClassProvider;
import com.arondor.common.reflection.model.java.AccessibleClass;

/**
 * Abstract stuff for java-based AccessibleClass provider
 * 
 * @author Francois Barre
 * 
 */
public abstract class AbstractJavaAccessibleClassProvider implements AccessibleClassProvider
{
    private static final Logger LOG = Logger.getLogger(AbstractJavaAccessibleClassProvider.class);

    private ClassLoader classLoader = this.getClass().getClassLoader();

    protected ClassLoader getEffectiveClassLoader()
    {
        return classLoader;
    }

    protected void setEffectiveClassLoader(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }

    private AccessibleClassParser accessibleClassParser = new JavaAccessibleClassParser();

    public AccessibleClassParser getAccessibleClassParser()
    {
        return accessibleClassParser;
    }

    public void setAccessibleClassParser(AccessibleClassParser accessibleClassParser)
    {
        this.accessibleClassParser = accessibleClassParser;
    }

    private boolean allowClassWithNoEmptyConstructor = true;

    private boolean allowInterfaces = true;

    private List<String> packagePrefixes;

    public void setPackagePrefixes(List<String> packagePrefixes)
    {
        this.packagePrefixes = packagePrefixes;
    }

    public List<String> getPackagePrefixes()
    {
        return packagePrefixes;
    }

    protected boolean isClassInPackagePrefixes(String clazz)
    {
        for (String prefix : getPackagePrefixes())
        {
            if (clazz.startsWith(prefix))
            {
                return true;
            }
        }
        return false;
    }

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

    protected void scanDirectory(AccessibleClassCatalog catalog, File pathFile)
    {
        LOG.debug("Scanning directory : " + pathFile);
        if (!pathFile.exists())
        {
            LOG.warn("Skipping directory " + pathFile.getAbsolutePath() + ", does not exist");
            return;
        }
        scanDirectory(catalog, pathFile, null);
    }

    private void scanDirectory(AccessibleClassCatalog catalog, File pathFile, String root)
    {
        File children[] = pathFile.listFiles();
        String subRootPrefix = (root != null ? (root + ".") : "");
        for (int idx = 0; idx < children.length; idx++)
        {
            File entry = children[idx];
            if (!entry.exists())
            {
                LOG.warn("Child " + entry.getAbsolutePath() + " does not exist !");
            }
            if (entry.isDirectory())
            {
                String subRoot = subRootPrefix + entry.getName();
                scanDirectory(catalog, entry, subRoot);
            }
            else if (entry.getName().endsWith(".class"))
            {
                String clz = subRootPrefix + entry.getName().substring(0, entry.getName().length() - ".class".length());
                if (isClassInPackagePrefixes(clz))
                {
                    addClass(catalog, clz);
                }
            }
            else if (entry.getName().endsWith(".jar"))
            {
                scanJar(catalog, entry);
            }
        }
    }

    protected void scanJar(AccessibleClassCatalog catalog, File pathFile)
    {
        doScanJar(catalog, pathFile);
    }

    protected void doScanJar(AccessibleClassCatalog catalog, File jarPath)
    {
        LOG.debug("Openning jar '" + jarPath.getAbsolutePath() + "'");
        try (JarFile jarFile = new JarFile(jarPath))
        {
            doScanJar(catalog, jarFile);
        }
        catch (IOException | RuntimeException e)
        {
            LOG.error("Could not scan jar : " + jarPath.getAbsolutePath(), e);
        }
    }

    private void doScanSubJar(AccessibleClassCatalog catalog, JarFile parentJar, JarEntry jarEntry) throws IOException
    {
        LOG.debug("Openning jar '" + jarEntry.getName() + "'");
        JarInputStream jis = new JarInputStream(parentJar.getInputStream(jarEntry));
        while (true)
        {
            JarEntry childEntry = jis.getNextJarEntry();
            if (childEntry == null)
                break;
            processEntryAsClass(catalog, childEntry);
        }
    }

    private void processEntryAsClass(AccessibleClassCatalog catalog, JarEntry entry)
    {
        if (entry.getName().endsWith(".class") && !entry.getName().contains("$"))
        {

            String clz = entry.getName().substring(0, entry.getName().length() - ".class".length());

            /**
             * Regardless on which platform we are on, convert both \\ and / to
             * a dot.
             */
            clz = clz.replace('\\', '.').replace('/', '.');
            if (isClassInPackagePrefixes(clz))
            {
                addClass(catalog, clz);
            }
        }
    }

    protected void doScanJar(AccessibleClassCatalog catalog, JarFile jarFile)
    {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements())
        {
            JarEntry entry = entries.nextElement();
            LOG.debug("Opening entry : " + entry.getName());
            processEntryAsClass(catalog, entry);
            if (entry.getName().endsWith(".jar"))
            {
                try
                {
                    doScanSubJar(catalog, jarFile, entry);
                }
                catch (IOException e)
                {
                    LOG.error("Could not read entry " + entry.getName() + " in " + jarFile.getName());
                }
            }
        }
    }

    protected void addClass(AccessibleClassCatalog catalog, String className)
    {
        try
        {
            Class<?> clazz = Class.forName(className, false, getEffectiveClassLoader());
            addClass(catalog, clazz);
        }
        catch (ClassNotFoundException e)
        {
            LOG.error("Could not get class for name : " + className);
        }
        catch (NoClassDefFoundError e)
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
