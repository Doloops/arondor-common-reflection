package com.arondor.common.reflection.noreflect.instantiator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.management.ReflectionException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiator;
import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.model.config.FieldConfiguration;
import com.arondor.common.reflection.model.config.FieldConfiguration.FieldConfigurationType;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.noreflect.model.FieldSetter;
import com.arondor.common.reflection.noreflect.model.ObjectConstructor;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.runtime.SimpleReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.testclasses.TestClassA;
import com.arondor.common.reflection.noreflect.testclasses.TestClassB;
import com.arondor.common.reflection.noreflect.testclasses.TestClassC;
import com.arondor.common.reflection.noreflect.testclasses.TestClassC.EnumValue;
import com.arondor.common.reflection.noreflect.testclasses.TestClassD;

public class TestReflectionInstantiatorNoReflect
{
    private ReflectionInstantiator reflectionInstantiator;

    private InstantiationContext instantationContext;

    private ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

    @Before
    public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException,
            SecurityException, IllegalArgumentException, InvocationTargetException

    {
        ReflectionInstantiatorCatalog catalog = new SimpleReflectionInstantiatorCatalog();

        catalog.registerObjectConstructor(TestClassA.class.getName(), new ObjectConstructor()
        {
            public Object create(List<Object> arguments)
            {
                if (arguments.isEmpty())
                {
                    return new com.arondor.common.reflection.noreflect.testclasses.TestClassA();
                }
                else if (arguments.size() == 1)
                {
                    return new com.arondor.common.reflection.noreflect.testclasses.TestClassA((String) arguments.get(0));
                }
                else if (arguments.size() == 2)
                {
                    return new com.arondor.common.reflection.noreflect.testclasses.TestClassA(
                            (String) arguments.get(0), Long.parseLong((String) arguments.get(1)));
                }
                throw new IllegalArgumentException("Count ?" + arguments.size());
            }
        });
        catalog.registerFieldSetter(TestClassA.class.getName(), "property1", new FieldSetter()
        {
            public void set(Object object, Object value)
            {
                ((TestClassA) object).setProperty1((String) value);
            }
        });

        catalog.registerFieldSetter(TestClassA.class.getName(), "property2", new FieldSetter()
        {
            public void set(Object object, Object value)
            {
                ((TestClassA) object).setProperty2(Long.parseLong((String) value));
            }
        });

        catalog.registerObjectConstructor(TestClassB.class.getName(), new ObjectConstructor()
        {
            public Object create(List<Object> arguments)
            {
                return new com.arondor.common.reflection.noreflect.testclasses.TestClassB();
            }
        });

        catalog.registerFieldSetter(TestClassB.class.getName(), "nestedClassA", new FieldSetter()
        {

            public void set(Object object, Object value)
            {
                ((TestClassB) object).setNestedClassA((TestClassA) value);
            }
        });

        catalog.registerObjectConstructor(TestClassC.class.getName(), new ObjectConstructor()
        {
            public Object create(List<Object> arguments)
            {
                if (arguments.isEmpty())
                {
                    return new TestClassC();
                }
                else if (arguments.size() == 1)
                {
                    return new TestClassC((EnumValue) arguments.get(0));
                }
                throw new IllegalArgumentException("Count ?" + arguments.size());
            }
        });

        catalog.registerFieldSetter(TestClassC.class.getName(), "enumValue", new FieldSetter()
        {
            public void set(Object object, Object value)
            {
                ((TestClassC) object).setEnumValue((EnumValue) value);
            }
        });

        catalog.registerObjectConstructor(EnumValue.class.getName(), new ObjectConstructor()
        {
            public Object create(List<Object> arguments)
            {
                if (arguments.size() != 1)
                {
                    throw new IllegalArgumentException("Count ?" + arguments.size());
                }
                return EnumValue.valueOf((String) arguments.get(0));
            }
        });

        catalog.registerObjectConstructor(TestClassD.class.getName(), new ObjectConstructor()
        {
            public Object create(List<Object> arguments)
            {
                return new TestClassD();
            }
        });

        catalog.registerFieldSetter(TestClassD.class.getName(), "listClassA", new FieldSetter()
        {
            @SuppressWarnings("unchecked")
            public void set(Object object, Object value)
            {
                ((TestClassD) object).setListClassA((List<TestClassA>) value);
            }
        });

        ReflectionInstantiatorNoReflect reflectionInstantiator = new ReflectionInstantiatorNoReflect();
        reflectionInstantiator.setReflectionInstantiatorCatalog(catalog);
        this.reflectionInstantiator = reflectionInstantiator;
        this.instantationContext = reflectionInstantiator.createDefaultInstantiationContext();
    }

    @Test
    public void testInstantiateClassA() throws ReflectionException
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(TestClassA.class.getName());

        configuration.setFields(new HashMap<String, FieldConfiguration>());

        FieldConfiguration fieldConf1 = objectConfigurationFactory.createFieldConfiguration();
        fieldConf1.setFieldConfigurationType(FieldConfigurationType.Primitive_Single);
        fieldConf1.setValue("Value for property1");

        configuration.getFields().put("property1", fieldConf1);

        FieldConfiguration fieldConf2 = objectConfigurationFactory.createFieldConfiguration();
        ;
        fieldConf2.setFieldConfigurationType(FieldConfigurationType.Primitive_Single);
        fieldConf2.setValue("82377");

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

