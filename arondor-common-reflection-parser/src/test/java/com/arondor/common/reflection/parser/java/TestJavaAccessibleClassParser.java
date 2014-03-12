package com.arondor.common.reflection.parser.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.arondor.common.management.mbean.annotation.Description;
import com.arondor.common.management.mbean.annotation.Mandatory;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.model.java.AccessibleMethod;

public class TestJavaAccessibleClassParser
{
    private static final Logger log = Logger.getLogger(TestJavaAccessibleClassParser.class);

    public static class SimpleClass
    {
        @Description("Dummy map")
        private Map<String, Long> map;

        public void setMap(Map<String, Long> map)
        {
            this.map = map;
        }

        public Map<String, Long> getMap()
        {
            return map;
        }

        public void myLittleMethod(String truff)
        {

        }
    }

    @Test
    public void testSimpleClass()
    {
        AccessibleClassParser parser = new JavaAccessibleClassParser();
        AccessibleClass clazz = parser.parseAccessibleClass(SimpleClass.class);
        assertNotNull(clazz);
        assertFalse(clazz.isAbstract());

        String className = clazz.getName();
        assertEquals(SimpleClass.class.getName(), className);
        assertTrue("This inner class shall contain '$'", className.contains("$"));

        log.info("Class : " + clazz.getClassBaseName());

        for (String fieldName : clazz.getAccessibleFields().keySet())
        {
            AccessibleField field = clazz.getAccessibleFields().get(fieldName);
            log.info("* Field : " + fieldName + ", class=" + field.getClassName());
        }

        assertEquals(1, clazz.getAccessibleFields().size());

        AccessibleField field = clazz.getAccessibleFields().get("map");
        assertNotNull(field);

        assertEquals("map", field.getName());
        assertEquals("java.util.Map", field.getClassName());
        assertEquals(2, field.getGenericParameterClassList().size());
        assertEquals("java.lang.String", field.getGenericParameterClassList().get(0));
        assertEquals("java.lang.Long", field.getGenericParameterClassList().get(1));

        assertEquals("Dummy map", field.getDescription());

        assertNotNull(clazz.getAccessibleMethods());
        for (AccessibleMethod mth : clazz.getAccessibleMethods())
        {
            log.info("Method : " + mth.getName());
        }

        assertEquals(1, clazz.getAccessibleMethods().size());
        assertEquals("myLittleMethod", clazz.getAccessibleMethods().get(0).getName());
    }

    public static class ClassWithIncompatibleGetterAndSetter
    {
        private int field;

        public int getField()
        {
            return field;
        }

        public void setField(String value)
        {
            this.field = Integer.parseInt(value);
        }
    }

    @Test
    public void testClassWithIncompatibleGetterAndSetter()
    {
        AccessibleClassParser parser = new JavaAccessibleClassParser();
        AccessibleClass clazz = parser.parseAccessibleClass(ClassWithIncompatibleGetterAndSetter.class);

        assertNotNull(clazz);
        log.info("Class : " + clazz.getClassBaseName());

        assertEquals(1, clazz.getAccessibleFields().size());

        AccessibleField field = clazz.getAccessibleFields().get("field");
        assertNotNull(field);

        assertEquals(String.class.getName(), field.getClassName());
    }

    public static class ClassWithStringList
    {
        private List<String> stringList;

        public List<String> getStringList()
        {
            return stringList;
        }

        public void setStringList(List<String> stringList)
        {
            this.stringList = stringList;
        }
    }

    @Test
    public void testClassWithStringList()
    {
        AccessibleClassParser parser = new JavaAccessibleClassParser();
        AccessibleClass clazz = parser.parseAccessibleClass(ClassWithStringList.class);

        assertNotNull(clazz);
        log.info("Class : " + clazz.getClassBaseName());

        assertEquals(1, clazz.getAccessibleFields().size());

        AccessibleField field = clazz.getAccessibleFields().get("stringList");
        assertNotNull(field);

        assertEquals(List.class.getName(), field.getClassName());
        assertNotNull(field.getGenericParameterClassList());
        assertEquals(1, field.getGenericParameterClassList().size());
        assertEquals(String.class.getName(), field.getGenericParameterClassList().get(0));
    }

    public static class ClassWithMapWithGenerics
    {
        public static class EmbeddedClass
        {

        }

        private Map<String, EmbeddedClass> myMap;

        public Map<String, EmbeddedClass> getMyMap()
        {
            return myMap;
        }

        public void setMyMap(Map<String, EmbeddedClass> myMap)
        {
            this.myMap = myMap;
        }
    }

    @Test
    public void testClassWithMapWithGenerics()
    {
        AccessibleClassParser parser = new JavaAccessibleClassParser();
        AccessibleClass clazz = parser.parseAccessibleClass(ClassWithMapWithGenerics.class);

        assertNotNull(clazz);
        log.info("Class : " + clazz.getClassBaseName());

        assertEquals(1, clazz.getAccessibleFields().size());

        AccessibleField field = clazz.getAccessibleFields().get("myMap");
        assertNotNull(field);

        assertEquals(Map.class.getName(), field.getClassName());
        assertNotNull(field.getGenericParameterClassList());
        assertEquals(2, field.getGenericParameterClassList().size());
        assertEquals(String.class.getName(), field.getGenericParameterClassList().get(0));
        assertEquals(ClassWithMapWithGenerics.EmbeddedClass.class.getName(), field.getGenericParameterClassList()
                .get(1));
    }

