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
package com.arondor.common.reflection.noreflect.gwtgenerator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.logging.Logger;

import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.noreflect.generator.NoReflectRegistrarGenerator;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public abstract class GWTNoReflectRegistrarGenerator extends Generator
{
    private static final Logger LOGGER = Logger.getLogger(GWTNoReflectRegistrarGenerator.class.getName());

    public abstract String getPackageName();

    public abstract String getClassName();

    public abstract Collection<AccessibleClass> getAccessibleClasses();

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName)
            throws UnableToCompleteException
    {
        LOGGER.finest("Generating for: " + typeName);

        String packageName = getPackageName();
        String className = getClassName();
        String completeName = packageName + "." + className;

        PrintWriter src = context.tryCreate(logger, packageName, className);

        if (src == null)
        {
            LOGGER.finest("Already generated for : " + completeName);
            return completeName;
        }

        Collection<AccessibleClass> accessibleClasses = getAccessibleClasses();

        NoReflectRegistrarGenerator noReflect = new NoReflectRegistrarGenerator();
        noReflect.setClassName(className);
        noReflect.setPackageName(packageName);

        JavaAccessibleClassParser accessibleClassParser = new JavaAccessibleClassParser();
        accessibleClassParser.setTryInstantiateClassForDefaultValue(false);
        noReflect.setAccessibleClassParser(accessibleClassParser);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        noReflect.generate(printStream, accessibleClasses);

        printStream.close();

        String srcCode = new String(baos.toByteArray());

        LOGGER.info("Generated : " + srcCode.length() + " bytes for " + completeName);

        src.append(srcCode);

        context.commit(logger, src);

        return completeName;
    }

}
