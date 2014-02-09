/*
 *  Copyright 2014, Arondor
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;

public class DirectoryAccessibleClassProvider extends AbstractJavaAccessibleClassProvider
{
    private static final Logger LOG = Logger.getLogger(DirectoryAccessibleClassProvider.class);

    private List<String> directories;

    public List<String> getDirectories()
    {
        return directories;
    }

    public void setDirectories(List<String> directories)
    {
        this.directories = directories;
    }

    public DirectoryAccessibleClassProvider()
    {
        // setShallLoadJars(true);
    }

    protected void scanDir(List<URL> jars, File dirFile)
    {
        if (!dirFile.exists() || !dirFile.isDirectory())
        {
            return;
        }
        for (File child : dirFile.listFiles())
        {
            if (child.isDirectory())
            {
                scanDir(jars, child);
            }
            else if (child.getName().endsWith(".jar"))
            {
                try
                {
                    URL jarUrl = child.toURI().toURL();
                    LOG.info("Adding jar : " + jarUrl);
                    jars.add(jarUrl);
                }
                catch (MalformedURLException e)
                {
                    LOG.error("COuld not get name", e);
                }
            }
        }
    }

    private static final URL[] toArray(List<URL> objects)
    {
        URL[] array = new URL[objects.size()];
        for (int idx = 0; idx < objects.size(); idx++)
        {
            array[idx] = objects.get(idx);
        }
        return array;
    }

    public void provideClasses(AccessibleClassCatalog catalog)
    {
        List<URL> urls = new ArrayList<URL>();
        for (String directory : directories)
        {
            try
            {
                File directoryFile = new File(directory);
                URL url = directoryFile.toURI().toURL();
                LOG.info("Added url=" + url);
                urls.add(url);
                scanDir(urls, directoryFile);
            }
            catch (MalformedURLException e)
            {
                LOG.error("MalformedURLException", e);
                throw new RuntimeException("Could not prepare class loader", e);
            }
        }

        URL urlsArray[] = toArray(urls);
        URLClassLoader dirsClassLoader = new URLClassLoader(urlsArray, getEffectiveClassLoader());
        setEffectiveClassLoader(dirsClassLoader);

        for (String directory : directories)
        {
            LOG.info("Scanning directory : " + directory);
            File directoryFile = new File(directory);
            scanDirectory(catalog, directoryFile);
        }
    }

}