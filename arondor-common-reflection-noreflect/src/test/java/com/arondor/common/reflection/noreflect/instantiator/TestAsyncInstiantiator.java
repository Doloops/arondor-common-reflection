package com.arondor.common.reflection.noreflect.instantiator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.api.instantiator.InstantiationCallback;
import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiator;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiatorAsync;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.noreflect.generator.NoReflectRegistrarGenerator;
import com.arondor.common.reflection.noreflect.model.AsyncPackages;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorRegistrar;
import com.arondor.common.reflection.noreflect.runtime.SimpleReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.testclasses.async.TestClass1;
import com.arondor.common.reflection.noreflect.testclasses.async.submodule0.TestModule1Class1;
import com.arondor.common.reflection.noreflect.testclasses.async.submodule1.TestModule2Class1;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.thirdparty.guava.common.collect.Lists;

public class TestAsyncInstiantiator
{
    protected InstantiationContext instantationContext;

    protected ReflectionInstantiator instantiator;

    protected ReflectionInstantiatorAsync asyncInstantiator;

    protected AccessibleClassParser parser;

    protected ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

    private final static Logger LOG = Logger.getLogger(TestNoReflectRegistrarGenerator.class);

    public static void junitRunAsync(final RunAsyncCallback callback)
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    LOG.error("Could not wait", e);
                }
                callback.onSuccess();
            }
        }.start();

    }

    private void doGenerate() throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException
    {
        parser = new JavaAccessibleClassParser();

        List<AccessibleClass> classes = new ArrayList<AccessibleClass>();

        classes.add(parser.parseAccessibleClass(TestClass1.class));
        AccessibleClass acModule1Class1 = parser.parseAccessibleClass(TestModule1Class1.class);
        // classes.add(acModule1Class1);
        AccessibleClass acModule2Class1 = parser.parseAccessibleClass(TestModule2Class1.class);
        // classes.add(acModule2Class1);

        AsyncPackages asyncPackages = new AsyncPackages();
        asyncPackages.put("module1", Lists.newArrayList(acModule1Class1));
        asyncPackages.put("module2", Lists.newArrayList(acModule2Class1));

        NoReflectRegistrarGenerator gen = new NoReflectRegistrarGenerator();

        gen.setAccessibleClassParser(parser);
        gen.setGwtRunAsyncMethod(TestAsyncInstiantiator.class.getName() + ".junitRunAsync");

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

        this.instantiator = noReflect;
        this.asyncInstantiator = (ReflectionInstantiatorAsync) this.instantiator;
        this.instantationContext = noReflect.createDefaultInstantiationContext();
    }

    private Class<?> runtimeCompileAndLoadClass(String path, String completeClassName) throws ClassNotFoundException
    {
        @SuppressWarnings("restriction")
        javax.tools.JavaCompiler javaCompiler = javax.tools.ToolProvider.getSystemJavaCompiler();
        @SuppressWarnings("restriction")
        int result = javaCompiler.run(null, null, null, "-d", "target/classes/", path);
        LOG.info("Compile result : " + result);
        @SuppressWarnings("restriction")
        ClassLoader classLoader = javax.tools.ToolProvider.getSystemToolClassLoader();

        Class<?> clazz = classLoader.loadClass(completeClassName);
        LOG.info("Loaded class : " + clazz.getName());
        return clazz;
    }

    @Before
    public void setup() throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException
    {
        doGenerate();
    }

    @Test(timeout = 1000)
    public void testSimpleDependency() throws InterruptedException
    {
        ObjectConfiguration root = objectConfigurationFactory.createObjectConfiguration();
        root.setObjectName("Root");
        root.setClassName(TestClass1.class.getName());

        root.setFields(new HashMap<String, ElementConfiguration>());

        ObjectConfiguration child1 = objectConfigurationFactory.createObjectConfiguration();
        child1.setClassName(TestModule1Class1.class.getName());
        root.getFields().put("testModule1Class1", child1);

        final CountDownLatch latch = new CountDownLatch(1);

        asyncInstantiator.instanciateObject(root, Object.class, instantationContext,
                new InstantiationCallback<Object>()
                {
                    @Override
                    public void onFailure(Throwable caught)
                    {
                    }

                    @Override
                    public void onSuccess(Object result)
                    {
                        assertNotNull(result);
                        assertTrue(result instanceof TestClass1);
                        TestClass1 testClass1 = (TestClass1) result;
                        assertNotNull(testClass1.getTestModule1Class1());

                        latch.countDown();
                    }
                });

        latch.await();
    }

    @Test(timeout = 1000)
    public void testDoubleDependency() throws InterruptedException
    {
        ObjectConfiguration root = objectConfigurationFactory.createObjectConfiguration();
        root.setObjectName("Root");
        root.setClassName(TestClass1.class.getName());

        root.setFields(new HashMap<String, ElementConfiguration>());

        ObjectConfiguration child1 = objectConfigurationFactory.createObjectConfiguration();
        child1.setClassName(TestModule1Class1.class.getName());
        root.getFields().put("testModule1Class1", child1);
        child1.setFields(new HashMap<String, ElementConfiguration>());

        ObjectConfiguration grandChild1 = objectConfigurationFactory.createObjectConfiguration();
        grandChild1.setClassName(TestModule2Class1.class.getName());
        child1.getFields().put("testModule2Class1", grandChild1);

        final CountDownLatch latch = new CountDownLatch(1);

        asyncInstantiator.instanciateObject(root, Object.class, instantationContext,
                new InstantiationCallback<Object>()
                {
                    @Override
                    public void onFailure(Throwable caught)
                    {
                    }

                    @Override
                    public void onSuccess(Object result)
                    {
                        assertNotNull(result);
                        assertTrue(result instanceof TestClass1);
                        TestClass1 testClass1 = (TestClass1) result;
                        assertNotNull(testClass1.getTestModule1Class1());

                        TestModule1Class1 testModule1Class1 = testClass1.getTestModule1Class1();

                        assertNotNull(testModule1Class1.getTestModule2Class1());
                        latch.countDown();
                    }
                });

        latch.await();
    }

}
