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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.arondor.common.reflection.model.java.AccessibleClass;

public class AccessibleClassListParser
{
    private static final Logger LOGGER = Logger.getLogger(AccessibleClassListParser.class.getName());

    private JavaAccessibleClassParser accesssibleClassParser = new JavaAccessibleClassParser();

    public JavaAccessibleClassParser getAccesssibleClassParser()
    {
        return accesssibleClassParser;
    }

    public void setAccesssibleClassParser(JavaAccessibleClassParser accesssibleClassParser)
    {
        this.accesssibleClassParser = accesssibleClassParser;
    }

    private static final Logger getLog()
    {
        return LOGGER;
    }

    public AccessibleClassListParser()
    {

    }

    public Collection<AccessibleClass> parseAccessibleClasses(Collection<String> classNames)
            throws ClassNotFoundException
    {
        return parseAccessibleClasses(this.getClass().getClassLoader(), classNames);
    }

    public Collection<AccessibleClass> parseAccessibleClasses(ClassLoader classLoader, Collection<String> classNames)
            throws ClassNotFoundException
    {
        List<AccessibleClass> accessibleClasses = new ArrayList<AccessibleClass>();

        for (String className : classNames)
        {
            getLog().finest("Parsing AccessibleClass for class : " + className);
            Class<?> srcClazz = classLoader.loadClass(className);
            AccessibleClass clazz = accesssibleClassParser.parseAccessibleClass(srcClazz);
            accessibleClasses.add(clazz);
        }
        return accessibleClasses;

    }

    public Collection<String> parseClassNames(InputStream inputStream) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        List<String> classNames = new ArrayList<String>();
        try
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String className = line.trim();
                if (className.startsWith("#") || className.isEmpty())
                {
                    continue;
                }
                getLog().finest("Class to process : " + className);
                classNames.add(className);
            }
        }
        finally
        {
            br.close();
        }
        return classNames;
    }

}
