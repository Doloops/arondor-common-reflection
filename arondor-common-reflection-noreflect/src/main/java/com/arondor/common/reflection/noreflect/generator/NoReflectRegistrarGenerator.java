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
package com.arondor.common.reflection.noreflect.generator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.api.instantiator.InstantiationCallback;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleConstructor;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.noreflect.model.AsyncPackages;
import com.arondor.common.reflection.noreflect.model.FieldSetter;
import com.arondor.common.reflection.noreflect.model.ObjectConstructor;
import com.arondor.common.reflection.noreflect.model.ObjectConstructorAsync;
import com.arondor.common.reflection.noreflect.model.PackageInstantiatorAsync;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorRegistrar;
import com.arondor.common.reflection.noreflect.runtime.PrimitiveStringConverter;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

public class NoReflectRegistrarGenerator
{
    private static final String PACKAGE_KEYWORD = "package ";

    private static final String IMPORT_KEYWORD = "import ";

    private final static Logger LOG = Logger.getLogger(NoReflectRegistrarGenerator.class);

    private String packageName;

    private String className;

    private boolean generateAsyncObjectConstructors = false;

    private String gwtRunAsyncMethod = "GWT.runAsync";

    private AccessibleClassParser accessibleClassParser;

    public AccessibleClassParser getAccessibleClassParser()
    {
        return accessibleClassParser;
    }

    public void setAccessibleClassParser(AccessibleClassParser accessibleClassParser)
    {
        this.accessibleClassParser = accessibleClassParser;
    }

    public NoReflectRegistrarGenerator()
    {
    }

    public void generate(PrintStream out, Collection<AccessibleClass> syncClasses, AsyncPackages asyncPackages)
    {
        out.println(PACKAGE_KEYWORD + getPackageName() + ";");
        out.println("");
        out.println(IMPORT_KEYWORD + ReflectionInstantiatorCatalog.class.getName() + ";");
        out.println(IMPORT_KEYWORD + ObjectConstructor.class.getName() + ";");
        out.println(IMPORT_KEYWORD + ObjectConstructorAsync.class.getName() + ";");
        out.println(IMPORT_KEYWORD + PackageInstantiatorAsync.class.getName() + ";");
        out.println(IMPORT_KEYWORD + InstantiationCallback.class.getName() + ";");
        out.println(IMPORT_KEYWORD + RunAsyncCallback.class.getName() + ";");
        out.println(IMPORT_KEYWORD + GWT.class.getName() + ";");
        out.println(IMPORT_KEYWORD + FieldSetter.class.getName() + ";");
        out.println(IMPORT_KEYWORD + PrimitiveStringConverter.class.getName() + ";");
        out.println(IMPORT_KEYWORD + ReflectionInstantiatorRegistrar.class.getName() + ";");
        out.println(IMPORT_KEYWORD + List.class.getName() + ";");
        out.println(IMPORT_KEYWORD + ArrayList.class.getName() + ";");

        out.println("public class " + getClassName() + " implements ReflectionInstantiatorRegistrar");
        out.println("{");

        out.println("    public " + getClassName() + "() {}");

        out.println("    public void register(final ReflectionInstantiatorCatalog catalog)");
        out.println("    {");
        for (AccessibleClass accessibleClass : syncClasses)
        {
            generateClassMethodCall(out, accessibleClass);
        }
        for (Map.Entry<String, List<AccessibleClass>> asyncEntry : asyncPackages.entrySet())
        {
            generateAsyncPackageCall(out, asyncEntry.getKey(), asyncEntry.getValue());
        }
        out.println("    }");

        out.println("    // Sync classes");
        for (AccessibleClass accessibleClass : syncClasses)
        {
            generateClassMethod(out, accessibleClass);
        }

        out.println("    // ASync Packages");
        for (String packageName : asyncPackages.keySet())
        {
            generateAsyncPackageInstantiator(out, packageName);
        }

        out.println("    // ASync classes");
        for (Map.Entry<String, List<AccessibleClass>> asyncEntry : asyncPackages.entrySet())
        {
            generateAsyncPackageMethod(out, asyncEntry.getKey(), asyncEntry.getValue());
        }

        generateStaticLabels(out);

        out.println("}");
    }

