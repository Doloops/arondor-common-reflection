package com.arondor.common.reflection.noreflect.instantiator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.api.instantiator.InstantiationCallback;
import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiator;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiatorAsync;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.bean.config.ObjectConfigurationMapBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.noreflect.generator.NoReflectRegistrarGenerator;
import com.arondor.common.reflection.noreflect.model.AsyncPackages;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorRegistrar;
import com.arondor.common.reflection.noreflect.runtime.SimpleReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.testclasses.async.TestClass1;
import com.arondor.common.reflection.noreflect.testclasses.async.submodule0.TestModule1Class1;
import com.arondor.common.reflection.noreflect.testclasses.async.submodule0.TestModule1Class2;
import com.arondor.common.reflection.noreflect.testclasses.async.submodule1.TestModule2Class1;
import com.arondor.common.reflection.noreflect.testclasses.async.submodule1.TestModule2Class2;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.thirdparty.guava.common.collect.Lists;

public class TestAsyncInstantiator
{
    private static final Logger LOG = Logger.getLogger(TestNoReflectRegistrarGenerator.class);

    protected InstantiationContext instantationContext;

    protected ReflectionInstantiator instantiator;

    protected ReflectionInstantiatorAsync asyncInstantiator;

    protected AccessibleClassParser parser;

    protected ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

    private static long RUN_ASYNC_DELAY = 0;

    public static void junitRunAsync(final RunAsyncCallback callback)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                if (RUN_ASYNC_DELAY > 0)
                {
                    try
                    {
                        Thread.sleep(RUN_ASYNC_DELAY);
                    }
                    catch (InterruptedException e)
                    {
                        LOG.error("Could not wait", e);
                    }
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

        AsyncPackages asyncPackages = new AsyncPackages();
        asyncPackages.put("module1", Lists.newArrayList(parser.parseAccessibleClass(TestModule1Class1.class),
                parser.parseAccessibleClass(TestModule1Class2.class)));
        asyncPackages.put("module2", Lists.newArrayList(parser.parseAccessibleClass(TestModule2Class1.class),
                parser.parseAccessibleClass(TestModule2Class2.class)));

        NoReflectRegistrarGenerator gen = new NoReflectRegistrarGenerator();

        gen.setAccessibleClassParser(parser);
        gen.setGwtRunAsyncMethod(TestAsyncInstantiator.class.getName() + ".junitRunAsync");

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
        javax.tools.JavaCompiler javaCompiler = javax.tools.ToolProvider.getSystemJavaCompiler();
        int result = javaCompiler.run(null, null, null, "-Xlint:unchecked", "-d", "target/classes/", path);
        LOG.info("Compile result : " + result);

        try
        {
            URL[] urls = { new URL("file://target/classes/") };
            URLClassLoader urlClassLoader = new URLClassLoader(urls, getClass().getClassLoader());
            if (urlClassLoader != null)
            {
                Class<?> clazz = urlClassLoader.loadClass(completeClassName);
                LOG.info("Loaded class : " + clazz.getName());
                return clazz;
            }
        }
        catch (RuntimeException | MalformedURLException e)
        {
            LOG.error("Caught " + e.getMessage());
        }

        ClassLoader classLoader = javax.tools.ToolProvider.getSystemToolClassLoader();

        Class<?> clazz = classLoader.loadClass(completeClassName);
        LOG.info("Loaded class : " + clazz.getName());
        return clazz;
    }

    @Before
    public void setup() throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException
    {
        // LogManager.getLogManager().getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME).setLevel(Level.FINEST);
        // LogManager.getLogManager().getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME)
        // .addHandler(new ConsoleLogHandler());
        java.util.logging.Logger.getLogger(ReflectionInstantiatorNoReflect.class.getName()).setLevel(Level.FINEST);

        long start = System.currentTimeMillis();
        doGenerate();
        long end = System.currentTimeMillis();
        LOG.info("setup() took " + (end - start) + "ms");
    }

    @Test(timeout = 1000)
    public void testAsyncSingleton() throws InterruptedException
    {
        ObjectConfiguration root = objectConfigurationFactory.createObjectConfiguration();
        root.setObjectName("Root");
        root.setClassName(TestClass1.class.getName());
        root.setSingleton(true);

        ObjectConfigurationMap objetConfigurations = new ObjectConfigurationMapBean();
        objetConfigurations.put(root.getObjectName(), root);
        instantationContext.addSharedObjectConfigurations(objetConfigurations);

        asyncInstantiator.instanciateObject("Root", TestClass1.class, instantationContext,
                new InstantiationCallback<TestClass1>()
                {

                    @Override
                    public void onFailure(Throwable caught)
                    {
                        Assert.fail("Failed : " + caught);
                    }

                    @Override
                    public void onSuccess(final TestClass1 result1)
                    {
                        Assert.assertNotNull(result1);
                        asyncInstantiator.instanciateObject("Root", TestClass1.class, instantationContext,
                                new InstantiationCallback<TestClass1>()
                                {

                                    @Override
                                    public void onFailure(Throwable caught)
                                    {
                                        Assert.fail("Failed : " + caught);
                                    }

                                    @Override
                                    public void onSuccess(TestClass1 result2)
                                    {
                                        Assert.assertNotNull(result2);
                                        Assert.assertTrue("Diverging results !", result1 == result2);
                                    }
                                });
                    }
                });
    }

