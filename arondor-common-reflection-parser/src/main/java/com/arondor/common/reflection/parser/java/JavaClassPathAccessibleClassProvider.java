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
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

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

    private String extraClassPath;

    @Override
    public void provideClasses(AccessibleClassCatalog catalog)
    {
        doParse(catalog);
    }

    private void doParse(AccessibleClassCatalog catalog)
    {
        LOG.info("Parsing classpath for prefixes : ");
        for (String prefix : getPackagePrefixes())
        {
            LOG.info("* " + prefix);
        }

        String classpath = System.getProperty("java.class.path");
        doScanClasspathForPackages(catalog, classpath);

        String loaderpath = System.getProperty("loader.path");
        if (loaderpath != null)
        {
            doScanClasspathForPackages(catalog, loaderpath);
        }
        if (extraClassPath != null)
        {
            doScanClasspathForPackages(catalog, extraClassPath);
        }
    }

    private String readManifestLoaderPath() throws IOException
    {
        Enumeration<URL> urls = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");

        for (URL url = urls.nextElement(); urls.hasMoreElements(); url = urls.nextElement())
        {
            LOG.info("At manifest : " + url);
            try (InputStream is = url.openStream())
            {
                LOG.info("Manifest at " + is);
                if (is == null)
                    return null;
                Manifest manifest = new Manifest(is);
                LOG.info("Manifest is " + manifest);
                manifest.getMainAttributes().keySet().forEach(x -> LOG.info("Main attr key=" + x));

                manifest.getEntries().keySet().forEach(x -> LOG.info("Entries key=" + x));
                Object attr = manifest.getMainAttributes().get("Loader-Path");
                if (attr != null)
                    return attr.toString();
            }
        }
        return null;
    }

    protected void doScanClasspathForPackages(AccessibleClassCatalog catalog, String classpath)
    {
        LOG.info("Scanning classpath : " + classpath);
        char separator[] = { File.pathSeparatorChar };
        String classpathItem[] = classpath.split(new String(separator));

        for (int idx = 0; idx < classpathItem.length; idx++)
        {
            String path = classpathItem[idx];
            File pathFile = new File(path);
            if (path.endsWith(".jar"))
            {
                scanJar(catalog, pathFile);
            }
            else if (pathFile.exists() && pathFile.isDirectory())
            {
                scanDirectory(catalog, pathFile);
            }
            else
            {
                LOG.warn("Do not known how to handle classpath : " + pathFile.getAbsolutePath());
            }
        }
    }

    public String getExtraClassPath()
    {
        return extraClassPath;
    }

    public void setExtraClassPath(String extraClassPath)
    {
        this.extraClassPath = extraClassPath;
    }

}
