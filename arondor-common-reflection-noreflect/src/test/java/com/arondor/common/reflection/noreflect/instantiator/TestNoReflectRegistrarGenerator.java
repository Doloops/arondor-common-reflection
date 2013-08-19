package com.arondor.common.reflection.noreflect.instantiator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.management.ReflectionException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiator;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.noreflect.generator.NoReflectRegistrarGenerator;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorRegistrar;
import com.arondor.common.reflection.noreflect.runtime.SimpleReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.testclasses.TestClassA;
import com.arondor.common.reflection.noreflect.testclasses.TestClassB;
import com.arondor.common.reflection.noreflect.testclasses.TestClassC;
import com.arondor.common.reflection.noreflect.testclasses.TestClassC.EnumValue;
import com.arondor.common.reflection.noreflect.testclasses.TestClassD;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;

public class TestNoReflectRegistrarGenerator
{
    private final static Logger LOG = Logger.getLogger(TestNoReflectRegistrarGenerator.class);

    private InstantiationContext instantationContext;

    private ReflectionInstantiator reflectionInstantiator;

    private AccessibleClassParser parser;

    private ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

    private void doGenerate() throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException
    {
        parser = new JavaAccessibleClassParser();

        List<AccessibleClass> classes = new ArrayList<AccessibleClass>();

        classes.add(parser.parseAccessibleClass(TestClassA.class));
        classes.add(parser.parseAccessibleClass(TestClassB.class));
        classes.add(parser.parseAccessibleClass(TestClassC.class));
        classes.add(parser.parseAccessibleClass(TestClassC.EnumValue.class));
        classes.add(parser.parseAccessibleClass(TestClassD.class));

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

        gen.generate(out, classes);
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
    public void initialize() throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException
    {
        doGenerate();
    }

    @Test
    public void testInstantiateClassA() throws ReflectionException
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(TestClassA.class.getName());

        configuration.setFields(new HashMap<String, ElementConfiguration>());

        ElementConfiguration fieldConf1 = objectConfigurationFactory
                .createPrimitiveConfiguration("Value for property1");
        configuration.getFields().put("property1", fieldConf1);

        ElementConfiguration fieldConf2 = objectConfigurationFactory.createPrimitiveConfiguration("82377");
        configuration.getFields().put("property2", fieldConf2);

        Object objectA = reflectionInstantiator.instanciateObject(configuration, Object.class, instantationContext);

        Assert.assertEquals(TestClassA.class, objectA.getClass());
        Assert.assertTrue(objectA instanceof TestClassA);

        TestClassA classA = (TestClassA) objectA;

        Assert.assertEquals("Value for property1", classA.getProperty1());
        Assert.assertEquals(82377, classA.getProperty2());

    }

    @Test
    public void testInstantiateClassAWithConstructor() throws ReflectionException
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(TestClassA.class.getName());

        configuration.setConstructorArguments(new ArrayList<ElementConfiguration>());

        ElementConfiguration fieldConf1 = objectConfigurationFactory
                .createPrimitiveConfiguration("Value for property1");

        configuration.getConstructorArguments().add(fieldConf1);

        ElementConfiguration fieldConf2 = objectConfigurationFactory.createPrimitiveConfiguration("82377");

        configuration.getConstructorArguments().add(fieldConf2);

        Object objectA = reflectionInstantiator.instanciateObject(configuration, Object.class, instantationContext);

        Assert.assertEquals(TestClassA.class, objectA.getClass());
        Assert.assertTrue(objectA instanceof TestClassA);

        TestClassA classA = (TestClassA) objectA;

