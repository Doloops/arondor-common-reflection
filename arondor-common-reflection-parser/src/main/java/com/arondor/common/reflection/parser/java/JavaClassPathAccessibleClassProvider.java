package com.arondor.common.reflection.parser.java;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;

/**
 * Parse all class path in search of classes
 * 
 * @author Francois Barre
 * 
 */
public class JavaClassPathAccessibleClassProvider extends AbstractJavaAccessibleClassProvider
{
    private static final Logger LOG = Logger.getLogger(JavaAccessibleClassParser.class);

    public void provideClasses(AccessibleClassCatalog catalog)
    {
        doParse(catalog);
    }

    private List<String> packagePrefixes;

    public void setPackagePrefixes(List<String> packagePrefixes)
    {
        this.packagePrefixes = packagePrefixes;
    }

    public List<String> getPackagePrefixes()
    {
        return packagePrefixes;
    }

    private void doParse(AccessibleClassCatalog catalog)
    {
        LOG.info("Parsing classpath for prefixes : ");
        for (String prefix : getPackagePrefixes())
        {
            LOG.info("* " + prefix);
        }

        for (String prefix : packagePrefixes)
        {
            doScanClasspathForPackages(catalog, prefix);
        }

    }

    protected void doScanClasspathForPackages(AccessibleClassCatalog catalog, String packagePrefix)
    {
        String classpath = System.getProperty("java.class.path");
        LOG.info("Scanning classpath : " + classpath);
        char separator[] = { File.pathSeparatorChar };
        String classpathItem[] = classpath.split(new String(separator));

        for (int idx = 0; idx < classpathItem.length; idx++)
        {
            String path = classpathItem[idx];
            File pathFile = new File(path);
            if (path.endsWith(".jar"))
            {
                scanJar(catalog, pathFile, packagePrefix);
            }
            else if (pathFile.exists() && pathFile.isDirectory())
            {
                scanDirectory(catalog, pathFile, packagePrefix);
            }
            else
            {
                LOG.warn("Do not known how to handle classpath : " + pathFile.getAbsolutePath());
            }
        }
    }

    private void scanDirectory(AccessibleClassCatalog catalog, File pathFile, String packagePrefix)
    {
        LOG.debug("Scanning classpath directory : " + pathFile);
        scanDirectory(catalog, pathFile, null, packagePrefix);
    }

    private void scanDirectory(AccessibleClassCatalog catalog, File pathFile, String root, String packagePrefix)
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
                scanDirectory(catalog, entry, subRoot, packagePrefix);
            }
            else if (entry.getName().endsWith(".class"))
            {
                String clz = subRootPrefix + entry.getName().substring(0, entry.getName().length() - ".class".length());
                if (clz.startsWith(packagePrefix))
                {
                    addClass(catalog, clz);
                }
            }
        }

    }

    private void scanJar(AccessibleClassCatalog catalog, File pathFile, String packagePrefix)
    {
        LOG.debug("Openning jar '" + pathFile.getAbsolutePath() + "'");
        JarFile jarFile = null;
        try
        {
            jarFile = new JarFile(pathFile);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements())
            {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class") && !entry.getName().contains("$"))
                {

                    String clz = entry.getName().substring(0, entry.getName().length() - ".class".length());

                    /**
                     * Regardless on which platform we are on, convert both \\
                     * and / to a dot.
                     */
                    clz = clz.replace('\\', '.').replace('/', '.');
                    if (clz.startsWith(packagePrefix))
                    {
                        addClass(catalog, clz);
                    }
                }
            }
        }
        catch (Throwable t)
        {
            LOG.error("Could not scan jar : " + pathFile.getAbsolutePath());
        }
        finally
        {
            if (jarFile != null)
            {
                try
                {
                    jarFile.close();
                }
                catch (IOException e)
                {
                    LOG.error("Could not close jar : " + pathFile.getAbsolutePath());
                }
            }
        }
    }

}