    public static class ClassWhichIsParent
    {
        private String parentField;

        public String getParentField()
        {
            return parentField;
        }

        public void setParentField(String parentField)
        {
            this.parentField = parentField;
        }
    }

    public static class ClassWhichIsChild extends ClassWhichIsParent
    {
        private String childField;

        public String getChildField()
        {
            return childField;
        }

        public void setChildField(String childField)
        {
            this.childField = childField;
        }
    }

    @Test
    public void testFieldScope_Parent()
    {
        AccessibleClassParser parser = new JavaAccessibleClassParser();
        AccessibleClass clazz = parser.parseAccessibleClass(ClassWhichIsParent.class);
        assertEquals(1, clazz.getAccessibleFields().size());
        AccessibleField parentField = clazz.getAccessibleFields().get("parentField");
        assertEquals(ClassWhichIsParent.class.getName(), parentField.getDeclaredInClass());
    }

    @Test
    public void testFieldScope_Child()
    {
        AccessibleClassParser parser = new JavaAccessibleClassParser();
        AccessibleClass clazz = parser.parseAccessibleClass(ClassWhichIsChild.class);
        assertEquals(2, clazz.getAccessibleFields().size());
        AccessibleField parentField = clazz.getAccessibleFields().get("parentField");
        assertEquals(ClassWhichIsParent.class.getName(), parentField.getDeclaredInClass());

        AccessibleField childField = clazz.getAccessibleFields().get("childField");
        assertEquals(ClassWhichIsChild.class.getName(), childField.getDeclaredInClass());

    }

    public static abstract class AbstractClass
    {

    }

    @Test
    public void testAbstractClass()
    {
        AccessibleClassParser parser = new JavaAccessibleClassParser();
        AccessibleClass clazz = parser.parseAccessibleClass(AbstractClass.class);

        assertTrue(clazz.isAbstract());
    }

    public static class ClassWithMandatoryField
    {
        @Mandatory
        private int myMandatoryField = 23;

        private int myNonMandatoryField;

        private String myDefaultValueString = "TestReflection";

        private String myValueString;

        public ClassWithMandatoryField()
        {

        }

        public int getMyMandatoryField()
        {
            return myMandatoryField;
        }

        public void setMyMandatoryField(int myMandatoryField)
        {
            this.myMandatoryField = myMandatoryField;
        }

        public int getMyNonMandatoryField()
        {
            return myNonMandatoryField;
        }

        public void setMyNonMandatoryField(int myNonMandatoryField)
        {
            this.myNonMandatoryField = myNonMandatoryField;
        }

        public String getMyDefaultValueString()
        {
            return myDefaultValueString;
        }

        public void setMyDefaultValueString(String myDefaultValueString)
        {
            this.myDefaultValueString = myDefaultValueString;
        }

        public String getMyValueString()
        {
            return myValueString;
        }

        public void setMyValueString(String myValueString)
        {
            this.myValueString = myValueString;
        }

    }

    @Test
    public void testClassWithMandatoryField()
    {
        AccessibleClassParser parser = new JavaAccessibleClassParser();
        AccessibleClass clazz = parser.parseAccessibleClass(ClassWithMandatoryField.class);

        AccessibleField mandatoryField = clazz.getAccessibleFields().get("myMandatoryField");
        assertNotNull(mandatoryField);
        assertTrue(mandatoryField.isMandatory());

        AccessibleField nonMandatoryField = clazz.getAccessibleFields().get("myNonMandatoryField");
        assertNotNull(nonMandatoryField);
        assertFalse(nonMandatoryField.isMandatory());

    }

    @Test
    public void testClassWithFieldDefaultValue()
    {
        JavaAccessibleClassParser parser = new JavaAccessibleClassParser();
        parser.setTryInstantiateClassForDefaultValue(true);
        AccessibleClass clazz = parser.parseAccessibleClass(ClassWithMandatoryField.class);

        AccessibleField mandatoryField = clazz.getAccessibleFields().get("myMandatoryField");
        assertNotNull(mandatoryField);
        assertEquals("23", mandatoryField.getDefaultValue());

        AccessibleField nonMandatoryField = clazz.getAccessibleFields().get("myNonMandatoryField");
        assertNotNull(nonMandatoryField);
        assertEquals("0", nonMandatoryField.getDefaultValue());

        AccessibleField defaultStringField = clazz.getAccessibleFields().get("myDefaultValueString");
        assertNotNull(defaultStringField);
        assertEquals("TestReflection", defaultStringField.getDefaultValue());

        AccessibleField stringField = clazz.getAccessibleFields().get("myValueString");
        assertNotNull(stringField);
        assertEquals(null, stringField.getDefaultValue());

    }
}
