package com.arondor.common.reflection.noreflect.gwtgenerator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.logging.Logger;

import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.noreflect.generator.NoReflectRegistrarGenerator;
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

        // try
        // {
        PrintWriter src = context.tryCreate(logger, packageName, className);

        if (src == null)
        {
            return completeName;
        }

        // String resource = "exposedClasses.txt";
        // AccessibleClassesParser accessibleClassesParser = new
        // AccessibleClassesParser();
        // Collection<String> classNames =
        // accessibleClassesParser.parseClassNames(this.getClass().getClassLoader()
        // .getResourceAsStream(resource));
        //
        // LOGGER.info("Parsed classNames : " + classNames.size());

        // Collection<AccessibleClass> accessibleClasses =
        // accessibleClassesParser.parseAccessibleClasses(this
        // .getClass().getClassLoader(), classNames);

        Collection<AccessibleClass> accessibleClasses = getAccessibleClasses();

        NoReflectRegistrarGenerator noReflect = new NoReflectRegistrarGenerator();
        noReflect.setClassName(className);
        noReflect.setPackageName(packageName);
        // noReflect.setHashMethod(HASH_METHOD);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        noReflect.generate(printStream, accessibleClasses);

        printStream.close();

        String srcCode = new String(baos.toByteArray());

        LOGGER.info("Generated : " + srcCode.length() + " bytes for " + completeName);

        src.append(srcCode);

        context.commit(logger, src);

        return completeName;

        // }
        // catch (IOException e)
        // {
        // LOGGER.log(Level.SEVERE, "IOException", e);
        // }
        // catch (ClassNotFoundException e)
        // {
        // LOGGER.log(Level.SEVERE, "ClassNotFoundException", e);
        // }
        // return null;
    }

}
