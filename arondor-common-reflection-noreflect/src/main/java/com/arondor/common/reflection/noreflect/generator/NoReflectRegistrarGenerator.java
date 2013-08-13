package com.arondor.common.reflection.noreflect.generator;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleConstructor;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.noreflect.model.FieldSetter;
import com.arondor.common.reflection.noreflect.model.ObjectConstructor;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorRegistrar;
import com.arondor.common.reflection.noreflect.runtime.PrimitiveStringConverter;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;

public class NoReflectRegistrarGenerator
{
    private final static Logger LOG = Logger.getLogger(NoReflectRegistrarGenerator.class);

    private String packageName;

    private String className;

    private AccessibleClassParser accessibleClassParser = new JavaAccessibleClassParser();

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

    public void generate(PrintStream out, Collection<AccessibleClass> accessibleClasses)
    {
        out.println("package " + getPackageName() + ";");
        out.println("");
        out.println("import " + ReflectionInstantiatorCatalog.class.getName() + ";");
        out.println("import " + ObjectConstructor.class.getName() + ";");
        out.println("import " + FieldSetter.class.getName() + ";");
        out.println("import " + PrimitiveStringConverter.class.getName() + ";");
        out.println("import " + ReflectionInstantiatorRegistrar.class.getName() + ";");
        out.println("import " + List.class.getName() + ";");

        out.println("public class " + getClassName() + " implements ReflectionInstantiatorRegistrar");
        out.println("{");

        out.println("    public " + getClassName() + "() {}");

        out.println("    public void register(ReflectionInstantiatorCatalog catalog)");
        out.println("    {");
        for (AccessibleClass accessibleClass : accessibleClasses)
        {
            generateClass(out, accessibleClass);
        }
        out.println("    }");
        out.println("}");
    }

    private void generateClass(PrintStream out, AccessibleClass accessibleClass)
    {

        LOG.info("Generating stub for " + accessibleClass.getName());
        out.println("        catalog.registerObjectConstructor(\"" + accessibleClass.getName() + "\",");
        out.println("            new ObjectConstructor(){");
        out.println("                public Object create(List<Object> constructorArguments){");

        generateClassConstructor(out, accessibleClass);
        out.println("            }");
        out.println("            });");

        for (AccessibleField field : accessibleClass.getAccessibleFields().values())
        {
            generateField(out, accessibleClass, field);
        }
    }

    private void generateClassConstructor(PrintStream out, AccessibleClass accessibleClass)
    {
        if (accessibleClass.getSuperclass().equals("java.lang.Enum"))
        {
            out.println("/* Enum Case */");
            out.println("if ( constructorArguments == null || constructorArguments.size() !=  1 )");
            out.println("{ throw new RuntimeException(\"Invalid constructor arguments : \" + constructorArguments); }");

            out.println("String value = (String) constructorArguments.get(0);");
            out.println("return " + accessibleClass.getName() + ".valueOf(value);");
            return;
        }
        for (AccessibleConstructor constructor : accessibleClass.getConstructors())
        {
            out.println("if ( constructorArguments.size() ==" + constructor.getArgumentTypes().size() + ")");
            out.println("{return new " + accessibleClass.getName() + "(");

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
        out.println("throw new RuntimeException(\"Invalid constructor arguments : \" + constructorArguments);");
    }

    private void generateCast(PrintStream out, String className, String valueLabel)
    {
        boolean primitive = accessibleClassParser.isPrimitiveType(className);

        if (primitive)
        {
            String primitiveMethod = PrimitiveStringConverter.getConvertionMethodFromClassName(className);
            out.println("  PrimitiveStringConverter." + primitiveMethod + "((String)" + valueLabel + ")");
        }
        else
        {
            out.println("(" + className + ") " + valueLabel);
        }

    }

    private void generateField(PrintStream out, AccessibleClass accessibleClass, AccessibleField field)
    {
        if (!field.getWritable())
        {
            LOG.debug("Skipping field " + field.getName() + ", not writable");
            return;
        }
        String setterName = accessibleClassParser.attributeToSetter(field.getName());

        out.println("        catalog.registerFieldSetter(\"" + accessibleClass.getName() + "\",\"" + field.getName()
                + "\",");
        out.println("             new FieldSetter() {");
        out.println("                          public void set(Object object, Object value) {");
        out.println("                          ((" + accessibleClass.getName() + ")object)." + setterName + "(");

        generateCast(out, field.getClassName(), "value");

        out.println(");");
        out.println("                          ");
        out.println("                          }");
        out.println("            });");
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

}