    private final boolean useStaticLabels = true;

    private final Map<String, String> staticLabels = new HashMap<String, String>();

    private final String labelPrefix = "__label";

    private String generateLabel(String labelName)
    {
        if (useStaticLabels)
        {
            String labelShortName;
            if (staticLabels.containsKey(labelName))
            {
                labelShortName = staticLabels.get(labelName);
            }
            else
            {
                labelShortName = labelPrefix + (staticLabels.size() + 1) + "_" + protectSymbolName(labelName);
                staticLabels.put(labelName, labelShortName);
            }
            return labelShortName;
        }
        else
        {
            return "\"" + labelName + "\"";
        }
    }

    private void generateStaticLabels(PrintStream out)
    {
        if (useStaticLabels)
        {
            out.println("    // Static Labels");
            for (Map.Entry<String, String> entry : staticLabels.entrySet())
            {
                out.println("    private static final String " + entry.getValue() + " = \"" + entry.getKey() + "\";");
            }
        }
    }

    private String protectSymbolName(String className)
    {
        return className.replace('.', '_').replace('$', '_');
    }

    private String generateClassUniqueName(AccessibleClass accessibleClass)
    {
        return protectSymbolName(accessibleClass.getName());
    }

    private void generateClassMethodCall(PrintStream out, AccessibleClass accessibleClass)
    {
        String classUniqueName = generateClassUniqueName(accessibleClass);
        out.println("        register_sync_" + classUniqueName + "(catalog);");
    }

    private void generateAsyncPackageCall(PrintStream out, String packageName, List<AccessibleClass> classes)
    {
        out.println("        register_async_package_instantiator_" + packageName + "(catalog);");
        for (AccessibleClass asyncClass : classes)
        {
            out.println("        register_async_" + generateClassUniqueName(asyncClass) + "(catalog);");
        }
    }

    private void generateAsyncPackageInstantiator(PrintStream out, String packageName)
    {
        out.println("    private final void register_async_package_instantiator_" + packageName
                + "(final ReflectionInstantiatorCatalog catalog)");
        out.println("    {");
        out.println("        catalog.registerPackageInstantiator(" + generateLabel(packageName)
                + ", new PackageInstantiatorAsync()");
        out.println("        {");
        out.println("            public void instantiatePackage(final InstantiationCallback<Void> callback)");
        out.println("            {");
        out.println("                register_async_package_" + packageName + "(catalog, callback);");
        out.println("            }");
        out.println("        });");
        out.println("    }");
    }

    private void generateAsyncPackageMethod(PrintStream out, String packageName, List<AccessibleClass> classes)
    {
        out.println("    // Async package : " + packageName);
        out.println("    private boolean async_package_" + packageName + "_inited=false;");
        out.println("    private final void register_async_package_" + packageName
                + "(final ReflectionInstantiatorCatalog catalog, final InstantiationCallback<Void> callback)");
        out.println("    {");
        out.println("        if ( async_package_" + packageName + "_inited )");
        out.println("        { callback.onSuccess(null); return; }");
        out.println("        " + getGwtRunAsyncMethod() + "(new RunAsyncCallback()");
        out.println("        {");
        out.println("            public void onSuccess()");
        out.println("            {");
        for (AccessibleClass asyncClass : classes)
        {
            generateClassMethodCall(out, asyncClass);
        }
        out.println("                callback.onSuccess(null);");
        out.println("                async_package_" + packageName + "_inited=true;");
        out.println("            }");
        out.println("            public void onFailure(Throwable reason)");
        out.println("            {");
        out.println("                callback.onFailure(reason);");
        out.println("            }");
        out.println("        });");
        out.println("    }");

        for (AccessibleClass asyncClass : classes)
        {
            generateClassMethod(out, asyncClass);
            generateClassAsyncMethod(out, packageName, asyncClass);
        }
    }

