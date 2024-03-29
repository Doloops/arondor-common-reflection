package com.arondor.common.reflection.reflect.instantiator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.management.ReflectionException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiator;
import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;
import com.arondor.common.reflection.reflect.testclasses.TestClassA;
import com.arondor.common.reflection.reflect.testclasses.TestClassA1;
import com.arondor.common.reflection.reflect.testclasses.TestClassB;
import com.arondor.common.reflection.reflect.testclasses.TestClassC;
import com.arondor.common.reflection.reflect.testclasses.TestClassC.EnumValue;
import com.arondor.common.reflection.reflect.testclasses.TestClassD;
import com.arondor.common.reflection.reflect.testclasses.TestClassE;

public class TestReflectionInstantiatorReflect
{
    private final static Logger LOG = Logger.getLogger(TestReflectionInstantiatorReflect.class);

    private InstantiationContext instantationContext;

    private ReflectionInstantiator reflectionInstantiator;

    private ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

    @Before
    public void initialize() throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException
    {
        ReflectionInstantiator reflectionInstantiator = new ReflectionInstantiatorReflect();
        this.reflectionInstantiator = reflectionInstantiator;
        this.instantationContext = this.reflectionInstantiator.createDefaultInstantiationContext();
        // this.parser = new JavaAccessibleClassParser();
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
        assertNotNull(objectA);

        assertEquals(TestClassA.class, objectA.getClass());
        assertTrue(objectA instanceof TestClassA);

        TestClassA classA = (TestClassA) objectA;

        assertEquals("Value for property1", classA.getProperty1());
        assertEquals(82377, classA.getProperty2());
    }

    @Test
    public void testInstantiateClassA_Null_StringPrimitive() throws ReflectionException
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(TestClassA.class.getName());

        configuration.setFields(new HashMap<String, ElementConfiguration>());

        configuration.getFields().put("property1", objectConfigurationFactory.createPrimitiveConfiguration(null));

        Object objectA = reflectionInstantiator.instanciateObject(configuration, Object.class, instantationContext);
        assertNotNull(objectA);

        assertEquals(TestClassA.class, objectA.getClass());
        assertTrue(objectA instanceof TestClassA);

        TestClassA classA = (TestClassA) objectA;