        configuration.setConstructorArguments(new ArrayList<FieldConfiguration>());

        FieldConfiguration fieldConf1 = objectConfigurationFactory.createFieldConfiguration();
        fieldConf1.setFieldConfigurationType(FieldConfigurationType.Primitive_Single);
        fieldConf1.setValue("Value for property1");

        configuration.getConstructorArguments().add(fieldConf1);

        FieldConfiguration fieldConf2 = objectConfigurationFactory.createFieldConfiguration();

        fieldConf2.setFieldConfigurationType(FieldConfigurationType.Primitive_Single);
        fieldConf2.setValue("82377");

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
        configurationA.setFields(new HashMap<String, FieldConfiguration>());

        FieldConfiguration fieldConf1 = objectConfigurationFactory.createFieldConfiguration();
        ;
        fieldConf1.setFieldConfigurationType(FieldConfigurationType.Primitive_Single);
        fieldConf1.setValue("Value for property1 : B");

        configurationA.getFields().put("property1", fieldConf1);

        FieldConfiguration fieldConf2 = objectConfigurationFactory.createFieldConfiguration();
        ;
        fieldConf2.setFieldConfigurationType(FieldConfigurationType.Primitive_Single);
        fieldConf2.setValue("82378");

        configurationA.getFields().put("property2", fieldConf2);

        ObjectConfiguration configurationB = objectConfigurationFactory.createObjectConfiguration();
        configurationB.setClassName(TestClassB.class.getName());
        configurationB.setFields(new HashMap<String, FieldConfiguration>());

        FieldConfiguration fieldConfClassA = objectConfigurationFactory.createFieldConfiguration();

        fieldConfClassA.setFieldConfigurationType(FieldConfigurationType.Object_Single);
        fieldConfClassA.setObjectConfiguration(configurationA);

        configurationB.getFields().put("nestedClassA", fieldConfClassA);

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
        configurationC.setFields(new HashMap<String, FieldConfiguration>());

        FieldConfiguration fieldConfEnum = objectConfigurationFactory.createFieldConfiguration();
        fieldConfEnum.setFieldConfigurationType(FieldConfigurationType.Object_Single);
        ObjectConfiguration confEnum = objectConfigurationFactory.createObjectConfiguration();
        confEnum.setClassName(EnumValue.class.getName());
        confEnum.setConstructorArguments(new ArrayList<FieldConfiguration>());

        FieldConfiguration fieldConfEnumValue = objectConfigurationFactory.createFieldConfiguration();
        fieldConfEnumValue.setFieldConfigurationType(FieldConfigurationType.Primitive_Single);
        fieldConfEnumValue.setValue("BLUE");

        confEnum.getConstructorArguments().add(fieldConfEnumValue);

        fieldConfEnum.setObjectConfiguration(confEnum);

        configurationC.getFields().put("enumValue", fieldConfEnum);

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
        configurationC.setFields(new HashMap<String, FieldConfiguration>());
        configurationC.setConstructorArguments(new ArrayList<FieldConfiguration>());

        FieldConfiguration fieldConfEnum = objectConfigurationFactory.createFieldConfiguration();
        fieldConfEnum.setFieldConfigurationType(FieldConfigurationType.Object_Single);
        ObjectConfiguration confEnum = objectConfigurationFactory.createObjectConfiguration();
        confEnum.setClassName(EnumValue.class.getName());
        confEnum.setConstructorArguments(new ArrayList<FieldConfiguration>());

        FieldConfiguration fieldConfEnumValue = objectConfigurationFactory.createFieldConfiguration();
        fieldConfEnumValue.setFieldConfigurationType(FieldConfigurationType.Primitive_Single);
        fieldConfEnumValue.setValue("BLUE");

        confEnum.getConstructorArguments().add(fieldConfEnumValue);

        fieldConfEnum.setObjectConfiguration(confEnum);

        configurationC.getConstructorArguments().add(fieldConfEnum);

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
        configurationD.setFields(new HashMap<String, FieldConfiguration>());

        FieldConfiguration fieldConfList = objectConfigurationFactory.createFieldConfiguration();
        fieldConfList.setFieldConfigurationType(FieldConfigurationType.Object_Multiple);
        fieldConfList.setObjectConfigurations(new ArrayList<FieldConfiguration>());

        configurationD.getFields().put("listClassA", fieldConfList);

        for (int i = 0; i < 10; i++)
        {
            FieldConfiguration fieldConfA = objectConfigurationFactory.createFieldConfiguration();

            fieldConfA.setFieldConfigurationType(FieldConfigurationType.Object_Single);

            ObjectConfiguration configurationA = objectConfigurationFactory.createObjectConfiguration();
            fieldConfA.setObjectConfiguration(configurationA);
            configurationA.setClassName(TestClassA.class.getName());
            configurationA.setFields(new HashMap<String, FieldConfiguration>());

            FieldConfiguration fieldConf2 = objectConfigurationFactory.createFieldConfiguration();
            fieldConf2.setFieldConfigurationType(FieldConfigurationType.Primitive_Single);
            fieldConf2.setValue(Integer.toString(10000 + i));

            configurationA.getFields().put("property2", fieldConf2);

            fieldConfList.getObjectConfigurations().add(fieldConfA);
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

}
