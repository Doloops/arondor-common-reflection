package com.arondor.common.reflection.noreflect.runtime;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class TestPrimitiveStringConverter
{
    @Test
    public void testStandardPrimitives()
    {
        assertEquals("Value", PrimitiveStringConverter.tojavalangString("Value"));
        assertEquals(true, PrimitiveStringConverter.toboolean("true"));
        assertEquals(false, PrimitiveStringConverter.toboolean("false"));
        assertEquals(false, PrimitiveStringConverter.toboolean("Hector"));
    }

    private static class TypedExample
    {
        private final String stringValue;

        private final Object objectValue;

        private final Class<?> objectClass;

        public TypedExample(String stringValue, Object objectValue, Class<?> objectClass)
        {
            this.stringValue = stringValue;
            this.objectValue = objectValue;
            this.objectClass = objectClass;
        }

        public String getStringValue()
        {
            return stringValue;
        }

        public Object getObjectValue()
        {
            return objectValue;
        }

        public Class<?> getObjectClass()
        {
            return objectClass;
        }
    }

    @Test
    public void testUsingClassName() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        TypedExample examples[] = { new TypedExample("123", 123, int.class),
                new TypedExample("123", 123, Integer.class), new TypedExample("123434", (long) 123434, long.class),
                new TypedExample("123434", (long) 123434, Long.class), new TypedExample("true", true, boolean.class),
                new TypedExample("true", true, Boolean.class), new TypedExample("43.75", (float) 43.75f, float.class),
                new TypedExample("77.12", (float) 77.12f, Float.class),
                new TypedExample("78.0", (double) 78.0f, double.class),
                new TypedExample("7", (double) 7, Double.class), new TypedExample("c", (char) 'c', char.class), };

        Class<?> parameterTypes = String.class;

        for (TypedExample example : examples)
        {
            String methodName = PrimitiveStringConverter.getConvertionMethodFromClassName(example.getObjectClass()
                    .getName());
            Method converterMethod = PrimitiveStringConverter.class.getMethod(methodName, parameterTypes);

            Object result = converterMethod.invoke(null, example.getStringValue());

            assertEquals("Invalid test from " + example.getObjectClass().getName(), example.getObjectValue(), result);
        }
    }
}
