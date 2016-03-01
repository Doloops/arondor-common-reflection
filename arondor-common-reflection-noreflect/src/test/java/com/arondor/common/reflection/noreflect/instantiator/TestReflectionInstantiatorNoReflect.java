package com.arondor.common.reflection.noreflect.instantiator;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.junit.Before;

import com.arondor.common.reflection.noreflect.model.FieldSetter;
import com.arondor.common.reflection.noreflect.model.ObjectConstructor;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.runtime.SimpleReflectionInstantiatorCatalog;
import com.arondor.common.reflection.noreflect.testclasses.TestAbstractParent;
import com.arondor.common.reflection.noreflect.testclasses.TestChildClass;
import com.arondor.common.reflection.noreflect.testclasses.TestChildWithAbstractParent;
import com.arondor.common.reflection.noreflect.testclasses.TestClassA;
import com.arondor.common.reflection.noreflect.testclasses.TestClassB;
import com.arondor.common.reflection.noreflect.testclasses.TestClassC;
import com.arondor.common.reflection.noreflect.testclasses.TestClassC.EnumValue;
import com.arondor.common.reflection.noreflect.testclasses.TestClassD;
import com.arondor.common.reflection.noreflect.testclasses.TestClassE;
import com.arondor.common.reflection.noreflect.testclasses.TestGrandChildClass;
import com.arondor.common.reflection.noreflect.testclasses.TestNestedClass;
import com.arondor.common.reflection.noreflect.testclasses.TestParentClass;

public class TestReflectionInstantiatorNoReflect extends TestNoReflectSharedTests
{
    @Before
    public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException,
            SecurityException, IllegalArgumentException, InvocationTargetException

    {
        ReflectionInstantiatorCatalog catalog = new SimpleReflectionInstantiatorCatalog();

        catalog.registerObjectConstructor(TestClassA.class.getName(), new ObjectConstructor()
        {
            @Override
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
            @Override
            public void set(Object object, Object value)
            {
                ((TestClassA) object).setProperty1((String) value);
            }
        });

        catalog.registerFieldSetter(TestClassA.class.getName(), "property2", new FieldSetter()
        {
            @Override
            public void set(Object object, Object value)
            {
                ((TestClassA) object).setProperty2(Long.parseLong((String) value));
            }
        });

        catalog.registerObjectConstructor(TestClassB.class.getName(), new ObjectConstructor()
        {
            @Override
            public Object create(List<Object> arguments)
            {
                return new com.arondor.common.reflection.noreflect.testclasses.TestClassB();
            }
        });

        catalog.registerFieldSetter(TestClassB.class.getName(), "nestedClassA", new FieldSetter()
        {

            @Override
            public void set(Object object, Object value)
            {
                ((TestClassB) object).setNestedClassA((TestClassA) value);
            }
        });

        catalog.registerObjectConstructor(TestClassC.class.getName(), new ObjectConstructor()
        {
            @Override
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
            @Override
            public void set(Object object, Object value)
            {
                ((TestClassC) object).setEnumValue((EnumValue) value);
            }
        });

        catalog.registerObjectConstructor(EnumValue.class.getName(), new ObjectConstructor()
        {
            @Override
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
            @Override
            public Object create(List<Object> arguments)
            {
                return new TestClassD();
            }
        });

        catalog.registerFieldSetter(TestClassD.class.getName(), "listClassA", new FieldSetter()
        {
            @Override
            @SuppressWarnings("unchecked")
            public void set(Object object, Object value)
            {
                ((TestClassD) object).setListClassA((List<TestClassA>) value);
            }
        });

        catalog.registerObjectConstructor(TestClassE.class.getName(), new ObjectConstructor()
        {
            @Override
            public Object create(List<Object> arguments)
            {
                return new TestClassE();
            }
        });

        catalog.registerFieldSetter(TestClassE.class.getName(), "mapClassA", new FieldSetter()
        {
            @Override
            @SuppressWarnings("unchecked")
            public void set(Object object, Object value)
            {
                ((TestClassE) object).setMapClassA((Map<String, TestClassA>) value);
            }
        });

        catalog.registerObjectConstructor(TestChildClass.class.getName(), new ObjectConstructor()
        {
            @Override
            public Object create(List<Object> arguments)
            {
                return new TestChildClass();
            }
        });

        catalog.registerFieldSetter(TestParentClass.class.getName(), "parentField", new FieldSetter()
        {
            @Override
            public void set(Object object, Object value)
            {
                ((TestParentClass) object).setParentField((String) value);
            }
        });

        catalog.registerFieldSetter(TestChildClass.class.getName(), "childField", new FieldSetter()
        {
            @Override
            public void set(Object object, Object value)
            {
                ((TestChildClass) object).setChildField((String) value);
            }
        });

        String childClassInheritance[] = { TestParentClass.class.getName() };
        catalog.registerObjectInheritance(TestChildClass.class.getName(), childClassInheritance);

        String grandChildClassInheritance[] = { TestChildClass.class.getName() };
        catalog.registerObjectInheritance(TestGrandChildClass.class.getName(), grandChildClassInheritance);

        catalog.registerObjectConstructor(TestGrandChildClass.class.getName(), new ObjectConstructor()
        {
            @Override
            public Object create(List<Object> arguments)
            {
                return new TestGrandChildClass();
            }
        });

        catalog.registerFieldSetter(TestAbstractParent.class.getName(), "abstractField", new FieldSetter()
        {
            @Override
            public void set(Object object, Object value)
            {
                ((TestAbstractParent) object).setAbstractField((String) value);
            }
        });

        catalog.registerObjectConstructor(TestChildWithAbstractParent.class.getName(), new ObjectConstructor()
        {
            @Override
            public Object create(List<Object> arguments)
            {
                return new TestChildWithAbstractParent();
            }
        });

        String childAbstractInheritance[] = { TestAbstractParent.class.getName() };
        catalog.registerObjectInheritance(TestChildWithAbstractParent.class.getName(), childAbstractInheritance);

        catalog.registerObjectConstructor(TestNestedClass.class.getName(), new ObjectConstructor()
        {
            @Override
            public Object create(List<Object> arguments)
            {
                return new TestNestedClass();
            }
        });

        catalog.registerObjectConstructor(TestNestedClass.EmbeddedClass.class.getName(), new ObjectConstructor()
        {
            @Override
            public Object create(List<Object> arguments)
            {
                return new TestNestedClass.EmbeddedClass();
            }
        });

        catalog.registerFieldSetter(TestNestedClass.class.getName(), "embeddedClass", new FieldSetter()
        {
            @Override
            public void set(Object object, Object value)
            {
                ((TestNestedClass) object).setEmbeddedClass((TestNestedClass.EmbeddedClass) value);
            }
        });

        catalog.registerFieldSetter(TestNestedClass.EmbeddedClass.class.getName(), "fieldInEmbedded", new FieldSetter()
        {
            @Override
            public void set(Object object, Object value)
            {
                ((TestNestedClass.EmbeddedClass) object).setFieldInEmbedded((String) value);
            }
        });

        ReflectionInstantiatorNoReflect reflectionInstantiator = new ReflectionInstantiatorNoReflect();
        reflectionInstantiator.setReflectionInstantiatorCatalog(catalog);
        this.reflectionInstantiator = reflectionInstantiator;
        this.instantationContext = reflectionInstantiator.createDefaultInstantiationContext();
    }
}
