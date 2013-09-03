package com.arondor.common.reflection.noreflect.instantiator;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.Before;

import com.arondor.common.reflection.noreflect.model.FieldSetter;
import com.arondor.common.reflection.noreflect.model.ObjectConstructor;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.runtime.SimpleReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.testclasses.TestClassA;
import com.arondor.common.reflection.noreflect.testclasses.TestClassB;
import com.arondor.common.reflection.noreflect.testclasses.TestClassC;
import com.arondor.common.reflection.noreflect.testclasses.TestClassC.EnumValue;
import com.arondor.common.reflection.noreflect.testclasses.TestClassD;

public class TestReflectionInstantiatorNoReflect extends TestNoReflectSharedTests
{
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

}