    private void generateClassAsyncMethod(PrintStream out, String packageName, AccessibleClass asyncClass)
    {
        String classUniqueName = generateClassUniqueName(asyncClass);
        out.println("    // Async Class " + asyncClass.getName());
        out.println("    private final void register_async_" + classUniqueName
                + "(final ReflectionInstantiatorCatalog catalog)");
        out.println("    {");
        out.println("        catalog.registerClassInPackage(" + generateLabel(packageName) + ", "
                + generateLabel(asyncClass.getName()) + ");");

        if (isGenerateAsyncObjectConstructors())
        {
            out.println("        catalog.registerObjectConstructor(" + generateLabel(asyncClass.getName())
                    + ", new ObjectConstructorAsync(){");
            out.println("            public void getObjectConstructor(final InstantiationCallback<ObjectConstructor> callback)");
            out.println("            {");
            out.println("                register_async_package_" + packageName
                    + "(catalog, new InstantiationCallback<Void>(){");

            out.println("                    public void onSuccess(Void __void)");
            out.println("                    {");
            out.println("                       ObjectConstructor constructor = catalog.getObjectConstructor("
                    + generateLabel(asyncClass.getName()) + ");");
            out.println("                       callback.onSuccess(constructor);");
            out.println("                    }");
            out.println("                    public void onFailure(Throwable reason)");
            out.println("                    {");
            out.println("                       callback.onFailure(reason);");
            out.println("                    }");

            out.println("                });");
            out.println("            }");
            out.println("        });");
        }
        out.println("    }");
    }

    private void generateClassMethod(PrintStream out, AccessibleClass accessibleClass)
    {
        String classUniqueName = generateClassUniqueName(accessibleClass);
        out.println("    // Class " + accessibleClass.getName());
        out.println("    private final ObjectConstructor register_sync_" + classUniqueName
                + "(ReflectionInstantiatorCatalog catalog)");
        out.println("    {");
        generateClassContents(out, accessibleClass);
        out.println("    }");
    }