    @Test
    public void testAsyncNonSingleton() throws InterruptedException
    {
        ObjectConfiguration root = objectConfigurationFactory.createObjectConfiguration();
        root.setObjectName("Root");
        root.setClassName(TestClass1.class.getName());
        root.setSingleton(false);

        ObjectConfigurationMap objetConfigurations = new ObjectConfigurationMapBean();
        objetConfigurations.put(root.getObjectName(), root);
        instantationContext.addSharedObjectConfigurations(objetConfigurations);

        asyncInstantiator.instanciateObject("Root", TestClass1.class, instantationContext,
                new InstantiationCallback<TestClass1>()
                {

                    @Override
                    public void onFailure(Throwable caught)
                    {
                        Assert.fail("Failed : " + caught);
                    }

                    @Override
                    public void onSuccess(final TestClass1 result1)
                    {
                        asyncInstantiator.instanciateObject("Root", TestClass1.class, instantationContext,
                                new InstantiationCallback<TestClass1>()
                                {

                                    @Override
                                    public void onFailure(Throwable caught)
                                    {
                                        Assert.fail("Failed : " + caught);
                                    }

                                    @Override
                                    public void onSuccess(TestClass1 result2)
                                    {
                                        Assert.assertTrue("Equal results !", result1 != result2);
                                    }
                                });
                    }
                });
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

        asyncInstantiator.instanciateObject(root, Object.class, instantationContext, new InstantiationCallback<Object>()
        {
            @Override
            public void onFailure(Throwable caught)
            {
                Assert.fail("Failed : " + caught);
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

        asyncInstantiator.instanciateObject(root, Object.class, instantationContext, new InstantiationCallback<Object>()
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

    @Test
    // (timeout = 1000)
    public void testCyclicDependency() throws InterruptedException
    {
        ObjectConfiguration root = objectConfigurationFactory.createObjectConfiguration();
        root.setObjectName("Root");
        root.setClassName(TestClass1.class.getName());

        root.setFields(new HashMap<String, ElementConfiguration>());

        Map<String, ElementConfiguration> lastFields = root.getFields();

        final int maxCycle = 200;

        for (int cycle = 0; cycle < maxCycle; cycle++)
        {
            ObjectConfiguration child1 = objectConfigurationFactory.createObjectConfiguration();
            child1.setClassName(TestModule1Class1.class.getName());
            child1.setFields(new HashMap<String, ElementConfiguration>());
            lastFields.put("testModule1Class1", child1);

            ObjectConfiguration grandChild1 = objectConfigurationFactory.createObjectConfiguration();
            grandChild1.setClassName(TestModule2Class1.class.getName());
            grandChild1.setFields(new HashMap<String, ElementConfiguration>());
            child1.getFields().put("testModule2Class1", grandChild1);
            lastFields = grandChild1.getFields();
        }

        final CountDownLatch latch = new CountDownLatch(1);

        final long start = System.currentTimeMillis();

        asyncInstantiator.instanciateObject(root, Object.class, instantationContext, new InstantiationCallback<Object>()
        {
            @Override
            public void onFailure(Throwable caught)
            {
            }

            @Override
            public void onSuccess(Object result)
            {
                final long end = System.currentTimeMillis();

                LOG.info("Instantiation took :" + (end - start) + "ms");

                assertNotNull(result);
                assertTrue(result instanceof TestClass1);
                TestClass1 testClass1 = (TestClass1) result;
                assertNotNull(testClass1.getTestModule1Class1());

                TestModule1Class1 testModule1Class1 = testClass1.getTestModule1Class1();

                for (int cycle = 0; cycle < maxCycle; cycle++)
                {
                    TestModule2Class1 testModule2Class1 = testModule1Class1.getTestModule2Class1();
                    assertNotNull(testModule2Class1);
                    assertNotSame(testModule1Class1, testModule2Class1.getTestModule1Class1());
                    testModule1Class1 = testModule2Class1.getTestModule1Class1();
                    if (cycle < maxCycle - 1)
                    {
                        assertNotNull(testModule1Class1);
                    }
                    else
                    {
                        assertNull(testModule1Class1);
                        LOG.info("End of cycle:" + cycle);
                    }
                }
                latch.countDown();
            }
        });

        latch.await();
    }
}