        Assert.assertEquals("Value for property1", classA.getProperty1());
        Assert.assertEquals(82377, classA.getProperty2());

    }

    @Test
    public void testClassbBNestedClassA() throws ReflectionException
    {
        ObjectConfiguration configurationA = objectConfigurationFactory.createObjectConfiguration();
        configurationA.setClassName(TestClassA.class.getName());
        configurationA.setFields(new HashMap<String, ElementConfiguration>());

        ElementConfiguration fieldConf1 = objectConfigurationFactory
                .createPrimitiveConfiguration("Value for property1 : B");
        configurationA.getFields().put("property1", fieldConf1);

        ElementConfiguration fieldConf2 = objectConfigurationFactory.createPrimitiveConfiguration("82378");
        configurationA.getFields().put("property2", fieldConf2);

        ObjectConfiguration configurationB = objectConfigurationFactory.createObjectConfiguration();
        configurationB.setClassName(TestClassB.class.getName());
        configurationB.setFields(new HashMap<String, ElementConfiguration>());

        configurationB.getFields().put("nestedClassA", configurationA);
        Object objectB = reflectionInstantiator.instanciateObject(configurationB, Object.class, instantationContext);
        Assert.assertNotNull(objectB);
        Assert.assertTrue(objectB instanceof TestClassB);
        TestClassB classB = (TestClassB) objectB;
        Assert.assertNotNull(classB.getNestedClassA());
        TestClassA classA = classB.getNestedClassA();

        Assert.assertEquals("Value for property1 : B", classA.getProperty1());
        Assert.assertEquals(82378, classA.getProperty2());

    }

    @Test
    public void testClassC_WithEnum() throws ReflectionException
    {
        ObjectConfiguration configurationC = objectConfigurationFactory.createObjectConfiguration();
        configurationC.setClassName(TestClassC.class.getName());
        configurationC.setFields(new HashMap<String, ElementConfiguration>());

        ObjectConfiguration confEnum = objectConfigurationFactory.createObjectConfiguration();
        confEnum.setClassName(EnumValue.class.getName());
        confEnum.setConstructorArguments(new ArrayList<ElementConfiguration>());

        ElementConfiguration fieldConfEnumValue = objectConfigurationFactory.createPrimitiveConfiguration("BLUE");
        confEnum.getConstructorArguments().add(fieldConfEnumValue);

        configurationC.getFields().put("enumValue", confEnum);

        Object objectC = reflectionInstantiator.instanciateObject(configurationC, Object.class, instantationContext);
        Assert.assertNotNull(objectC);
        Assert.assertTrue(objectC instanceof TestClassC);

        TestClassC classC = (TestClassC) objectC;

        Assert.assertEquals(TestClassC.EnumValue.BLUE, classC.getEnumValue());

    }

    @Test
    public void testClassC_ConstructorWithEnum() throws ReflectionException
    {
        ObjectConfiguration configurationC = objectConfigurationFactory.createObjectConfiguration();
        configurationC.setClassName(TestClassC.class.getName());
        configurationC.setFields(new HashMap<String, ElementConfiguration>());
        configurationC.setConstructorArguments(new ArrayList<ElementConfiguration>());

        ObjectConfiguration confEnum = objectConfigurationFactory.createObjectConfiguration();
        confEnum.setClassName(EnumValue.class.getName());
        confEnum.setConstructorArguments(new ArrayList<ElementConfiguration>());

        ElementConfiguration fieldConfEnumValue = objectConfigurationFactory.createPrimitiveConfiguration("BLUE");
        confEnum.getConstructorArguments().add(fieldConfEnumValue);

        configurationC.getConstructorArguments().add(confEnum);

        Object objectC = reflectionInstantiator.instanciateObject(configurationC, Object.class, instantationContext);
        Assert.assertNotNull(objectC);
        Assert.assertTrue(objectC instanceof TestClassC);

        TestClassC classC = (TestClassC) objectC;

        Assert.assertEquals(TestClassC.EnumValue.BLUE, classC.getEnumValue());

    }

    @Test
    public void testClassD_List()
    {
        ObjectConfiguration configurationD = objectConfigurationFactory.createObjectConfiguration();
        configurationD.setClassName(TestClassD.class.getName());
        configurationD.setFields(new HashMap<String, ElementConfiguration>());

        ListConfiguration fieldConfList = objectConfigurationFactory.createListConfiguration();
        fieldConfList.setListConfiguration(new ArrayList<ElementConfiguration>());

        configurationD.getFields().put("listClassA", fieldConfList);

        for (int i = 0; i < 10; i++)
        {
            ObjectConfiguration configurationA = objectConfigurationFactory.createObjectConfiguration();
            configurationA.setClassName(TestClassA.class.getName());
            configurationA.setFields(new HashMap<String, ElementConfiguration>());

            ElementConfiguration fieldConf2 = objectConfigurationFactory.createPrimitiveConfiguration(Integer
                    .toString(10000 + i));
            configurationA.getFields().put("property2", fieldConf2);

            fieldConfList.getListConfiguration().add(configurationA);
        }

        Object objectD = reflectionInstantiator.instanciateObject(configurationD, Object.class, instantationContext);

        Assert.assertNotNull(objectD);

        Assert.assertTrue(objectD instanceof TestClassD);

        TestClassD classD = (TestClassD) objectD;

        Assert.assertNotNull(classD.getListClassA());

        Assert.assertEquals(10, classD.getListClassA().size());

        for (int idx = 0; idx < classD.getListClassA().size(); idx++)
        {
            Assert.assertEquals(10000 + idx, classD.getListClassA().get(idx).getProperty2());
        }
    }

    @Test
    public void testClassA_NonSingleton()
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setSingleton(false);
        configuration.setClassName(TestClassA.class.getName());
        configuration.setFields(new HashMap<String, ElementConfiguration>());

        PrimitiveConfiguration fieldConf1 = objectConfigurationFactory
                .createPrimitiveConfiguration("Value for property1");
        configuration.getFields().put("property1", fieldConf1);

        ElementConfiguration fieldConf2 = objectConfigurationFactory.createPrimitiveConfiguration("82377");
        configuration.getFields().put("property2", fieldConf2);

        ObjectConfigurationMap configurationMap = objectConfigurationFactory.createObjectConfigurationMap();
        String objectName = "myClassA";
        configurationMap.put(objectName, configuration);
        instantationContext.setSharedObjectConfigurations(configurationMap);

        assertNull("Object shall not exist !", instantationContext.getSharedObject(objectName));

        Object objectA = reflectionInstantiator.instanciateObject(objectName, Object.class, instantationContext);
        assertNotNull("Object shall not be null !", objectA);

        Object objectA2 = reflectionInstantiator.instanciateObject(objectName, Object.class, instantationContext);
        assertNotSame(objectA, objectA2);
    }

    @Test
    public void testClassA_Singleton()
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setSingleton(true);
        configuration.setClassName(TestClassA.class.getName());
        configuration.setFields(new HashMap<String, ElementConfiguration>());

        ElementConfiguration fieldConf1 = objectConfigurationFactory
                .createPrimitiveConfiguration("Value for property1");
        configuration.getFields().put("property1", fieldConf1);

        ElementConfiguration fieldConf2 = objectConfigurationFactory.createPrimitiveConfiguration("82377");
        configuration.getFields().put("property2", fieldConf2);

        ObjectConfigurationMap configurationMap = objectConfigurationFactory.createObjectConfigurationMap();
        String objectName = "myClassA";
        configurationMap.put(objectName, configuration);
        instantationContext.setSharedObjectConfigurations(configurationMap);

        assertNull("Object shall not exist !", instantationContext.getSharedObject(objectName));

        Object objectA = reflectionInstantiator.instanciateObject(objectName, Object.class, instantationContext);
        assertNotNull("Object shall not be null !", objectA);

        Object objectA2 = reflectionInstantiator.instanciateObject(objectName, Object.class, instantationContext);
        assertEquals(objectA, objectA2);

        /**
         * Now check that we are really dealing with the same objects
         */

        TestClassA tca = (TestClassA) objectA;
        TestClassA tca2 = (TestClassA) objectA2;

        tca.setProperty2(99976);

        assertEquals(tca.getProperty2(), tca2.getProperty2());
    }

    @Test
    public void testClassB_Reference_To_ClassA()
    {
        ObjectConfiguration configurationA = objectConfigurationFactory.createObjectConfiguration();
        configurationA.setSingleton(true);
        configurationA.setClassName(TestClassA.class.getName());
        configurationA.setFields(new HashMap<String, ElementConfiguration>());

        ElementConfiguration fieldConf1 = objectConfigurationFactory
                .createPrimitiveConfiguration("Referenced value from TestClassA");
        configurationA.getFields().put("property1", fieldConf1);

        ElementConfiguration fieldConf2 = objectConfigurationFactory.createPrimitiveConfiguration("777802");
        configurationA.getFields().put("property2", fieldConf2);

        ObjectConfigurationMap configurationMap = objectConfigurationFactory.createObjectConfigurationMap();
        String objectName = "myClassA";
        configurationMap.put(objectName, configurationA);
        instantationContext.setSharedObjectConfigurations(configurationMap);

        ObjectConfiguration configurationB = objectConfigurationFactory.createObjectConfiguration();
        configurationB.setClassName(TestClassB.class.getName());
        configurationB.setFields(new HashMap<String, ElementConfiguration>());

        ReferenceConfiguration referenceClassA = objectConfigurationFactory.createReferenceConfiguration();
        referenceClassA.setReferenceName(objectName);
        configurationB.getFields().put("nestedClassA", referenceClassA);

        Object objectB = reflectionInstantiator.instanciateObject(configurationB, Object.class, instantationContext);
        Assert.assertNotNull(objectB);
        Assert.assertTrue(objectB instanceof TestClassB);
        TestClassB classB = (TestClassB) objectB;
        Assert.assertNotNull(classB.getNestedClassA());
        TestClassA classA = classB.getNestedClassA();

        Assert.assertEquals("Referenced value from TestClassA", classA.getProperty1());
        Assert.assertEquals(777802, classA.getProperty2());

    }

}