        assertEquals(null, classA.getProperty1());
    }

    @Test
    public void testInstantiateClassA1_Null_StringPrimitive_NO_null_override() throws ReflectionException
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(TestClassA1.class.getName());

        configuration.setFields(new HashMap<String, ElementConfiguration>());

        configuration.getFields().put("property1", objectConfigurationFactory.createPrimitiveConfiguration(null));

        Object objectA = reflectionInstantiator.instanciateObject(configuration, Object.class, instantationContext);
        assertNotNull(objectA);

        assertEquals(TestClassA1.class, objectA.getClass());
        assertTrue(objectA instanceof TestClassA1);

        TestClassA1 classA = (TestClassA1) objectA;

        assertEquals("DefaultString", classA.getProperty1());
    }

    @Test
    public void testInstantiateClassA1_Null_StringPrimitive_WITH_null_override() throws ReflectionException
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(TestClassA1.class.getName());

        configuration.setFields(new HashMap<String, ElementConfiguration>());

        configuration.getFields().put("property1", objectConfigurationFactory.createPrimitiveConfiguration(null));

        ((ReflectionInstantiatorReflect) reflectionInstantiator).setSkipNullFieldConfigurations(false);
        Object objectA = reflectionInstantiator.instanciateObject(configuration, Object.class, instantationContext);
        assertNotNull(objectA);

        assertEquals(TestClassA1.class, objectA.getClass());
        assertTrue(objectA instanceof TestClassA1);

        TestClassA1 classA = (TestClassA1) objectA;

        assertEquals(null, classA.getProperty1());
    }

    @Test
    public void testInstantiateClassA1_Null_ObjectConfiguration() throws ReflectionException
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(TestClassA1.class.getName());

        configuration.setFields(new HashMap<String, ElementConfiguration>());

        configuration.getFields().put("property1", null);

        Object objectA = reflectionInstantiator.instanciateObject(configuration, Object.class, instantationContext);
        assertNotNull(objectA);

        assertEquals(TestClassA1.class, objectA.getClass());
        assertTrue(objectA instanceof TestClassA1);

        TestClassA1 classA = (TestClassA1) objectA;

        /**
         * Default configuration is to skip null ElementConfiguration, so the
         * default shall remain
         */
        assertEquals("DefaultString", classA.getProperty1());
    }

    @Test
    public void testInstantiateClassA_Null_JavaPrimitive() throws ReflectionException
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(TestClassA.class.getName());

        configuration.setFields(new HashMap<String, ElementConfiguration>());

        configuration.getFields().put("property2", objectConfigurationFactory.createPrimitiveConfiguration(null));

        Object objectA = reflectionInstantiator.instanciateObject(configuration, Object.class, instantationContext);
        assertNotNull(objectA);

        assertEquals(TestClassA.class, objectA.getClass());
        assertTrue(objectA instanceof TestClassA);

        TestClassA classA = (TestClassA) objectA;

        assertEquals(null, classA.getProperty1());
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

        assertEquals(TestClassA.class, objectA.getClass());
        assertTrue(objectA instanceof TestClassA);

        TestClassA classA = (TestClassA) objectA;

        assertEquals("Value for property1", classA.getProperty1());
        assertEquals(82377, classA.getProperty2());

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
        assertNotNull(objectB);
        assertTrue(objectB instanceof TestClassB);
        TestClassB classB = (TestClassB) objectB;
        assertNotNull(classB.getNestedClassA());
        TestClassA classA = classB.getNestedClassA();

        assertEquals("Value for property1 : B", classA.getProperty1());
        assertEquals(82378, classA.getProperty2());

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
        assertNotNull(objectC);
        assertTrue(objectC instanceof TestClassC);

        TestClassC classC = (TestClassC) objectC;

        assertEquals(TestClassC.EnumValue.BLUE, classC.getEnumValue());

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
        assertNotNull(objectC);
        assertTrue(objectC instanceof TestClassC);

        TestClassC classC = (TestClassC) objectC;

        assertEquals(TestClassC.EnumValue.BLUE, classC.getEnumValue());

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

            ElementConfiguration fieldConf2 = objectConfigurationFactory
                    .createPrimitiveConfiguration(Integer.toString(10000 + i));
            configurationA.getFields().put("property2", fieldConf2);

            fieldConfList.getListConfiguration().add(configurationA);
        }

        Object objectD = reflectionInstantiator.instanciateObject(configurationD, Object.class, instantationContext);

        assertNotNull(objectD);

        assertTrue(objectD instanceof TestClassD);

        TestClassD classD = (TestClassD) objectD;

        assertNotNull(classD.getListClassA());

        assertEquals(10, classD.getListClassA().size());

        for (int idx = 0; idx < classD.getListClassA().size(); idx++)
        {
            assertEquals(10000 + idx, classD.getListClassA().get(idx).getProperty2());
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
        instantationContext.addSharedObjectConfigurations(configurationMap);

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
        instantationContext.addSharedObjectConfigurations(configurationMap);

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
        instantationContext.addSharedObjectConfigurations(configurationMap);

        ObjectConfiguration configurationB = objectConfigurationFactory.createObjectConfiguration();
        configurationB.setClassName(TestClassB.class.getName());
        configurationB.setFields(new HashMap<String, ElementConfiguration>());

        ReferenceConfiguration referenceClassA = objectConfigurationFactory.createReferenceConfiguration();
        referenceClassA.setReferenceName(objectName);
        configurationB.getFields().put("nestedClassA", referenceClassA);

        Object objectB = reflectionInstantiator.instanciateObject(configurationB, Object.class, instantationContext);
        assertNotNull(objectB);
        assertTrue(objectB instanceof TestClassB);
        TestClassB classB = (TestClassB) objectB;
        assertNotNull(classB.getNestedClassA());
        TestClassA classA = classB.getNestedClassA();

        assertEquals("Referenced value from TestClassA", classA.getProperty1());
        assertEquals(777802, classA.getProperty2());
    }

    @Test
    public void testClassA_StrongReference_To_ClassA()
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
        instantationContext.addSharedObjectConfigurations(configurationMap);

        ReferenceConfiguration referenceClassA = objectConfigurationFactory.createReferenceConfiguration();
        referenceClassA.setReferenceName(objectName);

        ObjectConfiguration objectReferenceClassA = objectConfigurationFactory
                .createObjectConfigurationFromReference(referenceClassA);

        Object objectA = reflectionInstantiator.instanciateObject(objectReferenceClassA, Object.class,
                instantationContext);
        assertNotNull(objectA);
        assertTrue("Invalid class " + objectA.getClass().getName(), objectA instanceof TestClassA);
        TestClassA classA = (TestClassA) objectA;

        assertEquals("Referenced value from TestClassA", classA.getProperty1());
        assertEquals(777802, classA.getProperty2());
    }

    @Test
    public void instantiateMap()
    {
        MapConfiguration mapConfiguration = objectConfigurationFactory.createMapConfiguration();
        mapConfiguration.setMapConfiguration(new HashMap<ElementConfiguration, ElementConfiguration>());
        mapConfiguration.getMapConfiguration().put(objectConfigurationFactory.createPrimitiveConfiguration("key1"),
                objectConfigurationFactory.createPrimitiveConfiguration("value1"));
        mapConfiguration.getMapConfiguration().put(objectConfigurationFactory.createPrimitiveConfiguration("key2"),
                objectConfigurationFactory.createPrimitiveConfiguration("value2"));

        ObjectConfiguration configurationE = objectConfigurationFactory.createObjectConfiguration();
        configurationE.setClassName(TestClassE.class.getName());
        configurationE.setFields(new HashMap<String, ElementConfiguration>());
        configurationE.getFields().put("mapOfStrings", mapConfiguration);

        TestClassE objectE = reflectionInstantiator.instanciateObject(configurationE, TestClassE.class,
                instantationContext);
        assertNotNull(objectE);
        assertNotNull(objectE.getMapOfStrings());
        assertEquals(2, objectE.getMapOfStrings().size());
        assertEquals("value1", objectE.getMapOfStrings().get("key1"));
        assertEquals("value2", objectE.getMapOfStrings().get("key2"));
    }
}
