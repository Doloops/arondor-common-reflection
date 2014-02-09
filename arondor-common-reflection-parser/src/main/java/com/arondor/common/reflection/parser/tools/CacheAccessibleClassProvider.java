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
package com.arondor.common.reflection.parser.tools;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.api.parser.AccessibleClassProvider;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.parser.java.JarAccessibleClassProvider;

public class CacheAccessibleClassProvider implements AccessibleClassProvider
{
    private static final Logger LOG = Logger.getLogger(JarAccessibleClassProvider.class);

    private String cacheFile;

    private AccessibleClassProvider provider;

    public void provideClasses(AccessibleClassCatalog catalog)
    {
        if (readCache(catalog))
        {
            LOG.info("Successfully read classes from cache");
            return;
        }
        provider.provideClasses(catalog);

        writeCache(catalog);
    }

    private boolean readCache(AccessibleClassCatalog catalog)
    {
        File cache = new File(getCacheFile());
        if (cache.exists())
        {
            ObjectInputStream ois = null;
            try
            {
                ois = new ObjectInputStream(new FileInputStream(cache));
                while (true)
                {
                    try
                    {
                        AccessibleClass accessibleClass = (AccessibleClass) ois.readObject();
                        catalog.addAccessibleClass(accessibleClass);
                    }
                    catch (EOFException e)
                    {
                        break;
                    }
                }
                return true;
            }
            catch (IOException e)
            {
                LOG.error("Could not get file from cache " + cache.getAbsolutePath(), e);
            }
            catch (ClassNotFoundException e)
            {
                LOG.error("Could not get file from cache " + cache.getAbsolutePath(), e);
            }
            finally
            {
                if (ois != null)
                {
                    try
                    {
                        ois.close();
                    }
                    catch (IOException e)
                    {
                        LOG.error("Could not close", e);
                    }
                }
            }
        }
        return false;
    }

    private void writeCache(AccessibleClassCatalog catalog)
    {
        File cache = new File(getCacheFile());

        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(new FileOutputStream(cache));
            Collection<AccessibleClass> classes = catalog.getImplementingAccessibleClasses(java.lang.Object.class
                    .getName());
            for (AccessibleClass clazz : classes)
            {
                oos.writeObject(clazz);
            }
        }
        catch (IOException e)
        {
            LOG.error("Could not get file from cache " + cache.getAbsolutePath(), e);
        }
        finally
        {
            if (oos != null)
            {
                try
                {
                    oos.close();
                }
                catch (IOException e)
                {
                    LOG.error("Could not close");
                }
            }
        }
    }

    public AccessibleClassProvider getProvider()
    {
        return provider;
    }

    public void setProvider(AccessibleClassProvider provider)
    {
        this.provider = provider;
    }

    public String getCacheFile()
    {
        return cacheFile;
    }

    public void setCacheFile(String cacheFile)
    {
        this.cacheFile = cacheFile;
    }

}
