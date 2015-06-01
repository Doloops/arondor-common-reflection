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
package com.arondor.common.reflection.sample.gwt.server;

import java.io.IOException;
import java.util.Collection;

import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.noreflect.gwtgenerator.GWTNoReflectRegistrarGenerator;
import com.arondor.common.reflection.noreflect.model.AsyncPackages;
import com.arondor.common.reflection.parser.java.AccessibleClassListParser;

public class SampleCodeGenerator extends GWTNoReflectRegistrarGenerator
{
    @Override
    public String getPackageName()
    {
        return "com.arondor.viewer.client.defferedmodules";
    }

    @Override
    public String getClassName()
    {
        return "SampleGWYNoReflectGenerator";
    }

    private Collection<com.arondor.common.reflection.model.java.AccessibleClass> getAccessibleClasses()
    {
        AccessibleClassListParser accessibleClassListParser = new AccessibleClassListParser();
        accessibleClassListParser.getAccesssibleClassParser().setTryInstantiateClassForDefaultValue(false);
        try
        {
            Collection<String> classNames = accessibleClassListParser.parseClassNames(this.getClass().getClassLoader()
                    .getResourceAsStream("exposedClasses.txt"));
            return accessibleClassListParser.parseAccessibleClasses(classNames);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Could not parse", e);
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalArgumentException("Could not parse", e);
        }
    }

    @Override
    public Collection<AccessibleClass> getSyncClasses()
    {
        return getAccessibleClasses();
    }

    @Override
    public AsyncPackages getAsyncPackages()
    {
        return new AsyncPackages();
    }
}