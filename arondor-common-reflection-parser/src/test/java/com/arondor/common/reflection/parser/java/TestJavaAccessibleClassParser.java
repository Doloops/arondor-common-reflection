package com.arondor.common.reflection.parser.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.arondor.common.management.mbean.annotation.Description;
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
}