    private String list2StaticStringArray(List<String> stringList)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        boolean first = true;
        for (String stringValue : stringList)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                stringBuilder.append(", ");
            }
            stringBuilder.append(generateLabel(stringValue));
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    private void generateClassContents(PrintStream out, AccessibleClass accessibleClass)
    {
        LOG.debug("Generating stub for " + accessibleClass.getName());

        List<String> inheritance = new ArrayList<String>();
        for (String inherits : accessibleClass.getInterfaces())
        {
            if (!inherits.equals(Object.class.getName()))
            {
                inheritance.add(inherits);
            }
        }
        if (accessibleClass.getSuperclass() != null)
        {
            if (!accessibleClass.getSuperclass().equals(Object.class.getName()))
            {
                inheritance.add(accessibleClass.getSuperclass());
            }
        }
        Collections.sort(inheritance);

        if (!inheritance.isEmpty())
        {
            out.println("        String inheritance[] = " + list2StaticStringArray(inheritance) + ";");
            out.println("        catalog.registerObjectInheritance(" + generateLabel(accessibleClass.getName())
                    + ", inheritance);");
        }

        if (!accessibleClass.isAbstract())
        {
            out.println("        ObjectConstructor objectConstructor = new ObjectConstructor(){");
            out.println("                public Object create(List<Object> constructorArguments){");
            generateClassConstructor(out, accessibleClass);
            out.println("            }");
            out.println("            };");
            out.println("        catalog.registerObjectConstructor(" + generateLabel(accessibleClass.getName())
                    + ", objectConstructor);");
        }
        else
        {
            out.println("        ObjectConstructor objectConstructor = null;");
        }

        for (AccessibleField field : accessibleClass.getAccessibleFields().values())
        {
            generateField(out, accessibleClass, field);
        }
        out.println("        return objectConstructor;");
    }

    private void generateClassConstructor(PrintStream out, AccessibleClass accessibleClass)
    {
        if (accessibleClass.getSuperclass().equals("java.lang.Enum"))
        {
            out.println("/* Enum Case */");
            out.println("if ( constructorArguments == null || constructorArguments.size() !=  1 )");
            out.println("{ throw new RuntimeException(\"Invalid constructor arguments for class '"
                    + accessibleClass.getName() + "' : \" + constructorArguments); }");

            out.println("String value = (String) constructorArguments.get(0);");
            out.println("return " + normalizeClassName(accessibleClass.getName()) + ".valueOf(value);");
            return;
        }
        for (AccessibleConstructor constructor : accessibleClass.getConstructors())
        {
            out.println("                    if ( constructorArguments.size() =="
                    + constructor.getArgumentTypes().size() + ")");
            out.print("                    {return new " + normalizeClassName(accessibleClass.getName()) + "(");

            for (int argumentIndex = 0; argumentIndex < constructor.getArgumentTypes().size(); argumentIndex++)
            {
                if (argumentIndex > 0)
                {
                    out.print(",");
                }
                String argumentClass = constructor.getArgumentTypes().get(argumentIndex);
                generateCast(out, argumentClass, "constructorArguments.get(" + argumentIndex + ")");
            }

            out.println(");}");
        }
        out.println("                    throw new IllegalArgumentException(\"Invalid constructor arguments for class '"
                + accessibleClass.getName() + "' : \" + constructorArguments);");
    }

    private void generateCast(PrintStream out, String className, String valueLabel)
    {
        boolean primitive = PrimitiveTypeUtil.isPrimitiveType(className);
        if (primitive)
        {
            String primitiveMethod = PrimitiveStringConverter.getConvertionMethodFromClassName(className);
            out.print("PrimitiveStringConverter." + primitiveMethod + "((String)" + valueLabel + ")");
        }
        else
        {
            out.print("(" + normalizeClassName(className) + ") " + valueLabel);
        }

    }

    private void generateField(PrintStream out, AccessibleClass accessibleClass, AccessibleField field)
    {
        if (!field.getWritable())
        {
            LOG.debug("Skipping field " + field.getName() + " in class " + accessibleClass.getName() + ", not writable");
            return;
        }
        if (field.getDeclaredInClass() != null && !field.getDeclaredInClass().equals(accessibleClass.getName()))
        {
            LOG.debug("Skipping field " + field.getName() + " from class " + accessibleClass.getName()
                    + " because it is said to be declared in " + field.getDeclaredInClass());
            return;
        }
        String setterName = getAccessibleClassParser().attributeToSetter(field.getName());

        out.println("        catalog.registerFieldSetter(" + generateLabel(accessibleClass.getName()) + ","
                + generateLabel(field.getName()) + ",");
        out.println("             new FieldSetter() {");
        out.println("                 public void set(Object object, Object value) {");
        out.print("                          ((" + normalizeClassName(accessibleClass.getName()) + ")object)."
                + setterName + "(");

        generateCast(out, field.getClassName(), "value");

        out.println(");");
        out.println("                 }");
        out.println("             });");
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    private String normalizeClassName(final String className)
    {
        return className.replace('$', '.');
    }

    public String getGwtRunAsyncMethod()
    {
        return gwtRunAsyncMethod;
    }

    public void setGwtRunAsyncMethod(String gwtRunAsyncMethod)
    {
        this.gwtRunAsyncMethod = gwtRunAsyncMethod;
    }

    public boolean isGenerateAsyncObjectConstructors()
    {
        return generateAsyncObjectConstructors;
    }

    public void setGenerateAsyncObjectConstructors(boolean generateAsyncObjectConstructors)
    {
        this.generateAsyncObjectConstructors = generateAsyncObjectConstructors;
    }

}
