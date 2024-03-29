package com.arondor.common.reflection.noreflect.instantiator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.log4j.Logger;
import org.junit.Before;

import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.noreflect.generator.NoReflectRegistrarGenerator;
import com.arondor.common.reflection.noreflect.model.AsyncPackages;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorRegistrar;
import com.arondor.common.reflection.noreflect.runtime.SimpleReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.testclasses.TestAbstractParent;
import com.arondor.common.reflection.noreflect.testclasses.TestChildClass;
import com.arondor.common.reflection.noreflect.testclasses.TestChildWithAbstractParent;
import com.arondor.common.reflection.noreflect.testclasses.TestClassA;
import com.arondor.common.reflection.noreflect.testclasses.TestClassB;
import com.arondor.common.reflection.noreflect.testclasses.TestClassC;
import com.arondor.common.reflection.noreflect.testclasses.TestClassD;
import com.arondor.common.reflection.noreflect.testclasses.TestClassE;
import com.arondor.common.reflection.noreflect.testclasses.TestGrandChildClass;
import com.arondor.common.reflection.noreflect.testclasses.TestNestedClass;
import com.arondor.common.reflection.noreflect.testclasses.TestParentClass;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;

public class TestNoReflectRegistrarGenerator extends TestNoReflectSharedTests
{
    private final static Logger LOG = Logger.getLogger(TestNoReflectRegistrarGenerator.class);

    private void doGenerate() throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException, MalformedURLException
    {
        parser = new JavaAccessibleClassParser();

        List<AccessibleClass> classes = new ArrayList<AccessibleClass>();

        classes.add(parser.parseAccessibleClass(TestClassA.class));
        classes.add(parser.parseAccessibleClass(TestClassB.class));
        classes.add(parser.parseAccessibleClass(TestClassC.class));
        classes.add(parser.parseAccessibleClass(TestClassC.EnumValue.class));
        classes.add(parser.parseAccessibleClass(TestClassD.class));
        classes.add(parser.parseAccessibleClass(TestClassE.class));
        classes.add(parser.parseAccessibleClass(TestParentClass.class));
        classes.add(parser.parseAccessibleClass(TestChildClass.class));
        classes.add(parser.parseAccessibleClass(TestGrandChildClass.class));
        classes.add(parser.parseAccessibleClass(TestAbstractParent.class));
        classes.add(parser.parseAccessibleClass(TestChildWithAbstractParent.class));
        classes.add(parser.parseAccessibleClass(TestNestedClass.class));
        classes.add(parser.parseAccessibleClass(TestNestedClass.EmbeddedClass.class));

        AsyncPackages asyncPackages = new AsyncPackages();

        NoReflectRegistrarGenerator gen = new NoReflectRegistrarGenerator();

        gen.setAccessibleClassParser(parser);

        String packageName = "testing_";
        String className = "AutoGen" + (int) (Math.random() * 10000);
        gen.setPackageName(packageName);
        gen.setClassName(className);

        // String path = "src/test/java/testing/" + .java";
        new File("target/" + packageName).mkdir();
        String path = "target/" + packageName + "/" + className + ".java";
        String completeClassName = packageName + "." + className;

        OutputStream os = new FileOutputStream(path);
        PrintStream out = new PrintStream(os);

        gen.generate(out, classes, asyncPackages);
        out.close();

        Class<?> clazz = runtimeCompileAndLoadClass(path, completeClassName);

        ReflectionInstantiatorRegistrar registrar = (ReflectionInstantiatorRegistrar) clazz.newInstance();

        ReflectionInstantiatorCatalog catalog = new SimpleReflectionInstantiatorCatalog();

        registrar.register(catalog);

        ReflectionInstantiatorNoReflect noReflect = new ReflectionInstantiatorNoReflect();
        noReflect.setReflectionInstantiatorCatalog(catalog);

        this.reflectionInstantiator = noReflect;
        this.instantationContext = noReflect.createDefaultInstantiationContext();
    }

    private Class<?> runtimeCompileAndLoadClass(String path, String completeClassName)
            throws ClassNotFoundException, MalformedURLException
    {
        @SuppressWarnings("restriction")
        javax.tools.JavaCompiler javaCompiler = javax.tools.ToolProvider.getSystemJavaCompiler();
        @SuppressWarnings("restriction")
        int result = javaCompiler.run(null, null, null, "-Xlint:unchecked", "-d", "target/classes/", path);
        LOG.info("Compile result : " + result);

        URL[] urls = { new URL("file://target/classes/") };
        URLClassLoader urlClassLoader = new URLClassLoader(urls, getClass().getClassLoader());
        if (urlClassLoader != null)
        {
            Class<?> clazz = urlClassLoader.loadClass(completeClassName);
            LOG.info("Loaded class : " + clazz.getName());
            return clazz;
        }

        @SuppressWarnings("restriction")
        ClassLoader classLoader = javax.tools.ToolProvider.getSystemToolClassLoader();
        if (classLoader != null)
        {
            Class<?> clazz = classLoader.loadClass(completeClassName);
            LOG.info("Loaded class : " + clazz.getName());
            return clazz;
        }

        Iterator<ClassLoader> classLoaders = ServiceLoader.load(ClassLoader.class).iterator();
        while (classLoaders.hasNext())
        {
            ClassLoader cl = classLoaders.next();
            Class<?> clazz = cl.loadClass(completeClassName);
            LOG.info("Loaded class : " + clazz.getName());
            return clazz;
        }
        throw new RuntimeException("Could not load class " + completeClassName + " from path " + path);
    }

    @Before
    public void initialize() throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException, MalformedURLException
    {
        doGenerate();
    }

}
