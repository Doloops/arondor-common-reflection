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
import java.util.List;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;

public class JarAccessibleClassProvider extends AbstractJavaAccessibleClassProvider
{
    private static final Logger LOG = Logger.getLogger(JarAccessibleClassProvider.class);

    private List<String> jars;

    public JarAccessibleClassProvider()
    {

    }

    public void provideClasses(AccessibleClassCatalog catalog)
    {
        for (String jar : jars)
        {
            LOG.debug("Scanning jar : " + jar);
            File jarFile = new File(jar);
            scanJar(catalog, jarFile);
        }
    }

    public List<String> getJars()
    {
        return jars;
    }

    public void setJars(List<String> jars)
    {
        this.jars = jars;
    }

}